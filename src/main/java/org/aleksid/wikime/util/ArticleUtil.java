package org.aleksid.wikime.util;

import org.aleksid.wikime.model.Article;
import org.aleksid.wikime.model.Tag;

import java.util.*;
import java.util.stream.Collectors;

public class ArticleUtil {
    public static List<String> splitTags(String input) {
        return Arrays.stream(input.split(", ")).collect(Collectors.toList());
    }

    public static List<String> splitParagraphs(String input) {
        return Arrays.stream(input.split("<paragraph>")).collect(Collectors.toList());
    }

    public static String parsToStr(Article article) {
        return String.join("<paragraph>", article.getParagraphs());
    }

    public static String tagsToStr(Article article) {
        return article.getTags().stream().map(Tag::toString).collect(Collectors.joining(","));
    }

    public static Article constructAndReturn(int id, String header, String tags, String text) {
        if(tags.length()<2){tags = "  ";}
        if(text.length()<2){text = "  ";}
        String[] tagsStringsArray = tags.substring(1, tags.length() - 1).split(", ");//корявенько
        String[] paragraphs = text.substring(1, text.length() - 1).split(", ");//нужен нормальный шаблонизатор для фронта.
//        String[] tagsStringsArray = new Gson().fromJson(tags, String[].class);//не работает
//        String[] paragraphs = new Gson().fromJson(text, String[].class);//формат строк в джисоне должен быть с кавычками

//        System.out.println(new Gson().toJson(tagsStringsArray));
//        System.out.println(new Gson().toJson(paragraphs));

        Article article;
        Set<Tag> collect = Arrays.stream(tagsStringsArray)
                .map(Tag::new)
                .collect(Collectors.toSet());
        List<String> paragraphsList = Arrays.asList(paragraphs);

        if (id != 0) {
            article = new Article();
            article.setId(id);
            article.setHeader(header);
            article.setParagraphs(paragraphsList);
            article.setTags(
                    collect
            );
        } else {
            article = new Article(collect, header, paragraphsList);
            article.setId(0);
        }
        return article;
    }

    public static Article getEmptyArticle() {
        return constructAndReturn(0, "", "[]", "[]");
    }
}