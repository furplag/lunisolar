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

public class LunisolarDate {

  final double julianDate;

  final long julianDayNumber;

  final long epochMilli;

  final long year;

  final int yearOfEra;

  final int monthOfYear;

  final int dayOfMonth;

  final boolean intercalary;

  final int heavenlyStem;

  final int earthlyBranch;

  public LunisolarDate(double julianDate, long julianDayNumber, long epochMilli, long year, int yearOfEra, int monthOfYear, int dayOfMonth, boolean intercalary, int heavenlyStem, int earthlyBranch) {
    super();
    this.julianDate = julianDate;
    this.julianDayNumber = julianDayNumber;
    this.epochMilli = epochMilli;
    this.year = year;
    this.yearOfEra = yearOfEra;
    this.monthOfYear = monthOfYear;
    this.dayOfMonth = dayOfMonth;
    this.intercalary = intercalary;
    this.heavenlyStem = heavenlyStem;
    this.earthlyBranch = earthlyBranch;
  }
}
