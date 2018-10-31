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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.Test;

import jp.furplag.time.Julian;

public class LunisolarTest {

  @Test
  public void test() {
    assertThat(Lunisolar.doOurOwnBest(null, 1), is(1.0));
    assertThat(Lunisolar.doOurOwnBest(new HashMap<>(), 2), is(2.0));
    assertThat(Lunisolar.doOurOwnBest(IntStream.range(0, 10).mapToObj(Integer::valueOf).collect(Collectors.toMap(k -> Double.valueOf(10.0 - k), v -> (v * 1.0), (v1, v2) -> v1)), 2), is(9.0));
  }

  @Test
  public void testOf2017() {
    final OffsetDateTime t = Instant.parse("1844-12-01T12:00:00.000Z").atOffset(ZoneOffset.ofHours(9));
    LunisolarCalendar lunisolar2017 = new LunisolarCalendar(Lunisolar.ofJulian(Julian.ofEpochMilli(t.with(ChronoField.YEAR, 2017).toInstant().toEpochMilli())), Julian.ofEpochMilli(t.with(ChronoField.YEAR, 2017).toInstant().toEpochMilli()));
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
    final LunisolarCalendar lunisolar2033 = new LunisolarCalendar(Lunisolar.ofJulian(Julian.ofEpochMilli(t.with(ChronoField.YEAR, 2033).toInstant().toEpochMilli())), Julian.ofEpochMilli(t.with(ChronoField.YEAR, 2033).toInstant().toEpochMilli()));
    assertThat(lunisolar2033.monthsOfYear.size(), is(13));
    assertThat(lunisolar2033.monthsOfYear.stream().filter(e->e.intercalary).findAny().orElse(new LunarMonth(0, 1, null)).monthOfYear, is(11));
    // @formatter:off800
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
  public void paintItGreen() throws ReflectiveOperationException {
    Lunisolar lunisolar = new StandardLunisolar(365.242234, 29.530588, ZoneOffset.ofHours(9));

    lunisolar.latestNewMoon(Julian.ofEpochMilli(Instant.parse("2034-01-20T00:00:00.000Z").toEpochMilli()));
    lunisolar.closestTerm(Julian.ofEpochMilli(Instant.parse("2034-01-20T00:00:00.000Z").toEpochMilli()), 270);

    OffsetDateTime offsetDateTime = Instant.parse("2033-01-01T00:00:00.000Z").atOffset(lunisolar.zoneOffset);
    // @formatter:off
    LongStream.rangeClosed(0, 400)
      .mapToObj(d -> offsetDateTime.plusDays(d).toInstant().toEpochMilli())
      .mapToDouble(Julian::ofEpochMilli)
      .forEach(j -> {
        lunisolar.closestTerm(j, 359.99);
        lunisolar.springEquinox(j);
        lunisolar.closestTerm(j, 90);
        lunisolar.closestTerm(j, 180);
        lunisolar.winterSolstice(j);
        lunisolar.closestTerm(j, 0.01);
      });
    // @formatter:on

    Lunisolar paintItGreen = new Lunisolar(365.242234, 29.530588, ZoneOffset.ofHours(9), 1E-30, -1) {
      @Override
      List<Long> termsToFirstDays(List<SolarTerm> solarTerms) {
        return lunisolar.termsToFirstDays(solarTerms);
      }

      @Override
      List<SolarTerm> termsOfBase(double julianDate) {
        return lunisolar.termsOfBase(julianDate);
      }

      @Override
      double closestTerm(double julianDate, double degree) {
        // TODO 自動生成されたメソッド・スタブ
        return lunisolar.closestTerm(julianDate, degree);
      }
    };
    paintItGreen.latestNewMoon(Julian.ofEpochMilli(Instant.parse("2034-01-20T00:00:00.000Z").toEpochMilli()));
    paintItGreen.closestTerm(Julian.ofEpochMilli(Instant.parse("2034-01-20T00:00:00.000Z").toEpochMilli()), 270);
  }
}
