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

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jp.furplag.time.Millis;
import jp.furplag.time.lunisolar.SolarTerm.MidClimate;
import jp.furplag.time.lunisolar.SolarTerm.PreClimate;

public final class LunarMonth implements Comparable<LunarMonth>, Serializable {

  final ValueRange range;

  final List<SolarTerm> preClimates;

  final List<SolarTerm> midClimates;

  final boolean intercalaryable;

  boolean intercalary;

  final boolean november;

  int monthOfYear;

  LunarMonth(long fromEpochMilli, long toEpochMilli, List<SolarTerm> solarTerms) {
    range = ValueRange.of(fromEpochMilli, toEpochMilli);
    preClimates = PreClimate.stream(solarTerms).filter(t->range.isValidValue(Millis.ofJulian(t.julianDate))).collect(Collectors.toList());
    midClimates = MidClimate.stream(solarTerms).filter(t->range.isValidValue(Millis.ofJulian(t.julianDate))).collect(Collectors.toList());
    november = midClimates.stream().anyMatch(t -> t.longitude == 270);
    intercalaryable = midClimates.isEmpty();
  }

  public static final Comparator<LunarMonth> comparator() {
    return new Comparator<LunarMonth>() {
      @Override
      public int compare(LunarMonth o1, LunarMonth o2) {
        return o1.compareTo(o2);
      }};
  }

  static List<LunarMonth> constructs(final List<SolarTerm> solarTerms, final List<Long> firstDays) {
    final List<Long> _firstDays = Optional.ofNullable(firstDays).orElse(new ArrayList<>());
    // @formatter:off
    return monthOfYear(intercalaryze(IntStream.range(_firstDays.isEmpty() ? 0 : 1, _firstDays.size())
      .mapToObj(index -> new LunarMonth(firstDays.get(index - 1), firstDays.get(index) - 1, solarTerms))
      .collect(Collectors.toList())));
    // @formatter:on
  }

  private static List<LunarMonth> intercalaryze(final List<LunarMonth> lunarMonths) {
    List<LunarMonth> _lunarMonths = Optional.ofNullable(lunarMonths).orElse(new ArrayList<>());
    final LunarMonth firstOfWinterSolstices = _lunarMonths.stream().filter(LunarMonth::isNovember).sorted(comparator()).findFirst().orElse(null);
    final LunarMonth secondOfWinterSolstices = _lunarMonths.stream().filter(LunarMonth::isNovember).filter(e -> firstOfWinterSolstices.range.getMinimum() < e.range.getMinimum()).sorted(comparator()).findFirst().orElse(null);
    materialize(_lunarMonths.stream().filter(e -> firstOfWinterSolstices.range.getMinimum() <= e.range.getMinimum() && e.range.getMinimum() < secondOfWinterSolstices.range.getMinimum()).sorted(comparator()).collect(Collectors.toList()));
    final LunarMonth thirdOfWinterSolstices = _lunarMonths.stream().filter(LunarMonth::isNovember).filter(e -> secondOfWinterSolstices.range.getMinimum() < e.range.getMinimum()).sorted(comparator()).findFirst().orElse(null);
    materialize(_lunarMonths.stream().filter(e -> secondOfWinterSolstices.range.getMinimum() <= e.range.getMinimum() && e.range.getMinimum() < thirdOfWinterSolstices.range.getMinimum()).sorted(comparator()).collect(Collectors.toList()));

    return _lunarMonths;
  }

  private static void materialize(final List<LunarMonth> monthsOfWinterSolstice) {
    final boolean hasIntercalary = monthsOfWinterSolstice.size() > 12;
    monthsOfWinterSolstice.stream().filter(e -> hasIntercalary && e.intercalaryable).findFirst().ifPresent(e -> e.intercalary = true);
    final int[] monthOfYear = {10};
    monthsOfWinterSolstice.forEach(e -> {
      monthOfYear[0] += e.intercalary ? 0 : 1;
      if (e.monthOfYear == 0) {
        e.monthOfYear = (monthOfYear[0] % 12 == 0 ? 12 : monthOfYear[0] % 12);
      }
    });
  }

  private static List<LunarMonth> monthOfYear(final List<LunarMonth> lunarMonths) {
    final LunarMonth january = lunarMonths.stream().filter(e -> e.monthOfYear == 1).findFirst().orElse(null);
    final LunarMonth december = lunarMonths.stream().filter(e -> e.monthOfYear == 12).max(comparator()).orElse(null);

    return Optional.ofNullable(lunarMonths).orElse(new ArrayList<>()).stream()
      .filter(e -> january.range.getMinimum() <= e.range.getMinimum() && e.range.getMinimum() <= december.range.getMaximum())
      .collect(Collectors.toList());
  }

  @Override
  public int compareTo(LunarMonth o) {
    return Long.compare(range.getMinimum(), o.range.getMinimum());
  }

  public boolean isIntercalary() {
    return intercalary;
  }

  public boolean isIntercalaryable() {
    return intercalaryable;
  }

  public boolean isNovember() {
    return november;
  }

  @Override
  public String toString() {
    // @formatter:off
    return
      (intercalary ? "閏" : "")
      + monthOfYear
      + ", range: "
      + Instant.ofEpochMilli(range.getMinimum()).atZone(ZoneId.systemDefault())
      + " - "
      + Instant.ofEpochMilli(range.getMaximum()).atZone(ZoneId.systemDefault())
      + " ("
      + Duration.of(range.getMaximum() + 1 - range.getMinimum(), ChronoUnit.MILLIS).toDays()
      + ")"
      + ", intercalaryable: "
      + intercalaryable
      + ", preClimates: "
      + preClimates.stream().map(c->c.longitude).collect(Collectors.toList()).toString()
      + ", midClimates: "
      + midClimates.stream().map(c->c.longitude).collect(Collectors.toList()).toString()
      ;
    // @formatter:on
  }
}
