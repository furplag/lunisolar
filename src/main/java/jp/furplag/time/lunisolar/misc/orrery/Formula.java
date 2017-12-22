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
import java.util.Objects;
import java.util.Optional;

/**
 * a formula for calculates the longitude of the sun and moon .
 *
 * @author furplag
 *
 */
class Formula implements Comparable<Formula> {

  /** the type of formula . */
  static enum FormulaType {
    // @formatter:off
    Normal(0)
  , Exclusive(1);
    // @formatter:on

    private final int id;

    private FormulaType(int id) {
      this.id = id;
    }

    boolean is(int id) {
      return this.id == id;
    }

    boolean is(FormulaType formulaType) {
      return this.equals(formulaType);
    }

    static FormulaType valueOf(final int id) {
      return Arrays.stream(FormulaType.values()).filter(t -> t.is(id)).findFirst().orElseGet(null);
    }
  }

  /** radian to angle . */
  static final double radianizr;

  /** angle to radian . */
  static final double degreezr;

  /** coefficients of the Moon. */
  static final Formula[] sun;

  /** coefficients of the Moon. */
  static final Formula[] moon;
  static {
    radianizr = Math.PI / 180.0;
    degreezr = 180.0 / Math.PI;

    // @formatter:off
    sun =
      construct(new double[][]{
        { 0, 0.0004, 31557.0, 161.0}
      , { 0, 0.0004, 29930.0, 48.0}
      , { 0, 0.0005, 2281.0, 221.0}
      , { 0, 0.0005, 155.0, 118.0}
      , { 0, 0.0006, 33718.0, 316.0}
      , { 0, 0.0007, 9038.0, 64.0}
      , { 0, 0.0007, 3035.0, 110.0}
      , { 0, 0.0007, 65929.0, 45.0}
      , { 0, 0.0013, 22519.0, 352.0}
      , { 0, 0.0015, 45038.0, 254.0}
      , { 0, 0.0018, 445267.0, 208.0}
      , { 0, 0.0018, 19.0, 159.0}
      , { 0, 0.002, 32964.0, 158.0}
      , { 1, -0.0048, 35999.0, 268.0}
      , { 0, 0.02, 71998.1, 265.1}
      , { 0, 1.9147, 35999.05, 267.52}
      , { 0, 280.4659, 0.0, 0.0}
      , { 1, 36000.7695, 0.0, 0.0}
      , { 0, -0.0057, 0.0, 0.0 }
      , { 0, 0.0048, 1934.0, 145.0 }
      });

    moon =
      construct(new double[][]{
        { 0.0, 0.0003, 958465.0, 340.0 }
      , { 0.0, 0.0003, 381404.0, 354.0 }
      , { 0.0, 0.0003, 349472.0, 337.0 }
      , { 0.0, 0.0003, 1808933.0, 58.0 }
      , { 0.0, 0.0003, 549197.0, 220.0 }
      , { 0.0, 0.0003, 4067.0, 70.0 }
      , { 0.0, 0.0003, 2322131.0, 191.0 }
      , { 0.0, 0.0004, 39871.0, 223.0 }
      , { 0.0, 0.0004, 12006.0, 187.0 }
      , { 0.0, 0.0005, 1908795.0, 90.0 }
      , { 0.0, 0.0005, 1745069.0, 24.0 }
      , { 0.0, 0.0005, 509131.0, 242.0 }
      , { 0.0, 0.0006, 111869.0, 38.0 }
      , { 0.0, 0.0006, 2258267.0, 156.0 }
      , { 0.0, 0.0007, 790672.0, 114.0 }
      , { 0.0, 0.0007, 405201.0, 50.0 }
      , { 0.0, 0.0007, 485333.0, 186.0 }
      , { 0.0, 0.0007, 27864.0, 127.0 }
      , { 0.0, 0.0008, 1403732.0, 98.0 }
      , { 0.0, 0.0009, 858602.0, 129.0 }
      , { 0.0, 0.0011, 1920802.0, 186.0 }
      , { 0.0, 0.0012, 1267871.0, 249.0 }
      , { 0.0, 0.0016, 1856938.0, 152.0 }
      , { 0.0, 0.0018, 401329.0, 274.0 }
      , { 0.0, 0.0021, 990397.0, 357.0 }
      , { 0.0, 0.0021, 71998.0, 85.0 }
      , { 0.0, 0.0021, 341337.0, 16.0 }
      , { 0.0, 0.0022, 818536.0, 151.0 }
      , { 0.0, 0.0023, 922466.0, 163.0 }
      , { 0.0, 0.0024, 99863.0, 122.0 }
      , { 0.0, 0.0026, 1379739.0, 17.0 }
      , { 0.0, 0.0027, 918399.0, 182.0 }
      , { 0.0, 0.0028, 1934.0, 145.0 }
      , { 0.0, 0.0037, 541062.0, 259.0 }
      , { 0.0, 0.0038, 1781068.0, 21.0 }
      , { 0.0, 0.004, 1331734.0, 283.0 }
      , { 0.0, 0.004, 1844932.0, 56.0 }
      , { 0.0, 0.004, 133.0, 29.0 }
      , { 0.0, 0.005, 481266.0, 205.0 }
      , { 0.0, 0.0052, 31932.0, 107.0 }
      , { 0.0, 0.0068, 926533.0, 323.0 }
      , { 0.0, 0.0079, 449334.0, 188.0 }
      , { 0.0, 0.0085, 826671.0, 111.0 }
      , { 0.0, 0.01, 1431597.0, 315.0 }
      , { 0.0, 0.0107, 1303870.0, 246.0 }
      , { 0.0, 0.011, 489205.0, 142.0 }
      , { 0.0, 0.0125, 1443603.0, 52.0 }
      , { 0.0, 0.0154, 75870.0, 41.0 }
      , { 0.0, 0.0304, 513197.9, 222.5 }
      , { 0.0, 0.0347, 445267.1, 27.9 }
      , { 0.0, 0.0409, 441199.8, 47.4 }
      , { 0.0, 0.0458, 854535.2, 148.2 }
      , { 0.0, 0.0533, 1367733.1, 280.7 }
      , { 0.0, 0.0571, 377336.3, 13.2 }
      , { 0.0, 0.0588, 63863.5, 124.2 }
      , { 0.0, 0.1144, 966404.0, 276.5 }
      , { 0.0, 0.1851, 35999.05, 87.53 }
      , { 0.0, 0.2136, 954397.74, 179.93 }
      , { 0.0, 0.6583, 890534.22, 145.7 }
      , { 0.0, 1.274, 413335.35, 10.74 }
      , { 0.0, 6.2888, 477198.868, 44.963 }
      , { 0.0, 218.3162, 0.0, 0.0 }
      , { 1.0, 481267.8809, 0.0, 0.0 }
    });
    // @formatter:on
  }

