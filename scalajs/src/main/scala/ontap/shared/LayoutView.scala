package ontap.shared

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.auth.LogOutAction
import ontap.{AppCircuit, AppPage, HomePage}

object LayoutView {

  case class Props(ctl: RouterCtl[AppPage], username: Option[String])

  class Backend($: BackendScope[Props, Unit]) {

    def logout = Callback {
      AppCircuit.dispatch(LogOutAction)
    }

    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      val username = p.username
      <.nav(
        ^.className := "teal darken-2",
        ^.role := "navigation",
        <.div(
          ^.className := "nav-wrapper container",
          ctl.link(HomePage)("Money", ^.className := "brand-logo"),
          username.whenDefined(username =>
            <.ul(^.id := "nav-mobile", ^.className := "right hide-on-med-and-down",
              <.li(username),
              <.li(
                <.a(^.onClick --> logout,
                  <.i(^.className := "material-icons", "exit_to_app")
                )
              )
            )
          )
        )
      )
    }
  }

  val component = ScalaComponent.builder[Props]("Layout")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[AppPage], username: Option[String]) = component(Props(ctl, username))
}
