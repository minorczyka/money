package ontap.shared

import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.{Div, Input, Select, TextArea}

object SharedView {

  val circularLoading: TagOf[Div] = <.div(^.className := "parent-center",
    <.div(^.className := "preloader-wrapper big active",
      <.div(^.className := "spinner-layer spinner-blue",
        <.div(^.className := "circle-clipper left",
          <.div(^.className := "circle")
        ),
        <.div(^.className := "gap-patch",
          <.div(^.className := "circle")
        ),
        <.div(^.className := "circle-clipper right",
          <.div(^.className := "circle")
        )
      )
    )
  )

  val textInput: TagOf[Input] = <.input(^.`type` := "text")

  val moneyInput: TagOf[Input] = <.input(^.`type` := "number", ^.min := "0.01", ^.max := "1000", ^.step := "0.01")

  val passwordInput: TagOf[Input] = <.input(^.`type` := "password")

  val checkboxInput: TagOf[Input] = <.input(^.`type` := "checkbox")

  val datePickerInput: TagOf[Input] = <.input(^.`type` := "date", ^.className := "datepicker")

  val textAreaInput: TagOf[TextArea] = <.textarea(^.className := "materialize-textarea")

  val selectInput: TagOf[Select] = <.select()

  val multiSelectInput: TagOf[Select] = <.select(^.multiple := "true")
}
