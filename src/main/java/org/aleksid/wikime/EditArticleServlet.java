package org.aleksid.wikime;

import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.service.ArticlesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(
//        name = "EditArticle",
//        urlPatterns = {"/edit", "/new"})

public class EditArticleServlet //extends HttpServlet
 {

    private final ArticlesService articlesService = ArticlesService.getInstance();

//    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idFromReq = req.getParameter("id");
        Article article;
        if (idFromReq != null) {
            Integer id = Integer.valueOf(idFromReq);
            article = articlesService.getArticleByID(id);
        } else {
            article = new Article();
            article.setId(0);
        }
        req.setAttribute("article", article);
//        getServletContext().getRequestDispatcher("/addArticle.jsp").forward(req, resp);
    }

//    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        String title = req.getParameter("title");
        String tags = req.getParameter("tags");
        String text = req.getParameter("text");

        Article article = articlesService.constructAndReturn(id, title, tags, text);

        if (title.length() < 1 && tags.length() <= 2 && text.length() <= 2) {//если все поля пусты
            if (id != 0) {//и сообщение не новое
                articlesService.deleteArticleByID(article.getId());
            }
            resp.sendRedirect("articles");
        } else {
            if (id != 0) {
                articlesService.update(article);
            } else {
                Article article1 = articlesService.addArticle(article);
                id = article1.getId();
            }
            resp.sendRedirect("article?id=" + id);
//            resp.sendRedirect("articles");
        }
    }
}
