package repository

import javax.inject.{Inject, Singleton}
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.Future

@Singleton()
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserTable with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def insert(user: User): Future[Int] =
    db.run {
      userTableQueryInc += user
    }

  def insertAll(users: List[User]): Future[Seq[Int]] =
    db.run {
      userTableQueryInc ++= users
    }

  def search(username:String,pass:String):Future[Option[User]] =
  {
    db.run {
      userTableQuery.filter(_.userName===username).filter(_.password === pass).result.headOption
    }
  }

  def update(user:User): Future[Int] =
    db.run {
      userTableQuery.filter(_.id === user.id).update(user)
    }

  def delete(id: Int): Future[Int] =
    db.run {
      userTableQuery.filter(_.id === id).delete
    }

  def getAll(): Future[List[User]] =
    db.run {
      userTableQuery.to[List].result
    }

  def getById(userId: Int): Future[Option[User]] =
    db.run {
      userTableQuery.filter(_.id === userId).result.headOption
    }



  def ddl = userTableQuery.schema

}

private[repository] trait UserTable  {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  lazy protected val userTableQuery = TableQuery[UserTable]
  lazy protected val  userTableQueryInc = userTableQuery returning userTableQuery.map(_.id)

  private[UserTable] class UserTable(tag: Tag) extends Table[User](tag, "user") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val firstName = column[String]("fname")
    val lastName = column[String]("lname")
    val userName = column[String]("username")
    val email = column[String]("email")
    val password = column[String]("password")
    val createdDate = column[String]("created_date")

    def * = (firstName,lastName,userName,email,password,createdDate, id.?) <> (User.tupled, User.unapply)

  }

}

