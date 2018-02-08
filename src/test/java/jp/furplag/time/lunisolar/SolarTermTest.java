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

import org.junit.Test;

import jp.furplag.time.Julian;

public class SolarTermTest {

  @Test
  public void test() {
    Instant instant = Instant.parse("2088-03-21T00:00:00.000Z");
    double julianDate = Julian.ofEpochMilli(instant.toEpochMilli());
    assertThat(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate)).compareTo(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate))), is(0));
    assertThat(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate)).compareTo(SolarTerm.ofClosest(julianDate, 0, Lunisolar.ofJulian(julianDate))), is(1));
    assertThat(SolarTerm.ofClosest(julianDate, 90, Lunisolar.ofJulian(julianDate)).compareTo(SolarTerm.ofClosest(julianDate, 180, Lunisolar.ofJulian(julianDate))), is(-1));
  }

}
