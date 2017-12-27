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

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class LunisolarTest {

  @Test
  public void test() {
    assertThat(Lunisolar.doOurOwnBest(null, 1), is(1.0));
    assertThat(Lunisolar.doOurOwnBest(new HashMap<>(), 2), is(2.0));
    assertThat(Lunisolar.doOurOwnBest(IntStream.range(0, 10).mapToObj(Integer::valueOf).collect(Collectors.toMap(k -> Double.valueOf(10.0 - k), v -> (v * 1.0), (v1, v2) -> v1)), 2), is(9.0));
  }
}
