package ontap.group

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.shared.PaginationView
import ontap.{AppCircuit, AppPage, Database, NewPaymentPage}

object PaymentsView {

  val pageSize = 10

  case class Props(groupDetails: GroupDetails, ctl: RouterCtl[AppPage])

  def sortableDate(date: String): String = {
    date.substring(6, 10) + date.substring(3, 5) + date.substring(0, 2)
  }

  class Backend($: BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      val groupKey = p.groupDetails.key
      val paymentsPage = p.groupDetails.paymentsPage
      val paymentsFrom = p.groupDetails.paymentsPage * pageSize
      val paymentsTo = (p.groupDetails.paymentsPage + 1) * pageSize
      val allPayments = p.groupDetails.payments.toSeq
      val payments = allPayments.sortBy(x => sortableDate(x._2.date))(Ordering[String].reverse)
        .slice(paymentsFrom, paymentsTo)
      val members = p.groupDetails.members
      val userId = Database.loggedUser().map(u => u.uid).getOrElse("")
      <.div(
        <.h4("Payments",
          ctl.link(NewPaymentPage(groupKey))(^.className := "right waves-effect waves-light btn", "New payment")
        ),
        if (payments.isEmpty) {
          <.div
        } else {
          <.div(
            <.ul(^.className := "collection",
              payments.toVdomArray(x => PaymentItemView(groupKey, x._2, members, userId, ctl))
            ),
            PaginationView(paymentsPage, allPayments.size, pageSize, (x) => AppCircuit.dispatch(ChangePaymentsPage(x)))
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