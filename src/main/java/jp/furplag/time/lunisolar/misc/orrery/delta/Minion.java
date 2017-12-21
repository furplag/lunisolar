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
import java.util.Objects;
import java.util.Optional;
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
    origin = new Minion(Long.MIN_VALUE, Long.MAX_VALUE) {
      @Override double estimate(double decimalYear) {
        return -20 + 32 * Math.pow((decimalYear - 1820) / 100, 2);
      }
    };

    minions = Collections.unmodifiableSet(
      Arrays.asList(
          new StandardMinion(-500, 499, 0, 100, 10583.6, -1014.41, 33.78311, -5.952053, -0.1798452, 0.022174192, 0.0090316521, 0)
        , new StandardMinion(500, 1599, 1000, 100, 1574.2, -556.01, 71.23472, 0.319781, -0.8503463, -0.005050998, 0.0083572073, 0)
        , new MedievalMinion(1600, 1699, 1600, 120, -0.9808, -0.01532, 1, 1, 7129, 0, 1, 0, 1)
        , new MedievalMinion(1700, 1799, 1700, 8.83, 0.1603, -0.0059285, 1, 0.00013336, 1, -1, 1174000, 0, 1)
        , new StandardMinion(1800, 1859, 1800, 1, 13.72, -0.332447, 0.0068612, 0.0041116, -0.00037436, 0.0000121272, -0.0000001699, 0.000000000875)
        , new MedievalMinion(1860, 1899, 1860, 7.62, 0.5737, -0.251754, 1, 0.01680668, 1, -0.0004473624, 1, 1, 233174)
        , new StandardMinion(1900, 1919, 1900, 1, -2.79, 1.494119, -0.0598939, 0.0061966, -0.000197, 0, 0, 0)
        , new StandardMinion(1920, 1940, 1920, 1, 21.20, 0.84493, -0.076100, 0.0020936, 0, 0, 0, 0)
        , new MedievalMinion(1941, 1960, 1950, 29.07, 0.407, -1, 233, 1, 2547, 0, 1, 0, 1)
        , new MedievalMinion(1961, 1985, 1975, 45.45, 1.067, -1, 260, -1, 718, 0, 1, 0, 1)
        , new StandardMinion(1986, 2004, 2000, 1, 63.86, 0.3345, -0.060374, 0.0017275, 0.000651814, 0.00002373599, 0, 0)
        , new StandardMinion(2005, 2049, 2000, 1, 62.92, 0.32217, 0.005589, 0, 0, 0, 0, 0)

        , new Minion(2050, 2149) {@Override double estimate(double decimalYear) {
          return origin.estimate(decimalYear) - 0.5628 * (2150 - decimalYear);
        }}
      ).stream().collect(Collectors.toSet())
    );
    // @formatter:on
  }

  private static final class StandardMinion extends Minion {
    private StandardMinion(long min, long max, double... parameters) {
      super(min, max, parameters);
    }

    @Override
    public double estimate(double decimalYear) {
      double ut = (decimalYear - n[0]) / n[1];

      return n[2] + (n[3] * ut) + (n[4] * Math.pow(ut, 2)) + (n[5] * Math.pow(ut, 3)) + (n[6] * Math.pow(ut, 4)) + (n[7] * Math.pow(ut, 5)) + (n[8] * Math.pow(ut, 6)) + (n[9] * Math.pow(ut, 7));
    }
  }

  private static final class MedievalMinion extends Minion {
    private MedievalMinion(long min, long max, double... parameters) {
      super(min, max, parameters);
    }

    @Override
    public double estimate(double decimalYear) {
      double t = decimalYear - n[0];

      return n[1] + (n[2] * t) + (n[3] * Math.pow(t, 2) / n[4]) + (n[5] * Math.pow(t, 3) / n[6]) + (n[7] * Math.pow(t, 4) / n[8]) + (n[9] * Math.pow(t, 5) / n[10]);
    }
  }

  protected final ValueRange range;

  protected final double[] n;

  private Minion(long min, long max, double... parameters) {
    range = ValueRange.of(min, max);
    n = Objects.requireNonNull(Arrays.stream(Optional.ofNullable(parameters).orElse(new double[] {})).toArray());
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

  /**
   * estimate Delta T for the given year.
   * This is based on Espenak and Meeus, "Five Millennium Canon of Solar Eclipses: -1999 to +3000" (NASA/TP-2006-214141).
   *
   * @param decimalYear the year from 0000-01-01T0Z.
   * @return estimated delta T value (seconds)
   */
  abstract double estimate(double decimalYear);
}
