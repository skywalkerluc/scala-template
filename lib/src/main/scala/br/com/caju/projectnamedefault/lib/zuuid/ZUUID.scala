package br.com.caju.projectnamedefault.lib.zuuid

import zio.{UIO, ZIO, ZLayer}

import java.util.UUID

trait ZUUID {
  def uuid: UIO[UUID]
}

case class ZUUIDImpl() extends ZUUID {
  override def uuid: UIO[UUID] = ZIO.succeed(UUID.randomUUID())
}

object ZUUID {
  val layer = ZLayer.succeed(ZUUIDImpl())
}
