package util;

import com.google.gson.Gson;
import model.Article;
import model.aTag;

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
        return article.getTags().stream().map(aTag::toString).collect(Collectors.joining(","));
    }

    public static Article constructAndReturn(int id, String header, String tags, String text) {
//        System.out.println(tags);
//        System.out.println(text);

        String[] tagsStringsArray = tags.substring(1, tags.length() - 1).split(",");//корявенько
        String[] paragraphs = tags.substring(1, tags.length() - 1).split(",");
//        String[] tagsStringsArray = new Gson().fromJson(tags, String[].class);//не работает
//        String[] paragraphs = new Gson().fromJson(text, String[].class);//формат строк в джисоне должен быть с кавычками

        System.out.println(new Gson().toJson(tagsStringsArray));
        System.out.println(new Gson().toJson(paragraphs));

        Article article = new Article();
        article.setId(id);
        article.setHeader(header);
        article.setParagraphs(Arrays.asList(paragraphs));
        article.setTags(
                Arrays.stream(tagsStringsArray)
                        .map(aTag::new)
                        .collect(Collectors.toSet())
        );
        return article;
    }
}