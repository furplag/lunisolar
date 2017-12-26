/**
 * Copyright (C) 2017+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.furplag.time.lunisolar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jp.furplag.time.Julian;
import jp.furplag.time.lunisolar.misc.Astror;
import jp.furplag.time.lunisolar.misc.orrery.EclipticLongitude;

/**
 * lunisolar calendar system .
 *
 * <h1>Overview</h1>
 * <ol>
 * <li>compute the winter solstice of last year.</li>
 * <li>compute the solar terms in the year.</li>
 * <li>compute moments of new moon in the year.</li>
 * <li>construct the months of the year using the moments of new moon.</li>
 * <li>indexing the months using moments of the solar terms.</li>
 * <li>detecting the leap month.</li>
 * </ol>
 *
 * @author furplag
 *
 */
public abstract class Lunisolar {

  static final double precision = 5E-10;

  static final int loopLimit = 100;

  final double daysOfYear;

  final double daysOfMonth;

  final ZoneOffset zoneOffset;

  Lunisolar(double daysOfYear, double daysOfMonth, ZoneOffset zoneOffset) {
    this.daysOfYear = daysOfYear;
    this.daysOfMonth = daysOfMonth;
    this.zoneOffset = zoneOffset;
  }

  /**
   * returns the result that have minimum delta.
   *
   * @param results {@link Map} of delta: julianDate
   * @param _default a fallback
   * @return the result
   */
  static double doOurOwnBest(final Map<Double, Double> results, final double _default) {
    // @formatter:off
    return Objects.requireNonNull(results.entrySet()).stream()
      .sorted(new Comparator<Map.Entry<Double, Double>>() {
        @Override public int compare(Entry<Double, Double> o1, Entry<Double, Double> o2) {
          return o1.getKey().compareTo(o2.getKey());
        }
      }).mapToDouble(Map.Entry::getValue).min().orElse(_default);
    // @formatter:on
  }

  /**
   * similar to {@link LocalDate#atStartOfDay(java.time.ZoneId)} .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return the time at 00:00:00 in specified day
   */
  protected long asStartOfDay(double julianDate) {
    return Julian.toInstant(julianDate).atOffset(zoneOffset).truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli();
  }

  /**
   * substitute for {@link Instant#atOffset(ZoneOffset)} .
   *
   * @param instant {@link Instant}
   * @return an {@link OffsetDateTime}
   */
  protected OffsetDateTime atOffset(Instant instant) {
    return instant.atOffset(zoneOffset);
  }

  /**
   * calculate the closest instant in which the ecliptic longitude of the sun places at the specified angle from the specified julian date.
   *
   * @param julianDate an instant represented by astronomical julian date
   * @param degree the degree which circlyzed 0 to 360
   * @return the closest instant in which the ecliptic longitude of the sun places at the specified angle
   */
  abstract double closestTerm(double julianDate, double degree);

  /**
   *
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return
   */
  protected double firstDayOfYear(double julianDate) {
    double springEquinox = closestTerm(Julian.ofEpochMilli(atOffset(Julian.toInstant(julianDate)).with(ChronoField.MONTH_OF_YEAR, 4).toInstant().toEpochMilli()), 0);
    double midClimateOfFirst = closestTerm(springEquinox, 330);
    double temporalFirst = latestNewMoon(midClimateOfFirst);
    double temporalNext = latestNewMoon(plusMonth(temporalFirst, 1.1));
    long dayOfMidClimateOfFirst = asStartOfDay(midClimateOfFirst);
    long dayOfTemporalFirst = asStartOfDay(temporalFirst);
    long dayOfTemporalNext = asStartOfDay(temporalNext);

    return dayOfTemporalFirst < dayOfMidClimateOfFirst ? (dayOfMidClimateOfFirst < dayOfTemporalNext ? temporalFirst : temporalNext) : dayOfTemporalFirst > dayOfMidClimateOfFirst ? latestNewMoon(plusMonth(temporalFirst, -.1)) : temporalFirst < midClimateOfFirst ? temporalFirst : temporalNext;
  }

