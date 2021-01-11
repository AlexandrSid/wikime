import model.Article;
import repository.InMemoryArticlesRepository;

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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        InMemoryArticlesRepository instance = InMemoryArticlesRepository.getInstance();
        Article article = instance.getById(id);
        req.setAttribute("article", article);
        RequestDispatcher view = req.getRequestDispatcher("article.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        InMemoryArticlesRepository repository = InMemoryArticlesRepository.getInstance();
        repository.delete(id);
        resp.sendRedirect("/wikime/articles");
    }
}
