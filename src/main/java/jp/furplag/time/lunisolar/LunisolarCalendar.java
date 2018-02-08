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
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import jp.furplag.time.Millis;

/**
 * east asian Lunisolar calendar system .
 *
 * @author furplag
 *
 */
public final class LunisolarCalendar {

  final Lunisolar lunisolar;

  final ValueRange rangeOfYear;

  final List<LunarMonth> monthsOfYear;

  public LunisolarCalendar(@Nonnull Lunisolar lunisolar, double julianDate) {
    this.lunisolar = lunisolar;
    final List<SolarTerm> solarTerms = lunisolar.termsOfBase(julianDate);
    monthsOfYear = LunarMonth.constructs(solarTerms, lunisolar.termsToFirstDays(solarTerms));
    rangeOfYear = ValueRange.of(monthsOfYear.get(0).range.getMinimum(), monthsOfYear.get(monthsOfYear.size() - 1).range.getMaximum());
  }

  /**
   * {@inheritDoc}
   */
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
