package org.aleksid.wikime.model;

import org.aleksid.wikime.dto.DBArticle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleTest {

    Article a1;
    Article a2;
    Article a3;

    @BeforeEach
    void setup(){
        String new_header = new String("New Header");
        String new_header2 = new String("New Header");
        String paragraph1 = new String("paragraph1");
        String paragraph11 = new String("paragraph1");
        String paragraph2 = new String("paragraph2");
        String paragraph22 = new String("paragraph2");

        a1 = new Article();
        a1.setId(1);
        a1.setHeader(new_header);
        a1.setParagraphs(List.of(paragraph1, paragraph2));
        a1.setTags(Collections.emptySet());

        a2 = new Article();
        a2.setId(1);
        a2.setHeader(new_header2);
        a2.setParagraphs(List.of(paragraph11, paragraph22));
        a2.setTags(Collections.emptySet());

        DBArticle dbA = mock(DBArticle.class);
        when(dbA.getHeader()).thenReturn(new_header);
        when(dbA.getTags()).thenReturn(Collections.emptySet());
        when(dbA.getText()).thenReturn(List.of(paragraph1, paragraph22).toString());
        a3 = new Article(dbA);
    }

    @Test
    void testEquals() {
        Assertions.assertEquals(a1, a2);
        Assertions.assertEquals(a1, a3);
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(a1, a2);
        Assertions.assertEquals(a1, a3);
    }
}