package ontap

import firebase.database.Reference
import firebase.{Firebase, FirebaseConfig, User, UserInfo}
import ontap.group.{GroupDetails, PaymentDetails, UserDetails}
import ontap.home.Group

import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.ScalaJSDefined

object Database {

  private val devFirebaseConfig = FirebaseConfig(
    apiKey = "AIzaSyApjqr8k7JbuHMcTHMNag2epZ4QbtqAHSQ",
    authDomain =  "money-cb2f0.firebaseapp.com",
    databaseURL = "https://money-cb2f0.firebaseio.com",
    storageBucket = "",
    messagingSenderId = "904785204869"
  )

  private val firebaseConfig = FirebaseConfig(
    apiKey = "AIzaSyD2IJ_pnVbUF9ddk9pFPxgiPJyFlbCR7zw",
    authDomain =  "money-2de74.firebaseapp.com",
    databaseURL = "https://money-2de74.firebaseio.com",
    storageBucket = "",
    messagingSenderId = "43882604043"
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
    var user: User = null
    auth.createUserWithEmailAndPassword(email, password)
      .then(u => {
        user = u.asInstanceOf[User]
        user.updateProfile(js.Dictionary("displayName" -> username))
      })
      .then(_ => {
        database.ref(s"user/${user.uid}/email").set(email)
        database.ref(s"user/${user.uid}/username").set(username)
      })
      .then(_ => {
        promise.success()
    })
      .`catch`(e => promise.failure(new Exception(e.message)))
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

  def createGroup(name: String): Unit = {
    loggedUser() match {
      case Some(user) =>
        val groups = database.ref("groups")
        val group = js.Dictionary("name" -> name, "members" -> js.Dictionary(user.uid -> user.displayName))
        val newGroup = groups.push(group)
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
          val groups = if (values != null) {
            values.map(i => Group(i._1, i._2.asInstanceOf[String])).toSeq
          } else {
            Seq()
          }
          f(groups)
        })
        Some(groupsRef)
      case None =>
        println("There is no user!")
        None
    }
  }

  private def parsePayment(key: String, dict: js.Dictionary[Any]): PaymentDetails = {
    val name = dict.getOrElse("name", "").asInstanceOf[String]
    val description = dict.getOrElse("description", "").asInstanceOf[String]
    val date = dict.getOrElse("date", "").asInstanceOf[String]
    val cost = dict.getOrElse("cost", "0").asInstanceOf[String].toInt
    val payer = dict.getOrElse("payer", "").asInstanceOf[String]
    val people = dict.get("people") match {
      case Some(p) => p.asInstanceOf[js.Dictionary[String]].keys.toSeq
      case None => Seq()
    }
    PaymentDetails(key, name, description, date, cost, payer, people)
  }

  def observeGroup(groupId: String, f: (GroupDetails => Unit)): Option[Reference] = {
    loggedUser() match {
      case Some(_) =>
        val groupRef = database.ref(s"groups/$groupId")
        groupRef.on("value", (snapshot, _) => {
          val values = snapshot.`val`().asInstanceOf[js.Dictionary[Any]]
          val name = values.getOrElse("name", "").asInstanceOf[String]
          val members = values.getOrElse("members", js.Dictionary[String]()).asInstanceOf[js.Dictionary[String]].toMap
          val payments: Map[String, PaymentDetails] = values.get("payments") match {
            case Some(p) =>
              p.asInstanceOf[js.Dictionary[js.Dictionary[Any]]]
                .map(x => (x._1, parsePayment(x._1, x._2)))
                .toMap
            case None => Map()
          }
          val group = GroupDetails(groupId, name, members, payments)
          f(group)
        })
        Some(groupRef)
      case None =>
        println("There is no user!")
        None
    }
  }

  def getUserId(email: String): Future[UserDetails] = {
    val promise = Promise[UserDetails]()
    database.ref("user").orderByChild("email").equalTo(email).once("value", (snapshot, _) => {
      val obj = snapshot.`val`()
      if (obj != null) {
        val entry = obj.asInstanceOf[js.Dictionary[js.Dynamic]].toMap.head
        val uid = entry._1
        val user = entry._2
        promise.success(UserDetails(uid, user.username.asInstanceOf[String], email))
      } else {
        promise.failure(new Exception("User not found"))
      }
    })
    promise.future
  }

  def addUserToGroup(userDetails: UserDetails, groupDetails: GroupDetails): Future[Unit] = {
    val promise = Promise[Unit]()
    database.ref(s"groups/${groupDetails.key}/members/${userDetails.uid}").set(userDetails.username)
    database.ref(s"user/${userDetails.uid}/groups/${groupDetails.key}").set(groupDetails.name)
    promise.future
  }

  def createPayment(groupKey: String, paymentDetails: PaymentDetails): Unit = {
    val paymentsRef = database.ref(s"groups/${groupKey}/payments")
    val payment = js.Dynamic.literal(
      "name" -> paymentDetails.name,
      "description" -> paymentDetails.description,
      "date" -> paymentDetails.date,
      "cost" -> paymentDetails.cost.toString,
      "payer" -> paymentDetails.payer,
      "people" -> paymentDetails.people.map(x => (x, "true")).toMap.toJSDictionary
    )
    paymentsRef.push(payment)
  }

  def updatePayment(groupKey: String, paymentDetails: PaymentDetails): Unit = {
    val paymentRef = database.ref(s"groups/$groupKey/payments/${paymentDetails.key}")
    paymentRef.set(js.Dynamic.literal(
      "name" -> paymentDetails.name,
      "description" -> paymentDetails.description,
      "date" -> paymentDetails.date,
      "cost" -> paymentDetails.cost.toString,
      "payer" -> paymentDetails.payer,
      "people" -> paymentDetails.people.map(x => (x, "true")).toMap.toJSDictionary
    ))
  }
}
