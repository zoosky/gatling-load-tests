#!/bin/sh
exec scala "$0" "$@"
!#

object Metrics {
  def main(args: Array[String]) {
    for (ln <- io.Source.stdin.getLines) println(ln)
  }
}
