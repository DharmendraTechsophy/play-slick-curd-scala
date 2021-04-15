package route

import models.{Student, University, User}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import utils.JsonFormat._

import java.sql.Date

class RouteSpec extends PlaySpec with GuiceOneAppPerSuite {

  "Routes" should {

    "get student list" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/student/list"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """[{"name":"Bob","email":"bob@xyz.com","UID":3,"id":1},{"name":"Rob","email":"rob@abc.com","UID":2,"id":2},{"name":"Joe","email":"joe@xyz.com","UID":1,"id":3}]"""
    }

    "create  student" in new WithApplication() {
      val student = Student("john snow","john@gmail.com",2,Some(4))
      val Some(result) = route(app, FakeRequest(POST, "/student/create").withBody(Json.toJson(student)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"id":4}"""
    }

    "get student" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/student/get/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"name":"Bob","email":"bob@xyz.com","UID":3,"id":1}"""
    }

    "Get invalid student" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/student/get/156"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "update student" in new WithApplication() {
      val student = Student("BOB", "bob@xyz.com", 1, Some(1))
      val Some(result) = route(app, FakeRequest(POST, "/student/update").withBody(Json.toJson(student)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "delete student" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/student/delete/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }



    "get university list" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/university/list"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """[{"name":"HCU","location":"Hyderabad","id":1},{"name":"BHU","location":"Banaras","id":2},{"name":"PUNE University","location":"Pune","id":3}]"""
    }

    "create university" in new WithApplication() {
      val university = University("sIT","shivpuri",Some(4))
      val Some(result) = route(app, FakeRequest(POST, "/university/create").withBody(Json.toJson(university)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"id":4}"""
    }

    "get university" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/university/get/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"name":"HCU","location":"Hyderabad","id":1}"""
    }

    "Get invalid university" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/university/get/156"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "update university" in new WithApplication() {
      val university = University("HCU", "Gachibowli",Some(1))
      val Some(result) = route(app, FakeRequest(POST, "/university/update").withBody(Json.toJson(university)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "delete university" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/university/delete/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }


    //user
    "get user list" in new WithApplication {
      val Some(result) = route(app, FakeRequest(GET, "/user/list"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """[{"firstName":"John","lastName":"Snow","userName":"jSnow11","email":"john@xyz.com","password":"12345678","createdDate":"1996-06-05","id":1},{"firstName":"Jammie","lastName":"Lanister","userName":"jlanister","email":"jimmy@xyz.com","password":"12345678","createdDate":"1997-06-05","id":2},{"firstName":"Jorah","lastName":"Mormant","userName":"mormantJorah","email":"mormant@xyz.com","password":"12345678","createdDate":"1998-06-05","id":3}]"""
    }

    "create user" in new WithApplication() {
      val date = java.time.LocalDate.now.toString()
      val user = User("Perth","Chowhan","pc101","pc101@xyz.com", "12345678",date, Some(4))
      val Some(result) = route(app, FakeRequest(POST, "/user/create").withBody(Json.toJson(user)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"id":4}"""
    }

    "get user" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/user/get/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"firstName":"John","lastName":"Snow","userName":"jSnow11","email":"john@xyz.com","password":"12345678","createdDate":"1996-06-05","id":1}"""
    }

    "Get invalid user" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/user/get/156"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "update user" in new WithApplication() {
     // val date = java.time.LocalDate.now.toString()
      val user = User("Perth","Chowhan","pc101","pc101@xyz.com","12345678","2021-04-12", Some(1))
      val Some(result) = route(app, FakeRequest(POST, "/user/update").withBody(Json.toJson(user)))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }

    "delete user" in new WithApplication() {
      val Some(result) = route(app, FakeRequest(GET, "/user/delete/1"))
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """"{}""""
    }


  }

}