  /** {@link FormulaType} */
  final FormulaType formulaType;

  final double amplitude;

  final double angularVelocity;

  final double initialPhase;

  private Formula(double... perturbations) {
    this.formulaType = Objects.requireNonNull(FormulaType.valueOf((int) perturbations[0]));
    this.amplitude = perturbations[1];
    this.angularVelocity = perturbations[2];
    this.initialPhase = perturbations[3];
  }

  @Override
  public int compareTo(Formula o) {
    return o == null ? 1 : formulaType.compareTo(o.formulaType);
  }

  /**
   * calculations .
   *
   * @param terrestrialTime T (terrestrialized julian date)
   * @return result
   */
  double estimate(double terrestrialTime) {
    // @formatter:off
    return
      (formulaType.is(FormulaType.Exclusive) ? (amplitude * terrestrialTime) : amplitude) * Math.cos(((angularVelocity * terrestrialTime) + initialPhase) * radianizr);
    // @formatter:on
  }

  /**
   * initialization of coefficient
   *
   * @param perturbations {@link #sun} or  {@link #moon}
   * @return {@link Formula formulas}
   */
  private static Formula[] construct(final double[]... perturbations) {
    return Arrays.stream(Optional.ofNullable(perturbations).orElse(new double[][] {{0, 1, 1, 0}})).map(Formula::new).toArray(Formula[]::new);
  }
}
