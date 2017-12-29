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
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import jp.furplag.time.Julian;
import jp.furplag.time.Millis;
import jp.furplag.time.lunisolar.misc.Astror;
import jp.furplag.time.lunisolar.misc.orrery.EclipticLongitude;

public final class StandardLunisolar extends Lunisolar {

  StandardLunisolar(double daysOfYear, double daysOfMonth, ZoneOffset zoneOffset) {
    super(daysOfYear, daysOfMonth, zoneOffset);
  }

  @Override
  double closestTerm(double julianDate, double degree) {
    final double expect = Astror.circulate(degree);
    double numeric = (long) julianDate;
    double floating = julianDate - numeric;
    double delta = 0.0;
    double diff = 0.0;
    int counter = 0;
    Map<Double, Double> results = new HashMap<>();
    do {
      delta = EclipticLongitude.Sun.ofJulian(numeric + floating) - expect;
      delta += delta > 180.0 ? -360.0 : delta < -180.0 ? 360.0 : 0;
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
      results.put(Math.abs(diff), (numeric + floating));
    } while (Math.abs(diff) > precision && counter < loopLimit);

    return counter < loopLimit ? (numeric + floating) : (doOurOwnBest(results, (numeric + floating)));
  }

  @Override
  protected List<Long> termsToFirstDays(@Nonnull List<SolarTerm> solarTerms) {
    // @formatter:off
    return solarTerms.stream()
      .mapToDouble(solarTerm -> solarTerm.julianDate)
      .mapToObj(julianDate -> asStartOfDay(latestNewMoon(julianDate)))
      .distinct()
      .sorted()
      .collect(Collectors.toList());
    // @formatter:on
  }

  @Override
  List<SolarTerm> termsOfBase(double julianDate) {
    List<SolarTerm> solarTerms = new ArrayList<>(Arrays.asList(SolarTerm.ofClosest(plusMonth(winterSolstice(julianDate), -13), 255, this)));
    do {
      final SolarTerm solarTerm = lastOf(solarTerms);
      solarTerms.add(SolarTerm.ofClosest(solarTerm.julianDate, solarTerm.longitude + 15, this));
    } while (solarTerms.stream().filter(e -> e.longitude == 270).count() != 3);

    return solarTerms;
  }

  public static void main(String[] args) {
    List<SolarTerm> solarTerms = Kyoho.termsOfBase(Julian.ofEpochMilli(Instant.parse("2032-12-31T15:00:00.000Z").toEpochMilli()));
    printr(solarTerms.stream());
    List<Long> firstDays = Kyoho.termsToFirstDays(solarTerms).stream().map(Millis::ofJulian).collect(Collectors.toList());
    printr(firstDays.stream().map(Millis::toInstant).map(Kyoho::atOffset));

    List<LunarMonth> monthOfYear = LunarMonth.constructs(solarTerms, firstDays);
    printr(monthOfYear.stream());
//
//    final LunarMonth lastWinterSolstice = monthOfYear.stream().filter(e -> e.november).map(e -> {e.monthOfYear = 11; return e;}).findFirst().orElse(null);
//    final LunarMonth winterSolstice = monthOfYear.stream().filter(e -> e.november && e.compareTo(lastWinterSolstice) > 0).findFirst().orElse(null);
//    final ValueRange termRange = ValueRange.of(lastWinterSolstice.range.getMinimum(), winterSolstice.range.getMinimum() - 1);
//    List<LunarMonth> termTable = monthOfYear.stream().filter(e -> termRange.isValidValue(e.range.getMinimum())).collect(Collectors.toList());
//    final boolean hasIntercalary = termTable.size() > 12;
//    final int[] theMonth = {10};
//    termTable.stream().filter(e -> hasIntercalary && e.intercalaryable).findFirst().ifPresent(e -> e.intercalary = true);
//    termTable.forEach(e -> {
//      theMonth[0] += e.intercalary ? 0 : 1;
//      if (e.monthOfYear == 0) {
//        e.monthOfYear = (theMonth[0] % 12 == 0 ? 12 : theMonth[0] % 12);
//      }
//    });
//    final LunarMonth nextWinterSolstice = monthOfYear.stream().filter(e -> e.november && e.compareTo(winterSolstice) > 0).findFirst().orElse(null);
//    final ValueRange nextTermRange = ValueRange.of(winterSolstice.range.getMinimum(), nextWinterSolstice.range.getMinimum() - 1);
//    List<LunarMonth> nextTermTable = monthOfYear.stream().filter(e -> nextTermRange.isValidValue(e.range.getMinimum())).collect(Collectors.toList());
//    final boolean hasIntercalaryNext = nextTermTable.size() > 12;
//    theMonth[0] = 10;
//    nextTermTable.stream().filter(e -> hasIntercalaryNext && e.intercalaryable).findFirst().ifPresent(e -> e.intercalary = true);
//    nextTermTable.forEach(e -> {
//      theMonth[0] += e.intercalary ? 0 : 1;
//      if (e.monthOfYear == 0) {
//        e.monthOfYear = (theMonth[0] % 12 == 0 ? 12 : theMonth[0] % 12);
//      }
//    });
//
////    if (winterSolstice.monthOfYear != 11) {
////      final LunarMonth nextWinterSolstice = monthOfYear.stream().filter(e -> e.november && e.compareTo(winterSolstice) > 0).findFirst().orElse(null);
////      final ValueRange nextTermRange = ValueRange.of(winterSolstice.range.getMinimum(), nextWinterSolstice.range.getMaximum());
////      List<LunarMonth> nextTermTable = monthOfYear.stream().filter(e -> nextTermRange.isValidValue(e.range.getMinimum())).collect(Collectors.toList());
////    }
////    System.out.println(winterSolstice);
//
//    termTable.addAll(nextTermTable);
//    final LunarMonth january = termTable.stream().filter(e -> e.monthOfYear == 1).findFirst().orElse(null);
//    final LunarMonth december = nextTermTable.stream().filter(e -> e.monthOfYear == 12).max(new Comparator<LunarMonth>() {
//      @Override
//      public int compare(LunarMonth o1, LunarMonth o2) {
//        return o1.compareTo(o2);
//      }}).orElse(null);
//    printr(termTable.stream().filter(e -> january.range.getMinimum() <= e.range.getMinimum() && e.range.getMinimum() <= december.range.getMaximum()));
  }

  static void printr(Stream<?> stream) {
    stream.forEach(System.out::println);
  }

  @SuppressWarnings("unchecked")
  protected static <T> T lastOf(Collection<T> collection) {
    return collection == null || collection.isEmpty() ? null : (T) collection.toArray()[collection.size() - 1];
  }

}
