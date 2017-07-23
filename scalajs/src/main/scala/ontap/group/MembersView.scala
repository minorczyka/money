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

    def submit(e: ReactEvent) = e.preventDefaultCB >> CallbackTo[Boolean] {
      val email = newMemberRef.value
      if (!email.isEmpty) {
        AppCircuit.dispatch(AddNewMemberAction(email))
      }
      false
    }

    def render(p: Props): VdomElement = {
      val members = membersWithBalance(p.groupDetails).sortBy(x => x.username.toLowerCase)
      val newMemberError = p.newMemberError
      membersWithBalance(p.groupDetails)
      <.div(
        <.h4("Members"),
        <.ul(^.className := "collection",
          members.toVdomArray(x =>
            <.li(^.key := x.key, ^.className := "collection-item",
              x.username,
              <.span(^.classSet("right" -> true, "green-text" -> (x.balance >= 0), "red-text" -> (x.balance < 0)),
                "%.2f zł".format(x.balance / 100.0)
              )
            )
          )
        ),
        <.div(
          <.form(^.className := "form-flex", ^.onSubmit ==> submit,
            <.div(^.className := "input-field form-flex-item",
              SharedView.emailInput.ref(newMemberRef = _)(^.id := "group-name"),
              <.label(^.`for` := "group-name", "Email")
            ),
            <.div(^.className := "input-field",
              <.button(^.className := "btn", ^.`type` := "submit", "Invite")
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
