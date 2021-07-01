package org.aleksid.wikime.repository;

import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.aTag;

import java.util.List;

public interface ArticlesRepository {
    Article add(Article article);
    List<Article> getAll();
    List<Article> getFilteredByTags(List<aTag> tags);
    Article getById (int id);
    boolean delete(int id);

    Article update(Article article);

    static ArticlesRepository getRepository(){
        return InMemoryArticlesRepository.getInstance();
    }
}
