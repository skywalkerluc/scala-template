package br.com.caju.projectnamedefault

//import br.com.caju.projectnamedefault.consumer.infra.Environment.consumersEnvironment
import zio.{ZIO, ZIOAppDefault}

object ConsumerMain extends ZIOAppDefault {
  // Reference: https://patorjk.com/software/taag/#p=display&f=Big&t=Projeto%20Default
  private val art = """CREATE AN ART""".stripMargin

  override def run = {
    val consumersMain = for {
      _ <- ZIO.logInfo(s"$art\nConsumers main started!")
    } yield ()

    ZIO
      .scoped(consumersMain *> ZIO.never)
//      .provide(consumersEnvironment)
      .tapDefect(e => ZIO.log(s"Consumer error: $e"))
      .exitCode
  }
}
