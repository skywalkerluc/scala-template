package br.com.caju.projectnamedefault

import br.com.caju.projectnamedefault.core.driven.CheckingDatabase
import zio.mock.mockable

object Mocks {
  @mockable[CheckingDatabase]
  object MockCheckingDatabase
}
