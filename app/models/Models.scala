package models


case class Student(name: String, email: String, UID:Int,user_id:Int, id: Option[Int]=None)
case class StudentInfo(name: String, email: String, UID:Int,user_id:Int, id: Option[Int]=None)

case class University(name: String,location:String,userId:Int, id: Option[Int] = None)
case class UniversityInfo(name: String,location:String,userId:Int, id: Option[Int] = None)

case class StudentUniversity(name:String,universityName:String)
case class UniversityStudentCount(name:String,count:Int)


case class User(firstName:String,lastName:String,userName:String,email:String,password:String,createdDate:String,id:Option[Int]=None)



case class UserInfo(firstName:String,userName:String,email:String,id:Option[Int]=None)
case class Auth(email:String,password:String)
