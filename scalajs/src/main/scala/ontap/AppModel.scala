package ontap

import diode.Circuit
import diode.data.Pot
import diode.react.ReactConnector
import ontap.auth.{AuthHandler, AuthModel}
import ontap.home.{HomeHandler, HomeModel}

case class AppModel(authModel: AuthModel, homeModel: HomeModel)

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {

  override def initialModel: AppModel = AppModel(
    AuthModel(None, None, None),
    HomeModel(Pot.empty)
  )

  override val actionHandler = composeHandlers(
    new AuthHandler(zoomTo(_.authModel)),
    new HomeHandler(zoomTo(_.homeModel))
  )
}
