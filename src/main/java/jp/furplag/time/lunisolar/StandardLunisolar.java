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
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.furplag.time.lunisolar.misc.Astror;
import jp.furplag.time.lunisolar.misc.orrery.EclipticLongitude;

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
  List<Long> firstDaysOfYear(List<SolarTerm> solarTerms, ValueRange rangeOfYear) {
    // @formatter:off
    return Optional.ofNullable(solarTerms).orElse(new ArrayList<>())
      .stream()
      .map(solarTerm -> solarTerm.julianDate)
      .map(this::latestNewMoon)
      .mapToLong(this::asStartOfDay)
      .distinct()
      .mapToObj(Long::valueOf)
      .collect(Collectors.toList());
    // @formatter:on
  }

  @Override
  List<SolarTerm> termsOfBase(double julianDate) {
    List<SolarTerm> terms = new ArrayList<>();
    int deg = 360;
    final double winterSolsticeOfLastYear = closestTerm(firstDayOfYear(julianDate), 270d);
    do {
      terms.add(SolarTerm.ofClosest(terms.isEmpty() ? plusMonth(winterSolsticeOfLastYear, 14) : terms.get(terms.size() - 1).julianDate, deg - (15 * (terms.size())), this));
    } while (terms.get(terms.size() - 1).julianDate > winterSolsticeOfLastYear);

    return terms.stream().sorted().collect(Collectors.toList());
  }
}
