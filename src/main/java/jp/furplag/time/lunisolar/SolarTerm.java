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
import java.util.Objects;

import jp.furplag.misc.Astror;
import jp.furplag.misc.orrery.EclipticLongitude;
import jp.furplag.time.Julian;

/**
 * solar term in east asian calendar .
 *
 * @author furplag
 *
 */
public abstract class SolarTerm implements Comparable<SolarTerm>, Serializable {

  /** milliseconds from 1970-01-01T00:00:00.000Z . */
  final long epochMilli;

  /** AJD . */
  final double julianDate;

  /** longitude of sun . */
  final double actualLongitude;

  /** 360&deg; / 15&deg; . */
  final int longitude;

  /** 360&deg; / 24 . */
  final int termIndex;

  private SolarTerm(double julianDate) {
    this.julianDate = julianDate;
    this.epochMilli = Julian.toInstant(julianDate).toEpochMilli();
    this.actualLongitude = EclipticLongitude.Sun.ofJulian(julianDate);
    longitude = ((int) Astror.circulate(actualLongitude + .5));
    this.termIndex = ((int) ((longitude + 45.0) / 15.0)) % 24;
  }

  /**
   * meant &quot;中気&quot; .
   *
   * @author furplag
   *
   */
  static final class MidClimate extends SolarTerm {
    MidClimate(double julianDate) {
      super(julianDate);
    }
  }

  /**
   * meant &quot;二十四節気&quot; .
   *
   * @author furplag
   *
   */
  static final class PreClimate extends SolarTerm {
    PreClimate(double julianDate) {
      super(julianDate);
    }
  }

  /**
   * calculate the closest instant in which the ecliptic longitude of the sun
   * places at the specified angle from the specified julian date.
   *
   * @param julianDate an instant represented by astronomical julian date
   * @param degree the degree which circlyzed 0 to 360
   * @param lunisolar {@link Lunisolar}
   * @return the closest instant in which the ecliptic longitude of the sun
   *          places at the specified angle
   */
  public static SolarTerm ofClosest(final double julianDate, final int degree, final Lunisolar lunisolar) {
    Objects.requireNonNull(lunisolar);
    final int longitude = 15 * ((degree % 360) / 15);
    double closestTerm = lunisolar.closestTerm(julianDate, longitude);

    return longitude % 30 == 0 ? new MidClimate(closestTerm) : new PreClimate(closestTerm);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(SolarTerm o) {
    return Double.compare(julianDate, o.julianDate);
  }
}
