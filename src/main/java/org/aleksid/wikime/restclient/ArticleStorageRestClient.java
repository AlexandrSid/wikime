package org.aleksid.wikime.restclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class ArticleStorageRestClient {

    private static final Logger logger = LogManager.getLogger(ArticleStorageRestClient.class);

    private static final String BASE_URI = "http://localhost:8081/article";
    private static final String BASE_URI_ID = "http://localhost:8081/article/{id}";

    private WebClient webClient;

    public ArticleStorageRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ArticleRestDao getArticleTextById(Long id) {
        try {
            return webClient
                    .get()
                    .uri(BASE_URI_ID, id)
                    .retrieve()
                    .bodyToMono(ArticleRestDao.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Error response code is {} and the response body is {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            System.err.format("WebClient Response Exception. Code is %s and the response body is %s", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    public ArticleRestDao addNewArticle(ArticleRestDao article) {

//        System.out.println("---------------\n---->     " + article.getGSon() + "     <----\n----------------");

        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("articleId", article.getArticleId());
        map.add("text", article.getText());

        return webClient
                .post()
                .uri(BASE_URI)
                .body(BodyInserters.fromMultipartData(map))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ArticleRestDao.class)
                .block();
    }

    public ArticleRestDao updateArticle(ArticleRestDao article) {


        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("articleId", article.getArticleId());
        map.add("text", article.getText());

        return webClient
                .put()
                .uri(BASE_URI_ID, article.getArticleId())
                .body(BodyInserters.fromMultipartData(map))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ArticleRestDao.class)
                .block();
    }

    public String deleteTextById(Long id){
        return webClient
                .delete()
                .uri(BASE_URI_ID, id)
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}
