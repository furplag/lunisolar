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

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonProperty;

import jp.furplag.data.json.Jsonifier;
import jp.furplag.stream.streamr.Streamizr;
import jp.furplag.time.lunisolar.SolarTerm.PreClimate;

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
  @JsonProperty(index = 0)
  int monthOfYear;

  LunarMonth(long fromEpochMilli, long toEpochMilli, List<SolarTerm> solarTerms) {
    range = ValueRange.of(fromEpochMilli, toEpochMilli);
    preClimates = new ArrayList<>();
    midClimates = Streamizr.of(solarTerms)
      .filteringBy(t -> range.isValidValue(t.epochMilli))
      .peekedAtBy(t -> {
        if (t instanceof PreClimate) {
          preClimates.add(t);

          return null;
        }

        return t;
      }).sortedBy(Comparator.naturalOrder()).toList();
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
    return monthOfYear(
      intercalaryze(IntStream.range(1, firstDays.size())
      .mapToObj(index -> new LunarMonth(firstDays.get(index - 1), firstDays.get(index) - 1, solarTerms))
      .collect(Collectors.toList())));
    // @formatter:on
  }

  @Nonnull
  private static List<LunarMonth> intercalaryze(final @Nonnull List<LunarMonth> lunarMonths) {
    List<LunarMonth> novembers = Streamizr.of(lunarMonths).filteringBy(t -> t.november).toList();
    for (int index = 1; index < novembers.size(); index++) {
      materialize(lunarMonths, novembers.get(index - 1), novembers.get(index));
    }

    return lunarMonths;
  }

  private static void materialize(final @Nonnull List<LunarMonth> lunarMonths, final LunarMonth minimum, final LunarMonth maximum) {
    materialize(lunarMonths.stream().filter(e -> minimum.compareTo(e) < 1 && maximum.compareTo(e) > 0).sorted().collect(Collectors.toList()));
  }

  private static void materialize(final @Nonnull List<LunarMonth> monthsOfWinterSolstice) {
    final boolean hasIntercalary = monthsOfWinterSolstice.size() > 12;
    monthsOfWinterSolstice.stream().filter(e -> hasIntercalary && e.intercalaryable).findFirst().ifPresent(e -> e.intercalary = true);
    final int[] monthOfYear = {10};
    monthsOfWinterSolstice.forEach(e -> {
      monthOfYear[0] += e.intercalary ? 0 : 1;
      e.monthOfYear = (monthOfYear[0] % 12 == 0 ? 12 : monthOfYear[0] % 12);
    });
  }

  @Nonnull
  private static List<LunarMonth> monthOfYear(final @Nonnull List<LunarMonth> lunarMonths) {
    final LunarMonth january = Streamizr.of(lunarMonths).filteringBy(e -> e.monthOfYear == 1).first(null);
    ValueRange r = ValueRange.of(january.range.getMinimum(), Streamizr.of(lunarMonths).filteringBy(e -> e.monthOfYear == 12, e -> january.compareTo(e) < 0).first(january).range.getMinimum());

    return Streamizr.of(lunarMonths).filteringBy(e -> r.isValidValue(e.range.getMinimum())).toList();
  }

  @Override
  public int compareTo(LunarMonth o) {
    return Long.compare(range.getMinimum(), o.range.getMinimum());
  }

  @Override
  public String toString() {
    return Jsonifier.serializeLazy(this);
  }
}
