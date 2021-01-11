package util;

import model.Article;
import model.aTag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleUtil {
    public static List<String> splitTags(String input){
        return Arrays.stream(input.split(", ")).collect(Collectors.toList());
    }

    public static List<String> splitParagraphs(String input){
        return Arrays.stream(input.split("<paragraph>")).collect(Collectors.toList());
    }

    public static String parsToStr(Article article){
        return String.join("<paragraph>", article.getParagraphs());
    }

    public static String tagsToStr(Article article){
        return article.getTags().stream().map(aTag::toString).collect(Collectors.joining(","));
    }
}