package controllers

import authorization.{RoomActionBuilder, TokenGenerator}
import com.google.inject.{Inject, Singleton}
import dao.RoomDao
import models._
import play.api.libs.json.Json
import play.api.mvc.Controller

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class RoomController @Inject() (
  val tokenGenerator: TokenGenerator,
  val roomDao: RoomDao) extends Controller with RoomActionBuilder {

  implicit val modelFormat = Json.format[RoomModel]
  implicit val createModelFormat = Json.format[RoomCreateModel]
  implicit val addPersonModelFormat = Json.format[RoomAddPersonModel]
  implicit val personModelFormat = Json.format[RoomPersonModel]
  implicit val detailsModelFormat = Json.format[RoomDetailsModel]

  def create = TokenAuthorizedAction.async(parse.json) { request =>
    request.body.validate[RoomCreateModel].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid request body.")))
      },
      room => {
        roomDao.insert(room.name, request.user.id).map(x => Ok(Json.obj("roomId" -> x.id)))
      }
    )
  }

  def list = TokenAuthorizedAction.async { request =>
    roomDao.listAvailableRooms(request.user.id)
      .map(s => s.map(r => RoomModel(r.id, r.name, r.created)))
      .map(s => Ok(Json.obj("rooms" -> s)))
  }

  def details(roomId: Int) = RoomAction(roomId).async { request =>
    roomDao.peopleForRoom(roomId).map { people =>
      Ok(Json.toJson(RoomDetailsModel(request.room.name, people.map(x => RoomPersonModel(x.id, x.name)))))
    }
  }

  def addPerson(roomId: Int) = RoomAction(roomId).async(parse.json) { request =>
    request.body.validate[RoomAddPersonModel].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid request body.")))
      },
      person => {
        roomDao.addPerson(roomId, person.name).map(p => Ok(Json.obj("personId" -> p.id)))
      }
    )
  }
}
