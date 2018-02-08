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

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import jp.furplag.time.lunisolar.misc.Astror;
import jp.furplag.time.lunisolar.misc.orrery.EclipticLongitude;

/**
 * current Lunisolar calendar system .
 *
 * @author furplag
 *
 */
public final class StandardLunisolar extends Lunisolar {

  StandardLunisolar(double daysOfYear, double daysOfMonth, ZoneOffset zoneOffset) {
    super(daysOfYear, daysOfMonth, zoneOffset);
  }

  @Override
  double closestTerm(double julianDate, double degree) {
    final double expect = Astror.circulate(degree);
    double numeric = (long) julianDate;
    double floating = julianDate - numeric;
    double delta = 0.0;
    double diff = 0.0;
    int counter = 0;
    Map<Double, Double> results = new HashMap<>();
    do {
      delta = EclipticLongitude.Sun.ofJulian(numeric + floating) - expect;
      delta += delta > 180.0 ? -360.0 : delta < -180.0 ? 360.0 : 0;
      counter++;

      diff = delta * daysOfYear / 360.0;
      numeric -= (long) diff;
      floating -= diff - ((long) diff);
      if (floating < 0) {
        floating++;
        numeric--;
      } else if (floating > 1) {
        floating--;
        numeric++;
      }
      results.put(Math.abs(diff), (numeric + floating));
    } while (Math.abs(diff) > precision && counter < loopLimit);

    return counter < loopLimit ? (numeric + floating) : (doOurOwnBest(results, (numeric + floating)));
  }

  @Override
  protected List<Long> termsToFirstDays(@Nonnull List<SolarTerm> solarTerms) {
    // @formatter:off
    return solarTerms.stream()
      .mapToDouble(solarTerm -> solarTerm.julianDate)
      .mapToObj(julianDate -> asStartOfDay(latestNewMoon(julianDate)))
      .distinct()
      .sorted()
      .collect(Collectors.toList());
    // @formatter:on
  }

  @Override
  List<SolarTerm> termsOfBase(double julianDate) {
    List<SolarTerm> solarTerms = new ArrayList<>(Arrays.asList(SolarTerm.ofClosest(plusMonth(winterSolstice(julianDate), -13), 255, this)));
    do {
      final SolarTerm solarTerm = solarTerms.get(solarTerms.size() - 1);
      solarTerms.add(SolarTerm.ofClosest(solarTerm.julianDate, solarTerm.longitude + 15, this));
    } while (solarTerms.stream().filter(e -> e.longitude == 315).count() != 3);

    return solarTerms;
  }
}
