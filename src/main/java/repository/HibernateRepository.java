package repository;

import dto.DBArticle;
import dto.DBTag;
import model.Article;
import model.aTag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HibernateRepository implements ArticlesRepository{

    public static ArticlesRepository getInstance() {
        if (instance == null) {
            instance = new HibernateRepository();
        }
        return instance;
    }

    private HibernateRepository() {}

    private static ArticlesRepository instance;

    private SessionFactory sessionFactory = new Configuration().configure("db/hibernate.cfg.xml").buildSessionFactory();
    private Session session = sessionFactory.openSession();

    public void teardown(){//не знаю как это вызывть. вэб-контейнер по идее должен
        session.close();
        sessionFactory.close();
    }

    @Override
    public boolean add(Article article) {
        session.beginTransaction();
        DBArticle dbArticle = new DBArticle(article);
        dbArticle.setTags(tagsAddIfExistReturn(dbArticle.getTags()));
        session.save(dbArticle);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public List<Article> getAll() {
        session.beginTransaction();
        Criteria criteria = session.createCriteria(DBArticle.class);
//        criteria.setMaxResults(100);
        List<DBArticle> articles = criteria.list();
        session.getTransaction().commit();
        return articles.stream().map(Article::new).collect(Collectors.toList());
    }

    @Override
    public List<Article> getFilteredByTags(List<aTag> tags) {
        Set<DBTag> nonManagedTags = tags.stream().map(DBTag::new).collect(Collectors.toSet());
        Set<DBTag> manageddbTags = tagsAddIfExistReturn(nonManagedTags);
        List<Article> result = manageddbTags.stream().map(DBTag::getId).flatMap(this::getDBArticlesStreamByTag).distinct().map(Article::new).collect(Collectors.toList());
        return result;
    }

    private Stream<DBArticle> getDBArticlesStreamByTag(Integer tagId) {
        session.beginTransaction();
        DBTag tag = session.get(DBTag.class, tagId);
        session.getTransaction().commit();
        return tag.getArticles().stream();
    }

    @Override
    public Article getById(int id) {
        session.beginTransaction();
        DBArticle article = session.get(DBArticle.class, id);
        session.getTransaction().commit();
        return new Article(article);
    }

    @Override
    public boolean delete(int id) {
        session.beginTransaction();
        DBArticle article = session.get(DBArticle.class, id);
        article.getTags().forEach(t -> t.getArticles().remove(article));
        article.setTags(Collections.emptySet());
        session.delete(article);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Article article) {
        int id = article.getId();
        session.beginTransaction();
        DBArticle article2Update = session.get(DBArticle.class, id);
        DBArticle articleFromFront = new DBArticle(article);
        article2Update.setHeader(articleFromFront.getHeader());
        article2Update.setText(articleFromFront.getText());
        article2Update.setTags(tagsAddIfExistReturn(articleFromFront.getTags()));
        session.update(article2Update);
        session.getTransaction().commit();
        return true;
    }

    //подробнее метод представлен в тестовом классе
    public Set<DBTag> tagsAddIfExistReturn(Set<DBTag> tagsToAdd) {
        Set<DBTag> result = new HashSet<>();

        Criteria criteria = session.createCriteria(DBTag.class);
        List<DBTag> existing = criteria.list();

        if (existing.isEmpty()) {
            tagsToAdd.forEach(session::persist);
            result.addAll(tagsToAdd);
        } else {
            List<String> toAddBodies = tagsToAdd.stream().map(DBTag::getTag).collect(Collectors.toList());
            List<String> existingBodies = existing.stream().map(DBTag::getTag).collect(Collectors.toList());

            Map<Boolean, List<String>> newAndExisting = toAddBodies.stream().collect(Collectors.partitioningBy(existingBodies::contains));
            final List<DBTag> newTagsToAdd = newAndExisting.get(false).stream().map(DBTag::new).collect(Collectors.toList());
            newTagsToAdd.forEach(session::persist);
            List<DBTag> existingPartToAdd = existing.stream().filter(dbt -> newAndExisting.get(true).contains(dbt.getTag())).collect(Collectors.toList());

            result.addAll(existingPartToAdd);
            result.addAll(newTagsToAdd);
        }
        return result;
    }
}
