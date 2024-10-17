package br.com.caju.projectnamedefault.lib.logger

import zio.logging.backend.SLF4J
import zio.logging.{LogAnnotation, logContext}
import zio.{Runtime, UIO, ZIO, ZLayer}

import java.util.UUID

object LoggerUtils {

  type LoggerConfig = Unit
  lazy val layer: ZLayer[Any, Nothing, LoggerConfig] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  private val correlationIdLogAnnotation  = makeAnnotation("correlationId")
  private val businessIdLogAnnotation     = makeAnnotation("businessId")
  private val requesterIdLogAnnotation    = makeAnnotation("requesterId")
  private val sessionContextLogAnnotation = makeAnnotation("sessionContext")
  private val httpMethodLogAnnotation     = makeAnnotation("httpMethod")
  private val httpPathLogAnnotation       = makeAnnotation("httpPath")
  private val topicNameLogAnnotation      = makeAnnotation("topicName")
  private val topicKeyLogAnnotation       = makeAnnotation("topicKey")
  private val consumerGroupLogAnnotation  = makeAnnotation("consumerGroup")
  private val clientIpLogAnnotation       = makeAnnotation("clientIpAddress")
  private val errorAnnotation             = makeAnnotation("error")

  @inline
  private def makeAnnotation(name: String): LogAnnotation[String] =
    LogAnnotation[String](
      name = name,
      combine = (_, r) => r,
      render = s => s
    )

  def getCorrelationId: UIO[String] =
    getContextOrElse(correlationIdLogAnnotation, UUID.randomUUID().toString)

  private def getContextOrElse(key: LogAnnotation[String], default: String) =
    logContext.get.map(_.get(key).getOrElse(default))

  implicit class ZIOLogOps[R, E, A](val z: ZIO[R, E, A]) {
    def logDefects: ZIO[R, E, A] =
      z.tapDefect(defect =>
        ZIO
          .logError("Defect!")
          .withLogAnnotation("defect", defect.toString)
      )

    def withError(error: String): ZIO[R, E, A] =
      z @@ errorAnnotation(error)

    def withCorrelationId: ZIO[R, E, A] =
      withCorrelationId(UUID.randomUUID().toString)

    def withCorrelationId(correlationId: Option[String]): ZIO[R, E, A] =
      correlationId match {
        case Some(value) => withCorrelationId(value)
        case None        => withCorrelationId
      }

    def withCorrelationId(correlationId: String): ZIO[R, E, A] =
      z @@ correlationIdLogAnnotation(correlationId)

    def withBusinessId(id: String): ZIO[R, E, A] =
      z @@ businessIdLogAnnotation(id)

    def withRequesterId(id: String): ZIO[R, E, A] =
      z @@ requesterIdLogAnnotation(id)

    def withSessionContext(id: String): ZIO[R, E, A] =
      z @@ sessionContextLogAnnotation(id)

    def withHttpMethod(method: String): ZIO[R, E, A] =
      z @@ httpMethodLogAnnotation(method)

    def withHttpPath(path: String): ZIO[R, E, A] =
      z @@ httpPathLogAnnotation(path)

    def withTopicName(name: String): ZIO[R, E, A] =
      z @@ topicNameLogAnnotation(name)

    def withTopicKey(key: Option[String]): ZIO[R, E, A] =
      key match {
        case Some(value) => z @@ topicKeyLogAnnotation(value)
        case _           => z
      }

    def withConsumerGroup(group: Option[String]): ZIO[R, E, A] =
      group match {
        case Some(value) => z @@ consumerGroupLogAnnotation(value)
        case _           => z
      }

    def withClientIpAddress(
        maybeForwardedIpAddresses: Option[String]
    ): ZIO[R, E, A] =
      maybeForwardedIpAddresses.map(_.split(",")).flatMap(_.headOption) match {
        case Some(clientIpAddress) =>
          z @@ clientIpLogAnnotation(clientIpAddress)
        case None => z
      }

    def withLogAnnotation(key: String, value: String): ZIO[R, E, A] =
      z @@ makeAnnotation(key)(value)
  }
}
