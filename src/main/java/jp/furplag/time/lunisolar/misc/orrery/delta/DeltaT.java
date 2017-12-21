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
package jp.furplag.time.lunisolar.misc.orrery.delta;

import jp.furplag.time.lunisolar.misc.Astror;

/**
 * Finding values for Delta T, the difference between Terrestrial Time (TT) and Universal Time (UT1).
 *
 * <p>based on <a href="https://github.com/KlausBrunner/solarpositioning">KlausBrunner/solarpositioning</a> .</p>
 *
 * @author furplag
 *
 */
public final class DeltaT {

  /**
   * estimate Delta T for the given year.
   * This is based on Espenak and Meeus, "Five Millennium Canon of Solar Eclipses: -1999 to +3000" (NASA/TP-2006-214141).
   *
   * @param julianDate the astronomical julian date
   * @return estimated delta T value (seconds)
   */
  public static double estimate(final double julianDate) {
    double decimalYear = Astror.yearize(julianDate);

    return Minion.of(decimalYear).estimate(decimalYear);
  }

  /**
   * DeltaT instances should NOT be constructed in standard programming .
   */
  private DeltaT() {}
}
