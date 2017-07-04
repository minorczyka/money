package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.{AppPage, NewPaymentPage}

object PaymentsView {

  case class Props(groupKey: String, members: Map[String, String], payments: Map[String, PaymentDetails], ctl: RouterCtl[AppPage])

  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      <.div(
        <.h4("Payments",
          ctl.link(NewPaymentPage(p.groupKey))(^.className := "right waves-effect waves-light btn", "New payment")
        ),
        if (p.payments.isEmpty) {
          <.div
        } else {
          <.ul(^.className := "collection",
            p.payments.toVdomArray(x => PaymentItemView(x._1, x._2, p.members))
          )
        }
      )
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentsView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(groupKey: String, members: Map[String, String], payments: Map[String, PaymentDetails], ctl: RouterCtl[AppPage]) =
    component(Props(groupKey, members, payments, ctl))
}