import model.aTag;
import service.ArticlesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(
        name = "ListArticlesServlet",
        urlPatterns = "/articles"
)
public class ArticlesServlet extends HttpServlet {

//    @Autowired - это потом :)
    private final ArticlesService service = ArticlesService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = (String) req.getAttribute("message");
        if (message == null) message = " ";
        req.setAttribute("message", message + "ArticlesServlet.doPost");

        String articleTags = req.getParameter("tag");

        //TODO перенести ли логику в Service?
        List tags = ((articleTags == null) || (articleTags.isEmpty()))
                ? Collections.emptyList()
                : Arrays.stream(articleTags.split(", ")).distinct().map(aTag::new).collect(Collectors.toList());
//        tags = service.createTagsFromRequest(articleTags);

        List requestedArticles = service.getArticlesContainingTags(tags);

        req.setAttribute("articles", requestedArticles);
        req.getRequestDispatcher("articles.jsp").forward(req, resp);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", "ArticlesServlet.doGet");
        doPost(req, resp);
    }
}