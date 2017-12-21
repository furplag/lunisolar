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
import java.util.List;
import java.util.stream.IntStream;

import jp.furplag.time.lunisolar.misc.Astror;

/**
 * precession of the sun .
 *
 * @author furplag
 *
 */
public class Precession {

  /** construct parameter of &zeta; . */
  static final List<Double> constOfZeta;

  /** construct parameter of &Zeta; . */
  static final List<Double> constOfZ;

  /** construct parameter of &theta; . */
  static final List<Double> constOfTheta;
  static {
    constOfZeta = Arrays.asList(2306.2181, 0.30188, 0.017998);
    constOfZ = Arrays.asList(2306.2181, 1.09468, 0.018203);
    constOfTheta = Arrays.asList(2004.3109, -0.42665, -0.041833);
  }

  final double terrestrialTime;

  final Minion zeta;

  final Minion z;

  final Minion theta;

  double longitude;

  double latitude;

  static final class Minion {

    private final double value;

    private final double sin;

    private final double cos;

    private static Minion ofT(final List<Double> constOf, final double terrestrialTime) {
      return new Minion(initialization(constOf, terrestrialTime));
    }

    private Minion(double value) {
      this.value = value;
      this.sin = Math.sin(value);
      this.cos = Math.cos(value);
    }

    @Override
    public String toString() {
      // @formatter:off
      return new StringBuilder("Minion { ")
        .append("value : ").append(value)
        .append(", sin : ").append(sin)
        .append(", cos : ").append(cos)
        .append(" }")
        .toString();
      // @formatter:on
    }
  }

  Precession(double terrestrialTime) {
    this.terrestrialTime = terrestrialTime;
    zeta = Minion.ofT(constOfZeta, terrestrialTime);
    z = Minion.ofT(constOfZ, terrestrialTime);
    theta = Minion.ofT(constOfTheta, terrestrialTime);
  }

  Precession compute(double alpha, double delta) {
    final double l = Math.cos(alpha) * Math.cos(delta);
    final double m = Math.sin(alpha) * Math.cos(delta);
    final double n = Math.sin(delta);
    double r2 = (((zeta.cos * z.cos * theta.cos) - (zeta.sin * z.sin)) * l) + (((-zeta.sin * z.cos * theta.cos) - (zeta.cos * z.sin)) * m) + ((-z.cos * theta.sin) * n);
    double r3 = (((zeta.cos * z.sin * theta.cos) + (zeta.sin * z.cos)) * l) + (((-zeta.sin * z.sin * theta.cos) + (zeta.cos * z.cos)) * m) + ((-z.sin * theta.sin) * n);
    double r4 = (zeta.cos * theta.sin * l) + (-zeta.sin * theta.sin * m) + (theta.cos * n);

    longitude = (Math.atan(r3 / r2)) + (r2 < 0 ? 180.0 : r3 < 0 ? 360.0 : 0);
    latitude = Math.asin(r4) * Formula.degreezr;

    return this;
  }

  /**
   * optimize longitude with precession .
   *
   * @param longitude the longitude of the sun
   * @return optimized longitude
   */
  double optimize(double longitude) {
    return Astror.circulate(longitude - this.longitude);
  }

  private static double initialization(final List<Double> constOf, final double terrestrialTime) {
    // @formatter:off
    return
      constOf.stream().mapToDouble(c-> c *
        IntStream.range(0, constOf.indexOf(c) + 1)
        .mapToDouble((nope)->Double.valueOf(terrestrialTime))
        .reduce(1.0, (o1, o2) -> o1 * o2)
      ).sum() / 3600.0 * Formula.radianizr;
    // @formatter:on
  }

}