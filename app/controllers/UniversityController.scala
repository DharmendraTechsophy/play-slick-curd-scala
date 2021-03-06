
package controllers

import com.google.inject.Inject
import models.{University, UniversityInfo}
import org.slf4j.LoggerFactory
import play.api.Logger
import play.api.i18n._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import play.api.mvc._
import repository.{StudentRepository, UniversityRepository}
import utils.{Constants, SecureAction}
import utils.JsonFormat._

import scala.concurrent.{ExecutionContext, Future}

class UniversityController @Inject()(
                                   cc: ControllerComponents,
                                   universityRepository: UniversityRepository,
                                   action:SecureAction,
                                 )(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  import Constants._

  def getByIdByUser: Action[AnyContent] =
    action.async {
      req=>
      universityRepository.getByIdByUser(req.user.id.get).map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def list: Action[AnyContent] =
    action.async {
      universityRepository.getAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }


    def createByIdByUser: Action[JsValue] =
      action.async(parse.json) { request =>
        request.body.validate[UniversityInfo].fold(error => Future.successful(BadRequest(JsError.toJson(error))), {
          uni =>
            val university = University(uni.name,uni.location,request.user.id.get,uni.id)
          universityRepository.insert(university).map { createdUniversiytId =>
            Ok(Json.toJson(Map("id" -> createdUniversiytId))).withHeaders("Access-Control-Allow-Origin" -> "*")
          }
        })
      }


  def create: Action[JsValue] =
    action.async(parse.json) { request =>
      request.body.validate[University].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { uni =>
        universityRepository.insert(uni).map { createdUniversiytId =>
          Ok(Json.toJson(Map("id" -> createdUniversiytId))).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      })
    }

  def delete(uId: Int): Action[AnyContent] =
    action.async { _ =>
      universityRepository.delete(uId).map { _ =>
        Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def get(universityId: Int): Action[AnyContent] =
    action.async { _ =>
      universityRepository.getById(universityId).map { universityOpt =>
        universityOpt.fold(Ok(Json.toJson("{}")))(university => Ok(
          Json.toJson(university))).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def update: Action[JsValue] =
    action.async(parse.json) { request =>
      request.body.validate[University].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { university =>
        universityRepository.update(university).map { _ => Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*") }
      })
    }

}

