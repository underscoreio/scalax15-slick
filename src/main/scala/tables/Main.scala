package tables

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import slick.driver.H2Driver.api._

object Main {

  // Tables -------------------------------------

  case class Album(
    artist : String,
    title  : String,
    year   : Int,
    rating : Rating,
    id     : Long = 0L)

  class AlbumTable(tag: Tag) extends Table[Album](tag, "messages") {
    def artist = column[String]("artist")
    def title  = column[String]("title")
    def year   = column[Int]("year")
    def rating = column[Rating]("rating")
    def id     = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (artist, title, year, rating, id) <> (Album.tupled, Album.unapply)
  }

  lazy val AlbumTable = TableQuery[AlbumTable]



  // Actions ------------------------------------

  val createTableAction =
    AlbumTable.schema.create

  val insertAlbumsAction =
    AlbumTable ++= Seq(
      Album( "Rick Astley"   , "Whenever You Need Somebody"   , 1987 , Rating.Awesome ),
      Album( "Manowar"       , "The Triumph of Steel"         , 1992 , Rating.Awesome ),
      Album( "Spice Girls"   , "Spice"                        , 1996 , Rating.Awesome ),
      Album( "Keyboard Cat"  , "Keyboard Cat's Greatest Hits" , 2009 , Rating.Awesome ),
      Album( "Justin Bieber" , "Believe"                      , 2013 , Rating.Awesome ))

  val selectAlbumsAction =
    AlbumTable.result



  // Database -----------------------------------

  val db = Database.forConfig("scalaxdb")



  // Let's go! ----------------------------------

  def exec[T](action: DBIO[T]): T =
    Await.result(db.run(action), 2 seconds)

  def main(args: Array[String]): Unit = {
    exec(createTableAction)
    exec(insertAlbumsAction)
    exec(selectAlbumsAction).foreach(println)
  }

}
