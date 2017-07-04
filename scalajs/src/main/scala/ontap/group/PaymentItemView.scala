package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.{AppPage, EditPaymentPage}

object PaymentItemView {

  case class Props(groupKey: String, paymentDetails: PaymentDetails, members: Map[String, String], ctl: RouterCtl[AppPage])

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
      val cost = p.paymentDetails.cost / 100.0
      p.ctl.link(EditPaymentPage(p.groupKey, p.paymentDetails.key))(^.className := "collection-item",
        <.b(name),
        date,
        <.span(^.className := "right", "%.2f zł".format(cost)),
        <.br,
        payerName
      )
//      <.a(^.className := "collection-item", ^.href := "#",
//        <.b(name),
//        date,
//        <.span(^.className := "right", s"${cost} zł"),
//        <.br,
//        payerName
//      )
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentItemView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(groupKey: String, paymentDetails: PaymentDetails, members: Map[String, String], ctl: RouterCtl[AppPage]) =
    component.withKey(paymentDetails.key)(Props(groupKey, paymentDetails, members, ctl))
}
