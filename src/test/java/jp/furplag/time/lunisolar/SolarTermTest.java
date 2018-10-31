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
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.time.Julian;
import jp.furplag.time.Millis;

public class SolarTermTest {

  @Test
  public void test() {
    Instant instant = Instant.parse("2088-03-21T00:00:00.000Z");
    double julianDate = Julian.ofEpochMilli(instant.toEpochMilli());
    assertThat(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate)).compareTo(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate))), is(0));
    assertThat(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate)).compareTo(SolarTerm.ofClosest(julianDate, 0, Lunisolar.ofJulian(julianDate))), is(1));
    assertThat(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate)).compareTo(SolarTerm.ofClosest(julianDate, 180, Lunisolar.ofJulian(julianDate))), is(-1));
  }

  @Test
  public void testOfClosest() throws ReflectiveOperationException {
    final Instant instant = Instant.parse("2033-01-01T00:00:00.000Z");
    final double springEquinox = Lunisolar.Tenpo.springEquinox(Julian.ofEpochMilli(instant.toEpochMilli()));
    final double automnEquinox = Lunisolar.Tenpo.closestTerm(Julian.ofEpochMilli(instant.plus(200, ChronoUnit.DAYS).toEpochMilli()), 180);
    // @formatter:off
    final Map<OffsetDateTime, SolarTerm> terms = IntStream.range(0, 360)
      .mapToObj(d->SolarTerm.ofClosest((d < 180 ? springEquinox : automnEquinox), d, Lunisolar.Tenpo))
      .collect(Collectors.toMap(k -> Lunisolar.Tenpo.atOffset(Millis.toInstant(Lunisolar.Tenpo.asStartOfDay(k.julianDate))), v -> v, (v1, v2) -> v1, LinkedHashMap::new));
    // @formatter:on
    assertNotNull(terms.get(OffsetDateTime.parse("2033-03-20T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-04-04T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-04-20T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-05-05T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-05-21T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-06-05T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-06-21T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-07-07T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-07-22T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-08-07T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-08-23T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-09-07T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-09-23T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-10-08T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-10-23T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-11-07T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-11-22T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-12-07T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2033-12-21T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2034-01-05T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2034-01-20T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2034-02-04T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2034-02-18T00:00+09:00")));
    assertNotNull(terms.get(OffsetDateTime.parse("2034-03-05T00:00+09:00")));
  }
}
