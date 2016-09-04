package dao

import java.sql.Date

import com.google.inject.Inject
import models.Tables
import models.Tables.{ManyUserHasManyRoomRow, PersonRow, RoomRow}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RoomDao @Inject()(val dbConfigProvider: DatabaseConfigProvider) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val db = dbConfig.db

  import Tables.profile.api._

  def insert(name: String, userId: Int): Future[RoomRow] = {
    db.run((Tables.Room returning Tables.Room) += RoomRow(0, name, new Date(System.currentTimeMillis)))
      .flatMap { r =>
        db.run(Tables.ManyUserHasManyRoom += ManyUserHasManyRoomRow(userId, r.id, creator = true)).map(_ => r)
      }
  }

  def listAvailableRooms(userId: Int): Future[Seq[RoomRow]] = {
    val q = for {
      (r, u) <- Tables.Room join Tables.ManyUserHasManyRoom on (_.id === _.idRoom)
      if u.idUser === userId
    } yield r
    db.run(q.result)
  }

  def find(roomId: Int) : Future[Option[RoomRow]] = {
    val q = Tables.Room.filter(_.id === roomId)
    db.run(q.result.headOption)
  }

  def addPerson(roomId: Int, name: String): Future[PersonRow] = {
    val q = (Tables.Person returning Tables.Person) += PersonRow(0, name, roomId)
    db.run(q)
  }

  def peopleForRoom(roomId: Int) : Future[Seq[PersonRow]] = {
    val q = Tables.Person.filter(_.idRoom === roomId)
    db.run(q.result)
  }
}
