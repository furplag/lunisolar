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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonProperty;

import jp.furplag.data.json.Jsonifier;
import jp.furplag.sandbox.stream.Streamr;
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

  /**
   *
   *
   * @param fromEpochMilli milliseconds @from
   * @param toEpochMilli milliseconds @to
   * @param solarTerms list of {@link SolarTerm}
   */
  LunarMonth(long fromEpochMilli, long toEpochMilli, List<SolarTerm> solarTerms) {
    range = ValueRange.of(fromEpochMilli, toEpochMilli);
    preClimates = new ArrayList<>();
    midClimates = Streamr.stream(solarTerms).filter((t) -> range.isValidValue(t.epochMilli))
      .map(t -> {
        if (t instanceof PreClimate) {
          preClimates.add(t);

          return null;
        }

        return t;
      })
      .filter(Objects::nonNull)
      .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    november = midClimates.stream().anyMatch(t -> t.longitude == 270);
    intercalaryable = midClimates.isEmpty();
  }

  /**
   * constructing the calendar of the year .
   *
   * @param solarTerms list of {@link SolarTerm}
   * @param firstDays list of first day of months
   * @return the calendar of the year
   */
  static List<LunarMonth> constructs(final @lombok.NonNull List<SolarTerm> solarTerms, final @lombok.NonNull List<Long> firstDays) {
    // @formatter:off
    return monthOfYear(
      intercalaryze(IntStream.range(1, firstDays.size())
      .mapToObj(index -> new LunarMonth(firstDays.get(index - 1), firstDays.get(index) - 1, solarTerms))
      .collect(Collectors.toList())));
    // @formatter:on
  }

  /**
   * constructing the calendar of the year .
   *
   * @param list of {@link LunarMonth}
   * @return optimized lunarMonths
   */
  private static List<LunarMonth> intercalaryze(final @lombok.NonNull List<LunarMonth> lunarMonths) {
    List<LunarMonth> novembers = Streamr.stream(lunarMonths).filter(t -> t.november).collect(Collectors.toList());
    for (int index = 1; index < novembers.size(); index++) {
      materialize(lunarMonths, novembers.get(index - 1), novembers.get(index));
    }

    return lunarMonths;
  }

  /**
   * constructing the calendar of the year .
   *
   * @param lunarMonths list of {@link LunarMonth}
   * @param minimum first of winter solstice
   * @param maximum last of winter solstice
   */
  private static void materialize(final @lombok.NonNull List<LunarMonth> lunarMonths, final LunarMonth minimum, final LunarMonth maximum) {
    materialize(Streamr.stream(lunarMonths).filter((e) -> minimum.compareTo(e) < 1 && maximum.compareTo(e) > 0).sorted().collect(Collectors.toList()));
  }

  /**
   * constructing the calendar of the year .
   *
   * @param monthsOfWinterSolstice the list of months from november of last year to november of this year
   */
  private static void materialize(final @lombok.NonNull List<LunarMonth> monthsOfWinterSolstice) {
    final boolean hasIntercalary = monthsOfWinterSolstice.size() > 12;
    monthsOfWinterSolstice.stream().filter(e -> hasIntercalary && e.intercalaryable).findFirst().ifPresent(e -> e.intercalary = true);
    final int[] monthOfYear = {10};
    monthsOfWinterSolstice.forEach(e -> {
      monthOfYear[0] += e.intercalary ? 0 : 1;
      e.monthOfYear = (monthOfYear[0] % 12 == 0 ? 12 : monthOfYear[0] % 12);
    });
  }

  /**
   * constructing the calendar of the year .
   *
   * @param list of {@link LunarMonth}
   * @return optimized lunarMonths
   */
  private static List<LunarMonth> monthOfYear(final @lombok.NonNull List<LunarMonth> lunarMonths) {
    final LunarMonth january = Streamr.Filter.filtering(lunarMonths, (e) -> e.monthOfYear == 1).findFirst().orElse(null);
    // @formatter:off
    ValueRange r = ValueRange.of(
        january.range.getMinimum()
      , Optional.ofNullable(Streamr.Filter.filtering(lunarMonths, (e) -> e.monthOfYear == 12, (e) -> january.compareTo(e) < 0).findFirst().orElse(null)).orElse(january).range.getMinimum()
    );
    // @formatter:on

    return Streamr.collect(Streamr.stream(lunarMonths).filter((e) -> r.isValidValue(e.range.getMinimum())), ArrayList::new);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(LunarMonth o) {
    return Long.compare(range.getMinimum(), o.range.getMinimum());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return Jsonifier.serialize(this);
  }
}
