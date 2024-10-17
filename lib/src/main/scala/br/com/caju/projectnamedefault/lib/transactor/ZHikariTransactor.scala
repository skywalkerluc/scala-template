package br.com.caju.projectnamedefault.lib.transactor

import cats.Functor
import cats.effect.{Async, Resource, Sync, Temporal}
import com.zaxxer.hikari.HikariDataSource
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor
import zio.Task
import zio.interop.catz._

import scala.concurrent.duration.DurationInt

object ZHikariTransactor {
  def makeHikari(config: TransactorConnection) =
    (for {
      executionContext <- ExecutionContexts.fixedThreadPool[Task](
                            config.threadPoolSize
                          )
      _          <- Resource.eval(Async[Task].delay(Class.forName(config.driver)))
      dataSource <- createDataSourceResource[Task](new HikariDataSource)
      transactor = Transactor.fromDataSource[Task](dataSource, executionContext)
      _ <- buildHikariConfig(transactor, config)
    } yield transactor).toScopedZIO.orDie

  private def createDataSourceResource[M[_]: Sync: Temporal](
      factory: => HikariDataSource
  )(implicit F: Functor[M]): Resource[M, HikariDataSource] = {
    val alloc = Sync[M].delay(factory)

    def close(maxCalls: Long)(ds: HikariDataSource): M[Unit] = {
      val pool = ds.getHikariPoolMXBean
      if (maxCalls >= 1 && pool.getTotalConnections > 0)
        Temporal[M].flatMap(Temporal[M].sleep(1.second)) { _ =>
          Sync[M].flatMap(Sync[M].delay(pool.softEvictConnections()))(_ => close(maxCalls - 1)(ds))
        }
      else Sync[M].delay(ds.close())
    }
    def free(maxCalls: Long)(ds: HikariDataSource): M[Unit] =
      Sync[M].flatMap(Sync[M].delay(ds.setMinimumIdle(0)))(_ => close(maxCalls)(ds))

    Resource.make(alloc)(free(maxCalls = 15))(F)
  }

  private def buildHikariConfig(
      transactor: HikariTransactor[Task],
      dbConfig: TransactorConnection
  ): Resource[Task, Unit] =
    Resource.eval {
      transactor.configure { hikariConfig =>
        Async[Task].delay {
          hikariConfig.setJdbcUrl(dbConfig.url)
          hikariConfig.setDriverClassName(dbConfig.driver)
          hikariConfig.setUsername(dbConfig.user)
          hikariConfig.setPassword(dbConfig.password)
          hikariConfig.setMinimumIdle(dbConfig.minimumIdle)
          hikariConfig.setPoolName(dbConfig.poolName)
          hikariConfig.setMaximumPoolSize(dbConfig.maxPoolSize)

        }
      }
    }

  case class TransactorConnection(
      driver: String,
      url: String,
      user: String,
      password: String,
      threadPoolSize: Int,
      maxPoolSize: Int,
      minimumIdle: Int,
      poolName: String
  )
}
