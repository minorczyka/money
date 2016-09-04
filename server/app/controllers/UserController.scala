package controllers

import authorization.TokenGenerator
import com.google.inject.{Inject, Singleton}
import dao.UserDao
import models.{SignInModel, SignUpModel}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserController @Inject () (
  val tokenGenerator: TokenGenerator,
  val userDao: UserDao) extends Controller {

  implicit val signUpModelFormat = Json.format[SignUpModel]
  implicit val signInModelFormat = Json.format[SignInModel]

  def signUp = Action.async(parse.json) { request =>
    request.body.validate[SignUpModel].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid request body.")))
      },
      signUp => {
        userDao.find(signUp.username).flatMap {
          case Some(user) => Future.successful(BadRequest(Json.obj("message" -> "User with this username already exists!")))
          case None => {
            userDao.insert(signUp).map(u => Ok(Json.obj("message" -> "User created.")))
          }
        }
      }
    )
  }

  def signIn = Action.async(parse.json) { request =>
    request.body.validate[SignInModel].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid request body.")))
      },
      signIn => {
        tokenGenerator.generate(signIn).map {
          case Some(token) => Ok(Json.obj("token" -> token))
          case None => Unauthorized(Json.obj("message" -> "Invalid credentials."))
        }
      }
    )
  }
}
