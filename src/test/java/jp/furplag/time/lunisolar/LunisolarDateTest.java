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

import java.time.OffsetDateTime;

import org.junit.Test;

import jp.furplag.time.Julian;

public class LunisolarDateTest {

  @Test
  public void test() {
    assertThat(LunisolarDate.ofJulian(Julian.j2000).toString(), is("1999年11月25日"));
    assertThat(LunisolarDate.ofEpochMilli(0).toString(), is("1969年11月24日"));
    assertThat(LunisolarDate.ofEpochMilli(OffsetDateTime.parse("2017-06-24T00:00+09:00").toInstant().toEpochMilli()).toString(), is("2017年閏5月1日"));
    assertThat(LunisolarDate.ofEpochMilli(OffsetDateTime.parse("2033-12-22T00:00+09:00").toInstant().toEpochMilli()).toString(), is("2033年閏11月1日"));
    assertThat(LunisolarDate.ofEpochMilli(OffsetDateTime.parse("2033-12-31T00:00+09:00").toInstant().toEpochMilli()).toString(), is("2033年閏11月10日"));
    assertThat(LunisolarDate.ofEpochMilli(OffsetDateTime.parse("2034-01-01T00:00+09:00").toInstant().toEpochMilli()).toString(), is("2033年閏11月11日"));
    assertThat(LunisolarDate.ofEpochMilli(OffsetDateTime.parse("2034-01-19T00:00+09:00").toInstant().toEpochMilli()).toString(), is("2033年閏11月29日"));
  }

}
