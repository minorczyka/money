package ontap.auth

import diode._
import ontap.Database
import scala.concurrent.ExecutionContext.Implicits.global

case class Credentials(username: String, password: String)

case class AuthModel(credentials: Option[Credentials],
                     signInError: Option[String],
                     signUpError: Option[String])

case class SignUpAction(username: String, email:String, password: String) extends Action
case class SignUpErrorAction(message: String) extends Action
case object SignUpSuccessAction extends Action

case class SignInAction(email: String, password: String) extends Action
case class SignInErrorAction(message: String) extends Action
case object SignInSuccessAction extends Action

case object LogOutAction extends Action

class AuthHandler[M](modelRW: ModelRW[M, AuthModel]) extends ActionHandler(modelRW) {

  override def handle = {
    case SignUpAction(username, email, password) =>
      val effect = Effect(Database.createUser(username, email, password)
        .map(u => SignUpSuccessAction)
        .recover { case e => SignUpErrorAction(e.getMessage)})
      updated(value, effect)
    case SignUpErrorAction(message) =>
      updated(value.copy(signUpError = Some(message)))
    case SignUpSuccessAction =>
      updated(value.copy(signUpError = None))
    case SignInAction(email, password) =>
      val effect = Effect(Database.loginUser(email, password)
        .map(u => SignInSuccessAction)
        .recover { case e => SignInErrorAction(e.getMessage) })
      updated(value, effect)
    case SignInErrorAction(message) =>
      updated(value.copy(signInError = Some(message)))
    case SignInSuccessAction =>
      updated(value.copy(signInError = None))
    case LogOutAction =>
      Database.logOut()
      noChange
  }
}