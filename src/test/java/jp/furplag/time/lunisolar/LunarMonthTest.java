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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
      .sorted().findFirst().orElse(new LunarMonth(10, 11, null)).toString()
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
  }

  @Test
  public void paintItGreen() throws Throwable {
    try {
      LunarMonth.constructs(null, null);
      fail("must raise NPE .");
    } catch (NullPointerException ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    try {
      LunarMonth.constructs(null, Arrays.asList(0L, 1L, 2L));
      fail("must raise NPE .");
    } catch (NullPointerException ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    try {
      SolarTerm.ofClosest(Julian.ofEpochMilli(0), 0, Lunisolar.Tenpo);
      LunarMonth.constructs(Stream.of(90, 105, 120).map((d) -> SolarTerm.ofClosest(Julian.ofEpochMilli(0), d, Lunisolar.Tenpo)).collect(Collectors.toList()), null);
      fail("must raise NPE .");
    } catch (NullPointerException ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    try {
      MethodHandle intercalaryze = null;
      intercalaryze = MethodHandles.privateLookupIn(LunarMonth.class, MethodHandles.lookup()).findStatic(LunarMonth.class, "intercalaryze", MethodType.methodType(List.class, List.class));
      intercalaryze.invoke(null);
      fail("must raise NPE .");
    } catch (NullPointerException ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    try {
      MethodHandle materialize = null;
      materialize = MethodHandles.privateLookupIn(LunarMonth.class, MethodHandles.lookup()).findStatic(LunarMonth.class, "materialize", MethodType.methodType(void.class, List.class, LunarMonth.class, LunarMonth.class));
      materialize.invoke(null, null, null);
      fail("must raise NPE .");
    } catch (NullPointerException ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    try {
      MethodHandle materialize = null;
      materialize = MethodHandles.privateLookupIn(LunarMonth.class, MethodHandles.lookup()).findStatic(LunarMonth.class, "materialize", MethodType.methodType(void.class, List.class));
      materialize.invoke(null);
      fail("must raise NPE .");
    } catch (NullPointerException ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
    try {
      MethodHandle monthOfYear = null;
      monthOfYear = MethodHandles.privateLookupIn(LunarMonth.class, MethodHandles.lookup()).findStatic(LunarMonth.class, "monthOfYear", MethodType.methodType(List.class, List.class));
      monthOfYear.invoke(null);
      fail("must raise NPE .");
    } catch (NullPointerException ex) {
      assertThat(ex instanceof NullPointerException, is(true));
    }
  }
}
