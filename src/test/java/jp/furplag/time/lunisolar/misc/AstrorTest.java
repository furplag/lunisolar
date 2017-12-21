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

package jp.furplag.time.lunisolar.misc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.time.Julian;

public class AstrorTest {

  @Test
  public void test() throws ReflectiveOperationException, SecurityException {
    Constructor<Astror> c = Astror.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertThat(c.newInstance() instanceof Astror, is(true));

    assertThat(Astror.toTerrestrialTime(Julian.j2000), is(2.0240131997637844E-8));
  }

  @Test
  public void testCirculate() {
    assertThat(Astror.circulate(0), is(0.0));
    assertThat(Astror.circulate(-0), is(0.0));
    assertThat(Astror.circulate(90), is(90.0));
    assertThat(Astror.circulate(360), is(0.0));
    assertThat(Astror.circulate(-90), is(270.0));
    assertThat(Astror.circulate(360*360), is(0.0));
    assertThat(Astror.circulate(360*-360), is(0.0));
    assertThat(Astror.circulate(90*25), is(90.0));
    assertThat(Astror.circulate(90*-25), is(270.0));
    assertThat(BigDecimal.valueOf(Astror.circulate(36123.123454321)).setScale(9, RoundingMode.HALF_UP).doubleValue(), is(123.123454321));
    assertThat(BigDecimal.valueOf(Astror.circulate(-236.876545679)).setScale(9, RoundingMode.HALF_UP).doubleValue(), is(123.123454321));
  }

  private static double decimalYear(final Method decimalYear, final GregorianCalendar forDate) {
    try {
      return (double) decimalYear.invoke(null, forDate);
   } catch (ReflectiveOperationException | SecurityException e) {}

   return 0.0;
  }

  @Test
  public void testYearize() throws ReflectiveOperationException, SecurityException {
    final ZonedDateTime dateTime = Instant.parse("2001-01-01T00:00:00.000Z").atZone(ZoneOffset.UTC);
    final Method decimalYear = net.e175.klaus.solarpositioning.DeltaT.class.getDeclaredMethod("decimalYear", GregorianCalendar.class);
    decimalYear.setAccessible(true);

    // @formatter:off
    IntStream.rangeClosed(-10000, 10000)
    .forEach(y -> {
        assertThat(
          Objects.toString(y)
        , Astror.yearize(Julian.ofEpochMilli(dateTime.withYear(y).toInstant().toEpochMilli()))
        , is(decimalYear(decimalYear, GregorianCalendar.from(dateTime.withYear(y))))
        );
    });
    // @formatter:on
  }
}
