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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.stream.LongStream;

import org.junit.Test;

import jp.furplag.time.Julian;

public class LunarMonthTest {

  @Test
  public void test() {
    // @formatter:off
    assertThat(
      LongStream.of(0, 1, 2, 3)
      .mapToObj(l -> new LunarMonth(l, l + 1L, null))
      .min(new Comparator<LunarMonth>() {
        @Override
        public int compare(LunarMonth o1, LunarMonth o2) {
          return o1.compareTo(o2);
        }}).orElse(new LunarMonth(10, 11, null)).toString()
    , is(new LunarMonth(0, 1, null).toString()));

    assertThat(
      LongStream.of(0, 1, 2, 3)
      .mapToObj(l -> new LunarMonth(l, l + 1L, null))
      .max(new Comparator<LunarMonth>() {
        @Override
        public int compare(LunarMonth o1, LunarMonth o2) {
          return o1.compareTo(o2);
        }}).orElse(new LunarMonth(10, 11, null)).toString()
    , is(new LunarMonth(3, 4, null).toString()));
    // @formatter:on
    final StandardLunisolar lunisolar = new StandardLunisolar(365.242234, 29.530588, ZoneOffset.ofHours(9));
    assertThat(new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(Instant.parse("2017-09-01T00:00:00.000Z").toEpochMilli())).monthsOfYear.get(0).intercalaryze(false), is(false));
    assertThat(new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(Instant.parse("2017-09-01T00:00:00.000Z").toEpochMilli())).monthsOfYear.get(0).intercalaryze(true), is(true));
    assertThat(new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(Instant.parse("2017-09-01T00:00:00.000Z").toEpochMilli())).monthsOfYear.get(5).intercalaryze(false), is(true));
    assertThat(new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(Instant.parse("2017-09-01T00:00:00.000Z").toEpochMilli())).monthsOfYear.get(5).intercalaryze(true), is(true));
  }
}
