package model;

import com.google.gson.Gson;
import repository.ArticlesRepository;
import repository.DBArticleRepository;

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

        DBArticleRepository repository = (DBArticleRepository) ArticlesRepository.getRepository();
        repository.add(article1);
        repository.add(article9);

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


            addArticleToTable(statement, article1);
            addArticleToTable(statement, article9);

            Article articleFromTable = getArticleFromTable(statement, 1720838266);
            System.out.println(articleFromTable);
            List<Article> articles = getArticles(statement);
            articles.forEach(System.out::println);
            System.out.println(removeArticleByID(statement, 1720838266));

            Article article1edited = new Article(
                    new HashSet<>(Arrays.asList(
                            new aTag("tag3"),
                            new aTag("tag4"),
                            new aTag("tag6"))
                    ),
                    "Article 1",
                    Arrays.asList("word 1", "words words", "String-3", "Strogmn_-_4")
            );
//            int editedId = article1.getId();
            int editedId = 42;
            article1edited.setId(editedId);
            updateArticle(statement, article1edited);
            System.out.println(getArticleFromTable(statement, article1.getId()));
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

    private static boolean addArticleToTable(Statement statement, Article article){
        final String addArticle = "insert into articles (id, title, text) values (%d, '%s', '%s')";
        final String addTag = "insert into tags (id, tag_body) values (%d, '%s') on conflict do nothing";
        final String addRelations = "insert into tags_connector (tag_id, article_id) values (%d, %d) on conflict do nothing";

        try {
            Gson gson = new Gson();
            String articlesTableQuery = String.format(addArticle, article.getId(), article.getHeader(), gson.toJson(article.getParagraphs()));
            statement.executeUpdate(articlesTableQuery);
            Set<aTag> tags = article.getTags();
            for (aTag tag : tags) {
                statement.executeUpdate(String.format(addTag, tag.getId(), tag.getTag()));
                statement.executeUpdate(String.format(addRelations, tag.getId(), article.getId()));
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static Article getArticleFromTable(Statement statement, int id) {
        final String getArticleById = "select * from articles where id=%d";
        final String getTagsByArticleID = "select * from tags where id in (select tag_id from tags_connector where article_id=%d)";

        try {
            ResultSet resultSet = statement.executeQuery(String.format(getArticleById, id));
            String title = null;
            String textGson = null;
            while (resultSet.next()) {//doesn't work wo .next()
//                int anInt = resultSet.getInt(1);
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
            Article article = new Article(tags, title, paragraphs);
            article.setId(id);//вообще не нравится способ генерации ИДшника...
            return article;
        } catch (SQLException e) {
            return null;
        }
    }

    private static List<Article> getArticles(Statement statement) throws SQLException {//returns articles with no text for showing in main menu and tag searching
        Map<Integer, Article> conveyor = new HashMap<>();

        ResultSet rs = statement.executeQuery("select id, title from articles");

        int id;
        String title;
        //collect Articles wo tags and text
        while (rs.next()) {
            id = rs.getInt("id");
            title = rs.getString("title");
            Article article = new Article(new HashSet<>(), title, Collections.EMPTY_LIST);
            article.setId(id);
            conveyor.put(article.getId(), article);
        }
        //extracting tags and join them to articles in result
        final String bothIDandTagBodyQuery = "select article_id, tag_id, tags.tag_body from tags_connector left join tags on tags_connector.tag_id = tags.id order by article_id";
        rs = statement.executeQuery(bothIDandTagBodyQuery);
        while (rs.next()) {
            int article_id = rs.getInt("article_id");
            int tag_id = rs.getInt(2);
            String tag_body = rs.getString("tag_body");
            aTag tag = new aTag(tag_body);
            if (tag.getId() != tag_id)
                System.out.format("TagID import Error. Imported fromDB: %d Generated: %d \n", tag_id, tag.getId());
            conveyor.get(article_id).getTags().add(tag);
        }
        return new ArrayList<>(conveyor.values());
    }

    private static boolean removeArticleByID(Statement statement, int id) {
        final String deleteQuery = "delete from articles where id=%d";
        //tags connector should delete automaticaly.
        try {
            statement.executeUpdate(String.format(deleteQuery, id));
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    private static boolean updateArticle(Statement statement, Article article) throws SQLException {
        //не оптимально, но просто для понимания.
        int id = article.getId();
        final boolean removed = removeArticleByID(statement, id);
        final boolean added = addArticleToTable(statement, article);
        return added & removed;

    }

    static Article article9 = new Article(
            new HashSet<>(Arrays.asList(
                    new aTag("tag1"),
                    new aTag("tag3"),
                    new aTag("tag4"))
            ),
            "Article 9",
            Arrays.asList("Word1", "Word2", "words 3", "Words 4")
    );

    static Article article1 = new Article(
            new HashSet<>(Arrays.asList(
                    new aTag("tag1"),
                    new aTag("tag2"),
                    new aTag("tag4"))
            ),
            "Article 1",
            Arrays.asList("String 1", "Strogmn2", "String 3", "Strogmn4")
    );
}
