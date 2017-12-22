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
  private static final List<Double> constOfZ;

  /** construct parameter of &theta; . */
  private static final List<Double> constOfTheta;
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

  /**
   * a formula .
   *
   * @author furplag
   *
   */
  private static final class Minion {

    @SuppressWarnings("unused")
    final double value;

    final double sin;

    final double cos;

    private static Minion ofT(final List<Double> constOf, final double terrestrialTime) {
      return new Minion(initialization(constOf, terrestrialTime));
    }

    private Minion(double value) {
      this.value = value;
      sin = Math.sin(value);
      cos = Math.cos(value);
    }
  }

  Precession(double terrestrialTime) {
    this.terrestrialTime = terrestrialTime;
    zeta = Minion.ofT(constOfZeta, terrestrialTime);
    z = Minion.ofT(constOfZ, terrestrialTime);
    theta = Minion.ofT(constOfTheta, terrestrialTime);
  }

  /**
   * initialize using location .
   *
   * @param alpha longitude
   * @param delta latitude
   * @return result
   */
  Precession compute(double alpha, double delta) {
    double r2 = rOf(alpha, delta, (zeta.cos * z.cos * theta.cos), (zeta.sin * z.sin * -1), (-zeta.sin * z.cos * theta.cos), (zeta.cos * z.sin * -1), (-z.cos * theta.sin));
    double r3 = rOf(alpha, delta, (zeta.cos * z.sin * theta.cos), (zeta.sin * z.cos), (-zeta.sin * z.sin * theta.cos), (zeta.cos * z.cos), (-z.sin * theta.sin));
    double r4 = rOf(alpha, delta, (zeta.cos * theta.sin), 0, (-zeta.sin * theta.sin), 0, theta.cos);
    longitude = (Math.atan(r3 / r2)) + (r2 < 0 ? 180.0 : r3 < 0 ? 360.0 : 0);
    latitude = Math.asin(r4) * Formula.degreezr;

    return this;
  }

  /**
   * calculations .
   *
   * @param alpha longitude
   * @param delta latitude
   * @param f
   * @return result
   */
  private double rOf(double alpha, double delta, double... f) {
    final double l = Math.cos(alpha) * Math.cos(delta);
    final double m = Math.sin(alpha) * Math.cos(delta);
    final double n = Math.sin(delta);

    return (f[0] + f[1]) * l + (f[2] + f[3]) * m + (f[4]) * n;
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
