package models

case class RoomModel(id: Int, name: String, created: java.sql.Date)

case class RoomCreateModel(name: String)

case class RoomAddPersonModel(name: String)

case class RoomDetailsModel(name: String, people: Seq[RoomPersonModel])

case class RoomPersonModel(id: Int, name: String)