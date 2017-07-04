package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.{AppPage, NewPaymentPage}

object PaymentsView {

  case class Props(groupDetails: GroupDetails, ctl: RouterCtl[AppPage])

  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      val groupKey = p.groupDetails.key
      val payments = p.groupDetails.payments.toSeq.sortBy(_._2.date)
      val members = p.groupDetails.members
      <.div(
        <.h4("Payments",
          ctl.link(NewPaymentPage(groupKey))(^.className := "right waves-effect waves-light btn", "New payment")
        ),
        if (payments.isEmpty) {
          <.div
        } else {
          <.ul(^.className := "collection",
            payments.toVdomArray(x => PaymentItemView(groupKey, x._2, members, ctl))
          )
        }
      )
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentsView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(groupDetails: GroupDetails, ctl: RouterCtl[AppPage]) =
    component(Props(groupDetails, ctl))
}