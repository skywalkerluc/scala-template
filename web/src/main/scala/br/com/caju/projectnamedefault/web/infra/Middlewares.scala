package br.com.caju.projectnamedefault.web.infra

import zio.http._

object Middlewares {
  val withSessionValidation: Middleware[Any] =
    new Middleware[Any] {
      override def apply[Env, Err](routes: Routes[Env, Err]): Routes[Env, Err] =
        routes.transform[Env] { handle =>
          handler((req: Request) => handle(req))
        }
    }
}
