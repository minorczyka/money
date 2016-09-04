package authorization

import models.Tables
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait AuthorizationActionBuilders {

  def tokenGenerator: TokenGenerator

  def TokenAuthorizedAction = new ActionBuilder[UserRequest] {
    override def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      request.headers.get("Authorization")
        .map(x => tokenGenerator.validate(x)) match {
        case Some(user) => {
          user.flatMap {
            case Some(u) => block(new UserRequest[A](u, request))
            case None => Future.successful(Results.Unauthorized(Json.obj("message" -> "Invalid token")))
          }
        }
        case None => {
          Future.successful(Results.Unauthorized(Json.obj("message" -> "Token not found")))
        }
      }
    }
  }
}

class UserRequest[A](val user: Tables.UserRow, request: Request[A]) extends WrappedRequest[A](request)
