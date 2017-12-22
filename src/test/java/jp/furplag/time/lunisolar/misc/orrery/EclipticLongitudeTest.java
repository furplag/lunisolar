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

package jp.furplag.time.lunisolar.misc.orrery;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.time.Julian;
import jp.furplag.time.lunisolar.misc.Astror;

public class EclipticLongitudeTest {

  @Test
  public void test() {
    assertThat(Formula.sun[0].compareTo(null), is(1));
    assertThat(Formula.sun[0].compareTo(Arrays.stream(Formula.sun).filter(f -> f.formulaType.is(1)).findFirst().orElse(null)), is(-1));
    assertThat(Arrays.stream(Formula.sun).sorted().toArray(Formula[]::new)[Formula.sun.length - 2].angularVelocity, is(35999.0));
  }

  @Test
  public void testOfSun() {
    // @formatter:off
    Arrays.stream(new double[][] {
      // http://eco.mtk.nao.ac.jp/koyomi/yoko/pdf/jiko1951.pdf
        {Julian.ofEpochMilli(OffsetDateTime.parse("1951-01-06T12:31+09:00").toInstant().toEpochMilli()), 285.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-01-21T05:53+09:00").toInstant().toEpochMilli()), 300.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-02-05T00:14+09:00").toInstant().toEpochMilli()), 315.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-02-19T20:10+09:00").toInstant().toEpochMilli()), 330.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-03-06T18:27+09:00").toInstant().toEpochMilli()), 345.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-03-21T19:26+09:00").toInstant().toEpochMilli()),   0.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-04-05T23:33+09:00").toInstant().toEpochMilli()),  15.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-04-21T06:49+09:00").toInstant().toEpochMilli()),  30.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-05-06T17:10+09:00").toInstant().toEpochMilli()),  45.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-05-22T06:16+09:00").toInstant().toEpochMilli()),  60.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-06-06T21:33+09:00").toInstant().toEpochMilli()),  75.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-06-22T14:25+09:00").toInstant().toEpochMilli()),  90.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-07-08T07:54+09:00").toInstant().toEpochMilli()), 105.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-07-24T01:21+09:00").toInstant().toEpochMilli()), 120.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-08-08T17:38+09:00").toInstant().toEpochMilli()), 135.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-08-24T08:17+09:00").toInstant().toEpochMilli()), 150.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-09-08T20:19+09:00").toInstant().toEpochMilli()), 165.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-09-24T05:38+09:00").toInstant().toEpochMilli()), 180.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-10-09T11:37+09:00").toInstant().toEpochMilli()), 195.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-10-24T14:37+09:00").toInstant().toEpochMilli()), 210.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-11-08T14:37+09:00").toInstant().toEpochMilli()), 225.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-11-23T11:52+09:00").toInstant().toEpochMilli()), 240.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-12-08T07:03+09:00").toInstant().toEpochMilli()), 255.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("1951-12-23T02:05+09:00").toInstant().toEpochMilli()), 270.0}

      // http://eco.mtk.nao.ac.jp/koyomi/yoko/pdf/yoko2001.pdf
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-01-05T06:49+09:00").toInstant().toEpochMilli()), 285.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-01-20T09:16+09:00").toInstant().toEpochMilli()), 300.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-02-04T03:29+09:00").toInstant().toEpochMilli()), 315.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-02-18T23:27+09:00").toInstant().toEpochMilli()), 330.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-03-05T21:32+09:00").toInstant().toEpochMilli()), 345.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-03-20T22:31+09:00").toInstant().toEpochMilli()),   0.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-04-05T02:24+09:00").toInstant().toEpochMilli()),  15.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-04-20T09:36+09:00").toInstant().toEpochMilli()),  30.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-05-05T19:45+09:00").toInstant().toEpochMilli()),  45.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-05-21T08:44+09:00").toInstant().toEpochMilli()),  60.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-06-05T23:54+09:00").toInstant().toEpochMilli()),  75.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-06-21T16:38+09:00").toInstant().toEpochMilli()),  90.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-07-07T10:07+09:00").toInstant().toEpochMilli()), 105.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-07-23T03:26+09:00").toInstant().toEpochMilli()), 120.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-08-07T19:52+09:00").toInstant().toEpochMilli()), 135.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-08-23T10:27+09:00").toInstant().toEpochMilli()), 150.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-09-07T22:46+09:00").toInstant().toEpochMilli()), 165.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-09-23T08:04+09:00").toInstant().toEpochMilli()), 180.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-10-08T14:25+09:00").toInstant().toEpochMilli()), 195.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-10-23T17:26+09:00").toInstant().toEpochMilli()), 210.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-11-07T17:37+09:00").toInstant().toEpochMilli()), 225.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-11-22T15:00+09:00").toInstant().toEpochMilli()), 240.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-12-07T10:29+09:00").toInstant().toEpochMilli()), 255.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2001-12-22T04:21+09:00").toInstant().toEpochMilli()), 270.0}

      // http://eco.mtk.nao.ac.jp/koyomi/yoko/pdf/yoko2018.pdf
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-01-05T18:49+09:00").toInstant().toEpochMilli()), 285.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-01-20T12:09+09:00").toInstant().toEpochMilli()), 300.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-02-04T06:28+09:00").toInstant().toEpochMilli()), 315.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-02-19T02:18+09:00").toInstant().toEpochMilli()), 330.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-03-06T00:28+09:00").toInstant().toEpochMilli()), 345.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-03-21T01:15+09:00").toInstant().toEpochMilli()),   0.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-04-05T05:13+09:00").toInstant().toEpochMilli()),  15.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-04-20T12:13+09:00").toInstant().toEpochMilli()),  30.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-05-05T22:25+09:00").toInstant().toEpochMilli()),  45.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-05-21T11:15+09:00").toInstant().toEpochMilli()),  60.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-06-06T02:29+09:00").toInstant().toEpochMilli()),  75.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-06-21T19:07+09:00").toInstant().toEpochMilli()),  90.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-07-07T12:42+09:00").toInstant().toEpochMilli()), 105.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-07-23T06:00+09:00").toInstant().toEpochMilli()), 120.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-08-07T22:31+09:00").toInstant().toEpochMilli()), 135.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-08-23T13:09+09:00").toInstant().toEpochMilli()), 150.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-09-08T01:30+09:00").toInstant().toEpochMilli()), 165.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-09-23T10:54+09:00").toInstant().toEpochMilli()), 180.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-10-08T17:15+09:00").toInstant().toEpochMilli()), 195.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-10-23T20:22+09:00").toInstant().toEpochMilli()), 210.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-11-07T20:32+09:00").toInstant().toEpochMilli()), 225.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-11-22T18:02+09:00").toInstant().toEpochMilli()), 240.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-12-07T13:26+09:00").toInstant().toEpochMilli()), 255.0}
      , {Julian.ofEpochMilli(OffsetDateTime.parse("2018-12-22T07:23+09:00").toInstant().toEpochMilli()), 270.0}
    })
      .forEach(n -> {
        assertThat(
          Julian.toInstant(n[0]).atOffset(ZoneOffset.ofHours(9)).toString()
        , Astror.circulate(Math.round(EclipticLongitude.Sun.ofJulian(n[0])))
        , is(n[1])
        );
      });
    // @formatter:on
  }


  @Test
  public void testOfMoon() {
    // @formatter:off
    Arrays.asList(
      // http://eco.mtk.nao.ac.jp/koyomi/yoko/pdf/yoko1955.pdf
        OffsetDateTime.parse("1955-01-24T10:06+09:00")
      , OffsetDateTime.parse("1955-02-23T00:54+09:00")
      , OffsetDateTime.parse("1955-03-24T12:42+09:00")
      , OffsetDateTime.parse("1955-04-22T22:06+09:00")
      , OffsetDateTime.parse("1955-05-22T05:58+09:00")
      , OffsetDateTime.parse("1955-06-20T13:12+09:00")
      , OffsetDateTime.parse("1955-07-19T20:34+09:00")
      , OffsetDateTime.parse("1955-08-18T04:58+09:00")
      , OffsetDateTime.parse("1955-09-16T15:19+09:00")
      , OffsetDateTime.parse("1955-10-16T04:32+09:00")
      , OffsetDateTime.parse("1955-11-14T21:01+09:00")
      , OffsetDateTime.parse("1955-12-14T06:07+09:00")

      // http://eco.mtk.nao.ac.jp/koyomi/yoko/pdf/yoko2017.pdf
      , OffsetDateTime.parse("2017-01-28T09:07+09:00")
      , OffsetDateTime.parse("2017-02-26T23:58+09:00")
      , OffsetDateTime.parse("2017-03-28T11:57+09:00")
      , OffsetDateTime.parse("2017-04-26T21:16+09:00")
      , OffsetDateTime.parse("2017-05-26T04:44+09:00")
      , OffsetDateTime.parse("2017-06-24T11:31+09:00")
      , OffsetDateTime.parse("2017-07-23T18:46+09:00")
      , OffsetDateTime.parse("2017-08-22T03:30+09:00")
      , OffsetDateTime.parse("2017-09-20T14:30+09:00")
      , OffsetDateTime.parse("2017-10-20T04:12+09:00")
      , OffsetDateTime.parse("2017-11-18T20:42+09:00")
      , OffsetDateTime.parse("2017-12-18T15:30+09:00")

      // http://eco.mtk.nao.ac.jp/koyomi/yoko/pdf/yoko2018.pdf
      , OffsetDateTime.parse("2018-01-17T11:17+09:00")
      , OffsetDateTime.parse("2018-02-16T06:05+09:00")
      , OffsetDateTime.parse("2018-03-17T22:12+09:00")
      , OffsetDateTime.parse("2018-04-16T10:57+09:00")
      , OffsetDateTime.parse("2018-05-15T20:48+09:00")
      , OffsetDateTime.parse("2018-06-14T04:43+09:00")
      , OffsetDateTime.parse("2018-07-13T11:48+09:00")
      , OffsetDateTime.parse("2018-08-11T18:58+09:00")
      , OffsetDateTime.parse("2018-09-10T03:01+09:00")
      , OffsetDateTime.parse("2018-10-09T12:47+09:00")
      , OffsetDateTime.parse("2018-11-08T01:02+09:00")
      , OffsetDateTime.parse("2018-12-07T16:20+09:00")
    ).forEach(expect -> {
      final Map<Double, Instant> phases = new HashMap<>();
      double closest = IntStream.range(0, 3600)
        .mapToDouble(min -> {
          Instant i = expect.truncatedTo(ChronoUnit.DAYS).plusMinutes(min).toInstant();
          double julianDate = Julian.ofEpochMilli(i.toEpochMilli());
          double sun = EclipticLongitude.Sun.ofJulian(julianDate);
          double moon = EclipticLongitude.Moon.ofJulian(julianDate);
          double diff = Math.abs(moon - sun);
          phases.put(diff, i);

          return diff;
        }).min().orElse(90);
      final Instant actual = phases.getOrDefault(closest, Instant.parse("0001-01-01T00:00:00.000Z"));
      assertThat(Objects.toString(expect.toInstant()) + ":" + Objects.toString(actual), Duration.between(actual, expect.toInstant()).get(ChronoUnit.SECONDS) < 2, is(true));
    });

    // @formatter:on
  }
}
