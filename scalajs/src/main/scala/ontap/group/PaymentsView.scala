package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.{AppPage, NewPaymentPage}

object PaymentsView {

  case class Props(groupKey: String, payments: Map[String, PaymentDetails], ctl: RouterCtl[AppPage])

  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      <.div(
        <.h4("Payments"),
        if (p.payments.isEmpty) {
          <.div
        } else {
          <.ul(^.className := "collection",
            p.payments.toVdomArray(x =>
              <.li(^.key := x._1, ^.className := "collection-item", x._2.name)
            )
          )
        },
        ctl.link(NewPaymentPage(p.groupKey))(^.className := "waves-effect waves-light btn", "New payment")
      )
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentsView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(groupKey: String, payments: Map[String, PaymentDetails], ctl: RouterCtl[AppPage]) =
    component(Props(groupKey, payments, ctl))
}