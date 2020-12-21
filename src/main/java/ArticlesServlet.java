import model.aTag;
import service.ArticlesFilterService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet(
        name = "ShowArticlesServlet",
        urlPatterns = "/articles"
)
public class ArticlesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String articleTag = req.getParameter("tag");

        ArticlesFilterService service = new ArticlesFilterService();

        List tags = (articleTag.isEmpty()) ? Collections.emptyList() : Arrays.asList(new aTag(articleTag));

        List requestedArticles = service.getAllArticles(tags);

        req.setAttribute("articles", requestedArticles);
        RequestDispatcher view = req.getRequestDispatcher("articles.jsp");
        view.forward(req, resp);

    }
}