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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import jp.furplag.time.Julian;

public final class DeltaT {

  private static final Minion over2150 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return -20 + 32 * Math.pow((decimalYear - 1820) / 100, 2);
    }
  };

  private static final Minion over2050 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return -20 + 32 * Math.pow(((decimalYear - 1820) / 100), 2) - 0.5628 * (2150 - decimalYear);
    }
  };

  private static final Minion over2005 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 62.92 + 0.32217 * (decimalYear - 2000) + 0.005589 * Math.pow(decimalYear - 2000, 2);
    }
  };

  private static final Minion over1986 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 63.86 + 0.3345 * (decimalYear - 2000) - 0.060374 * Math.pow((decimalYear - 2000), 2) + 0.0017275 * Math.pow((decimalYear - 2000), 3) + 0.000651814 * Math.pow((decimalYear - 2000), 4) + 0.00002373599 * Math.pow((decimalYear - 2000), 5);
    }
  };

  private static final Minion over1961 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 45.45 + 1.067 * (decimalYear - 1975) - Math.pow((decimalYear - 1975), 2) / 260 - Math.pow((decimalYear - 1975), 3) / 718;
    }
  };

  private static final Minion over1941 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 29.07 + 0.407 * (decimalYear - 1950) - Math.pow((decimalYear - 1950), 2) / 233 + Math.pow((decimalYear - 1950), 3) / 2547;
    }
  };

  private static final Minion over1920 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 21.20 + 0.84493 * (decimalYear - 1920) - 0.076100 * Math.pow((decimalYear - 1920), 2) + 0.0020936 * Math.pow((decimalYear - 1920), 3);
    }
  };

  private static final Minion over1900 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return -2.79 + 1.494119 * (decimalYear - 1900) - 0.0598939 * Math.pow((decimalYear - 1900), 2) + 0.0061966 * Math.pow((decimalYear - 1900), 3) - 0.000197 * Math.pow((decimalYear - 1900), 4);
    }
  };

  private static final Minion over1860 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 7.62 + 0.5737 * (decimalYear - 1860) - 0.251754 * Math.pow((decimalYear - 1860), 2) + 0.01680668 * Math.pow((decimalYear - 1860), 3) - 0.0004473624 * Math.pow((decimalYear - 1860), 4) + Math.pow((decimalYear - 1860), 5) / 233174;
    }
  };

  private static final Minion over1800 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 13.72 - 0.332447 * (decimalYear - 1800) + 0.0068612 * Math.pow((decimalYear - 1800), 2) + 0.0041116 * Math.pow((decimalYear - 1800), 3) - 0.00037436 * Math.pow((decimalYear - 1800), 4) + 0.0000121272 * Math.pow((decimalYear - 1800), 5) - 0.0000001699 * Math.pow((decimalYear - 1800), 6) + 0.000000000875 * Math.pow((decimalYear - 1800), 7);
    }
  };

  private static final Minion over1700 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 8.83 + 0.1603 * (decimalYear - 1700) - 0.0059285 * Math.pow((decimalYear - 1700), 2) + 0.00013336 * Math.pow((decimalYear - 1700), 3) - Math.pow((decimalYear - 1700), 4) / 1174000;
    }
  };

  private static final Minion over1600 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 120 - 0.9808 * (decimalYear - 1600) - 0.01532 * Math.pow((decimalYear - 1600), 2) + Math.pow((decimalYear - 1600), 3) / 7129;
    }
  };

  private static final Minion over500 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 1574.2 - 556.01 * ((decimalYear - 1000) / 100) + 71.23472 * Math.pow(((decimalYear - 1000) / 100), 2) + 0.319781 * Math.pow(((decimalYear - 1000) / 100), 3) - 0.8503463 * Math.pow(((decimalYear - 1000) / 100), 4) - 0.005050998 * Math.pow(((decimalYear - 1000) / 100), 5) + 0.0083572073 * Math.pow(((decimalYear - 1000) / 100), 6);
    }
  };

  private static final Minion overMinus500 = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return 10583.6 - 1014.41 * (decimalYear / 100) + 33.78311 * Math.pow((decimalYear / 100), 2) - 5.952053 * Math.pow((decimalYear / 100), 3) - 0.1798452 * Math.pow((decimalYear / 100), 4) + 0.022174192 * Math.pow((decimalYear / 100), 5) + 0.0090316521 * Math.pow((decimalYear / 100), 6);
    }
  };

  private static final Minion _default = new Minion() {
    @Override
    double estimate(double decimalYear) {
      return -20 + 32 * Math.pow((decimalYear - 1820) / 100, 2);
    }
  };

  private DeltaT() {}

  public static final double compute(final double julianDate) {
    return of(decimalYear(julianDate)).estimate(decimalYear(julianDate));
  }

  private static final Minion of(final double decimalYear) {
    // @formatter:off
    return
      decimalYear < -500 ? _default :
      decimalYear < 500 ? overMinus500 :
      decimalYear < 1600 ? over500 :
      decimalYear < 1700 ? over1600 :
      decimalYear < 1800 ? over1700 :
      decimalYear < 1860 ? over1800 :
      decimalYear < 1900 ? over1860 :
      decimalYear < 1920 ? over1900 :
      decimalYear < 1941 ? over1920 :
      decimalYear < 1961 ? over1941 :
      decimalYear < 1986 ? over1961 :
      decimalYear < 2005 ? over1986 :
      decimalYear < 2050 ? over2005 :
      decimalYear < 2150 ? over2050 :
      over2150;
    // @formatter:on
  }

  private static abstract class Minion {
    abstract double estimate(double decimalYear);
  }

  private static double decimalYear(final double julianDate) {
    ZonedDateTime utc = Julian.toInstant(julianDate).atZone(ZoneOffset.UTC);

    return utc.getYear() - (utc.getYear() < 0 ? 1 : 0) + ((utc.getMonthValue() - .5) / 12);
  }
}
