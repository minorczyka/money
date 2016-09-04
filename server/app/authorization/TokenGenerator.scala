package authorization

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import com.google.inject.{Inject, Singleton}
import dao.UserDao
import models.{SignInModel, Tables}
import play.api.Configuration

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class TokenGenerator @Inject() (
  val configuration: Configuration,
  val userDao: UserDao) {

  val secret = configuration.underlying.getString("play.crypto.secret")

  val tokenMillisValidity = 1000 * 60 * 60

  private def authenticate(request: SignInModel): Future[Option[Tables.UserRow]] = {
    userDao.find(request.username, request.password)
  }

  def generate(request: SignInModel): Future[Option[String]] = {
    authenticate(request).map {
      case Some(user) => {
        val header = JwtHeader("HS256")
        val claims = JwtClaimsSet(Map(
          "userId" -> user.id,
          "username" -> request.username,
          "date" -> System.currentTimeMillis.toString))
        Some(JsonWebToken(header, claims, secret))
      }
      case None => None
    }
  }

  def validate(token: String): Future[Option[Tables.UserRow]] = {
    if (!JsonWebToken.validate(token, secret)) {
      return Future.successful(None)
    }

    val user = (token match {
      case JsonWebToken(header, claims, secret) => claims.asSimpleMap.toOption
      case _ => None
    }).filter { claims =>
      claims.get("date")
        .map(date => date.toLong)
        .exists(date => date + tokenMillisValidity > System.currentTimeMillis)
    }.flatMap(claims => claims.get("userId"))
      .map(userId => userDao.find(userId.toInt))

    user match {
      case Some(u) => u
      case None => Future.successful(None)
    }
  }
}

