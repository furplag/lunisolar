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

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import jp.furplag.time.Julian;
import jp.furplag.time.Millis;

public class LunisolarCalendar {

  private final Lunisolar lunisolar;

  private final ValueRange rangeOfYear;

  private final List<LunarMonth> monthsOfYear;

  public LunisolarCalendar(Lunisolar lunisolar, double julianDate) {
//    System.out.println(lunisolar.atOffset(Julian.toInstant(julianDate)));
    this.lunisolar = Objects.requireNonNull(lunisolar);
    this.rangeOfYear = lunisolar.rangeOfYear(julianDate);
    final List<SolarTerm> solarTerms = lunisolar.solarTermsBase(julianDate);
    List<LunarMonth> monthsOfYear = lunisolar.monthsOfYear(solarTerms, lunisolar.firstDaysOfYear(solarTerms, rangeOfYear), rangeOfYear);
    this.monthsOfYear = monthsOfYear.size() > 12 ? leapmonthsOfYear(monthsOfYear) : monthsOfYear(monthsOfYear);
  }

  public static void main(String[] args) {
    final OffsetDateTime t = Instant.parse("1844-12-01T12:00:00.000Z").atOffset(ZoneOffset.ofHours(9));
    final Lunisolar lunisolar = new Lunisolar(0, 0, 365.242234, 29.530588, ZoneOffset.ofHours(9)) {};
    // @formatter:off

    LongStream.rangeClosed(1956, 1956)
      .mapToObj(y->new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(t.with(ChronoField.YEAR, y).toInstant().toEpochMilli())))
//      .filter(c->c.monthsOfYear.get(0).monthOfYear != 1 || c.monthsOfYear.get(c.monthsOfYear.size() - 1).monthOfYear != 12)
      .forEach(System.out::println)
      ;

    // @formatter:on
  }

  private static List<LunarMonth> monthsOfYear(final List<LunarMonth> monthsOfYear) {
    final List<LunarMonth> _monthsOfYear = Optional.ofNullable(monthsOfYear).orElse(new ArrayList<>());

    // @formatter:off
    return Collections.unmodifiableList(
      _monthsOfYear.stream()
      .map(e -> {
        e.monthOfYear = _monthsOfYear.indexOf(e) + 1;

        return e;
      }).collect(Collectors.toList()));
    // @formatter:on
  }

  private static List<LunarMonth> leapmonthsOfYear(final List<LunarMonth> monthsOfYear) {
    final LunarMonth november = Objects.requireNonNull(monthsOfYear).stream().filter(monthOfYear -> monthOfYear.november).findFirst().orElse(null);
    final boolean[] intercalyed = {false};
    // @formatter:off
    monthsOfYear.stream().filter(monthOfYear -> november.range.getMinimum() < monthOfYear.range.getMinimum())
    .forEach(e -> {
      if (e.intercalary && !intercalyed[0]) {
        e.intercalaryInCalendar = true;
        intercalyed[0] = true;
      }
    });
    monthsOfYear.stream().filter(monthOfYear -> november.range.getMinimum() > monthOfYear.range.getMinimum())
    .forEach(e -> {
      if (e.intercalary && !intercalyed[0]) {
        e.intercalaryInCalendar = true;
        intercalyed[0] = true;
      }
    });
    final int[] theMonth = {0};
    monthsOfYear.forEach(e -> {
      if (!e.intercalaryInCalendar) theMonth[0]++;
      e.monthOfYear = theMonth[0];
    });
    // @formatter:on

    return Collections.unmodifiableList(monthsOfYear);
  }

  @Override
  public String toString() {
    return new StringBuilder()
      .append(Millis.toInstant(rangeOfYear.getMinimum()).atOffset(lunisolar.zoneOffset).toString())
      .append(" - ")
      .append(Millis.toInstant(rangeOfYear.getMaximum()).atOffset(lunisolar.zoneOffset).toString())
      .append(" ( ")
      .append(Duration.of(rangeOfYear.getMaximum() - rangeOfYear.getMinimum(), ChronoUnit.MILLIS).toDays())
      .append(" days ) ")
      .append("\n")
      .append(monthsOfYear.stream().map(Objects::toString).collect(Collectors.joining("\n\t","\t","")))
      .toString();
  }
}
