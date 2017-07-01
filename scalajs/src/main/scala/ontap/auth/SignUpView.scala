package ontap.auth

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import ontap.shared.SharedView._
import ontap.{AppCircuit, AppPage}
import org.scalajs.dom.raw.HTMLInputElement

object SignUpView {

  case class Props(ctl: RouterCtl[AppPage], proxy: ModelProxy[AuthModel])

  class Backend($: BackendScope[Props, Unit]) {

    private var emailRef: HTMLInputElement = _
    private var usernameRef: HTMLInputElement = _
    private var password1Ref: HTMLInputElement = _
    private var password2Ref: HTMLInputElement = _

    def onClick = Callback {
      val email = emailRef.value
      val username = usernameRef.value
      val password1 = password1Ref.value
      val password2 = password2Ref.value
      if (email.isEmpty || username.isEmpty || password1.isEmpty || password2.isEmpty) {
        AppCircuit.dispatch(SignUpErrorAction("All fields are required"))
      } else if (password1 != password2) {
        AppCircuit.dispatch(SignUpErrorAction("Passwords are not the same!"))
      } else {
        AppCircuit.dispatch(SignUpAction(username, email, password1))
      }
    }

    def render(p: Props): VdomElement = {
      val ctl = p.ctl
      val error = p.proxy().signUpError
      <.div(^.className := "section",
        <.div(^.className := "container auth-box-container",
          <.h3("Sign up"),
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
                  textInput.ref(usernameRef = _),
                  <.label("Username")
                )
              ),
              <.div(^.className := "row",
                <.div(^.className := "input-field col s12",
                  passwordInput.ref(password1Ref = _),
                  <.label("Password")
                )
              ),
              <.div(^.className := "row",
                <.div(^.className := "input-field col s12",
                  passwordInput.ref(password2Ref = _),
                  <.label("Repeat password")
                )
              ),
              error.whenDefined(e =>
                <.div(^.className := "row",
                  <.p(e, ^.color := "red")
                )
              ),
              <.div(^.className := "row",
                <.button(^.className := "col s12 btn btn-large waves-effect indigo",
                  ^.onClick --> onClick,
                  "Register")
              )
            )
          )
        )
      )
    }
  }

  val component = ScalaComponent.builder[Props]("SignInView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[AppPage], proxy: ModelProxy[AuthModel]) =
    component(Props(ctl, proxy))
}