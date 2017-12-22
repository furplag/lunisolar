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

package jp.furplag.time.lunisolar.misc.orrery;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.ZonedDateTime;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.time.Julian;
import jp.furplag.time.lunisolar.misc.Astror;

public class PrecessionTest {

  @Test
  public void test() {
    ZonedDateTime zdt = ZonedDateTime.parse("2001-01-01T00:00Z");
    IntStream.rangeClosed(-1000, 1000)
      .forEach(y -> {
        final double j = Julian.ofEpochMilli(zdt.withYear(y).toInstant().toEpochMilli());
        IntStream.range(0, 360)
          .forEach(d -> {
            double actual = new Precession(Astror.toTerrestrialTime(j)).compute(Astror.circulate(d - 180), 360 - d).optimize(d);
            assertThat(0.0 <= actual && actual <= 360.0, is(true));
          });
      });

  }
}
