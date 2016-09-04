package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Bill.schema, ManyPersonHasManyBill.schema, ManyUserHasManyRoom.schema, Person.schema, PlayEvolutions.schema, Room.schema, User.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Bill
    *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
    *  @param amount Database column amount SqlType(money)
    *  @param date Database column date SqlType(date), Default(None)
    *  @param idPerson Database column id_person SqlType(int4) */
  case class BillRow(id: Int, amount: Double, date: Option[java.sql.Date] = None, idPerson: Int)
  /** GetResult implicit for fetching BillRow objects using plain SQL queries */
  implicit def GetResultBillRow(implicit e0: GR[Int], e1: GR[Double], e2: GR[Option[java.sql.Date]]): GR[BillRow] = GR{
    prs => import prs._
      BillRow.tupled((<<[Int], <<[Double], <<?[java.sql.Date], <<[Int]))
  }
  /** Table description of table bill. Objects of this class serve as prototypes for rows in queries. */
  class Bill(_tableTag: Tag) extends Table[BillRow](_tableTag, "bill") {
    def * = (id, amount, date, idPerson) <> (BillRow.tupled, BillRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(amount), date, Rep.Some(idPerson)).shaped.<>({r=>import r._; _1.map(_=> BillRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column amount SqlType(money) */
    val amount: Rep[Double] = column[Double]("amount")
    /** Database column date SqlType(date), Default(None) */
    val date: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("date", O.Default(None))
    /** Database column id_person SqlType(int4) */
    val idPerson: Rep[Int] = column[Int]("id_person")

    /** Foreign key referencing Person (database name person_fk) */
    lazy val personFk = foreignKey("person_fk", idPerson, Person)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Bill */
  lazy val Bill = new TableQuery(tag => new Bill(tag))

  /** Entity class storing rows of table ManyPersonHasManyBill
    *  @param idPerson Database column id_person SqlType(int4)
    *  @param idBill Database column id_bill SqlType(int4) */
  case class ManyPersonHasManyBillRow(idPerson: Int, idBill: Int)
  /** GetResult implicit for fetching ManyPersonHasManyBillRow objects using plain SQL queries */
  implicit def GetResultManyPersonHasManyBillRow(implicit e0: GR[Int]): GR[ManyPersonHasManyBillRow] = GR{
    prs => import prs._
      ManyPersonHasManyBillRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table many_person_has_many_bill. Objects of this class serve as prototypes for rows in queries. */
  class ManyPersonHasManyBill(_tableTag: Tag) extends Table[ManyPersonHasManyBillRow](_tableTag, "many_person_has_many_bill") {
    def * = (idPerson, idBill) <> (ManyPersonHasManyBillRow.tupled, ManyPersonHasManyBillRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(idPerson), Rep.Some(idBill)).shaped.<>({r=>import r._; _1.map(_=> ManyPersonHasManyBillRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id_person SqlType(int4) */
    val idPerson: Rep[Int] = column[Int]("id_person")
    /** Database column id_bill SqlType(int4) */
    val idBill: Rep[Int] = column[Int]("id_bill")

    /** Primary key of ManyPersonHasManyBill (database name many_person_has_many_bill_pk) */
    val pk = primaryKey("many_person_has_many_bill_pk", (idPerson, idBill))

    /** Foreign key referencing Bill (database name bill_fk) */
    lazy val billFk = foreignKey("bill_fk", idBill, Bill)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Person (database name person_fk) */
    lazy val personFk = foreignKey("person_fk", idPerson, Person)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table ManyPersonHasManyBill */
  lazy val ManyPersonHasManyBill = new TableQuery(tag => new ManyPersonHasManyBill(tag))

  /** Entity class storing rows of table ManyUserHasManyRoom
    *  @param idUser Database column id_user SqlType(int4)
    *  @param idRoom Database column id_room SqlType(int4)
    *  @param creator Database column creator SqlType(bool), Default(false) */
  case class ManyUserHasManyRoomRow(idUser: Int, idRoom: Int, creator: Boolean = false)
  /** GetResult implicit for fetching ManyUserHasManyRoomRow objects using plain SQL queries */
  implicit def GetResultManyUserHasManyRoomRow(implicit e0: GR[Int], e1: GR[Boolean]): GR[ManyUserHasManyRoomRow] = GR{
    prs => import prs._
      ManyUserHasManyRoomRow.tupled((<<[Int], <<[Int], <<[Boolean]))
  }
  /** Table description of table many_user_has_many_room. Objects of this class serve as prototypes for rows in queries. */
  class ManyUserHasManyRoom(_tableTag: Tag) extends Table[ManyUserHasManyRoomRow](_tableTag, "many_user_has_many_room") {
    def * = (idUser, idRoom, creator) <> (ManyUserHasManyRoomRow.tupled, ManyUserHasManyRoomRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(idUser), Rep.Some(idRoom), Rep.Some(creator)).shaped.<>({r=>import r._; _1.map(_=> ManyUserHasManyRoomRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id_user SqlType(int4) */
    val idUser: Rep[Int] = column[Int]("id_user")
    /** Database column id_room SqlType(int4) */
    val idRoom: Rep[Int] = column[Int]("id_room")
    /** Database column creator SqlType(bool), Default(false) */
    val creator: Rep[Boolean] = column[Boolean]("creator", O.Default(false))

    /** Primary key of ManyUserHasManyRoom (database name many_user_has_many_room_pk) */
    val pk = primaryKey("many_user_has_many_room_pk", (idUser, idRoom))

    /** Foreign key referencing Room (database name room_fk) */
    lazy val roomFk = foreignKey("room_fk", idRoom, Room)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing User (database name user_fk) */
    lazy val userFk = foreignKey("user_fk", idUser, User)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table ManyUserHasManyRoom */
  lazy val ManyUserHasManyRoom = new TableQuery(tag => new ManyUserHasManyRoom(tag))

  /** Entity class storing rows of table Person
    *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
    *  @param name Database column name SqlType(text)
    *  @param idRoom Database column id_room SqlType(int4) */
  case class PersonRow(id: Int, name: String, idRoom: Int)
  /** GetResult implicit for fetching PersonRow objects using plain SQL queries */
  implicit def GetResultPersonRow(implicit e0: GR[Int], e1: GR[String]): GR[PersonRow] = GR{
    prs => import prs._
      PersonRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table person. Objects of this class serve as prototypes for rows in queries. */
  class Person(_tableTag: Tag) extends Table[PersonRow](_tableTag, "person") {
    def * = (id, name, idRoom) <> (PersonRow.tupled, PersonRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(idRoom)).shaped.<>({r=>import r._; _1.map(_=> PersonRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text) */
    val name: Rep[String] = column[String]("name")
    /** Database column id_room SqlType(int4) */
    val idRoom: Rep[Int] = column[Int]("id_room")

    /** Foreign key referencing Room (database name room_fk) */
    lazy val roomFk = foreignKey("room_fk", idRoom, Room)(r => r.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Person */
  lazy val Person = new TableQuery(tag => new Person(tag))

  /** Entity class storing rows of table PlayEvolutions
    *  @param id Database column id SqlType(int4), PrimaryKey
    *  @param hash Database column hash SqlType(varchar), Length(255,true)
    *  @param appliedAt Database column applied_at SqlType(timestamp)
    *  @param applyScript Database column apply_script SqlType(text), Default(None)
    *  @param revertScript Database column revert_script SqlType(text), Default(None)
    *  @param state Database column state SqlType(varchar), Length(255,true), Default(None)
    *  @param lastProblem Database column last_problem SqlType(text), Default(None) */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
      PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends Table[PlayEvolutionsRow](_tableTag, "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(varchar), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at SqlType(timestamp) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script SqlType(text), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Default(None))
    /** Database column revert_script SqlType(text), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Default(None))
    /** Database column state SqlType(varchar), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem SqlType(text), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table Room
    *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
    *  @param name Database column name SqlType(text)
    *  @param created Database column created SqlType(date) */
  case class RoomRow(id: Int, name: String, created: java.sql.Date)
  /** GetResult implicit for fetching RoomRow objects using plain SQL queries */
  implicit def GetResultRoomRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Date]): GR[RoomRow] = GR{
    prs => import prs._
      RoomRow.tupled((<<[Int], <<[String], <<[java.sql.Date]))
  }
  /** Table description of table room. Objects of this class serve as prototypes for rows in queries. */
  class Room(_tableTag: Tag) extends Table[RoomRow](_tableTag, "room") {
    def * = (id, name, created) <> (RoomRow.tupled, RoomRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(created)).shaped.<>({r=>import r._; _1.map(_=> RoomRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text) */
    val name: Rep[String] = column[String]("name")
    /** Database column created SqlType(date) */
    val created: Rep[java.sql.Date] = column[java.sql.Date]("created")
  }
  /** Collection-like TableQuery object for table Room */
  lazy val Room = new TableQuery(tag => new Room(tag))

  /** Entity class storing rows of table User
    *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
    *  @param username Database column username SqlType(text)
    *  @param password Database column password SqlType(text)
    *  @param email Database column email SqlType(text) */
  case class UserRow(id: Int, username: String, password: String, email: String)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
      UserRow.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "user") {
    def * = (id, username, password, email) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(username), Rep.Some(password), Rep.Some(email)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(text) */
    val username: Rep[String] = column[String]("username")
    /** Database column password SqlType(text) */
    val password: Rep[String] = column[String]("password")
    /** Database column email SqlType(text) */
    val email: Rep[String] = column[String]("email")
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
