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
> testOnly *SampleTest*
```

## Real-time metrics

With [docker[(https://www.docker.com/whatisdocker/) started, run this shell script
```sh
$ sh support/scripts/graphite-install.sh
```
This should produce a host and port for the dashboard
```sh
Dashboard running at http://192.168.59.103:49154
```
In the gatling.conf add the above docker IP address and uncomment relevant key values.

```config
graphite {
      #light = false              # only send the all* stats
      host = "192.168.59.103"         # The host where the Carbon server is located
      port = 2003                # The port to which the Carbon server listens to
      # protocol = "tcp"           # The protocol used to send data to Carbon (currently supported : "tcp", "udp")
      rootPathPrefix = "gatling" # The common prefix of all metrics sent to Graphite
      # bufferSize = 8192          # GraphiteDataWriter's internal data buffer size, in bytes
    }
```

Configure the [Grafana dashboard](http://grafana.org/docs/features/intro/) and run test. 


