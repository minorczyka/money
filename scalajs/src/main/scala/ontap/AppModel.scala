package ontap

import diode.Circuit
import diode.react.ReactConnector
import ontap.auth.{AuthHandler, AuthModel}

case class AppModel(authModel: AuthModel)

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {

  override def initialModel: AppModel = AppModel(
    AuthModel(None, None, None)
  )

  override val actionHandler = composeHandlers(
    new AuthHandler(zoomTo(_.authModel))
  )
}
