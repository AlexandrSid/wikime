package model;

import com.google.gson.Gson;
import repository.ArticlesRepository;
import repository.InMemoryArticlesRepository;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Main {
    //не уверен, что данный класс лучше всего подходит для этого,
    // но чёт ничего более подходящего в проекте пока нет
    public static final void initializeRepo(ArticlesRepository repository) {
        repository.add(new Article(
                new HashSet<aTag>(Arrays.asList(
                        new aTag("tag1"),
                        new aTag("tag2"),
                        new aTag("tag4"))
                ),
                "Article 1",
                Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
        ));


        repository.add(new Article(
                new HashSet<aTag>(Arrays.asList(
                        new aTag("tag1"),
                        new aTag("tag3"),
                        new aTag("tag4"))
                ),
                "Article 6",
                Arrays.asList("Word1", "Word2", "words 3", "Words 4")
        ));

    }

    public static void main(String[] args) {
//        InMemoryArticlesRepository repository = InMemoryArticlesRepository.getInstance();
//        Main.initializeRepo(repository);
//        repository.getAll().stream().map(Article::toString).forEach(System.out::println);

//        String[] arrayOfNumbers = {"one", "two", "three", "four", "five"};
//        String json = new Gson().toJson(arrayOfNumbers);
//        System.out.println(json);
//
//        String tags = "[Word1,Word2,words 3,Words 4]";
//        String[] tagsStringsArray = new Gson().fromJson(json, String[].class);
//        Arrays.stream(tagsStringsArray).forEach(System.out::println);

        try {
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

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
//            statement.executeQuery("insert into tags (id, tags_gson) values (3, '[3,4,5]')");
//            statement.executeUpdate("insert into tags (tags_gson) values ('[3,4,5]')");
//            statement.executeUpdate("insert into tags (tags_gson) values ('[6,7,8]')");
//            statement.executeUpdate("update tags (tags_gson) values ('[6,7,8]') where id=7" );
//            boolean execute = statement.execute("insert into tags (tags_gson) values ('[6,7,8,13,23,44,1212]')");
//            boolean execute = statement.execute("insert into tags (tags_gson) values ('[23,44,1212]')");
//            ResultSet resultSet = statement.executeQuery("select * from tags");
            String query = "select * from tags";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                final int anInt = resultSet.getInt("id");
                final String string = resultSet.getString(2);
                System.out.println(anInt + " -> " + string);

            }
        }
    }


}
