import model.Article;
import repository.InMemoryArticlesRepository;

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
        Integer id = Integer.valueOf(req.getParameter("id"));
        if (id != null) {
            InMemoryArticlesRepository instance = InMemoryArticlesRepository.getInstance();
            Article article = instance.getById(id);
            req.setAttribute("article", article);
        }
        getServletContext().getRequestDispatcher("/addArticle.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
