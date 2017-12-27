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

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LunisolarCalendar {

  final Lunisolar lunisolar;

  final ValueRange rangeOfYear;

  final List<LunarMonth> monthsOfYear;

  public LunisolarCalendar(Lunisolar lunisolar, double julianDate) {
    this.lunisolar = Objects.requireNonNull(lunisolar);
    this.rangeOfYear = lunisolar.rangeOfYear(julianDate);
    final List<SolarTerm> solarTerms = lunisolar.termsOfBase(julianDate);
    List<LunarMonth> monthsOfYear = lunisolar.monthsOfYear(solarTerms, lunisolar.firstDaysOfYear(solarTerms, rangeOfYear), rangeOfYear);
    this.monthsOfYear = monthsOfYear.size() > 12 ? leapmonthsOfYear(monthsOfYear) : monthsOfYear(monthsOfYear);
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
    final boolean[] intercalaryed = {false};
    // @formatter:off
    LunarMonth.intercalaryze(monthsOfYear.stream().filter(monthOfYear -> monthOfYear.intercalary), intercalaryed[0]);
    final int[] theMonth = {0};
    monthsOfYear.forEach(e -> {
      if (!e.intercalaryInCalendar) theMonth[0]++;
      e.monthOfYear = theMonth[0];
    });
    // @formatter:on

    return Collections.unmodifiableList(monthsOfYear);
  }
}
