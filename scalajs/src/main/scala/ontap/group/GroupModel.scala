package ontap.group

import diode.data.{Pot, Ready}
import diode.{Action, ActionHandler, Effect, ModelRW}
import ontap.Database
import ontap.auth.LogOutAction

import scala.concurrent.ExecutionContext.Implicits.global

case class GroupDetails(key: String, name: String, members: Map[String, String],
                        payments: Map[String, PaymentDetails], paymentsPage: Int)
case class UserDetails(uid: String, username: String, email: String)
case class GroupMember(key: String, username: String, balance: Int)

case class PaymentDetails(key: String, name: String, description: String, date: String, cost: Int, payer: String, people: Seq[String]) {
  def costDivision(): Int = {
    (cost.toDouble / people.size).ceil.toInt
  }

  def moneyGain(person: String): Option[Int] = {
    val plus = if (payer == person) cost else 0
    val minus = if (people.contains(person)) costDivision() else 0
    plus - minus match {
      case 0 => None
      case x => Some(x)
    }
  }
}

case class GroupModel(group: Pot[GroupDetails], newMemberError: Option[String])

case object LoadingGroupDetailsAction extends Action
case class GroupDetailsLoadedAction(group: GroupDetails) extends Action
case class GroupDetailsFailedAction(error: Throwable) extends Action
case class AddNewMemberAction(email: String) extends Action
case class NewMemberDetailsAction(userDetails: UserDetails) extends Action
case class NewMemberErrorAction(error: Throwable) extends Action
case class ChangePaymentsPage(page: Int) extends Action

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
    case ChangePaymentsPage(page) =>
      value.group match {
        case Ready(group) =>
          updated(value.copy(group = value.group.ready(group.copy(paymentsPage = page))))
        case _ => noChange
      }
    case LogOutAction =>
      updated(value.copy(group = Pot.empty, newMemberError = None))
  }
}
