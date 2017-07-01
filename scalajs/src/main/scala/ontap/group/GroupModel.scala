package ontap.group

import diode.data.{Pot, Ready}
import diode.{Action, ActionHandler, Effect, ModelRW}
import ontap.Database

import scala.concurrent.ExecutionContext.Implicits.global

case class GroupDetails(key: String, name: String, members: Map[String, String], payments: Map[String, PaymentDetails])
case class UserDetails(uid: String, username: String, email: String)
case class PaymentDetails(name: String, description: String, date: String, cost: Int, payer: String, people: Seq[String])

case class GroupModel(group: Pot[GroupDetails], newMemberError: Option[String])

case object LoadingGroupDetailsAction extends Action
case class GroupDetailsLoadedAction(group: GroupDetails) extends Action
case class GroupDetailsFailedAction(error: Throwable) extends Action
case class AddNewMemberAction(email: String) extends Action
case class NewMemberDetailsAction(userDetails: UserDetails) extends Action
case class NewMemberErrorAction(error: Throwable) extends Action

class GroupHandler[M](modelRW: ModelRW[M, GroupModel]) extends ActionHandler(modelRW) {

  override def handle = {
    case LoadingGroupDetailsAction =>
      updated(value.copy(group = Pot.empty.pending()))
    case GroupDetailsLoadedAction(group) =>
      updated(value.copy(group = value.group.ready(group)))
    case GroupDetailsFailedAction(error) =>
      updated(value.copy(group = value.group.fail(error)))
    case AddNewMemberAction(email) =>
      val effect = Effect(Database.getUserId(email)
        .map(u => NewMemberDetailsAction(u))
        .recover { case e => NewMemberErrorAction(e) }
      )
      updated(value, effect)
    case NewMemberDetailsAction(userDetails) =>
      value.group match {
        case Ready(group) if !group.members.values.exists(x => x == userDetails.uid) =>
          Database.addUserToGroup(userDetails, group)
          updated(value.copy(newMemberError = None))
        case _ => noChange
      }
    case NewMemberErrorAction(error) =>
      updated(value.copy(newMemberError = Some(error.getMessage)))
  }
}
