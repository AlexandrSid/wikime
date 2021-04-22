package org.aleksid.wikime.repository;

import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.Main;
import org.aleksid.wikime.model.aTag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryArticlesRepository implements ArticlesRepository {
    private List<Article> articlesList = new ArrayList<>();
    private int idHolder;

    //make it singleton
    private static final InMemoryArticlesRepository instance = createAndInitializeRepo();

    private InMemoryArticlesRepository() {
        this.idHolder = 1;
    }

    private static InMemoryArticlesRepository createAndInitializeRepo() {
        InMemoryArticlesRepository repository = new InMemoryArticlesRepository();
        Main.initializeRepo(repository);
        return repository;
    }

    public static ArticlesRepository getInstance() {
        return instance;
    }

    @Override
    public Article add(Article article) {
        article.setId(idHolder++);
        articlesList.add(article);
        return article;
    }

    @Override
    public List<Article> getAll() {
        return articlesList;
    }

    @Override
    public List<Article> getFilteredByTags(List<aTag> tags) {
        return getAll()
                .stream()
                .filter(ar -> ar.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    @Override
    public Article getById(int id) {
        return getAll()
                .stream()
                .filter(article -> article.getId() == id)
                .findFirst()
                .orElseGet(Article::new);
    }

    @Override
    public boolean delete(int id) {
        Article byId = getById(id);
        return articlesList.remove(byId);
    }

    @Override
    public Article update(Article article) {
        Article toUpd = getById(article.getId());
        toUpd.setHeader(article.getHeader());
        toUpd.setTags(article.getTags());
        toUpd.setParagraphs(article.getParagraphs());
        return toUpd;
    }
}
