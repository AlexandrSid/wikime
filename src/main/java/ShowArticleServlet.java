import model.Article;
import service.ArticlesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name="ShowArticle",
        urlPatterns = "/article"
)
public class ShowArticleServlet extends HttpServlet {

    private final ArticlesService articlesService = ArticlesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("doGet@ShowArticleServlet with req params:");
//        req.getParameterMap().entrySet().stream().map(e-> e.getKey() + " " + Arrays.toString(e.getValue())).forEach(System.out::println);
        Integer id = Integer.valueOf(req.getParameter("id"));
        Article article = articlesService.getArticleByID(id);
        req.setAttribute("article", article);
        RequestDispatcher view = req.getRequestDispatcher("article.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        articlesService.deleteArticleByID(id);
        resp.sendRedirect("articles");
    }
}
