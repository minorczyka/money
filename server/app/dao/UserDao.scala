package dao

import com.google.inject.{Inject, Singleton}
import models.{SignUpModel, Tables}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import com.github.t3hnar.bcrypt._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserDao @Inject()(val dbConfigProvider: DatabaseConfigProvider) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val db = dbConfig.db

  import Tables.profile.api._

  def find(userId: Int): Future[Option[Tables.UserRow]] = {
    val q = Tables.User.filter(x => x.id === userId)
    db.run(q.result.headOption)
  }

  def find(username: String): Future[Option[Tables.UserRow]] = {
    val q = Tables.User.filter(x => x.username === username)
    db.run(q.result.headOption)
  }

  def find(username: String, password: String): Future[Option[Tables.UserRow]] = {
    val q = Tables.User.filter(u => u.username === username)
    db.run(q.result.headOption).map {
      case Some(user) => if (password.isBcrypted(user.password)) Some(user) else None
      case None => None
    }
  }

  def insert(credentials: SignUpModel): Future[Tables.UserRow] = {
    db.run((Tables.User returning Tables.User) += Tables.UserRow(
      0,
      credentials.username,
      credentials.password.bcrypt,
      credentials.email
    ))
  }
}
