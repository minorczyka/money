package ontap.home

import diode.data._
import diode.react.ModelProxy
import firebase.database.Reference
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.shared.{ModalView, SharedView}
import ontap.{AppCircuit, AppPage, Database, GroupPage}
import org.scalajs.dom.raw.HTMLInputElement

object HomeView {

  case class Props(proxy: ModelProxy[HomeModel], ctl: RouterCtl[AppPage])

  class Backend($: BackendScope[Props, Unit]) {

    private var groupNameRef: HTMLInputElement = _
    private var groupsRef: Option[Reference] = None

    def createGroup = Callback {
      val groupName = groupNameRef.value
      if (!groupName.isEmpty) {
        Database.createGroup(groupName)
        groupNameRef.value = ""
      }
    }

    def render(p: Props): VdomElement = {
      val proxy = p.proxy()
      val ctl = p.ctl
      val groups = proxy.groups
      <.div(
        <.div(^.className := "row",
          <.div(^.className := "col s12",
            <.form(^.action := "#", ^.className := "form-flex",
              <.div(^.className := "input-field form-flex-item",
                SharedView.textInput.ref(groupNameRef = _)(^.id := "group-name"),
                <.label(^.`for` := "group-name", "Group name")
              ),
              <.div(^.className := "input-field",
                <.div(^.className := "btn", ^.onClick --> createGroup,
                  <.span("Create")
                )
              )
            ),
            groups match {
              case Pending(_) => SharedView.circularLoading
              case Empty => <.div()
              case Ready(x) =>
                <.div(^.className := "collection",
                  x.toVdomArray(g => ctl.link(GroupPage(g.key))(^.key := g.key, ^.className := "collection-item", g.name))
                )
              case _ => <.div()
            }
          )
        )
      )
    }

    def start = Callback {
      Database.observeGroups(g => AppCircuit.dispatch(GroupsLoadedAction(g))) match {
        case Some(ref) => groupsRef = Some(ref)
        case None => AppCircuit.dispatch(GroupsFailedAction(new Exception("Failed loading groups")))
      }
    }

    def stop = Callback {
      groupsRef.map(r => r.off())
    }
  }

  val component = ScalaComponent.builder[Props]("HomeView")
    .stateless
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .componentWillUnmount(_.backend.stop)
    .build

  def apply(proxy: ModelProxy[HomeModel], ctl: RouterCtl[AppPage]) = component(Props(proxy, ctl))
}
