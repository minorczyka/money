package ontap.shared

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object PaginationView {

  case class Props(page: Int, count: Int, pageSize: Int, pageChanged: (Int) => Unit)

  class Backend($: BackendScope[Props, Unit]) {
    def changePage(page: Int, pageCount: Int, pageChanged: (Int) => Unit) = Callback {
      if (page >= 0 && page < pageCount) {
        pageChanged(page)
      }
    }

    def render(p: Props): VdomElement = {
      val page = p.page
      val count = p.count
      val pageSize = p.pageSize
      val pageChanged = p.pageChanged
      val pageCount = (count + pageSize - 1) / pageSize

      val left = <.li(^.classSet("disabled" -> (page == 0), "waves-effect" -> (page != 0)),
        <.a(^.onClick --> changePage(page - 1, pageCount, pageChanged),
          <.i(^.className := "material-icons", "chevron_left")
        )
      )
      val right = <.li(^.classSet("disabled" -> (page == pageCount - 1), "waves-effect" -> (page < pageCount - 1)),
        <.a(^.onClick --> changePage(page + 1, pageCount, pageChanged),
          <.i(^.className := "material-icons", "chevron_right")
        )
      )
      val pages = (0 until pageCount).map(x =>
        <.li(^.classSet("active teal lighten-1" -> (x == page), "waves-effect" -> (x != page)),
          <.a(^.onClick --> changePage(x, pageCount, pageChanged), (x + 1).toString)
        )
      )
      val all = Seq(left) ++ pages ++ Seq(right)

      <.ul(^.className := "pagination",
        all.toTagMod
      )
    }
  }

  val component = ScalaComponent.builder[Props]("PaginationView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(page: Int, count: Int, pageSize: Int, pageChanged: (Int) => Unit) =
    component(Props(page, count, pageSize, pageChanged))
}
