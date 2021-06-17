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
                        new Tag("tag1"),
                        new Tag("tag2"),
                        new Tag("tag4"))
                ),
                "Article 1",
                Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
        ));


        repository.add(new Article(
                new HashSet<>(Arrays.asList(
                        new Tag("tag1"),
                        new Tag("tag3"),
                        new Tag("tag4"))
                ),
                "Article 6",
                Arrays.asList("Word1", "Word2", "words 3", "Words 4")

        ));

    }

    public static void main(String[] args) {
//        InMemoryArticlesRepository org.aleksid.wikime.repository = InMemoryArticlesRepository.getInstance();
//        Main.initializeRepo(org.aleksid.wikime.repository);
//        org.aleksid.wikime.repository.getAll().stream().map(Article::toString).forEach(System.out::println);

//        String[] arrayOfNumbers = {"one", "two", "three", "four", "five"};
//        String json = new Gson().toJson(arrayOfNumbers);
//        System.out.println(json);
//
//        String tags = "[Word1,Word2,words 3,Words 4]";
//        String[] tagsStringsArray = new Gson().fromJson(json, String[].class);
//        Arrays.stream(tagsStringsArray).forEach(System.out::println);
        try {
            cleanDB();
            playSQL();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    private static final String URL = "jdbc:postgresql://localhost:5432/wikime_test";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";

    public static void playSQL() throws SQLException {
//        ResourceBundle resources = ResourceBundle.getBundle("src/main/resources/db/database.properties");
//        String url = resources.getString("database.url");
//        String username = resources.getString("database.username");
//        String password = resources.getString("database.password");

//        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
//            Statement statement = connection.createStatement();
//            statement.executeQuery("insert into tags (id, tags_gson) values (3, '[3,4,5]')");
//            statement.executeUpdate("insert into tags (tags_gson) values ('[3,4,5]')");
//            statement.executeUpdate("insert into tags (tags_gson) values ('[6,7,8]')");
//            statement.executeUpdate("update tags (tags_gson) values ('[6,7,8]') where id=7" );
//            boolean execute = statement.execute("insert into tags (tags_gson) values ('[6,7,8,13,23,44,1212]')");
//            boolean execute = statement.execute("insert into tags (tags_gson) values ('[23,44,1212]')");
//            ResultSet resultSet = statement.executeQuery("select * from tags");
//            String query = "select * from tags";
//            ResultSet resultSet = statement.executeQuery(query);
//            while (resultSet.next()){
//                final int anInt = resultSet.getInt("id");
//                final String string = resultSet.getString(2);
//                System.out.println(anInt + " -> " + string);
//            }

        //playing with DB through Designed-for-server tools.
        ArticlesRepository repository = ArticlesRepository.getRepository();
        List<Article> allEmpty = repository.getAll();
        System.out.println(allEmpty);
        System.out.println("\n---------------\n");
        repository.add(article1);
        repository.add(article9);
        System.out.println(repository.getAll());
        System.out.println("\n---------------\n");
        List<Tag> tag3 = Collections.singletonList(new Tag("tag3"));
        System.out.println(repository.getFilteredByTags(tag3));
        System.out.println("\n---------------\n");
        System.out.println(repository.getById(article1.getId()));
//        org.aleksid.wikime.repository.delete(-88853146);
//        DBArticleRepository dbArticleRepository = (DBArticleRepository) org.aleksid.wikime.repository;
//        dbArticleRepository.removeOutOfUseTags();


    }

    private static void cleanDB() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from articles");
            statement.executeUpdate("delete from tags");
            statement.executeUpdate("delete from tags_connector");
        }
    }


    static Article article9 = new Article(
            new HashSet<>(Arrays.asList(
                    new Tag("tag1"),
                    new Tag("tag3"),
                    new Tag("tag4"))
            ),
            "Article 9",
            Arrays.asList("Word1", "Word2", "words 3", "Words 4")
//            Collections.emptyList()
    );

    static Article article1 = new Article(
            new HashSet<>(Arrays.asList(
                    new Tag("tag1"),
                    new Tag("tag2"),
                    new Tag("tag4"))
            ),
            "Article 1",
            Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
//            Collections.emptyList()

    );

    static Article article1edited = new Article(
            new HashSet<>(Arrays.asList(
                    new Tag("tag3"),
                    new Tag("tag4"),
                    new Tag("tag6"))
            ),
            "Article 1",
            Arrays.asList("word 1", "words words", "String-3", "Strogmn_-_4")
    );
}
