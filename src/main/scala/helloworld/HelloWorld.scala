package helloworld

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import slick.driver.H2Driver.api._

object HelloWorld {

  // Tables -------------------------------------

  case class Message(sender: String, content: String, id: Long = 0L) {
    override def toString =
      s"${sender}: ${content} (#${id})"
  }

  class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
    def sender  = column[String]("sender")
    def content = column[String]("content")
    def id      = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (sender, content, id) <> (Message.tupled, Message.unapply)
  }

  lazy val MessageTable = TableQuery[MessageTable]



  // Actions ------------------------------------

  val createTablesAction =
    MessageTable.schema.create

  val insertMessagesAction =
    MessageTable ++= Seq(
      Message("Dave", "Hello, HAL. Do you read me, HAL?"),
      Message("HAL",  "Affirmative, Dave. I read you."),
      Message("Dave", "Open the pod bay doors, HAL."),
      Message("HAL",  "I'm sorry, Dave. I'm afraid I can't do that.")
    )

  val selectMessagesAction =
    MessageTable.result.map(_ map (_.toString))



  // Database and helpers -----------------------

  val db = Database.forConfig("scalaxdb")

  def exec[T](action: DBIO[T]): T =
    Await.result(db.run(action), 2 seconds)



  // Let's go! ----------------------------------

  def main(args: Array[String]): Unit =
    exec(createTablesAction andThen insertMessagesAction andThen selectMessagesAction).foreach(println)

}