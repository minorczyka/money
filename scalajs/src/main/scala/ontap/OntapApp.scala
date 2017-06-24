package ontap

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{BaseUrl, Redirect, Resolution, Router, RouterConfig, RouterConfigDsl, RouterCtl}
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import ontap.auth.{SignInView, SignUpView}
import ontap.home.HomeView
import ontap.shared.LayoutView
import org.scalajs.dom
import org.scalajs.dom.html.Div

import scala.scalajs.js.JSApp

object OntapApp extends JSApp {

  val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))

  val routerConfig: RouterConfig[AppPage] = RouterConfigDsl[AppPage].buildConfig { dsl =>
    import dsl._

    val authModelConnection = AppCircuit.connect(_.authModel)

    def checkAuthorization = {
      Database.isLogged()
    }

    val authorizedRoutes = (emptyRule
      | staticRoute(root, HomePage) ~> renderR(ctl => HomeView(ctl))
      ).addCondition(CallbackTo[Boolean](checkAuthorization))(_ => Some(redirectToPage(SignInPage)(Redirect.Replace)))

    val unauthorizedRoutes = (emptyRule
      | staticRoute("#signIn", SignInPage) ~> renderR(ctl => authModelConnection(proxy => SignInView(ctl, proxy)))
      | staticRoute("#signUp", SignUpPage) ~> renderR(ctl => authModelConnection(proxy => SignUpView(ctl, proxy)))
    ).addCondition(CallbackTo[Boolean](!checkAuthorization))(_ => Some(redirectToPage(HomePage)(Redirect.Replace)))

    (emptyRule
      | authorizedRoutes
      | unauthorizedRoutes
    ).notFound(redirectToPage(SignInPage)(Redirect.Replace)).renderWith(layout)
  }

  def layout(ctl: RouterCtl[AppPage], r: Resolution[AppPage]): TagOf[Div] =
    <.div(
      LayoutView(ctl, Database.loggedUser().map(_.displayName.asInstanceOf[String])),
      <.div(
        ^.className := "container",
        r.render()
      )
    )

  def main(): Unit = {
    val (router, ctl) = Router.componentAndCtl(baseUrl, routerConfig)
    router().renderIntoDOM(dom.document.getElementsByClassName("app")(0).domAsHtml)

    Database.onAuthStateChanged {
      case Some(user) => {
        ctl.set(HomePage).runNow()
      }
      case None => {
        ctl.set(SignInPage).runNow()
      }
    }
  }
}
