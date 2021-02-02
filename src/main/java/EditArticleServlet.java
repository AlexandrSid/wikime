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
        System.out.println("doGet@EditArticleServlet with req params:");
        req.getParameterMap().entrySet().stream().map(e->e.getKey().toString() + " " + req.getParameter(e.getKey())).forEach(System.out::println);
        Integer id = Integer.valueOf(req.getParameter("id"));
        if (id != null) {
            System.out.println(id);
            InMemoryArticlesRepository instance = InMemoryArticlesRepository.getInstance();
            Article article = instance.getById(id);
            System.out.println(article);
            req.setAttribute("article", article);
        }
        getServletContext().getRequestDispatcher("/addArticle.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost@EditArticleServlet with req params:");
        req.getParameterMap().entrySet().stream().map(e->e.getKey().toString() + " " + req.getParameter(e.getKey())).forEach(System.out::println);
        InMemoryArticlesRepository instance = InMemoryArticlesRepository.getInstance();
        int id = Integer.valueOf(req.getParameter("id"));
        String title = req.getParameter("title");
        String tags = req.getParameter("tags");
        String text = req.getParameter("text");
        System.out.println(id);
        System.out.println(title);
        System.out.println(tags);
        System.out.println(text);
        Article article = ArticleUtil.constructAndReturn(id,title,tags,text);
        instance.save(article);
        getServletContext().getRequestDispatcher("/article.jsp").forward(req, resp);

    }
}
