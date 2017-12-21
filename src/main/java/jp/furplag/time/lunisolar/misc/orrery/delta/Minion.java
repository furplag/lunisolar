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

import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Finding values for Delta T, the difference between Terrestrial Time (TT) and Universal Time (UT1).
 *
 * <p>based on <a href="https://github.com/KlausBrunner/solarpositioning">KlausBrunner/solarpositioning</a> .</p>
 *
 * @author furplag
 *
 */
abstract class Minion {

  private final static Set<Minion> minions;

  private final static Minion origin;
  static {
    // @formatter:off
    origin = new Minion(Long.MIN_VALUE, Long.MAX_VALUE) {@Override double estimate(double decimalYear) { return -20 + 32 * Math.pow((decimalYear - 1820) / 100, 2); }};
    minions = Collections.unmodifiableSet(
      Arrays.asList(
        new Minion(-500, 499) {@Override double estimate(double decimalYear) {
          double u = decimalYear / 100;
          return 10583.6 - 1014.41 * u + 33.78311 * Math.pow(u, 2) - 5.952053 * Math.pow(u, 3) - 0.1798452 * Math.pow(u, 4) + 0.022174192 * Math.pow(u, 5) + 0.0090316521 * Math.pow(u, 6);
        }}
      , new Minion(500, 1599) {@Override double estimate(double decimalYear) {
          double u = (decimalYear - 1000) / 100;
          return 1574.2 - 556.01 * u + 71.23472 * Math.pow(u, 2) + 0.319781 * Math.pow(u, 3) - 0.8503463 * Math.pow(u, 4) - 0.005050998 * Math.pow(u, 5) + 0.0083572073 * Math.pow(u, 6);
        }}
      , new Minion(1600, 1699) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1600;
          return 120 - 0.9808 * t - 0.01532 * Math.pow(t, 2) + Math.pow(t, 3) / 7129;
        }}
      , new Minion(1700, 1799) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1700;
          return 8.83 + 0.1603 * t - 0.0059285 * Math.pow(t, 2) + 0.00013336 * Math.pow(t, 3) - Math.pow(t, 4) / 1174000;
        }}
      , new Minion(1800, 1859) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1800;
          return 13.72 - 0.332447 * t + 0.0068612 * Math.pow(t, 2) + 0.0041116 * Math.pow(t, 3) - 0.00037436 * Math.pow(t, 4) + 0.0000121272 * Math.pow(t, 5) - 0.0000001699 * Math.pow(t, 6) + 0.000000000875 * Math.pow(t, 7);
        }}
      , new Minion(1860, 1899) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1860;
          return 7.62 + 0.5737 * t - 0.251754 * Math.pow(t, 2) + 0.01680668 * Math.pow(t, 3) - 0.0004473624 * Math.pow(t, 4) + Math.pow(t, 5) / 233174;
        }}
      , new Minion(1900, 1919) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1900;
          return -2.79 + 1.494119 * t - 0.0598939 * Math.pow(t, 2) + 0.0061966 * Math.pow(t, 3) - 0.000197 * Math.pow(t, 4);
        }}
      , new Minion(1920, 1940) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1920;
          return 21.20 + 0.84493 * t - 0.076100 * Math.pow(t, 2) + 0.0020936 * Math.pow(t, 3);
        }}
      , new Minion(1941, 1960) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1950;
          return 29.07 + 0.407 * t - Math.pow(t, 2) / 233 + Math.pow(t, 3) / 2547;
        }}
      , new Minion(1961, 1985) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 1975;
          return 45.45 + 1.067 * t - Math.pow(t, 2) / 260 - Math.pow(t, 3) / 718;
        }}
      , new Minion(1986, 2004) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 2000;
          return 63.86 + 0.3345 * t - 0.060374 * Math.pow(t, 2) + 0.0017275 * Math.pow(t, 3) + 0.000651814 * Math.pow(t, 4) + 0.00002373599 * Math.pow(t, 5);
        }}
      , new Minion(2005, 2049) {@Override double estimate(double decimalYear) {
          double t = decimalYear - 2000;
          return 62.92 + 0.32217 * t + 0.005589 * Math.pow(t, 2);
        }}
      , new Minion(2050, 2149) {@Override double estimate(double decimalYear) {
          return origin.estimate(decimalYear) - 0.5628 * (2150 - decimalYear);
        }}
      ).stream().collect(Collectors.toSet())
    );
    // @formatter:on
  }

  /**
   * Finding values for Delta T, the difference between Terrestrial Time (TT) and Universal Time (UT1).
   *
   * @param decimalYear the year from 0000-01-01T0Z.
   * @return {@link Minion}
   */
  static Minion of(final double decimalYear) {
    return minions.stream().filter(minion -> minion.range.isValidValue((long) (Math.floor(decimalYear)))).findFirst().orElse(origin);
  }

  protected final ValueRange range;

  private Minion(long min, long max) {
    range = ValueRange.of(min, max);
  }

  /**
   * estimate Delta T for the given year.
   * This is based on Espenak and Meeus, "Five Millennium Canon of Solar Eclipses: -1999 to +3000" (NASA/TP-2006-214141).
   *
   * @param decimalYear the year from 0000-01-01T0Z.
   * @return estimated delta T value (seconds)
   */
  abstract double estimate(double decimalYear);
}
