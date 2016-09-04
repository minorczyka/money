import javax.inject._
import play.api._
import play.api.http.HttpFilters
import play.api.mvc._

@Singleton
class Filters @Inject() () extends HttpFilters {

  override val filters = Seq.empty

}
