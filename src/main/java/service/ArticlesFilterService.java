package service;

import model.Article;
import model.Main;
import model.aTag;
import repository.ArticlesRepository;
import repository.InMemoryArticlesRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ArticlesFilterService {
    private final ArticlesRepository repository = new InMemoryArticlesRepository();
    {
        Main.initializeRepo(repository);
    }

    //этот кошмар переделаю
    public List<Article> getAllArticles(){
        return repository.getAll();
    }

    public List<Article> getAllArticles(List<aTag> tags){
        if (tags.isEmpty()) return getAllArticles();
        return repository.getFilteredByTags(tags);
    }

    public List<String> getAllArticleNames(){
        return getAllArticles().stream().map(Article::getHeader).collect(Collectors.toList());
    }

    public List<String> getAllArticleNames(List<aTag> tags){
        return getAllArticles(tags).stream().map(Article::getHeader).collect(Collectors.toList());
    }


}
