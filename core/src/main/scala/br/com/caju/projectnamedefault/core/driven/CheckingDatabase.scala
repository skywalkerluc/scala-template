package br.com.caju.projectnamedefault.core.driven

import io.github.gaelrenoux.tranzactio.doobie.Connection
import zio.ZIO

trait CheckingDatabase {
  def isHealthy: ZIO[Connection, Throwable, Boolean]
}
