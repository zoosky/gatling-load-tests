gatling-load-tests 
==================

## Introduction

### [Gatling](http://gatling.io/)

Gatling is a Scala based open-source load test tool, that makes a break from 
traditional load-tools by utilising asynchronous concurrency and IO, and by using a major 
language (Scala) for its scripting.

### [SBT](http://www.scala-sbt.org/)
SBT is the de-facto build tool for Scala projects.

## Installation 
### SBT
```bash
$ wget "https://dl.bintray.com/sbt/rpm/sbt-0.13.7.rpm"
$ sudo yum install -y "sbt-0.13.7.rpm"
```

### Test Resources
```bash 
$ git clone git@github.com:BBC/gatling-load-tests.git
```

## Execution
```bash 
$ cd gatling-load-tests
$ sbt
> testOnly *SampleTest
```

## Real-time metrics

```sh
$ sh bin/graphite-install
```

```config
# gatling.conf
 data {
     writers = "console, file, graphite"
     reader = file
 }
    
 graphite {
      light = false
      host = "localhost"
      port = 2003
 }
```

Open a separate terminal window
```sh 
$ nc -l 2003 | awk -f bin/pg.awk
```
