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

import java.time.temporal.ValueRange;

import javax.annotation.Nonnull;

/**
 * code snippets of {@link java.time.temporal.ValueRange} .
 *
 * @author furplag
 *
 */
public final class Ranges {

  /**
   * substitute for {@link ValueRange#isValidValue(long)} .
   *
   * @param range {@link ValueRange}
   * @param value the value to check
   * @return true if the value is valid
   */
  public static boolean contains(final @Nonnull ValueRange range, final long value) {
    return range != null && range.isValidValue(value);
  }

  /**
   * Ranges instances should NOT be constructed in standard programming .
   */
  private Ranges() {}
}
