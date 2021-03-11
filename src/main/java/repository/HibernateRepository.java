package repository;

import dto.DBArticle;
import dto.DBTag;
import model.Article;
import model.aTag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
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
        dbArticle.getTags().forEach(session::saveOrUpdate);
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
        return tags.stream().map(aTag::getId).flatMap(this::getDBArticlesStreamByTag).distinct().map(Article::new).collect(Collectors.toList());
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
        session.delete(session.get(DBArticle.class, id));
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Article article) {
        int id = article.getId();
        session.beginTransaction();
        DBArticle dbArticle = new DBArticle(article);
        dbArticle.setId(id);
        dbArticle.getTags().forEach(session::merge);
        session.merge(dbArticle);
        session.getTransaction().commit();
        return true;
    }
}
