package org.aleksid.wikime.restclient;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleStorageRestClientTest {

    private final static String B_URI = "http://localhost:8081/article/";
    private WebClient webClient = WebClient.create(B_URI);

    ArticleStorageRestClient client = new ArticleStorageRestClient(webClient);

    @Test
    public void getArtById() {
        ArticleRestDao artText1 = client.getArticleTextById(1l);
        assertNotNull(artText1);
        assertEquals(artText1.getText(), "text of 1");
    }

    @Test
    public void checkAllIsCorrect() {
        int count = 0;
        for (int i = 1; i <= 10; i++) {
            String number = String.valueOf(i);
            String text = client.getArticleTextById((long) i).getText();
            if (!text.endsWith(number)) break;
            count++;
        }
        assertEquals(count, 10);
    }

    @Test
    public void checkNotFound() {
        Assertions.assertThrows(
                WebClientResponseException.class,
                () -> client.getArticleTextById(798247L)
        );
    }

    @Test
    public void checkSave(){
        String text = "New super-duper text of 100!!";
        client.addNewArticle(new ArticleRestDao("100", text));
        assertEquals(client.getArticleTextById(100l).getText(), text);

    }

    @Test
    public void checkUpdate(){
        String text = "Newer super-pupper text of 100!!";
        client.updateArticle(new ArticleRestDao("100", text));
        assertEquals(client.getArticleTextById(100l).getText(), text);

    }

    @Test
    public void delete(){
        long id = 100l;
        assertNotNull(client.getArticleTextById(id));

        client.deleteTextById(id);

        Assertions.assertThrows(
                WebClientResponseException.class,
                () -> client.getArticleTextById(id)
        );
    }

}