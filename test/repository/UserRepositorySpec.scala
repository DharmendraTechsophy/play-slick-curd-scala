package repository

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test.{Injecting, WithApplication}

import java.sql.Date

class UserRepositorySpec extends PlaySpec with GuiceOneAppPerTest {

  import models._

  "User repository" should {


    "insert a row" in new WithUserRepository() {
      val date = java.time.LocalDate.now.toString()
      val user = User("Perth","Chowhan","pc101","pc101@xyz.com", "12345678",date, Some(1))
      val studentId = await(userRepo.insert(user))

      studentId mustBe 4
    }


    "update a row" in new WithUserRepository() {
      val date = java.time.LocalDate.now.toString()
      val result = await(userRepo.update(User("Perth","Chowhan","pc101","pc1011@xyz.com","12345678" ,date, Some(1))))
      result mustBe 1
    }

    "delete a row" in new WithUserRepository() {
      val result = await(userRepo.delete(1))
      result mustBe 1
    }
  }


}

trait WithUserRepository extends WithApplication with Injecting {

  val userRepo = inject[UserRepository]
}
