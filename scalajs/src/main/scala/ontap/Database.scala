package ontap

import firebase.database.Reference
import firebase.{Firebase, FirebaseConfig, User, UserInfo}
import ontap.home.Group

import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object Database {

  private val firebaseConfig = FirebaseConfig(
    apiKey = "AIzaSyApjqr8k7JbuHMcTHMNag2epZ4QbtqAHSQ",
    authDomain =  "money-cb2f0.firebaseapp.com",
    databaseURL = "https://money-cb2f0.firebaseio.com",
    storageBucket = "",
    messagingSenderId = "904785204869"
  )

  private val app = Firebase.initializeApp(firebaseConfig, "money-cb2f0")

  private val auth = Firebase.auth(app)

  @ScalaJSDefined
  class AuthStateChanged(f: (Option[UserInfo]) => (Unit)) extends js.Object {
    def next(user: UserInfo): Unit = {
      if (user == null) {
        f(None)
      } else {
        f(Some(user.asInstanceOf[UserInfo]))
      }
    }
  }

  def onAuthStateChanged(f: (Option[UserInfo]) => (Unit)): Unit = {
    auth.onAuthStateChanged(new AuthStateChanged(f))
  }

  def createUser(username: String, email: String, password: String): Future[Unit] = {
    val promise = Promise[Unit]()
    auth.createUserWithEmailAndPassword(email, password)
      .then(onResolve = u => {
        val user = u.asInstanceOf[User]
        user.updateProfile(js.Dictionary("displayName" -> username))
          .then(onResolve = _ => {
            promise.success()
          }, onReject = e => promise.failure(new Exception(e.message)))
      }, onReject = e => promise.failure(new Exception(e.message)))
    promise.future
  }

  def loginUser(email: String, password: String): Future[Unit] = {
    val promise = Promise[Unit]()
    auth.signInWithEmailAndPassword(email, password)
      .then(onResolve = _ => promise.success(), onReject = e => promise.failure(new Exception(e.message)))
    promise.future
  }

  def isLogged: Boolean = {
    auth.currentUser != null
  }

  def loggedUser(): Option[UserInfo] = {
    if (auth.currentUser == null) {
      None
    } else {
      Some(auth.currentUser.asInstanceOf[UserInfo])
    }
  }

  def logOut(): Unit = {
    auth.signOut()
  }

  private val database = Firebase.database(app)

  def createGroup(name: String) = {
    loggedUser() match {
      case Some(user) =>
        val groups = database.ref("groups")
        val newGroup = groups.push(name)
        database.ref(s"user/${user.uid}/groups/${newGroup.key}").set(name)
      case None => println("There is no user!")
    }
  }

  def observeGroups(f: (Seq[Group] => Unit)): Option[Reference] = {
    loggedUser() match {
      case Some(user) =>
        val groupsRef = database.ref(s"user/${user.uid}/groups")
        groupsRef.on("value", (snapshot, _) => {
          val values = snapshot.`val`().asInstanceOf[js.Dictionary[Any]]
          val groups = values.map(i => Group(i._1, i._2.asInstanceOf[String])).toSeq
          f(groups)
        })
        Some(groupsRef)
      case None =>
        println("There is no user!")
        None
    }
  }
}
