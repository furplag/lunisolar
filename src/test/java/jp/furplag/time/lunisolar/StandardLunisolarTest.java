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
      .filter(c->c.monthsOfYear.get(0).monthOfYear != 1 || c.monthsOfYear.get(c.monthsOfYear.size() - 1).monthOfYear != 12)
      .collect(Collectors.toList())
      , is(new ArrayList<>()))
    // @formatter:on
    ;
  }

}
