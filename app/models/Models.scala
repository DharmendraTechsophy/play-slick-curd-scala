package models

import java.sql.Date


case class Student(name: String, email: String, UID:Int, id: Option[Int]=None)

case class University(name: String,location:String, id: Option[Int] = None)

case class StudentUniversity(name:String,universityName:String)

case class UniversityStudentCount(name:String,count:Int)

case class User(firstName:String,lastName:String,userName:String,email:String,password:String,createdDate:String,id:Option[Int]=None)