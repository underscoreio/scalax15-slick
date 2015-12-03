package actions

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import slick.dbio.DBIOAction
import slick.profile.SqlAction

import slick.driver.H2Driver.api._

object Main {

  // Tables -------------------------------------

  case class Album(
    artist : String,
    title  : String,
    year   : Int,
    rating : Rating,
    id     : Long = 0L)

  class AlbumTable(tag: Tag) extends Table[Album](tag, "albums") {
    def artist = column[String]("artist")
    def title  = column[String]("title")
    def year   = column[Int]("year")
    def rating = column[Rating]("rating")
    def id     = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (artist, title, year, rating, id) <> (Album.tupled, Album.unapply)
  }

  lazy val AlbumTable = TableQuery[AlbumTable]



  // Schema actions -----------------------------

  val createTableAction: DBIOAction[Unit, NoStream, Effect.Schema] =
    AlbumTable.schema.create

  val dropTableAction: DBIOAction[Unit, NoStream, Effect.Schema] =
    AlbumTable.schema.drop



  // Select actions -----------------------------

  val selectAction: SqlAction[Seq[String], NoStream, Effect.Read] =
    AlbumTable
      .filter(_.artist === "Keyboard Cat")
      .map(_.title)
      .result



  // Update actions -----------------------------

  val updateAction: SqlAction[Int, NoStream, Effect.Write] =
    AlbumTable
      .filter(_.artist === "Keyboard Cat")
      .map(_.title)
      .update("Even Greater Hits")

  val updateAction2: SqlAction[Int, NoStream, Effect.Write] =
    AlbumTable
      .filter(_.artist === "Keyboard Cat")
      .map(a => (a.title, a.year))
      .update(("Even Greater Hits", 2010))



  // Delete actions -----------------------------

  val deleteAction: SqlAction[Int, NoStream, Effect.Write] =
    AlbumTable
      .filter(_.artist === "Justin Bieber")
      .delete



  // Insert actions -----------------------------

  val insertOneAction: SqlAction[Int, NoStream, Effect.Write] =
    AlbumTable += Album("Pink Floyd", "Dark Side of the Moon", 1978, Rating.Awesome )

  val insertAllAction: SqlAction[Option[Int], NoStream, Effect.Write] =
    AlbumTable ++= Seq(
      Album( "Keyboard Cat"  , "Keyboard Cat's Greatest Hits" , 2009 , Rating.Awesome ),
      Album( "Spice Girls"   , "Spice"                        , 1996 , Rating.Good    ),
      Album( "Rick Astley"   , "Whenever You Need Somebody"   , 1987 , Rating.NotBad  ),
      Album( "Manowar"       , "The Triumph of Steel"         , 1992 , Rating.Meh     ),
      Album( "Justin Bieber" , "Believe"                      , 2013 , Rating.Aaargh  ))



  // Exercise actions ---------------------------

  val insertThreeFaves: SqlAction[Option[Int], NoStream, Effect.Write] =
    AlbumTable ++= Seq(
      Album( "Chroma Key" , "Dead Air for Radios"     , 1998 , Rating.Awesome ),
      Album( "Chroma Key" , "You Go Now"              , 2000 , Rating.Awesome ),
      Album( "Chroma Key" , "Graveyard Mountain Home" , 2004 , Rating.Awesome ))

  def updateRecentAlbums(year: Int): SqlAction[Int, NoStream, Effect.Write] =
    AlbumTable
      .filter(_.year >= year)
      .map(_.rating)
      .update(Rating.Good)

  def deleteArtistsAlbums(artist: String): SqlAction[Int, NoStream, Effect.Write] =
    AlbumTable
      .filter(_.artist === artist)
      .delete

  def insertIfArtistIsAwesome(album: Album): DBIOAction[Seq[Album], NoStream, Effect.All] =
    for {
      awesome     <- AlbumTable
                      .filter(a =>
                        a.artist === album.artist &&
                        a.rating === Rating.awesome)
                      .result
      numInserted <- awesome.length match {
                       case 0 => DBIO.successful(0)
                       case _ => AlbumTable += album
                     }
      results     <- AlbumTable
                       .filter(_.artist === album.artist)
                       .sortBy(_.year.asc)
                       .result
    } yield results



  // Database -----------------------------------

  val db = Database.forConfig("scalaxdb")



  // Let's go! ----------------------------------

  def exec[T](action: DBIO[T]): T =
    Await.result(db.run(action), 2 seconds)

  def main(args: Array[String]): Unit = {
    val everythingAction =
      createTableAction >>
      insertAllAction >>
      insertOneAction >>
      insertThreeFaves >>
      deleteArtistsAlbums("Justin Bieber") >>
      insertIfArtistIsAwesome(Album("Pink Floyd", "Wish You Were Here", 1975, Rating.Awesome))

    exec(everythingAction.transactionally).foreach(println)
  }

}
