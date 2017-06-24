package ontap.home

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.{AppPage}

object HomeView {

  case class Props(ctl: RouterCtl[AppPage])

  val component = ScalaComponent.builder[Props]("HomeView")
    .render_P(p => <.div(
      ^.className := "row",
      <.div(
        ^.className := "collection"
      )
    )).build

  def apply(ctl: RouterCtl[AppPage]) = component(Props(ctl))
}
