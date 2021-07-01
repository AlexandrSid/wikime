package org.aleksid.wikime.model;

import org.aleksid.wikime.repository.ArticlesRepository;

import java.sql.*;
import java.util.*;

public class Main {
    //не уверен, что данный класс лучше всего подходит для этого,
    // но чёт ничего более подходящего в проекте пока нет
    public static void initializeRepo(ArticlesRepository repository) {
        repository.add(new Article(
                new HashSet<>(Arrays.asList(
                        new aTag("tag1"),
                        new aTag("tag2"),
                        new aTag("tag4"))
                ),
                "Article 1",
                Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
        ));


        repository.add(new Article(
                new HashSet<>(Arrays.asList(
                        new aTag("tag1"),
                        new aTag("tag3"),
                        new aTag("tag4"))
                ),
                "Article 6",
                Arrays.asList("Word1", "Word2", "words 3", "Words 4")

        ));

    }


    static Article article9 = new Article(
            new HashSet<>(Arrays.asList(
                    new aTag("tag1"),
                    new aTag("tag3"),
                    new aTag("tag4"))
            ),
            "Article 9",
            Arrays.asList("Word1", "Word2", "words 3", "Words 4")
//            Collections.emptyList()
    );

    static Article article1 = new Article(
            new HashSet<>(Arrays.asList(
                    new aTag("tag1"),
                    new aTag("tag2"),
                    new aTag("tag4"))
            ),
            "Article 1",
            Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
//            Collections.emptyList()

    );

    static Article article1edited = new Article(
            new HashSet<>(Arrays.asList(
                    new aTag("tag3"),
                    new aTag("tag4"),
                    new aTag("tag6"))
            ),
            "Article 1",
            Arrays.asList("word 1", "words words", "String-3", "Strogmn_-_4")
    );
}
