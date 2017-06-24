package ontap.shared

import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.{Div, Input}

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

  val passwordInput: TagOf[Input] = <.input(^.`type` := "password")

  val checkboxInput: TagOf[Input] = <.input(^.`type` := "checkbox")
}
