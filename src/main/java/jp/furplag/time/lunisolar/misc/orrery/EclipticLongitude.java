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

import java.util.Arrays;

import jp.furplag.time.lunisolar.misc.Astror;

/**
 * solar term and moon phase .
 *
 * @author furplag
 *
 */
public final class EclipticLongitude {

  /** the Moon. */
  public static final EclipticLongitude Moon;

  /** the Sun. */
  public static final EclipticLongitude Sun;

  static {
    Sun = new EclipticLongitude(Formula.sun);
    Moon = new EclipticLongitude(Formula.moon);
  }

  /** formula for calculation. */
  protected final Formula[] formula;

  private EclipticLongitude(Formula[] formula) {
    this.formula = formula;
  }

  /**
   * calculate longitude of the planet in instant represented by specified julian date .
   *
   * @param julianDate the astronomical julian date
   * @return longitude of the planet in instant represented by specified julian date
   */
  public final double ofJulian(final double julianDate) {
    return getEclipticLongitude(Astror.toTerrestrialTime(julianDate));
  }

  /**
   * calculate longitude of the planet in instant represented by specified julian date .
   *
   * @param terrestrialTime T (terrestrialized julian date)
   * @return longitude of the planet in instant represented by specified julian date
   */
  private double getEclipticLongitude(final double terrestrialTime) {
    return Astror.circulate(Arrays.stream(formula).mapToDouble(f -> f.compute(terrestrialTime)).sum());
  }
}
