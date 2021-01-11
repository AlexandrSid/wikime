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
        name = "ListArticlesServlet",
        urlPatterns = "/articles"
)
public class ArticlesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = (String) req.getAttribute("message");
        if (message == null) message = " ";
        req.setAttribute("message", message + "ArticlesServlet.doPost");


        String articleTag = req.getParameter("tag");

        ArticlesFilterService service = ArticlesFilterService.getInstance();

        List tags = ((articleTag==null)||(articleTag.isEmpty())) ? Collections.emptyList() : Arrays.asList(new aTag(articleTag));

        List requestedArticles = service.getAllArticles(tags);

        req.setAttribute("articles", requestedArticles);
        req.getRequestDispatcher("articles.jsp").forward(req, resp);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", "ArticlesServlet.doGet");
        doPost(req,resp);
    }
}