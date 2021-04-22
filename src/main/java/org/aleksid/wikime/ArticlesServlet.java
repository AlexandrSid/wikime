package org.aleksid.wikime;

import org.aleksid.wikime.service.ArticlesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//@WebServlet(
//        name = "ListArticlesServlet",
//        urlPatterns = "/articles"
//)
public class ArticlesServlet //extends HttpServlet
{

    //    @Autowired - это потом :)
    private final ArticlesService service = ArticlesService.getInstance();

//    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = (String) req.getAttribute("message");
        if (message == null) message = " ";
        req.setAttribute("message", message + "org.aleksid.wikime.ArticlesServlet.doPost");

        String articleTags = req.getParameter("tag");

        List tags = service.createTagsFromRequest(articleTags);

        List requestedArticles = service.getArticlesContainingTags(tags);

        req.setAttribute("articles", requestedArticles);
        req.getRequestDispatcher("articles.jsp").forward(req, resp);

    }

//    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("message", "org.aleksid.wikime.ArticlesServlet.doGet");
        doPost(req, resp);
    }
}