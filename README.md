# Psq2json (POC)
There are better ways to do so, but (for sport) I've created a Scala module parsing a PL/SQL subset, to generate a function converting a type to json. In case you need to do so, I suggest finding a PL/SQL native way.

The point was to play with [Scala.js](https://www.scala-js.org) and [Testcontainers](https://github.com/testcontainers) a bit (not the first time, but the first time on my own).

You can see it here: TODO

### What works:
- generation of a json serialiser (as a PL/SQL function) from PL/SQL object definition
- serving it via static webpage with no backend (the logic is a js script)

### Limitations:
All the fields must be of the type `varchar2`/`number`/`date`. `varchar2` can't specify the length unit (`byte`/`char`).

### How to build?

SBT is necessary. See https://www.scala-sbt.org.

**DEV:**

In the root directory:
```
sbt compile
sbt app/fastOptJS
```

**PROD:**
```
cd modules/app
npm run build
```
Check the `modules/app/dist` dir after this.
### How to test?

In the root diretory `sbt test` - tests only the Converter module. Docker must be available.

BTW you can export an env variable `PSQ2JSON_TEST_TC_REUSABLE=1`, and configure Testcontainers to allow container reuse (check the documentation for details). It's much faster this way, but the feature is experimental. The test commands are idempotent, so there is no harm in reusing.

To check the webpage, do this:
```
cd modules/app
npm run start
```
In the terminal you'll see and adress. Open and see the result 😉
