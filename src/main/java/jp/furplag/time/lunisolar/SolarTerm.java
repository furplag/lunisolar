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
package jp.furplag.time.lunisolar;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import jp.furplag.time.Julian;
import jp.furplag.time.lunisolar.misc.orrery.EclipticLongitude;

public abstract class SolarTerm implements Comparable<SolarTerm>, Serializable {

  final long epochMilli;

  final double julianDate;

  final double actualLongitude;

  final int longitude;

  final int termIndex;

  private SolarTerm(double julianDate, double actualLongitude) {
    this.julianDate = julianDate;
    this.epochMilli = Julian.toInstant(julianDate).toEpochMilli();
    this.actualLongitude = actualLongitude;
    longitude = ((int) (actualLongitude + .5d));
    this.termIndex = ((int) ((longitude + 45) / 15d)) % 24;
  }

  private SolarTerm(double julianDate) {
    this(julianDate, EclipticLongitude.Sun.ofJulian(julianDate));
  }

  public static SolarTerm ofClosest(final double julianDate, final int degree, final Lunisolar lunisolar) {
    Objects.requireNonNull(lunisolar, "");
    final int longitude = 15 * ((degree % 360) / 15);
    double closestTerm = lunisolar.getClosestTerm(julianDate, longitude);

    return longitude % 30 == 0 ? new MidClimate(closestTerm) : new PreClimate(closestTerm);
  }

  static final class MidClimate extends SolarTerm {
    MidClimate(double julianDate) {
      super(julianDate);
    }
  }

  static final class PreClimate extends SolarTerm {
    PreClimate(double julianDate) {
      super(julianDate);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(SolarTerm o) {
    return Double.compare(julianDate, o.julianDate);
  }

  @Override
  public String toString() {
    // @formatter:off
    return
      Instant.ofEpochMilli(epochMilli)
      + " ( "
      + actualLongitude
      + " ), julianDate: "
      + julianDate
      + ", termIndex: "
      + termIndex
      + ", longitude: "
      + longitude
      ;
    // @formatter:on
  }
}
