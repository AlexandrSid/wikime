package org.aleksid.wikime.restclient;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aleksid.wikime.model.Article;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRestDao {
    private String articleId;
    private String text;


    public ArticleRestDao getAttributesFrom(Article article) {
        return new ArticleRestDao(
                String.valueOf(article.getId()),
                new Gson().toJson(article.getParagraphs())
        );
    }

    public Article insertAttributesTo(Article article) {
        if (Long.valueOf(articleId).equals(article.getId())) {
            article.setParagraphs(Arrays.asList(new Gson().fromJson(text, String[].class)));
        } else {
            //private static final Logger logger = LogManager.getLogger(MainController.class);
            //log.error
        }
        return article;
    }

}