package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object PaymentItemView {

  case class Props(key: String, paymentDetails: PaymentDetails, members: Map[String, String])

  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val name = p.paymentDetails.name
      val date = if (p.paymentDetails.date.isEmpty) {
        ""
      } else {
        s" (${p.paymentDetails.date})"
      }
      val members = p.members
      val payerName = members.getOrElse(p.paymentDetails.payer, "")
      val cost = (p.paymentDetails.cost.toDouble / 100)
//      <.li(^.className := "collection-item",
      <.a(^.className := "collection-item", ^.href := "#",
        <.b(name),
        date,
        <.span(^.className := "right", s"${cost} zł"),
        <.br,
        payerName
      )
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentItemView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(key: String, paymentDetails: PaymentDetails, members: Map[String, String]) =
    component.withKey(key)(Props(key, paymentDetails, members))
}
