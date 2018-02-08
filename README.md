# lunisolar

[![Build Status](https://travis-ci.org/furplag/lunisolar.svg?branch=master)](https://travis-ci.org/furplag/lunisolar)
[![Coverage Status](https://coveralls.io/repos/github/furplag/lunisolar/badge.svg?branch=master)](https://coveralls.io/github/furplag/lunisolar?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3ab497704bdb440ba2e376b2012be08a)](https://www.codacy.com/app/furplag/lunisolar?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=furplag/lunisolar&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/9f57e867-a8b7-432e-9795-3559081c95e4)](https://codebeat.co/projects/github-com-furplag-lunisolar-master)
[![Maintainability](https://api.codeclimate.com/v1/badges/cd79ee2f2fbdbf867d99/maintainability)](https://codeclimate.com/github/furplag/lunisolar/maintainability)

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
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

## License
Code is under the [Apache Licence v2](LICENCE).
