package utils
import models._
import play.api.libs.json.Json

object JsonFormat {


  implicit val studentFormat = Json.format[Student]
  implicit val studentInfoFormat = Json.format[StudentInfo]
  implicit val universityFormat = Json.format[University]

  implicit val universityInfoInfoFormat = Json.format[UniversityInfo]
  implicit val studentUniversityFormat = Json.format[StudentUniversity]
  implicit val universityStudentCountFormat = Json.format[UniversityStudentCount]
  implicit val userFormat = Json.format[User]

  implicit val userInfoFormat = Json.format[UserInfo]
  implicit val authFormat = Json.format[Auth]


}


