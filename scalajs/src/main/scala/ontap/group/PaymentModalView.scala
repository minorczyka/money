package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import ontap.shared.SharedView._
import org.scalajs.dom.raw.{HTMLInputElement, HTMLTextAreaElement}

import scala.scalajs.js

object PaymentModalView {

  val htmlId = "payment-modal"

  private var nameRef: HTMLInputElement = _
  private var descriptionRef: HTMLTextAreaElement = _
  private var dateRef: HTMLInputElement = _

  case class Props()

  class Backend($: BackendScope[Props, Unit]) {

    def render(p: Props): VdomElement = {
      <.div(^.id := htmlId, ^.className := "modal",
        <.div(^.className := "modal-content",
          <.h4("Header"),
          <.form(
            <.div(^.className := "input-field col s12",
              textInput.ref(nameRef = _),
              <.label("Name")
            ),
            <.div(^.className := "input-field col s12",
              textAreaInput.ref(descriptionRef = _),
              <.label("Description")
            ),
            <.div(^.className := "input-field col s12",
              datePickerInput.ref(dateRef = _),
              <.label("Date")
            )
          )
        ),
        <.div(^.className := "modal-footer",
          <.a(^.href := "#!", ^.className := "modal-action modal-close waves-effect waves-green btn-flat", "Cancel"),
          <.a(^.href := "#!", ^.className := "modal-action modal-close waves-effect waves-green btn-flat", "Ok")
        )
      )
    }

    def start = Callback {
      val jQuery = js.Dynamic.global.jQuery
      jQuery(s"#$htmlId").modal()
      jQuery(s".datepicker").pickadate()
    }
  }

  val component = ScalaComponent.builder[Props]("Layout")
    .stateless
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .build

  def apply() = component(Props())
}
