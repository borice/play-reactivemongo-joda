package controllers

import javax.inject._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.DefaultDB
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import reactivemongo.bson.BSONObjectID

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents,
                               reactiveMongoApi: ReactiveMongoApi)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def db: Future[DefaultDB] = reactiveMongoApi.database
  def testColl: Future[JSONCollection] = db.map(_.collection[JSONCollection]("test"))

  val recId: BSONObjectID = BSONObjectID.generate()


  def load(): Action[AnyContent] = Action.async {
    testColl
      .flatMap(_.find(Json.obj("_id" -> recId), Option.empty[JsObject]).one[JsObject])
      .map {
        case Some(obj) =>
          val now = (obj \ "now").as[org.joda.time.DateTime]
          Ok(now.toString)

        case None => NotFound
      }
  }

  def save(): Action[AnyContent] =
    Action.async {
      val now = org.joda.time.DateTime.now()
      val jsNow = Json.obj("now" -> Json.toJson(now))
      testColl
        .flatMap(_.update(ordered = false)
          .one(
            Json.obj("_id" -> recId),
            Json.obj("$set" -> jsNow),
            upsert = true
          )
        )
        .map(_ => Ok)
    }
}