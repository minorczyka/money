package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object PaymentsView {

  case class Props()

  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
        <.div()
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentsView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply() = component(Props())
}