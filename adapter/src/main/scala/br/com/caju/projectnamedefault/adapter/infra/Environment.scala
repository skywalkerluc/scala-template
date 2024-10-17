package br.com.caju.projectnamedefault.adapter.infra

import br.com.caju.projectnamedefault.adapter.persistence.HealthRepository
import br.com.caju.projectnamedefault.core.driven.CheckingDatabase
import zio.ZLayer

object Environment {

  type AdapterEnvironment = CheckingDatabase

  val adapterEnvironment = ZLayer.make[AdapterEnvironment](
    HealthRepository.layer
  )
}
