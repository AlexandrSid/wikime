package org.aleksid.wikime.repository;

import org.aleksid.wikime.dto.DBArticle;
import org.aleksid.wikime.dto.DBTag;
import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.Tag;
import org.aleksid.wikime.util.ArticleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class SpringDataRepository implements ArticlesRepository {

    @Autowired
    private ArticleRepo repo;

    @Override
    public Article add(Article article) {
        DBArticle toSave = new DBArticle(article);
        DBArticle saved = repo.save(toSave);
        return new Article(saved);
    }

    @Override
    public List<Article> getAll() {
        List<DBArticle> articles = StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
        return articles.stream().map(Article::new).collect(Collectors.toList());
    }

    @Override
    public List<Article> getFilteredByTags(List<Tag> tags) {
        List<DBTag> tagsToSearch = tags.stream().map(DBTag::new).collect(Collectors.toList());
        List<DBArticle> foundByTags = repo.findAllByTagsIn(tagsToSearch);
        return foundByTags.stream().map(Article::new).collect(Collectors.toList());
    }

    @Override
    public Article getById(int id) {
        DBArticle found = repo.findById((long) id).orElse(new DBArticle());
        if (found.getId() != 0L) {
            return new Article(found);
        } else
            return ArticleUtil.getEmptyArticle();
    }

    @Override
    public boolean delete(int id) {
        repo.deleteById((long) id);
        return true;
    }

    @Override
    public Article update(Article article) {
        return add(article);
    }
}
