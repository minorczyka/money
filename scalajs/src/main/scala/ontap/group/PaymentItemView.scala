package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.{AppPage, EditPaymentPage}

object PaymentItemView {

  case class Props(groupKey: String, paymentDetails: PaymentDetails, members: Map[String, String], userId: String, ctl: RouterCtl[AppPage])

  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val groupKey = p.groupKey
      val paymentDetails = p.paymentDetails
      val name = paymentDetails.name
      val members = p.members
      val userId = p.userId
      val date = if (paymentDetails.date.isEmpty) {
        ""
      } else {
        s" (${paymentDetails.date})"
      }
      val payerName = members.getOrElse(paymentDetails.payer, "")
      val cost = paymentDetails.cost / 100.0
      val moneyGain = paymentDetails.moneyGain(userId)
      p.ctl.link(EditPaymentPage(groupKey, paymentDetails.key))(^.className := "collection-item",
        <.b(name),
        date,
        <.span(^.className := "right", "%.2f zł".format(cost)),
        <.br,
        payerName,
        moneyGain.whenDefined(gain =>
          <.small(^.classSet("right" -> true, "green-text" -> (gain >= 0), "red-text" -> (gain < 0)), "(%.2f zł)".format(gain / 100.0))
        )
      )
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentItemView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(groupKey: String, paymentDetails: PaymentDetails, members: Map[String, String], userId: String, ctl: RouterCtl[AppPage]) =
    component.withKey(paymentDetails.key)(Props(groupKey, paymentDetails, members, userId, ctl))
}
