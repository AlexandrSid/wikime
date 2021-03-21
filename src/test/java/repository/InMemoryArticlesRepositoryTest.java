package repository;

import model.Article;
import model.aTag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InMemoryArticlesRepositoryTest {
    @InjectMocks
    InMemoryArticlesRepository repository;
    @Spy
    ArrayList<Article> articleList;

    @Test
    void add() {

        Article a1 = new Article();
        a1.setId(1);
        a1.setHeader("Header 1");
        a1.setParagraphs(List.of("paragraph1", "paragraph2"));
        a1.setTags(Set.of(new aTag("tag1"), new aTag("tag2")));

        Article a2 = new Article();
        a2.setId(2);
        a2.setHeader("Header 2");
        a2.setParagraphs(List.of("paragraph1", "paragraph2"));
        a1.setTags(Set.of(new aTag("tag1"), new aTag("tag3")));

        Article a3 = new Article();
        a3.setId(3);
        a3.setHeader("Header 3");
        a3.setParagraphs(List.of("paragraph1", "paragraph2"));
        a3.setTags(Set.of(new aTag("tag3"), new aTag("tag2")));

        repository.add(a1);
        repository.add(a2);
        repository.add(a3);

        Mockito.verify(articleList).add(a3);
        Mockito.verify(articleList).add(a2);
        Mockito.verify(articleList).add(a1);
        assertEquals(3, articleList.size());

    }

}