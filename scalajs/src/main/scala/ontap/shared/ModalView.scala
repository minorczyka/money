package ontap.shared

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html

import scala.scalajs.js

object ModalView {

  case class Props(id: String, content: TagOf[html.Div])

  class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props): VdomElement = {
      <.div(^.id := p.id, ^.className := "modal",
        <.div(^.className := "modal-content",
          p.content
        ),
        <.div(^.className := "modal-footer",
          <.a(^.href := "#!", ^.className := "modal-action modal-close waves-effect waves-green btn-flat", "Cancel"),
          <.a(^.href := "#!", ^.className := "modal-action modal-close waves-effect waves-green btn-flat", "Ok")
        )
      )
    }

    def start = Callback {
      val jQuery = js.Dynamic.global.jQuery
      jQuery("#modal1").modal()
    }
  }

  val component = ScalaComponent.builder[Props]("Layout")
    .stateless
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .build

  def apply(id: String, content: TagOf[html.Div]) = component(Props(id, content))

}
