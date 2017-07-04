package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import ontap.AppCircuit
import ontap.shared.SharedView
import org.scalajs.dom.raw.HTMLInputElement

object MembersView {

  private var newMemberRef: HTMLInputElement = _

  case class Props(groupDetails: GroupDetails, newMemberError: Option[String])

  def costDivision(paymentDetails: PaymentDetails): Int = {
    (paymentDetails.cost.toDouble / paymentDetails.people.size).ceil.toInt
  }

  def membersWithBalance(groupDetails: GroupDetails): Seq[GroupMember] = {
    val payments = groupDetails.payments.values
    val plus = payments.groupBy(_.payer).mapValues(_.map(p => costDivision(p) * p.people.size).sum)
    val minus = payments.flatMap(p => p.people.map(x => (x, costDivision(p))))
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum)
    groupDetails.members.map(m =>
      GroupMember(m._1, m._2, plus.getOrElse(m._1, 0) - minus.getOrElse(m._1, 0))
    ).toSeq
  }

  class Backend($: BackendScope[Props, Unit]) {

    def inviteNewMember = Callback {
      val email = newMemberRef.value
      if (!email.isEmpty) {
        AppCircuit.dispatch(AddNewMemberAction(email))
      }
    }

    def render(p: Props): VdomElement = {
      val members = membersWithBalance(p.groupDetails).sortBy(_.username)
      val newMemberError = p.newMemberError
      membersWithBalance(p.groupDetails)
      <.div(
        <.h4("Members"),
        <.ul(^.className := "collection",
          members.toVdomArray(x =>
            <.li(^.key := x.key, ^.className := "collection-item",
              x.username,
              <.span(^.classSet("right" -> true, "green-text" -> (x.balance >= 0), "red-text" -> (x.balance < 0)),
                s"${x.balance / 100.0} zł"
              )
            )
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

  def apply(groupDetails: GroupDetails, newMemberError: Option[String]) =
    component(Props(groupDetails, newMemberError))
}
