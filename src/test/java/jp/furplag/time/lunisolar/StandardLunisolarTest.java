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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.Test;

import jp.furplag.time.Julian;

public class StandardLunisolarTest {

  @Test
  public void test() {
    final OffsetDateTime t = Instant.parse("1844-12-01T12:00:00.000Z").atOffset(ZoneOffset.ofHours(9));
    final StandardLunisolar lunisolar = new StandardLunisolar(365.242234, 29.530588, ZoneOffset.ofHours(9));
    // @formatter:off

    assertThat(
      LongStream.rangeClosed(1844, 2300)
      .mapToObj(y->new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(t.with(ChronoField.YEAR, y).toInstant().toEpochMilli())))
      .filter(c->(c.monthsOfYear.get(0).monthOfYear != 1 || c.monthsOfYear.get(c.monthsOfYear.size() - 1).monthOfYear != 12))
      .map(c->{
        System.out.println(c);
        return c;
      })
      .collect(Collectors.toList())
      , is(new ArrayList<>()))
    ;

  }

  @Test
  public void testOf2017() {
    final OffsetDateTime t = Instant.parse("1844-12-01T12:00:00.000Z").atOffset(ZoneOffset.ofHours(9));
    final StandardLunisolar lunisolar = new StandardLunisolar(365.242234, 29.530588, ZoneOffset.ofHours(9));
    LunisolarCalendar lunisolar2017 = new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(t.with(ChronoField.YEAR, 2017).toInstant().toEpochMilli()));
    assertThat(lunisolar2017.monthsOfYear.size(), is(13));
    assertThat(lunisolar2017.monthsOfYear.stream().filter(e->e.intercalary).findAny().orElse(new LunarMonth(0, 1, null)).monthOfYear, is(5));
    // @formatter:off
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 1)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-01-28T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-02-25T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 2)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-02-26T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-03-27T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 3)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-03-28T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-04-25T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 4)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-04-26T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-05-25T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 5) && !e.intercalaryable).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-05-26T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-06-23T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 5) && e.intercalaryable).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-06-24T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-07-22T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 6)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-07-23T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-08-21T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 7)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-08-22T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-09-19T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 8)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-09-20T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-10-19T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 9)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-10-20T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-11-17T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 10)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-11-18T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2017-12-17T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 11)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2017-12-18T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2018-01-16T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2017.monthsOfYear.stream().filter(e -> (e.monthOfYear == 12)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2018-01-17T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2018-02-15T23:59:59.999+09:00").toInstant().toEpochMilli())));
    // @formatter:on
  }

  @Test
  public void testOf2033() {
    final OffsetDateTime t = Instant.parse("1844-12-01T12:00:00.000Z").atOffset(ZoneOffset.ofHours(9));
    final StandardLunisolar lunisolar = new StandardLunisolar(365.242234, 29.530588, ZoneOffset.ofHours(9));
    LunisolarCalendar lunisolar2033 = new LunisolarCalendar(lunisolar, Julian.ofEpochMilli(t.with(ChronoField.YEAR, 2033).toInstant().toEpochMilli()));
    assertThat(lunisolar2033.monthsOfYear.size(), is(13));
    assertThat(lunisolar2033.monthsOfYear.stream().filter(e->e.intercalary).findAny().orElse(new LunarMonth(0, 1, null)).monthOfYear, is(11));
    // @formatter:off
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 1)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-01-31T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-02-28T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 2)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-03-01T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-03-30T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 3)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-03-31T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-04-28T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 4)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-04-29T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-05-27T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 5)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-05-28T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-06-26T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 6)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-06-27T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-07-25T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 7)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-07-26T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-08-24T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 8)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-08-25T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-09-22T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 9)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-09-23T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-10-22T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 10)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-10-23T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-11-21T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 11) && !e.intercalaryable).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-11-22T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2033-12-21T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 11) && e.intercalaryable).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2033-12-22T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2034-01-19T23:59:59.999+09:00").toInstant().toEpochMilli())));
    assertThat(
      lunisolar2033.monthsOfYear.stream().filter(e -> (e.monthOfYear == 12)).findAny().orElse(new LunarMonth(0, 1, null)).range
    , is(ValueRange.of(OffsetDateTime.parse("2034-01-20T00:00:00.000+09:00").toInstant().toEpochMilli(), OffsetDateTime.parse("2034-02-18T23:59:59.999+09:00").toInstant().toEpochMilli())));
    // @formatter:on
  }

  @Test
  public void paintItGreen() {
    try {
      Lunisolar.Tenpo.termsToFirstDays(null);
      fail("must raise NPE .");
    } catch (Exception ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }

    new StandardLunisolar(Lunisolar.Tenpo.daysOfYear, Lunisolar.Tenpo.daysOfMonth, Lunisolar.Tenpo.zoneOffset, 1E-20, Lunisolar.loopLimitDefault)
      .closestTerm(0, 270);
    new StandardLunisolar(Lunisolar.Tenpo.daysOfYear, Lunisolar.Tenpo.daysOfMonth, Lunisolar.Tenpo.zoneOffset, Lunisolar.precisionDefault, 1)
      .closestTerm(0, 270);
  }
}
