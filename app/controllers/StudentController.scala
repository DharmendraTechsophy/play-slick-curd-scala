package controllers

import com.google.inject.Inject
import models.{Student, StudentInfo, StudentUniversity, UniversityStudentCount}
import org.slf4j.LoggerFactory
import play.api.Logger
import play.api.i18n._
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsObject, JsValue, Json}
import play.api.mvc._
import repository.{StudentRepository, UniversityRepository}
import utils.Constants
import utils.JsonFormat._
import utils.SecureAction

import scala.concurrent.{ExecutionContext, Future}

class StudentController @Inject()(
                                    cc: ControllerComponents,
                                    studentRepository: StudentRepository,
                                    action:SecureAction,
                                  )(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  import Constants._

  def list: Action[AnyContent] =
    action.async {
      studentRepository.getAll().map { res =>
        Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

    def getByIdByUser: Action[AnyContent] =
      action.async {
        req=>
        studentRepository.getByIdByUser(req.user.id.get).map { res =>
          Ok(Json.toJson(res)).withHeaders("Access-Control-Allow-Origin" -> "*")
        }
      }

  def createByIdByUser: Action[JsValue] = {
    println("Requesting for Adding the Record......")
    action.async(parse.json) {
      request =>
        println("request is : "+request.body)
        request.body.validate[StudentInfo].fold(
          error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          { s =>
            val student = Student(s.name,s.email,s.UID,request.user.id.get,s.id)
            studentRepository.insert(student).map { createdStudentId =>
              Ok(Json.toJson(Map("id" -> createdStudentId))).withHeaders("Access-Control-Allow-Origin" -> "*")
            }
          })

    }
  }

  def create: Action[JsValue] = {
    println("Requesting for Adding the Record......")
    action.async(parse.json) {
      request =>
        println("request is : "+request.body)
      request.body.validate[Student].fold(
        error => Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
          { student =>
            studentRepository.insert(student).map { createdStudentId =>
              Ok(Json.toJson(Map("id" -> createdStudentId))).withHeaders("Access-Control-Allow-Origin" -> "*")
            }
      })

    }
  }

  def delete(studentId: Int): Action[AnyContent] =
    action.async { _ =>
      studentRepository.delete(studentId).map { _ =>
        Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def get(studentId: Int): Action[AnyContent] =
    action.async { _ =>
      studentRepository.getById(studentId).map { studentOpt =>
        studentOpt.fold(Ok(Json.toJson("{}")))(student => Ok(
          Json.toJson(student))).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def update: Action[JsValue] = {
    println("Requesting for Updating the Record......")
    action.async(parse.json) { request =>
      println("request is : "+request.body)
      request.body.validate[Student].fold(
        error =>
          Future.successful(BadRequest(JsError.toJson(error)).withHeaders("Access-Control-Allow-Origin" -> "*")),
        { student =>
        studentRepository.update(student).map { _ => Ok(Json.toJson("{}")).withHeaders("Access-Control-Allow-Origin" -> "*") }
        }
      )
    }
  }

  def getStudentNameWithUniversityName(): Action[AnyContent] =
    action.async { _ =>
      studentRepository.getStudentNameWithUniversityName().map {
        res =>
          val ans = for(i<-res)yield (StudentUniversity(i._1,i._2))
          Ok(Json.toJson(ans)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }

  def getUniversityNameAndNoOfStudents(): Action[AnyContent] =
    action.async { _ =>
      studentRepository.getUniversityNameAndNoOfStudents().map {
        res =>
          val ans = for(i<-res)yield (UniversityStudentCount(i._1,i._2))
          Ok(Json.toJson(ans)).withHeaders("Access-Control-Allow-Origin" -> "*")
      }
    }






}

