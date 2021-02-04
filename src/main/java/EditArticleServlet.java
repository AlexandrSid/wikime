import model.Article;
import repository.InMemoryArticlesRepository;
import util.ArticleUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "EditArticle",
        urlPatterns = {"/edit", "/new"})

public class EditArticleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("doGet@EditArticleServlet with req params:");
//        req.getParameterMap().entrySet().stream().map(e -> e.getKey().toString() + " " + req.getParameter(e.getKey())).forEach(System.out::println);
        String idFromReq = req.getParameter("id");
        Article article;
        if (idFromReq != null) {
            Integer id = Integer.valueOf(idFromReq);
            System.out.println(id);
            InMemoryArticlesRepository instance = InMemoryArticlesRepository.getInstance();
            article = instance.getById(id);
            System.out.println(article);
        } else {
            article = new Article();
            article.setId(0);
        }
        req.setAttribute("article", article);

        getServletContext().getRequestDispatcher("/addArticle.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

//        System.out.println("doPost@EditArticleServlet with req params:");
//        req.getParameterMap().entrySet().stream().map(e -> e.getKey().toString() + " " + req.getParameter(e.getKey())).forEach(System.out::println);
        InMemoryArticlesRepository instance = InMemoryArticlesRepository.getInstance();
        int id = Integer.valueOf(req.getParameter("id"));
        String title = req.getParameter("title");
        String tags = req.getParameter("tags");
        String text = req.getParameter("text");

        Article article = ArticleUtil.constructAndReturn(id, title, tags, text);

        if (title.length() < 1 && tags.length() <= 2 && text.length() <= 2) {//если все поля пусты
            if (id != 0) {//и сообщение не новое
                instance.delete(article.getId());
            }
            resp.sendRedirect("articles");
        } else {
            if (id != 0) {
                instance.update(article);
            } else {
                instance.add(article);
                id = article.getId();
            }
            resp.sendRedirect("article?id=" + id);
        }
    }
}
