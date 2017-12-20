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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jp.furplag.time.Julian;
import jp.furplag.time.Millis;
import jp.furplag.time.lunisolar.misc.Astror;
import jp.furplag.time.lunisolar.misc.orrery.EclipticLongitude;

public abstract class Lunisolar {

  static final double precision = 5E-10;

  static final double loopLimit = 100;

  protected final double daysOfYear;

  protected final double daysOfMonth;

  protected final ZoneOffset zoneOffset;

  protected Lunisolar(long rangeFrom, long rangeTo, double daysOfYear, double daysOfMonth, ZoneOffset zoneOffset) {
    this.daysOfYear = daysOfYear;
    this.daysOfMonth = daysOfMonth;
    this.zoneOffset = zoneOffset;
  }


  protected long asStartOfDay(double julianDate) {
    return Julian.toInstant(julianDate).atOffset(zoneOffset).truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli();
  }

  protected List<Long> firstDaysOfYear(List<Double> solarTerms) {
    // @formatter:off
    return Optional.ofNullable(solarTerms).orElse(new ArrayList<>()).stream()
      .filter(Objects::nonNull)
      .map(this::getLatestNewMoon)
      .mapToLong(this::asStartOfDay)
      .distinct()
      .mapToObj(Long::valueOf)
      .sorted()
      .collect(Collectors.toList());
    // @formatter:on
  }

  /**
   * calculate the closest instant in which the ecliptic longitude of the sun
   * places at the specified angle from the specified julian date.
   *
   * @param julianDate an instant represented by astronomical julian date
   * @param degree the degree which circlyzed 0 to 360
   * @return the closest instant in which the ecliptic longitude of the sun
   *          places at the specified angle
   */
  protected double getClosestTerm(final double julianDate, final double degree) {
    final double expect = Astror.circulate(degree);
    double numeric = (long) julianDate;
    double floating = julianDate - numeric;
    double delta = 0.0;
    double diff = 0.0;
    int counter = 0;
    do {
      delta = EclipticLongitude.Sun.ofJulian(numeric + floating) - expect;
      if (delta > 180.0) {
        delta -= 360.0;
      } else if (delta < -180.0) {
        delta += 360.0;
      }
      counter++;

      diff = delta * daysOfYear / 360.0;
      numeric -= (long) diff;
      floating -= diff - ((long) diff);
      if (floating < 0) {
        floating++;
        numeric--;
      } else if (floating > 1) {
        floating--;
        numeric++;
      }
    } while (Math.abs(diff) > precision && counter < loopLimit);
    if (counter >= (loopLimit / 2)) System.out.println("## (" + counter + ") fallen. : " + Julian.toInstant(julianDate) + "(" + julianDate + ")" + "[" + (numeric + floating) + "]");

    return numeric + floating;
  }

  OffsetDateTime atOffset(Instant instant) {
    return instant.atOffset(zoneOffset);
  }

  /**
   * calculate the first day of the year which contains specified instant .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return an instant of 1st Jan. of the year which contains specified instant
   */
  protected final double getFirstDayOfYear(double julianDate) {
    double springEquinox = getClosestTerm(Julian.ofEpochMilli(atOffset(Julian.toInstant(julianDate)).with(ChronoField.MONTH_OF_YEAR, 4).toInstant().toEpochMilli()), 0);
    double midClimateOfFirst = getClosestTerm(springEquinox, 330);
    double preClimateOfFirst = getClosestTerm(midClimateOfFirst, 315);
    double temporalFirst = getLatestNewMoon(midClimateOfFirst);
    double temporalPrevious = getLatestNewMoon(plusMonth(temporalFirst, -.1));
    double temporalNext = getLatestNewMoon(plusMonth(temporalFirst, 1.1));
    long dayOfMidClimateOfFirst = asStartOfDay(midClimateOfFirst);
    long dayOfTemporalFirst = asStartOfDay(temporalFirst);
    long dayOfTemporalNext = asStartOfDay(temporalNext);

    Map<String, Double> map = new HashMap<>();
    map.put("?12?", temporalPrevious);
    map.put("? 1?", temporalFirst);
    map.put("? 2?", temporalNext);
    map.put("?雨?", midClimateOfFirst);
    map.put("?春?", preClimateOfFirst);
    map.entrySet().stream().sorted(new Comparator<Map.Entry<String, Double>>() {
      @Override
      public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
        // TODO 自動生成されたメソッド・スタブ
        return o1.getValue().compareTo(o2.getValue());
      }})
    .forEach(e->{
//      System.out.print(e.getKey());
//      System.out.print("=");
//      System.out.print( Instant.ofEpochMilli(asStartOfDay(e.getValue())).atOffset(zoneOffset) );
//      System.out.println();
    });