  /**
   *
   *
   * @param solarTerms {@link SolarTerm} of the year
   * @param rangeOfYear the days of year
   * @return
   */
  abstract List<Long> firstDaysOfYear(List<SolarTerm> solarTerms, ValueRange rangeOfYear);

  /**
   * returns the list of {@link SolarTerm} between a winter solstice of the year that contains specified instant and last year's one .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return list of {@link SolarTerm} between the winter solstice of
   */
  abstract List<SolarTerm> termsOfBase(double julianDate);

  /**
   * calculate the first day of lunar month (typically the day contains an instant of new moon) .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return the first day of lunar month
   */
  protected double latestNewMoon(double julianDate) {
    final Map<Double, Double> results = new HashMap<>();

    double numeric = (long) (julianDate);
    double floating = julianDate - numeric;
    double delta = 0.0;
    double diffOfNumeric = 0.0;
    double diffOfFloating = 0.0;
    int counter = 0;
    do {
      delta = EclipticLongitude.Moon.ofJulian((numeric + floating)) - EclipticLongitude.Sun.ofJulian(numeric + floating);
      if (counter == 0 || delta < -15.0) {
        delta = Astror.circulate(delta);
      } else if (delta > 345.0) {
        delta -= 360.0;
      }
      counter++;

      diffOfFloating = delta * (daysOfMonth / 360.0);
      diffOfNumeric = (long) diffOfFloating;
      diffOfFloating -= diffOfNumeric;
      numeric -= diffOfNumeric;
      floating -= diffOfFloating;

      results.put(Math.abs(diffOfNumeric + diffOfFloating), (numeric + floating));
    } while (Math.abs(diffOfNumeric + diffOfFloating) > precision && counter < loopLimit);

    return counter < loopLimit ? (numeric + floating) : (doOurOwnBest(results, (numeric + floating)));
  }

  /**
   * optimize the lunar months to fit the year .
   *
   * @param solarTerms {@link SolarTerm} of the year
   * @param firstDaysOfYear first days of the year
   * @param rangeOfYear the days of year
   * @return lunar calendar in the year
   */
  protected List<LunarMonth> monthsOfYear(List<SolarTerm> solarTerms, List<Long> firstDaysOfYear, ValueRange rangeOfYear) {
    // @formatter:off
    return
      Objects.isNull(firstDaysOfYear) ? null :
      firstDaysOfYear.isEmpty() ? null :
      IntStream.range(1, firstDaysOfYear.size())
        .mapToObj(index -> new LunarMonth(firstDaysOfYear.get(index - 1), firstDaysOfYear.get(index) - 1, solarTerms))
        .filter(month -> rangeOfYear.isValidValue(month.range.getMinimum()))
        .collect(Collectors.toList());
    // @formatter:on
  }

  /**
   * returns a copy of specified instant with the specified amount added .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @param amount number of months which add
   * @return an instant with the specified amount added
   */
  protected double plusMonth(double julianDate, double amount) {
    return julianDate + (synodicMonth(julianDate) * amount);
  }

  /**
   * returns the {@link ValueRange} represented by the days of year that contains specified instant.
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return {@link ValueRange}
   */
  protected ValueRange rangeOfYear(double julianDate) {
    final double firstDayOfYear = firstDayOfYear(julianDate);
    double firstDayOfNextYear = firstDayOfYear(plusMonth(firstDayOfYear, 16));

    return ValueRange.of(asStartOfDay(firstDayOfYear), asStartOfDay(firstDayOfNextYear) - 1L);
  }

  /**
   * optimize the days of a synodic month which contains specified instant .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return the days of a synodic month
   */
  protected double synodicMonth(double julianDate) {
    return ((julianDate - Julian.j2000) * Julian.incrementOfSynodicMonth) + daysOfMonth;
  }
}
