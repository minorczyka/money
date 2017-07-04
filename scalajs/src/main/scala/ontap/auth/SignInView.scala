package ontap.auth

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.shared.SharedView._
import ontap.{AppCircuit, AppPage, SignUpPage}
import org.scalajs.dom.raw.HTMLInputElement

object SignInView {

  case class Props(ctl: RouterCtl[AppPage], proxy: ModelProxy[AuthModel])

  class Backend($: BackendScope[Props, Unit]) {

    private var emailRef: HTMLInputElement = _
    private var passwordRef: HTMLInputElement = _

    def submit(e: ReactEvent) = e.preventDefaultCB >> CallbackTo[Boolean] {
      val email = emailRef.value
      val password = passwordRef.value
      AppCircuit.dispatch(SignInAction(email, password))
      false
    }

    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      val error = p.proxy().signInError
      <.div(^.className := "section",
        <.div(^.className := "center",
          <.div(^.className := "row",
            <.div(^.className := "col m6 offset-m3 s12",
              <.h3(^.className := "", "Sign in"),
              <.div(^.className := "card-panel",
                <.form(^.className := "container", ^.onSubmit ==> submit,
                  <.div(^.className := "row",
                    <.div(^.className := "input-field col s12",
                      emailInput.ref(emailRef = _),
                      <.label("Email")
                    )
                  ),
                  <.div(^.className := "row",
                    <.div(^.className := "input-field col s12",
                      passwordInput.ref(passwordRef = _),
                      <.label("Password")
                    )
                  ),
                  error.whenDefined(e =>
                    <.div(^.className := "row",
                      <.p(e, ^.color := "red")
                    )
                  ),
                  <.div(^.className := "section"),
                  <.div(^.className := "row",
                    <.button(^.className := "col s12 btn btn-large waves-effect", ^.`type` := "submit", "Login")
                  )
                )
              ),
              ctl.link(SignUpPage)(^.className := "center",
                <.p("Create new account")
              )
            )
          )
        )
      )
    }
  }

  val component = ScalaComponent.builder[Props]("SignUpView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[AppPage], proxy: ModelProxy[AuthModel]) =
    component(Props(ctl, proxy))
}
