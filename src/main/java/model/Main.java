package model;

import com.google.gson.Gson;
import repository.ArticlesRepository;
import repository.InMemoryArticlesRepository;

import java.sql.*;
import java.util.*;

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
            cleanDB();
//            playSQL();
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
//            String query = "select * from tags";
//            ResultSet resultSet = statement.executeQuery(query);
//            while (resultSet.next()){
//                final int anInt = resultSet.getInt("id");
//                final String string = resultSet.getString(2);
//                System.out.println(anInt + " -> " + string);
//
//            }

            Article article9 = new Article(
                    new HashSet<aTag>(Arrays.asList(
                            new aTag("tag1"),
                            new aTag("tag3"),
                            new aTag("tag4"))
                    ),
                    "Article 9",
                    Arrays.asList("Word1", "Word2", "words 3", "Words 4")
            );

            Article article1 = new Article(
                    new HashSet<aTag>(Arrays.asList(
                            new aTag("tag1"),
                            new aTag("tag2"),
                            new aTag("tag4"))
                    ),
                    "Article 1",
                    Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
            );

            addArticleToTable(statement, article1);
            addArticleToTable(statement, article9);

            Article articleFromTable = getArticleFromTable(statement, 1720838266);
            System.out.println(articleFromTable);
        }
    }

    private static void cleanDB() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from articles");
            statement.executeUpdate("delete from tags");
            statement.executeUpdate("delete from tags_connector");
        }
    }

    private static void addArticleToTable(Statement statement, Article article) throws SQLException {
        final String addArticle = "insert into articles (id, title, text) values (%d, '%s', '%s')";
        final String addTag = "insert into tags (id, tag_body) values (%d, '%s') on conflict do nothing";
        final String addRelations = "insert into tags_connector (tag_id, article_id) values (%d, %d) on conflict do nothing";

        Gson gson = new Gson();
        String articlesTableQuery = String.format(addArticle, article.getId(), article.getHeader(), gson.toJson(article.getParagraphs()));
        statement.executeUpdate(articlesTableQuery);
        Set<aTag> tags = article.getTags();
        for (aTag tag : tags) {
            statement.executeUpdate(String.format(addTag, tag.getId(), tag.getTag()));
            statement.executeUpdate(String.format(addRelations, tag.getId(), article.getId()));
        }
    }

    private static Article getArticleFromTable(Statement statement, int id) throws SQLException {
        final String getArticleById = "select * from articles where id=%d";
        final String getTagsByArticleID = "select * from tags where id in (select tag_id from tags_connector where article_id=%d)";

        try {
            ResultSet resultSet = statement.executeQuery(String.format(getArticleById, id));
            String title = null;
            String textGson = null;
            while (resultSet.next()) {//doesn't work wo .next()
                int anInt = resultSet.getInt(1);
                title = resultSet.getString("title");
                textGson = resultSet.getString("text");
            }

            resultSet = statement.executeQuery(String.format(getTagsByArticleID, id));
            Set<aTag> tags = new HashSet<>();
            while (resultSet.next()) {
                aTag tag = new aTag(resultSet.getString("tag_body"));
                tags.add(tag);
                int tagIDfromDB = resultSet.getInt("id");
                if (tagIDfromDB != tag.getId())
                    System.out.format("TagID import Error. Imported fromDB: %d Generated: %d \n", tagIDfromDB, tag.getId());
            }
            Gson gson = new Gson();
            List<String> paragraphs = Arrays.asList(gson.fromJson(textGson, String[].class));
            return new Article(tags, title, paragraphs);
        } catch (SQLException e) {
            return null;
        }
    }
}
