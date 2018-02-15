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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jp.furplag.data.json.Jsonifier;
import jp.furplag.time.lunisolar.SolarTerm.MidClimate;
import jp.furplag.time.lunisolar.SolarTerm.PreClimate;
import jp.furplag.time.lunisolar.misc.Streamr;

/**
 * the month of year represented by lunar month .
 *
 * @author furplag
 *
 */
public final class LunarMonth implements Comparable<LunarMonth>, Serializable {

  /** {@link ValueRange} of the month . */
  @JsonProperty
  final ValueRange range;

  /** meant &quot;節&quot; of {@link SolarTerm} . */
  @JsonProperty
  final List<SolarTerm> preClimates;

  /** meant &quot;中気&quot; of {@link SolarTerm} . */
  @JsonProperty
  final List<SolarTerm> midClimates;

  /** true if that does not have any mid climate . */
  final boolean intercalaryable;

  /** meant &quot;閏月&quot; of lunar month . */
  @JsonProperty
  boolean intercalary;

  /** true if that month contains an instant of winter solstice . */
  final boolean november;

  /** number of the month . */
  @JsonProperty
  int monthOfYear;

  LunarMonth(long fromEpochMilli, long toEpochMilli, List<SolarTerm> solarTerms) {
    range = ValueRange.of(fromEpochMilli, toEpochMilli);
    preClimates = PreClimate.stream(solarTerms).filter(t -> range.isValidValue(t.epochMilli)).collect(Collectors.toList());
    midClimates = MidClimate.stream(solarTerms).filter(t -> range.isValidValue(t.epochMilli)).collect(Collectors.toList());
    november = midClimates.stream().anyMatch(t -> t.longitude == 270);
    intercalaryable = midClimates.isEmpty();
  }

  /**
   *
   *
   * @param solarTerms
   * @param firstDays
   * @return
   */
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
      e.monthOfYear = (monthOfYear[0] % 12 == 0 ? 12 : monthOfYear[0] % 12);
//      System.out.println(e.monthOfYear);
    });
//    System.out.println(monthsOfWinterSolstice.get(0).monthOfYear);
  }

  @Nonnull
  private static List<LunarMonth> monthOfYear(final @Nonnull List<LunarMonth> lunarMonths) {
    final LunarMonth january = Streamr.of(lunarMonths).filter(e -> e.monthOfYear == 1).filtering().first(Comparator.naturalOrder());
    final LunarMonth december = Streamr.of(lunarMonths).filter(e -> (january.range.getMinimum() < e.range.getMinimum()), e -> (e.monthOfYear == 12)).filtering().first(Comparator.naturalOrder());

    return Streamr.of(lunarMonths).filter(e -> ValueRange.of(january.range.getMinimum(), december.range.getMinimum()).isValidValue(e.range.getMinimum())).filtering().toCollection(ArrayList::new);
  }

  @Override
  public int compareTo(LunarMonth o) {
    return Long.compare(range.getMinimum(), o.range.getMinimum());
  }

  @Nullable
  private static LunarMonth firstNovember(final @Nonnull Stream<LunarMonth> lunarMonths, final LunarMonth start) {
    return lunarMonths.filter(e -> e.november && (start == null || start.range.getMinimum() < e.range.getMinimum())).sorted().findFirst().orElse(null);
  }

  /*
  @Nullable
  private static LunarMonth firstOf(final @Nonnull List<LunarMonth> lunarMonths, final int monthOfYear) {
    return Streamr.filtered(lunarMonths, e -> e.monthOfYear == (monthOfYear % 12 == 0 ? 12 : monthOfYear % 12), ArrayList::new).stream().findFirst().orElse(null);
  }

  private static int normalize(final int monthOfYear, final int normalizr) {
    return (monthOfYear % normalizr == 0 ? normalizr : monthOfYear % normalizr);
  }
*/
  @Override
  public String toString() {
    return Jsonifier.serializeLazy(this);
  }
}
