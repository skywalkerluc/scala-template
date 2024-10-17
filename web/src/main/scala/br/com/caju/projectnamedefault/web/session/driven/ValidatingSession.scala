package br.com.caju.projectnamedefault.web.session.driven

import br.com.caju.projectnamedefault.lib.auth.SessionIdentity
import zio.ZIO

trait ValidatingSession {

  def validate(
      maybeToken: Option[String]
  ): ZIO[Any, ValidateSessionError, ValidatedSession]
}

object ValidatingSession {

  def validate(
      maybeToken: Option[String]
  ): ZIO[ValidatingSession, ValidateSessionError, ValidatedSession] =
    ZIO.serviceWithZIO(_.validate(maybeToken))
}

case class ValidatedSession(si: SessionIdentity, bearerToken: String)

sealed trait ValidateSessionError

object ValidateSessionError {
  case object MissingToken           extends ValidateSessionError
  case object UndecodableBearerToken extends ValidateSessionError
  case object UserSessionExpired     extends ValidateSessionError
  case object InvalidUserSession     extends ValidateSessionError
}
