# lunisolar

[![Coverage Status](https://coveralls.io/repos/github/furplag/lunisolar/badge.svg?branch=master)](https://coveralls.io/github/furplag/lunisolar?branch=master)

help us to understand to  East Asian lunisolar calendars (especially in Japan) .

## Getting Start
Add the following snippet to any project's pom that depends on your project
```xml
<repositories>
  ...
  <repository>
    <id>lunisolar</id>
    <url>https://raw.github.com/furplag/lunisolar/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>jp.furplag.sandbox</groupId>
    <artifactId>lunisolar</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

## License
Code is under the [Apache Licence v2](LICENCE).
