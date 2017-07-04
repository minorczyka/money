package ontap.group

import diode.data.Ready
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.{AppPage, Database, GroupPage}
import ontap.shared.SharedView._
import org.scalajs.dom.raw.{HTMLInputElement, HTMLSelectElement, HTMLTextAreaElement}

import scala.scalajs.js

object PaymentView {

  case class Props(proxy: ModelProxy[GroupModel], ctl: RouterCtl[AppPage])

  case class State(errorMessage: Option[String])

  class Backend($: BackendScope[Props, State]) {
    private var nameRef: HTMLInputElement = _
    private var dateRef: HTMLInputElement = _
    private var costRef: HTMLInputElement = _
    private var payerRef: HTMLSelectElement = _
    private var peopleRef: HTMLSelectElement = _
    private var descriptionRef: HTMLTextAreaElement = _

    def render(p: Props, s: State): VdomElement = {
      //      val dataValue = VdomAttr("data-value")
      val ctl = p.ctl
      val proxy = p.proxy()

      def submit = {
        val name = nameRef.value
        val date = dateRef.value
        val cost = costRef.value
        val payer = payerRef.value
        val people = peopleRef.options
          .filter(x => x.selected)
          .map(x => x.value)
          .toSeq
        val description = descriptionRef.value
        if (date.isEmpty) {
          $.setState(State(Some("Date is required!")))
        } else if (payer.isEmpty) {
          $.setState(State(Some("Payer is not selected!")))
        } else if (people.isEmpty) {
          $.setState(State(Some("People are not selected!")))
        } else {
          proxy.group match {
            case Ready(g) =>
              val intCost = (cost.toDouble * 100).round.toInt
              Database.createPayment(g.key, PaymentDetails(name, description, date, intCost, payer, people))
              ctl.set(GroupPage(g.key)).runNow()
            case _ => println("There is no active group!")
          }
          $.setState(State(None))
        }
      }

      val defaultOption = <.option(^.value := "", ^.disabled := true, "Choose")
      val options = proxy.group match {
        case Ready(g) =>
          val o = g.members.map(x => <.option(^.value := x._1, x._2))
          (Seq(defaultOption) ++ o).toTagMod
        case _ => defaultOption
      }

      <.div(^.className := "row",
        <.h4(^.className := "col s12", "Add new payment"),
        <.form(^.className := "col s12", ^.onSubmit --> submit,
          <.div(^.className := "input-field",
            textInput.ref(nameRef = _)(^.required := true),
            <.label("Name")
          ),
          <.div(^.className := "input-field",
            datePickerInput.ref(dateRef = _),
            <.label("Date")
          ),
          <.div(^.className := "input-field",
            moneyInput.ref(costRef = _)(^.required := true),
            <.label("Cost")
          ),
          <.div(^.className := "input-field",
            selectInput.ref(payerRef = _)(^.defaultValue := "", options),
            <.label("Payer")
          ),
          <.div(^.className := "input-field",
            multiSelectInput.ref(peopleRef = _)(options),
            <.label("People")
          ),
          <.div(^.className := "input-field",
            textAreaInput.ref(descriptionRef = _),
            <.label("Description")
          ),
          s.errorMessage.whenDefined(e =>
            <.div(
              <.p(e, ^.color := "red"),
              <.section
            )
          ),
          <.button(^.className := "col s12 btn btn-large waves-effect", ^.`type` := "submit", "Submit")
        )
      )
    }

    def start = Callback {
      val jQuery = js.Dynamic.global.jQuery
      jQuery(".datepicker").pickadate(js.Dynamic.literal(
        format = "dd-mm-yyyy",
//        formatSubmit = "yyyy/mm/dd",
        firstDay = 1
      ))
      jQuery("select").material_select()
    }
  }

  val component = ScalaComponent.builder[Props]("PaymentView")
    .initialState(State(None))
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .build

  def apply(proxy: ModelProxy[GroupModel], ctl: RouterCtl[AppPage]) =
    component(Props(proxy, ctl))
}
