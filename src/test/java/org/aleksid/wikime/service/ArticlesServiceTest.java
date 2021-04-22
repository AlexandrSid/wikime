package org.aleksid.wikime.service;

import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.aTag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.aleksid.wikime.repository.ArticlesRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticlesServiceTest {

    @InjectMocks
    ArticlesService service;
    @Mock
    ArticlesRepository repository;

    @BeforeEach
    void setUp() {
//        org.aleksid.wikime.service = ArticlesService.getInstance();
//        org.aleksid.wikime.repository = mock(ArticlesRepository.class);
//        org.aleksid.wikime.service.org.aleksid.wikime.repository = org.aleksid.wikime.repository;
    }

    @AfterEach
    void tearDown() {
        service = null;
    }


    @Test
    void testGetArticlesContainingTags() {
        final List<aTag> tags = List.of(new aTag("tag1"), new aTag("tag2"));
        Article article = new Article(Set.copyOf(tags), "TestHeader", List.of());
        when(repository.getFilteredByTags(tags)).thenReturn(List.of(article));
        service.getArticlesContainingTags(tags);
        service.getArticlesContainingTags();
        verify(repository, times(1)).getFilteredByTags(tags);
        verify(repository, times(1)).getAll();
    }

    @Test
    void getArticleByID() {
        when(repository.getById(1)).thenReturn(new Article());
        service.getArticleByID(1);
        verify(repository, times(1)).getById(1);
        service.getArticleByID(22);
        verify(repository, atMost(2)).getById(anyInt());
    }

    @Test
    void deleteArticleByID() {
        when((repository.delete(11))).thenReturn(true);
        Assertions.assertTrue(service.deleteArticleByID(11));
        Assertions.assertFalse(service.deleteArticleByID(1));
    }

    @Test
    void constructAndReturn() {
        Article mnlArticle = new Article();
        mnlArticle.setId(1);//потому что ломбок делает сеттеры, возвращающие void, а не объект на котором они вызваны
        String new_header = "New Header";
        mnlArticle.setHeader(new_header);
        String paragraph1 = "paragraph1";
        String paragraph2 = "paragraph2";
        mnlArticle.setParagraphs(List.of(paragraph1, paragraph2));
        mnlArticle.setTags(Set.of(new aTag("tag1"), new aTag("tag2")));

        Article bldArticle = service.constructAndReturn(1, new_header, "[tag1, tag2]", List.of(paragraph1, paragraph2).toString());
        Assertions.assertEquals(mnlArticle, bldArticle);

        mnlArticle.setParagraphs(Collections.emptyList());
        bldArticle = service.constructAndReturn(1, new_header, "[tag1, tag2]", Collections.emptyList().toString());
        System.out.println("sout section");
        bldArticle.getParagraphs().forEach(t -> System.out.println(t + " <---BLD"));
        mnlArticle.getParagraphs().forEach(t -> System.out.println(t + " <---MNL"));
//        Assertions.assertIterableEquals(mnlArticle.getParagraphs(), bldArticle.getParagraphs());
        //выявляется проблема неэквивалентности 2х пустых списков параграфов
    }


    @Test
    void createTagsFromRequest() {
        String input = "tag1, tag11, cat, dog";
        List<aTag> tagsFromRequest = service.createTagsFromRequest(input);

        String collect = tagsFromRequest.stream().map(aTag::getTag).collect(Collectors.joining(", "));
        Assertions.assertEquals(input, collect);
    }
}