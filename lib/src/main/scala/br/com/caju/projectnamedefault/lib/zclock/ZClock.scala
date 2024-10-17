package br.com.caju.projectnamedefault.lib.zclock

import zio.{Clock, ZIO}

import java.time.{LocalDateTime, ZoneOffset}

object ZClock {

  def utcDateTime: ZIO[Any, Nothing, LocalDateTime] =
    Clock.currentDateTime.map(odt => LocalDateTime.ofInstant(odt.toInstant, ZoneOffset.UTC))

  def nanoTime: ZIO[Any, Nothing, Long] =
    Clock.nanoTime
}
