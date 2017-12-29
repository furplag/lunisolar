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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

  @Nonnull
  static List<LunarMonth> constructs(final @Nonnull List<SolarTerm> solarTerms, final @Nonnull List<Long> firstDays) {
    // @formatter:off
    return monthOfYear(intercalaryze(IntStream.range(firstDays.isEmpty() ? 0 : 1, firstDays.size())
      .mapToObj(index -> new LunarMonth(firstDays.get(index - 1), firstDays.get(index) - 1, solarTerms))
      .collect(Collectors.toList())));
    // @formatter:on
  }

  @Nonnull
  private static List<LunarMonth> intercalaryze(final @Nonnull List<LunarMonth> lunarMonths) {
    final LunarMonth firstOfWinterSolstices = firstNovember(lunarMonths.stream(), null);
    final LunarMonth secondOfWinterSolstices = firstNovember(lunarMonths.stream(), firstOfWinterSolstices);
    final LunarMonth thirdOfWinterSolstices = firstNovember(lunarMonths.stream(), secondOfWinterSolstices);
    materialize(lunarMonths, firstOfWinterSolstices.range.getMinimum(), secondOfWinterSolstices.range.getMinimum());
    materialize(lunarMonths, secondOfWinterSolstices.range.getMinimum(), thirdOfWinterSolstices.range.getMinimum());

    return lunarMonths;
  }

  private static void materialize(final @Nonnull List<LunarMonth> lunarMonths, final long minimum, final long maximum) {
    materialize(lunarMonths.stream().filter(e -> minimum <= e.range.getMinimum() && e.range.getMinimum() < maximum).sorted(comparator()).collect(Collectors.toList()));
  }

  private static void materialize(final @Nonnull List<LunarMonth> monthsOfWinterSolstice) {
    final boolean hasIntercalary = monthsOfWinterSolstice.size() > 12;
    monthsOfWinterSolstice.stream().filter(e -> hasIntercalary && e.intercalaryable).findFirst().ifPresent(e -> e.intercalary = true);
    final int[] monthOfYear = {10};
    monthsOfWinterSolstice.forEach(e -> {
      monthOfYear[0] += e.intercalary ? 0 : 1;
      if (e.monthOfYear == 0) {
        e.monthOfYear = normalize(monthOfYear[0], 12);
      }
    });
  }

  private static List<LunarMonth> monthOfYear(final @Nonnull List<LunarMonth> lunarMonths) {
    final LunarMonth january = Objects.requireNonNull(firstOf(lunarMonths, 1));
    final LunarMonth december = Objects.requireNonNull(firstOf(lunarMonths.stream().filter(e -> january.range.getMinimum() < e.range.getMinimum()).collect(Collectors.toList()), 12));

    List<LunarMonth> _lunarMonths = lunarMonths.stream()
      .filter(e -> january.range.getMinimum() <= e.range.getMinimum() && e.range.getMinimum() <= december.range.getMinimum())
      .collect(Collectors.toList());

    return _lunarMonths;
  }

  @Override
  public int compareTo(LunarMonth o) {
    return Long.compare(range.getMinimum(), o.range.getMinimum());
  }

  @Nullable
  private static LunarMonth firstNovember(final @Nonnull Stream<LunarMonth> lunarMonths, final LunarMonth start) {
    return lunarMonths.filter(e -> e.november).filter(e -> (start == null ? Long.MIN_VALUE : start.range.getMinimum()) < e.range.getMinimum()).sorted().findFirst().orElse(null);
  }

  @Nullable
  private static LunarMonth firstOf(final @Nonnull List<LunarMonth> lunarMonths, final int monthOfYear) {
    return lunarMonths.stream().filter(e -> e.monthOfYear == normalize(monthOfYear, 12)).findFirst().orElse(null);
  }

  private static int normalize(final int monthOfYear, final int normalizr) {
    return (monthOfYear % normalizr == 0 ? normalizr : monthOfYear % normalizr);
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
