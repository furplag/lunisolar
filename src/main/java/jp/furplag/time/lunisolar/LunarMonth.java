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
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.furplag.time.Millis;
import jp.furplag.time.lunisolar.SolarTerm.MidClimate;
import jp.furplag.time.lunisolar.SolarTerm.PreClimate;

public final class LunarMonth implements Comparable<LunarMonth>, Serializable {

  final ValueRange range;

  final List<SolarTerm> preClimates;

  final List<SolarTerm> midClimates;

  final boolean intercalary;

  boolean intercalaryInCalendar;

  final boolean november;

  int monthOfYear;

  LunarMonth(long fromEpochMilli, long toEpochMilli, List<SolarTerm> climates) {
    range = ValueRange.of(fromEpochMilli, toEpochMilli);
    preClimates = Optional.ofNullable(climates).orElse(new ArrayList<>()).stream().filter(t->t instanceof PreClimate).filter(t->range.isValidValue(Millis.ofJulian(t.julianDate))).collect(Collectors.toList());
    midClimates = Optional.ofNullable(climates).orElse(new ArrayList<>()).stream().filter(t->t instanceof MidClimate).filter(t->range.isValidValue(Millis.ofJulian(t.julianDate))).collect(Collectors.toList());
    november = midClimates.stream().anyMatch(t -> t.longitude == 270);
    intercalary = midClimates != null && midClimates.isEmpty();
  }

  @Override
  public int compareTo(LunarMonth o) {
    return Double.compare(range.getMinimum(), o.range.getMinimum());
  }

  @Override
  public String toString() {
    // @formatter:off
    return
      (intercalaryInCalendar ? "閏" : "")
      + monthOfYear
      + ", range: "
      + Instant.ofEpochMilli(range.getMinimum())
      + " - "
      + Instant.ofEpochMilli(range.getMaximum())
      + " ("
      + Duration.of(range.getMaximum() + 1 - range.getMinimum(), ChronoUnit.MILLIS).toDays()
      + ")"
      + ", intercalary: "
      + intercalary
      + ", preClimates: "
      + preClimates.stream().map(c->c.longitude).collect(Collectors.toList()).toString()
      + ", midClimates: "
      + midClimates.stream().map(c->c.longitude).collect(Collectors.toList()).toString()
      ;
    // @formatter:on
  }
}