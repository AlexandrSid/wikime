package repository;

import model.Article;
import model.aTag;

import java.util.List;

public interface ArticlesRepository {
    Article add(Article article);
    List<Article> getAll();
    List<Article> getFilteredByTags(List<aTag> tags);
    Article getById (int id);
    boolean delete(int id);

    Article update(Article article);

    static ArticlesRepository getRepository(){
//        return InMemoryArticlesRepository.getInstance();
//        return DBArticleRepository.getInstance();
        return  HibernateRepository.getInstance();
    }
}
