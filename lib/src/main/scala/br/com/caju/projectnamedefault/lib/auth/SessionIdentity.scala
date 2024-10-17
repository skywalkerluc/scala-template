package br.com.caju.projectnamedefault.lib.auth

import java.util.UUID

sealed trait SessionIdentity

object SessionIdentity {
  case class Admin(userId: UUID, profile: AdminProfile) extends SessionIdentity
}

sealed trait Profile
case class AdminProfile(id: UUID) extends Profile

object IdentityChecker {
  def isAdmin(sessionIdentity: SessionIdentity): Boolean =
    sessionIdentity match {
      case _: SessionIdentity.Admin => true
      case _                        => false
    }
}
