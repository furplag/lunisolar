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
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import jp.furplag.data.json.Jsonifier;
import jp.furplag.time.lunisolar.SolarTerm.MidClimate;
import jp.furplag.time.lunisolar.SolarTerm.PreClimate;

public final class LunarMonth implements Comparable<LunarMonth>, Serializable {

  @JsonProperty
  final ValueRange range;

  @JsonProperty
  final List<SolarTerm> preClimates;

  @JsonProperty
  final List<SolarTerm> midClimates;

  final boolean intercalaryable;

  @JsonProperty
  boolean intercalary;

  final boolean november;

  @JsonProperty
  int monthOfYear;

  LunarMonth(long fromEpochMilli, long toEpochMilli, List<SolarTerm> solarTerms) {
    range = ValueRange.of(fromEpochMilli, toEpochMilli);
    preClimates = PreClimate.stream(solarTerms).filter(t->range.isValidValue(t.epochMilli)).collect(Collectors.toList());
    midClimates = MidClimate.stream(solarTerms).filter(t->range.isValidValue(t.epochMilli)).collect(Collectors.toList());
    november = midClimates.stream().anyMatch(t -> t.longitude == 270);
    intercalaryable = midClimates.isEmpty();
  }

  @Nonnull
  static List<LunarMonth> constructs(final @Nonnull List<SolarTerm> solarTerms, final @Nonnull List<Long> firstDays) {
    // @formatter:off
    return monthOfYear(intercalaryze(IntStream.range(1, firstDays.size())
      .mapToObj(index -> new LunarMonth(firstDays.get(index - 1), firstDays.get(index) - 1, solarTerms))
      .collect(Collectors.toList())));
    // @formatter:on
  }

  @Nonnull
  private static List<LunarMonth> intercalaryze(final @Nonnull List<LunarMonth> lunarMonths) {
    final LunarMonth firstOfWinterSolstices = firstNovember(lunarMonths.stream(), null);
    final LunarMonth secondOfWinterSolstices = firstNovember(lunarMonths.stream(), firstOfWinterSolstices);
    materialize(lunarMonths, firstOfWinterSolstices.range.getMinimum(), secondOfWinterSolstices.range.getMinimum());
    materialize(lunarMonths, secondOfWinterSolstices.range.getMinimum(), firstNovember(lunarMonths.stream(), secondOfWinterSolstices).range.getMinimum());

    return lunarMonths;
  }

  private static void materialize(final @Nonnull List<LunarMonth> lunarMonths, final long minimum, final long maximum) {
    materialize(lunarMonths.stream().filter(e -> minimum <= e.range.getMinimum() && e.range.getMinimum() < maximum).sorted().collect(Collectors.toList()));
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

  @Nonnull
  private static List<LunarMonth> monthOfYear(final @Nonnull List<LunarMonth> lunarMonths) {
    final LunarMonth january = firstOf(lunarMonths, 1);
    final ValueRange range = ValueRange.of(january.range.getMinimum(), firstOf(filtered(lunarMonths.stream(), e -> january.range.getMinimum() < e.range.getMinimum()), 12).range.getMinimum());

    return filtered(lunarMonths.stream(), e -> range.isValidValue(e.range.getMinimum()));
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

  private static List<LunarMonth> filtered(final @Nonnull Stream<LunarMonth> stream, Predicate<LunarMonth> predicate) {
    return stream.filter(predicate).collect(Collectors.toList());
  }

  @Override
  public String toString() {
    // @formatter:off
    try {return Jsonifier.serialize(this);} catch (JsonProcessingException e) {}
    // @formatter:on
    return null;
  }
}
