package br.com.caju.projectnamedefault.web.session.driven

import br.com.caju.projectnamedefault.lib.auth.SessionIdentity
import zio.ZIO

trait DecodingToken {
  def decode(token: String): ZIO[Any, DecodeTokenError, TokenContent]
}

case class TokenContent(sessionIdentity: SessionIdentity)

sealed trait DecodeTokenError

object DecodeTokenError {
  case object CorruptedToken extends DecodeTokenError
  case object ExpiredToken   extends DecodeTokenError
  case object InvalidContent extends DecodeTokenError
}
