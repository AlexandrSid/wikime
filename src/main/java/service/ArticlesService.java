package service;

import model.Article;
import model.aTag;
import repository.ArticlesRepository;
import util.ArticleUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArticlesService {

    //make it singleton
    private static final ArticlesService service = new ArticlesService();

    private ArticlesService() {
    }

    public static ArticlesService getInstance() {
        return service;
    }

    private final ArticlesRepository repository = ArticlesRepository.getRepository();


    public List<Article> getArticlesContainingTags() {
        return repository.getAll();
    }

    public List<Article> getArticlesContainingTags(List<aTag> tags) {
        if (tags.isEmpty()) return getArticlesContainingTags();
        return repository.getFilteredByTags(tags);
    }

    public Article getArticleByID(int id) {
        return repository.getById(id);
    }

    public boolean deleteArticleByID(int id) {
        return repository.delete(id);
    }

    public Article constructAndReturn(int id, String header, String tags, String text) {
        return ArticleUtil.constructAndReturn(id, header, tags, text);
    }

    public Article update(Article article) {
        return repository.update(article);
    }

    public Article addArticle(Article article) {
        return repository.add(article);
    }


    public List createTagsFromRequest(String articleTags) {
        List tags = ((articleTags == null) || (articleTags.isEmpty()))
                ? Collections.emptyList()
                : Arrays.stream(articleTags.split(", ")).distinct().map(aTag::new).collect(Collectors.toList());
        return tags;
    }
}
