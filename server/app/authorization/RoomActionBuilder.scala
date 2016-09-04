package authorization

import dao.RoomDao
import models.Tables.RoomRow
import play.api.libs.json.Json
import play.api.mvc.{ActionRefiner, Result, Results, WrappedRequest}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait RoomActionBuilder extends AuthorizationActionBuilders {

  def roomDao: RoomDao

  def RoomAction(roomId: Int) = TokenAuthorizedAction andThen new ActionRefiner[UserRequest, RoomRequest] {
    override protected def refine[A](request: UserRequest[A]): Future[Either[Result, RoomRequest[A]]] = {
      roomDao.find(roomId).map {
        case Some(room) => Right(new RoomRequest(room, request))
        case None => Left(Results.NotFound(Json.obj("message" -> "Room not found.")))
      }
    }
  }
}

class RoomRequest[A](val room: RoomRow, request: UserRequest[A]) extends WrappedRequest[A](request) {
  def user = request.user
}
