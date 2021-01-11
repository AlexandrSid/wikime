package repository;

import model.Article;
import model.aTag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryArticlesRepository implements ArticlesRepository {
    private final List<Article> articlesList = new ArrayList<>();

    //make it singleton
    private static final InMemoryArticlesRepository instance = new InMemoryArticlesRepository();
    private InMemoryArticlesRepository(){}
    public static InMemoryArticlesRepository getInstance(){
        return instance;
    }

    @Override
    public boolean add(Article article) {
        return articlesList.add(article);
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
                .orElseGet(null);
    }

    @Override
    public boolean delete(int id) {
        Article byId = getById(id);
        return articlesList.remove(byId);
    }
}