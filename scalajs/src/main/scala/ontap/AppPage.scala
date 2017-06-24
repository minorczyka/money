package ontap

sealed trait AppPage

case object HomePage extends AppPage
case object SignInPage extends AppPage
case object SignUpPage extends AppPage
case class GroupPage(key: String) extends AppPage
