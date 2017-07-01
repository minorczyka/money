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

    def onClick = Callback {
      val email = emailRef.value
      val password = passwordRef.value
      AppCircuit.dispatch(SignInAction(email, password))
    }

    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      val error = p.proxy().signInError
      <.div(^.className := "section",
        <.div(^.className := "container auth-box-container",
          <.h3("Sign in"),
          <.div(^.className := "auth-box z-depth-1 grey lighten-4 row",
            <.form(^.className := "col s12",
              <.div(^.className := "row",
                <.div(^.className := "input-field col s12",
                  textInput.ref(emailRef = _),
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
                <.button(^.className := "col s12 btn btn-large waves-effect indigo",
                  ^.onClick --> onClick,
                  "Login")
              )
            )
          ),
          ctl.link(SignUpPage)("Create new account", ^.textAlign := "center")
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