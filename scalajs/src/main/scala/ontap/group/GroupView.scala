package ontap.group

import diode.data.{Empty, Pending, Ready}
import diode.react.ModelProxy
import firebase.database.Reference
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.shared.SharedView
import ontap.{AppCircuit, AppPage, Database}

object GroupView {

  case class Props(groupKey: String, proxy: ModelProxy[GroupModel], ctl: RouterCtl[AppPage])

  class Backend($: BackendScope[Props, Unit]) {

    private var groupRef: Option[Reference] = None

    def render(p: Props): VdomElement = {
      val proxy = p.proxy()
      val group = proxy.group
      val newMemberError = proxy.newMemberError
      <.div(
        ^.className := "row",
        group match {
          case Pending(startTime) => SharedView.circularLoading
          case Ready(g) =>
            <.div(
              <.div(^.className := "col l7 s12",
                PaymentsView(p.groupKey, g.members, g.payments, p.ctl)
              ),
              <.div(^.className := "col l5 s12",
                MembersView(g.members, newMemberError)
              )
            )
          case _ => <.div()
        }
      )
    }

    def start = Callback {
      val groupId = $.props.runNow().groupKey
      Database.observeGroup(groupId, g => AppCircuit.dispatch(GroupDetailsLoadedAction(g))) match {
        case Some(ref) => groupRef = Some(ref)
        case None => AppCircuit.dispatch(GroupDetailsFailedAction(new Exception("Failed loading group")))
      }
    }

    def stop = Callback {
      groupRef.map(r => r.off())
    }
  }

  val component = ScalaComponent.builder[Props]("GroupView")
    .stateless
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .componentWillUnmount(_.backend.stop)
    .build

  def apply(groupId: String, proxy: ModelProxy[GroupModel], ctl: RouterCtl[AppPage]) =
    component(Props(groupId, proxy, ctl))

}
