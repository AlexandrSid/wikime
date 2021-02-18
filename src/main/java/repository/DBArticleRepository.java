package repository;

import com.google.gson.Gson;
import model.Article;
import model.aTag;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DBArticleRepository implements ArticlesRepository {

    private final ConnectionHolder connectionHolder = ConnectionHolder.getInstance();

    public static ArticlesRepository getInstance() {
        if (instance == null) {
            instance = new DBArticleRepository();
        }
        return instance;
    }

    private static ArticlesRepository instance;

    private DBArticleRepository() {
    }

    @Override
    public boolean add(Article article) {
        Statement statement = connectionHolder.getStatement();
        final String addArticle = "insert into articles (id, title, text) values (%d, '%s', '%s')";
        final String addTag = "insert into tags (id, tag_body) values (%d, '%s') on conflict do nothing";
        final String addRelations = "insert into tags_connector (tag_id, article_id) values (%d, %d) on conflict do nothing";

        try {
            Gson gson = new Gson();
            String articlesTableQuery = String.format(addArticle, article.getId(), article.getHeader(), gson.toJson(article.getParagraphs()));
            statement.executeUpdate(articlesTableQuery);
            Set<aTag> tags = article.getTags();
            for (aTag tag : tags) {//возможно единственное место в существующей логике, где был бы уместнее PreparedStatement
                statement.executeUpdate(String.format(addTag, tag.getId(), tag.getTag()));
                statement.executeUpdate(String.format(addRelations, tag.getId(), article.getId()));
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Article> getAll() {
        try {//returns articles with no text for showing in main menu and tag searching
            Statement statement = connectionHolder.getStatement();
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
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Article> getFilteredByTags(List<aTag> tags) {
        return getAll().stream()
                .filter(article -> article.getTags().containsAll(tags))
                .collect(Collectors.toList());
    }

    @Override
    public Article getById(int id) {
        Statement statement = connectionHolder.getStatement();
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

    @Override
    public boolean delete(int id) {
        Statement statement = connectionHolder.getStatement();
        final String deleteQuery = "delete from articles where id=%d";
        //tags connector should delete automaticaly.
        try {
            statement.executeUpdate(String.format(deleteQuery, id));
            removeOutOfUseTags();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean update(Article article) {
        //не оптимально, но просто для понимания.
        Statement statement = connectionHolder.getStatement();
        int id = article.getId();
        final boolean removed = delete(id);
        final boolean added = add(article);
        return added & removed;

    }

    public void removeOutOfUseTags(){//starts every time when article deleted or updated
        Statement statement = connectionHolder.getStatement();
        final String query = "delete from tags where id in (select id from tags left join tags_connector on tags_connector.tag_id = tags.id where tags_connector.article_id is null)";
        try {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
