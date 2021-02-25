package repository;

import model.Article;
import model.aTag;

import java.util.List;

public interface ArticlesRepository {
    boolean add(Article article);
    List<Article> getAll();
    List<Article> getFilteredByTags(List<aTag> tags);
    Article getById (int id);
    boolean delete(int id);

    boolean update(Article article);
}
