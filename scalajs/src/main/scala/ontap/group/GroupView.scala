package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object GroupView {

  case class Props()

  class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props): VdomElement = {
      <.div(
        ^.className := "row",
        <.div(
          ^.className := "collection"
        )
      )
    }
  }

  val component = ScalaComponent.builder[Props]("GroupView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply() = component(Props())

}
