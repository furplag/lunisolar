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

import java.time.Duration;
import java.time.Instant;

import jp.furplag.time.Julian;
import jp.furplag.time.JulianDayNumber;
import jp.furplag.time.Millis;

/**
 * the day of east asian Lunisolar calendar system .
 *
 * @author furplag
 *
 */
public class LunisolarDate {

  /** AJD . */
  final double julianDate;

  /** JDN . */
  final long julianDayNumber;

  /** millis from epoch . */
  final long epochMilli;

  /** the year in AD.(BC.) . */
  final long year;

  /** year of era . */
  final int yearOfEra;

  /** month of year . */
  final int monthOfYear;

  /** day of month . */
  final long dayOfMonth;

  /** leap month . */
  final boolean intercalary;

  /** meant &quot;十干&quot; . */
  final int heavenlyStem;

  /** meant &quot;十二支&quot; . */
  final int earthlyBranch;

  /**
   * calculate lunisolar calendar .
   *
   * @param julianDate astronomical julian date
   * @return {@link LunisolarDate}
   */
  public static final LunisolarDate ofJulian(final double julianDate) {
    return new LunisolarDate(julianDate);
  }

  /**
   * calculate lunisolar calendar .
   *
   * @param epochMilli millis from epoch
   * @return {@link LunisolarDate}
   */
  public static final LunisolarDate ofEpochMilli(final long epochMilli) {
    return new LunisolarDate(Julian.ofEpochMilli(epochMilli));
  }

  /**
   *
   * @param julianDate astronomical julian date
   */
  private LunisolarDate(double julianDate) {
    this.julianDate = julianDate;
    julianDayNumber = JulianDayNumber.ofJulian(julianDate);
    epochMilli = Millis.ofJulian(julianDate);
    Lunisolar lunisolar = Lunisolar.ofJulian(julianDate);
    LunisolarCalendar lunisolarCalendar = new LunisolarCalendar(lunisolar, julianDate);
    if (!lunisolarCalendar.rangeOfYear.isValidValue(lunisolar.asStartOfDay(julianDate))) {
      lunisolarCalendar = new LunisolarCalendar(lunisolar, lunisolar.plusMonth(julianDate, -2.1));
    }
    LunarMonth month = lunisolarCalendar.monthsOfYear.stream().filter(e -> e.range.isValidValue(lunisolar.asStartOfDay(julianDate))).findFirst().orElse(null);
    year = lunisolar.atOffset(Instant.ofEpochMilli(lunisolarCalendar.monthsOfYear.stream().filter(e -> e.november).findAny().orElse(null).range.getMinimum())).getYear();
    monthOfYear = month.monthOfYear;
    dayOfMonth = Duration.between(Instant.ofEpochMilli(month.range.getMinimum()), Instant.ofEpochMilli(lunisolar.asStartOfDay(julianDate))).toDays() + 1;
    intercalary = month.intercalary;

    yearOfEra = (int) year;
    heavenlyStem = 0;
    earthlyBranch = 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return new StringBuilder()
      .append(year)
      .append("年")
      .append(intercalary ? "閏" : "").append(monthOfYear).append("月")
      .append(dayOfMonth).append("日")
      .toString()
      ;
  }
}
