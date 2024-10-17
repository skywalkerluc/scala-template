package br.com.caju.projectnamedefault.web.utils

import br.com.caju.projectnamedefault.lib.logger.LoggerUtils.ZIOLogOps
import br.com.caju.projectnamedefault.web.commons.ErrorResponse
import io.circe.Json
import sttp.tapir.Endpoint
import sttp.tapir.ztapir.ZPartialServerEndpoint
import zio.http.{Middleware, Request, Response, Routes, handler}
import zio.{UIO, ZIO}

import java.nio.charset.StandardCharsets
import java.util.UUID

object LoggingUtils {
  private val CajuCorrelationId    = "X-Caju-Correlation-Id"
  private val ForwardedIpAddresses = "X-Forwarded-For"

  implicit class ZHttpLogger[R, E, A <: Response](effect: ZIO[R, E, A]) {
    def withAuditLogs(
        request: Request
    ): ZIO[R, E, A] =
      effect
        .tap(r => logStatusCode(r))
        .withRequestLog(request)

    private def logStatusCode(r: Response): UIO[Unit] = {
      val result = s"${r.status.code} - ${r.status}"
      if (r.status.isSuccess) ZIO.logInfo(result)
      else
        r.body.asString.flatMap { er =>
          val key = ErrorResponse.selfDec
            .decodeJson(Json.fromString(er))
            .map(_.errorKey)

          ZIO
            .fromEither(key)
            .foldZIO(
              _ => ZIO.logError(result),
              k =>
                ZIO
                  .logError(result)
                  .withLogAnnotation("errorKey", k)
            )
        }.ignore
    }

  }

  implicit class RequestLogger[R, E, A](effect: ZIO[R, E, A]) {
    def withRequestLog(request: Request): ZIO[R, E, A] =
      effect
        .withCorrelationId(
          request.headers
            .get(CajuCorrelationId)
            .getOrElse(UUID.randomUUID().toString)
        )
        .withHttpMethod(request.method.toString())
        .withHttpPath(request.path.toString())
        .withClientIpAddress(request.headers.get(ForwardedIpAddresses))
  }

  def logMiddleware(
  ): Middleware[Any] =
    new Middleware[Any] {
      override def apply[Env1 <: Any, Err](routes: Routes[Env1, Err]): Routes[Env1, Err] =
        routes.transform[Env1] { h =>
          handler((req: Request) => logRawBody(req) *> h.runZIO(req).withAuditLogs(req))
        }
    }

  private def logRawBody(req: Request): UIO[Unit] = {
    val exec = for {
      body <- req.body.asString(StandardCharsets.UTF_8)
      _ <- ZIO
             .logInfo("Raw request body")
             .withLogAnnotation("body", body)
             .withRequestLog(req)
    } yield ()
    exec.catchAllCause(_ => ZIO.logError("Could not parse body as string"))
  }

  implicit class ZIOLogger[R, E, A](zio: ZIO[R, E, A]) {
    def withAuditingLogs(
        endpoint: ZPartialServerEndpoint[_, _, _, _, _, _, _],
        correlationId: Option[String]
    ): ZIO[R, E, A] =
      withGenericAuditingLogs(endpoint)
        .withCorrelationId(correlationId)

    def withAuditingLogs(
        endpoint: Endpoint[_, _, _, _, _],
        correlationId: Option[String]
    ): ZIO[R, E, A] =
      withGenericAuditingLogs(endpoint)
        .withCorrelationId(correlationId)

    def withAuditingLogs(
        endpoint: Endpoint[_, _, _, _, _]
    ): ZIO[R, E, A] =
      withGenericAuditingLogs(endpoint).withCorrelationId

    private def withGenericAuditingLogs(
        endpoint: ZPartialServerEndpoint[_, _, _, _, _, _, _]
    ): ZIO[R, E, A] =
      zio
        .withHttpPath(endpoint.showPathTemplate())
        .withHttpMethod(
          endpoint.method.getOrElse("UNKNOWN").toString.toUpperCase
        )

    private def withGenericAuditingLogs(
        endpoint: Endpoint[_, _, _, _, _]
    ): ZIO[R, E, A] =
      zio
        .withHttpPath(endpoint.showPathTemplate())
        .withHttpMethod(
          endpoint.method.getOrElse("UNKNOWN").toString.toUpperCase
        )
  }
}
