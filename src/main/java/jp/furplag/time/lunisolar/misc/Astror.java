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

import jp.furplag.time.Julian;
import jp.furplag.time.Millis;

/**
 * code snippets for any of calculation .
 *
 * @author furplag
 *
 */
public class Astror {

  /**
   * normalize the degree to range of 0&deg; - 360&deg; .
   *
   * @param degree the degree
   * @return normalized degree to range of 0&deg; - 360&deg;
   */
  public static double circulate(final double degree) {
    return (degree % 360.0) + (degree < 0.0 ? 360d : 0.0);
  }

  /**
   * calculate the delta between Universal Time (UT) and Terrestrial Time (TT) .
   *
   * @param julianDate the astronomical julian date
   * @return &Delta;T
   */
  public static double toTerrestrialTime(final double julianDate) {
    return (julianDate - Julian.j2000 + (getDeltaOfT(julianDate))) / (Julian.daysOfYear * 100.0);
  }

  public static double getDeltaOfT(final double julianDate) {
    return Julian.ofEpochMilli(((long) (DeltaT.compute(julianDate) * 1000))) - Millis.epoch;
  }

  private Astror() {}
}
