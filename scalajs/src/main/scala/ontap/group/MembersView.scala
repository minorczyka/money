package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import ontap.AppCircuit
import ontap.shared.SharedView
import org.scalajs.dom.raw.HTMLInputElement

object MembersView {

  private var newMemberRef: HTMLInputElement = _

  case class Props(members: Map[String, String], newMemberError: Option[String])

  class Backend($: BackendScope[Props, Unit]) {

    def inviteNewMember = Callback {
      val email = newMemberRef.value
      if (!email.isEmpty) {
        AppCircuit.dispatch(AddNewMemberAction(email))
      }
    }

    def render(p: Props): VdomElement = {
      val members = p.members
      val newMemberError = p.newMemberError
      <.div(
        <.h4("Members"),
        <.ul(^.className := "collection",
          members.toVdomArray(x =>
            <.li(^.key := x._1, ^.className := "collection-item", x._2)
          )
        ),
        <.div(
          <.form(^.action := "#", ^.className := "form-flex",
            <.div(^.className := "input-field form-flex-item",
              SharedView.textInput.ref(newMemberRef = _)(^.id := "group-name"),
              <.label(^.`for` := "group-name", "Email")
            ),
            <.div(^.className := "input-field",
              <.div(^.className := "btn", ^.onClick --> inviteNewMember,
                <.span("Invite")
              )
            )
          ),
          newMemberError.whenDefined(e =>
            <.div(^.className := "row",
              <.p(e, ^.color := "red")
            )
          )
        )
      )
    }
  }

  val component = ScalaComponent.builder[Props]("MembersView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(members: Map[String, String], newMemberError: Option[String]) = component(Props(members, newMemberError))
}
