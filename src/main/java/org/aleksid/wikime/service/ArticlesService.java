package org.aleksid.wikime.service;

import com.google.gson.Gson;
import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.Tag;
import org.aleksid.wikime.repository.ArticlesRepository;
import org.aleksid.wikime.restclient.ArticleRestDao;
import org.aleksid.wikime.restclient.ArticleStorageRestClient;
import org.aleksid.wikime.util.ArticleUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticlesService {

    private static final Logger logger = LogManager.getLogger(ArticlesService.class);

    //make it singleton
    private static final ArticlesService service = new ArticlesService();

    private ArticlesService() {
    }

    public static ArticlesService getInstance() {
        return service;
    }

    @Autowired
    private ArticlesRepository repository;

    @Autowired
    private ArticleStorageRestClient restClient;


    public List<Article> getArticlesContainingTags() {
        return repository.getAll();
    }

    public List<Article> getAllArticles() {
        return repository.getAll();
    }

    public List<Article> getArticlesContainingTags(List<Tag> tags) {
        if (tags.isEmpty()) return getArticlesContainingTags();
        return sortByTagsOrder(tags, repository.getFilteredByTags(tags));
    }

    public Article getArticleByID(int id) {
        if (id == 0) {
            return ArticleUtil.getEmptyArticle();
        }
        Article fromMainRepoWOBody = repository.getById(id);
        //for the case of not found
        if (fromMainRepoWOBody.getId() == 0) return fromMainRepoWOBody;

        ArticleRestDao articleText = restClient.getArticleTextById((long) id);
        insertAttributesTo(fromMainRepoWOBody, articleText);
        return fromMainRepoWOBody;
    }

    public boolean deleteArticleByID(int id) {
        restClient.deleteTextById((long) id);
        return repository.delete(id);
    }

    public Article constructAndReturn(int id, String header, String tags, String text) {
        return ArticleUtil.constructAndReturn(id, header, tags, text);
    }

    public Article update(Article article) {
        if (
                article.getHeader().isEmpty() &&
                        article.getTags().isEmpty() &&
                        article.getParagraphs().isEmpty()
        ) {
            repository.delete(article.getId());
            restClient.deleteTextById((long) article.getId());
            return new Article();
        }
        //пока весь текст не перенесён в новую таблицу, следующая строчка имеет смысл
//        restClient.addNewArticle(getAttributesFrom(article));

        restClient.updateArticle(getAttributesFrom(article));
        article.setParagraphs(Collections.emptyList());
        return repository.update(article);
    }

    public Article addArticle(Article article) {
        //вынимаем текст для сохранения и удаляем его из основного объекта
        ArticleRestDao attributesToSave = getAttributesFrom(article);
        article.setParagraphs(Collections.emptyList());
        //сохраняем основной объект с присваиванием ему id
        Article add = repository.add(article);
        //указываем атрибутам правильный id перед сохранением
        attributesToSave.setArticleId(String.valueOf(add.getId()));
        restClient.addNewArticle(attributesToSave);
        return add;
    }


    public List<Tag> createTagsFromRequest(String articleTags) {
        List tags = ((articleTags == null) || (articleTags.isEmpty()))
                ? Collections.emptyList()
                : Arrays.stream(articleTags.split(", ")).distinct().map(Tag::new).collect(Collectors.toList());
        return tags;
    }

    private List<Article> sortByTagsOrder(List<Tag> tags, Collection<Article> articles) {
        //внутренний класс для сортировки
        class ArticleContainer {
            Article article;
            int rating;

            public ArticleContainer(Article article) {
                this.article = article;
                this.rating = 0;
            }

            public Article getArticle() {
                return article;
            }

            public void setRating(int rating) {
                this.rating = rating;
            }
        }
        //каждому тегу в запрашиваемой последовательности назначается рейтинг, пришёл первым - получил меньше(1,2,4,8...)
        Map<String, Integer> tagRating = new HashMap<>();
        int r = 128;//первые 7 тегов, остальные не будут влиять на порядок
        for (Tag tag : tags) {
            tagRating.put(tag.getTag(), r /= 2);
        }

        //заполняется лист для сортировки...
        List<ArticleContainer> sortable = articles.stream().distinct().map(ArticleContainer::new).collect(Collectors.toList());
        //...рассчитывается рейтинг каждой подходящей статьи
        sortable.forEach(container -> container.setRating(container
                .getArticle()
                .getTags()
                .stream()
                .map(tag -> tagRating.getOrDefault(tag.getTag(), 0))
                .reduce(0, Integer::sum))
        );

        //список сортируется
        Collections.sort(sortable, new Comparator<ArticleContainer>() {
            @Override
            public int compare(ArticleContainer o1, ArticleContainer o2) {
                return o2.rating - o1.rating;
            }
        });

        return sortable.stream().map(ArticleContainer::getArticle).collect(Collectors.toList());
    }


//REST based separated storage mechanism components

    public ArticleRestDao getAttributesFrom(Article article) {
        return new ArticleRestDao(
                String.valueOf(article.getId()),
                new Gson().toJson(article.getParagraphs())
        );
    }

    public Article insertAttributesTo(Article article, ArticleRestDao textSource) {
        if (Long.valueOf(textSource.getArticleId()).equals((long) article.getId())) {
            article.setParagraphs(Arrays.asList(new Gson().fromJson(textSource.getText(), String[].class)));
        } else {
            logger.error("attributes id doesn't match target id: Article from db: %s, Article text id: %s", article.toString(), textSource.getArticleId());
        }
        return article;
    }
}