    if (dayOfTemporalFirst < dayOfMidClimateOfFirst) {
      if (dayOfMidClimateOfFirst < dayOfTemporalNext) {
        return temporalFirst;
      } else {
        return temporalNext;
      }
    } else if (dayOfTemporalFirst > dayOfMidClimateOfFirst) {
      return temporalPrevious;
    } else if (temporalFirst < midClimateOfFirst) {
      return temporalFirst;
    }
    return
//      dayOfTemporalFirst < dayOfMidClimateOfFirst ? temporalFirst :
//      dayOfTemporalFirst > dayOfMidClimateOfFirst ? getLatestNewMoon(plusMonth(temporalFirst, -.1)) :
      temporalNext;
  }

  /**
   * calculate the first day of lunar month (typically the day contains an instant of new moon) .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @return the first day of lunar month
   */
  protected double getLatestNewMoon(double julianDate) {
    double numeric = (long) (julianDate);
    double floating = julianDate - numeric;
    double delta = .0;
    double diffOfNumeric = .0;
    double diffOfFloating = .0;
    int counter = 0;

    do {
      delta = EclipticLongitude.Moon.ofJulian((numeric + floating)) - EclipticLongitude.Sun.ofJulian(numeric + floating);
      if (counter == 0) {
        delta = Astror.circulate(delta);
      } else if (delta > 345.0) {
        delta -= 360.0;
      } else if (delta < -15.0) {
        delta = Astror.circulate(delta);
      }
      counter++;

      diffOfFloating = delta * (daysOfMonth / 360.0);
      diffOfNumeric = (long) diffOfFloating;
      diffOfFloating -= diffOfNumeric;
      numeric -= diffOfNumeric;
      floating -= diffOfFloating;
    } while (Math.abs(diffOfNumeric + diffOfFloating) > precision && counter < loopLimit);
    if (counter >= (loopLimit / 2)) System.out.println("# (" + counter + ") fallen. : " + Julian.toInstant(julianDate) + "(" + julianDate + ")" + "[" + (numeric + floating) + "]");

    return numeric + floating;
  }

  /**
   * returns a copy of specified instant with the specified amount added .
   *
   * @param julianDate an instant represented by astronomical julian date
   * @param amount number of months which add
   * @return an instant with the specified amount added
   */
  protected double plusMonth(final double julianDate, final double amount) {
    return julianDate + (synodicMonth(julianDate) * amount);
  }

  protected List<SolarTerm> solarTermsBase(double julianDate) {
    List<SolarTerm> terms = new ArrayList<>();

    int deg = 360;
    double winterSolsticeOfLastYear = getClosestTerm(getFirstDayOfYear(julianDate), 270d);
    terms.add(SolarTerm.ofClosest(plusMonth(winterSolsticeOfLastYear, 14), deg, this));
    do {
      terms.add(SolarTerm.ofClosest(terms.get(terms.size() - 1).julianDate, deg - (15 * (terms.size())), this));
    } while (terms.get(terms.size() - 1).julianDate > winterSolsticeOfLastYear);

    return terms.stream().sorted().collect(Collectors.toList());
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

  ValueRange rangeOfYear(double julianDate) {
    final double firstDayOfYear = getFirstDayOfYear(julianDate);
    double firstDayOfNextYear = getFirstDayOfYear(plusMonth(firstDayOfYear, 16));
    if (asStartOfDay(firstDayOfYear) >= asStartOfDay(firstDayOfNextYear)) {
      System.out.println(atOffset(Millis.toInstant(asStartOfDay(firstDayOfYear))));
      System.out.println(atOffset(Millis.toInstant(asStartOfDay(firstDayOfNextYear))));
    }
    return ValueRange.of(asStartOfDay(firstDayOfYear), asStartOfDay(firstDayOfNextYear) - 1L);
  }

  List<Long> firstDaysOfYear(List<SolarTerm> solarTerms, ValueRange rangeOfYear) {
    // @formatter:off
    return Optional.ofNullable(solarTerms).orElse(new ArrayList<>())
      .stream()
      .map(solarTerm -> solarTerm.julianDate)
      .map(this::getLatestNewMoon)
      .mapToLong(this::asStartOfDay)
      .distinct()
      .mapToObj(Long::valueOf)
      .collect(Collectors.toList());
    // @formatter:on
  }

  List<LunarMonth> monthsOfYear(List<SolarTerm> solarTerms, List<Long> firstDaysOfYear, ValueRange rangeOfYear) {
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
}
