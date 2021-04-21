package controllers

import com.google.inject.Inject
import models.{User, UserInfo}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import repository.{StudentRepository, UserRepository}
import utils.AuthService
import utils.JsonFormat._

import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(
                                    cc: ControllerComponents,
                                    auth:AuthService,
                                    userRepository: UserRepository

                              ) (implicit ec: ExecutionContext)
  extends AbstractController(cc)  {



  def search(): Action[JsValue] = {
    Action.async(parse.json) { request =>
      request.body.validate[User].fold(error => {Future.successful(BadRequest(JsError.toJson(error)))},
        {
          user =>
            userRepository.search(user.email,user.password).map {
              case Some(user) =>
                Ok(Json.toJson(Map("token"-> auth.encodeToken(UserInfo(user.firstName,user.userName,user.email,user.id))))).withHeaders("Access-Control-Allow-Origin" -> "*")
              case None =>
                BadRequest("Email doesn't Exist").withHeaders("Access-Control-Allow-Origin" -> "*")
            }
        })
    }
  }



  def create: Action[JsValue] = {
    println("Requesting for Adding the Record......")
    Action.async(parse.json) {
      request =>
        println("request is : "+request.body)
      request.body.validate[User].fold(
        error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          { user =>
            userRepository.insert(user).map { createdUserId =>
              Ok(Json.toJson(Map("id" -> createdUserId))).withHeaders("Access-Control-Allow-Origin" -> "*")
            }

      })

    }
  }


  def delete(userId: Int): Action[AnyContent] =
    Action.async { _ =>
      userRepository.delete(userId).map { _ =>
        Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }



  def update: Action[JsValue] = {
    println("Requesting for Updating the Record......")
    Action.async(parse.json) {
      request =>
      println("request is : "+request.body)
      request.body.validate[User].fold(
        error =>
          Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
        { user =>
          userRepository.update(user).map { _ => Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*") }
        }
      )

    }
  }



}

