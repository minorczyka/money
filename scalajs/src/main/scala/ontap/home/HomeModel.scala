package ontap.home

import diode.data.Pot
import diode.{Action, ActionHandler, ModelRW}
import ontap.auth.LogOutAction

case class Group(key: String, name: String)

case class HomeModel(groups: Pot[Seq[Group]])

case object LoadingGroupsAction extends Action
case class GroupsLoadedAction(groups: Seq[Group]) extends Action
case class GroupsFailedAction(error: Throwable) extends Action

class HomeHandler[M](modelRW: ModelRW[M, HomeModel]) extends ActionHandler(modelRW) {

  override def handle = {
    case LoadingGroupsAction =>
      updated(value.copy(groups = Pot.empty.pending()))
    case GroupsLoadedAction(groups) =>
      updated(value.copy(groups = value.groups.ready(groups)))
    case GroupsFailedAction(error) =>
      updated(value.copy(groups = value.groups.fail(error)))
    case LogOutAction =>
      updated(value.copy(groups = Pot.empty))
  }
}
