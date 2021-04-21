package controllers

import models.User
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication, _}
import repository.{UserRepository}
import utils.JsonFormat._

import java.sql.Date
import scala.concurrent.{ExecutionContext, Future}


class UserControllerSpec extends PlaySpec with MockitoSugar with GuiceOneAppPerTest {

  implicit val mockedRepo: UserRepository = mock[UserRepository]

  "UserController " should {

    "create a user" in new WithUserApplication() {
      val date = java.time.LocalDate.now.toString()
      val user = User("Perth","Chowhan","pc101","pc101@xyz.com","12345678", date, Some(1))
      when(mockedRepo.insert(user)) thenReturn Future.successful(1)
      val result = userController.create().apply(FakeRequest().withBody(Json.toJson(user)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """{"id":1}"""
    }

    "update a user" in new WithUserApplication() {
      val date = java.time.LocalDate.now.toString()
      val updatedUser = User("Perth","Chowhan","pc101","pc101@xyz.com", "12345678",date, Some(1))
      when(mockedRepo.update(updatedUser)) thenReturn Future.successful(1)
      val result = userController.update().apply(FakeRequest().withBody(Json.toJson(updatedUser)))
      val resultAsString = contentAsString(result)
      resultAsString mustBe """"{}""""
    }

    "delete a user" in new WithUserApplication() {
      when(mockedRepo.delete(1)) thenReturn Future.successful(1)
      val result = userController.delete(1).apply(FakeRequest())
      val resultAsString = contentAsString(result)
       resultAsString mustBe """"{}""""
    }



  }

}

class WithUserApplication(implicit mockedRepo: UserRepository) extends WithApplication with Injecting {

  implicit val ec = inject[ExecutionContext]

  val messagesApi = inject[MessagesApi]
  val userController: UserController =
    new UserController(
      stubControllerComponents(),
      mockedRepo
    )

}
