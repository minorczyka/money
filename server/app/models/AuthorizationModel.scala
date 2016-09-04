package models

case class SignInModel(username: String, password: String)

case class SignUpModel(username: String, password: String, email: String)
