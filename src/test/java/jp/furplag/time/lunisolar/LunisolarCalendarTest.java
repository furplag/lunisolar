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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.OffsetDateTime;

import org.junit.Test;

import jp.furplag.time.Julian;

public class LunisolarCalendarTest {

  @Test
  public void test() {
    String expect = new StringBuilder("2033-01-31T00:00+09:00")
    .append(" - ")
    .append("2034-02-18T23:59:59.999+09:00")
    .append(" ( 383 days ) ")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":1990710000000,\"smallestMaximum\":1993215599999,\"fixed\":true,\"intValue\":false,\"minimum\":1990710000000,\"maximum\":1993215599999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":1}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":1993215600000,\"smallestMaximum\":1995807599999,\"fixed\":true,\"intValue\":false,\"minimum\":1993215600000,\"maximum\":1995807599999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":2}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":1995807600000,\"smallestMaximum\":1998313199999,\"fixed\":true,\"intValue\":false,\"minimum\":1995807600000,\"maximum\":1998313199999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":3}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":1998313200000,\"smallestMaximum\":2000818799999,\"fixed\":true,\"intValue\":false,\"minimum\":1998313200000,\"maximum\":2000818799999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":4}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2000818800000,\"smallestMaximum\":2003410799999,\"fixed\":true,\"intValue\":false,\"minimum\":2000818800000,\"maximum\":2003410799999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":5}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2003410800000,\"smallestMaximum\":2005916399999,\"fixed\":true,\"intValue\":false,\"minimum\":2003410800000,\"maximum\":2005916399999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":6}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2005916400000,\"smallestMaximum\":2008508399999,\"fixed\":true,\"intValue\":false,\"minimum\":2005916400000,\"maximum\":2008508399999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":7}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2008508400000,\"smallestMaximum\":2011013999999,\"fixed\":true,\"intValue\":false,\"minimum\":2008508400000,\"maximum\":2011013999999},\"preClimates\":[{}],\"intercalary\":false,\"monthOfYear\":8}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2011014000000,\"smallestMaximum\":2013605999999,\"fixed\":true,\"intValue\":false,\"minimum\":2011014000000,\"maximum\":2013605999999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":9}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2013606000000,\"smallestMaximum\":2016197999999,\"fixed\":true,\"intValue\":false,\"minimum\":2013606000000,\"maximum\":2016197999999},\"preClimates\":[{}],\"midClimates\":[{}],\"intercalary\":false,\"monthOfYear\":10}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2016198000000,\"smallestMaximum\":2018789999999,\"fixed\":true,\"intValue\":false,\"minimum\":2016198000000,\"maximum\":2018789999999},\"preClimates\":[{}],\"midClimates\":[{},{}],\"intercalary\":false,\"monthOfYear\":11}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2018790000000,\"smallestMaximum\":2021295599999,\"fixed\":true,\"intValue\":false,\"minimum\":2018790000000,\"maximum\":2021295599999},\"preClimates\":[{}],\"intercalary\":true,\"monthOfYear\":11}")
    .append("\n\t")
    .append("{\"range\":{\"largestMinimum\":2021295600000,\"smallestMaximum\":2023887599999,\"fixed\":true,\"intValue\":false,\"minimum\":2021295600000,\"maximum\":2023887599999},\"preClimates\":[{}],\"midClimates\":[{},{}],\"intercalary\":false,\"monthOfYear\":12}")
    .toString().replaceAll("\"range\"\\:\\{.*\\},", "");

    assertThat(new LunisolarCalendar(Lunisolar.Kyoho, Julian.ofEpochMilli(OffsetDateTime.parse("2033-01-01T00:00:00.000+09:00").toInstant().toEpochMilli())).toString().replaceAll("\"range\"\\:\\{.*\\},", ""), is(expect));
  }
}
