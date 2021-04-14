package controllers

import com.google.inject.Inject
import models.User
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import repository.{StudentRepository, UserRepository}
import utils.JsonFormat._

import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(
                                    cc: ControllerComponents,
                                    userRepository: UserRepository
                                  )(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def list: Action[AnyContent] =
    Action.async {
      userRepository.getAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
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

  def get(userId: Int): Action[AnyContent] =
    Action.async { _ =>
      userRepository.getById(userId).map { userOpt =>
        userOpt.fold(Ok(Json.toJson("{}")))(user => Ok(
          Json.toJson(user))).withHeaders("Access-Control-Allow-Origin" -> "*")
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

  def search: Action[JsValue] = {
    println("Requesting for Searching the Record......")
    Action.async(parse.json) {
      request =>
        println("Searching request is : "+request.body)
        request.body.validate[User].fold(
          error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          {
            user =>
            userRepository.search(user.userName,user.password).map { x => Ok(
              Json.toJson(x)).withHeaders("Access-Control-Allow-Origin" -> "*")
            }
          }
        )
    }
  }


}

