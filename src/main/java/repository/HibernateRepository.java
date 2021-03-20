package repository;

import dto.DBArticle;
import dto.DBTag;
import model.Article;
import model.aTag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HibernateRepository implements ArticlesRepository {

    public static ArticlesRepository getInstance() {
        if (instance == null) {
            instance = new HibernateRepository();
        }
        return instance;
    }

    private HibernateRepository() {
    }

    private static ArticlesRepository instance;

    private SessionFactory sessionFactory = new Configuration().configure("db/hibernate.cfg.xml").buildSessionFactory();
    private Session session = sessionFactory.openSession();

    public void teardown() {//не знаю как это вызывть. вэб-контейнер по идее должен
        session.close();
        sessionFactory.close();
    }

    @Override
    public Article add(Article article) {
        session.beginTransaction();
        DBArticle dbArticle = new DBArticle(article);
        dbArticle.setTags(tagsAddIfExistReturn(dbArticle.getTags()));
        dbArticle.getTags().forEach(t -> t.getArticles().add(dbArticle));

        session.save(dbArticle);
        session.getTransaction().commit();
        article.setId(dbArticle.getId());
        return article;
    }

    @Override
    public List<Article> getAll() {
        session.beginTransaction();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<DBArticle> criteriaQuery = criteriaBuilder.createQuery(DBArticle.class);
        Root<DBArticle> rootEntry = criteriaQuery.from(DBArticle.class);
        CriteriaQuery<DBArticle> all = criteriaQuery.select(rootEntry);
        TypedQuery<DBArticle> allQuery = session.createQuery(all);
        List<DBArticle> articles = allQuery.getResultList();

        session.getTransaction().commit();
        return articles.stream().map(Article::new).collect(Collectors.toList());
    }

    @Override
    public List<Article> getFilteredByTags(List<aTag> tags) {
        Set<DBTag> tagsFromFrontNoIDs = tags.stream().distinct().map(DBTag::new).collect(Collectors.toSet());
        Set<DBTag> tagsForSearch = tagsAddIfExistReturn(tagsFromFrontNoIDs);

        String queryString = "select distinct a from ARTICLES a join a.tags tag where tag.id in :tags";
        List<Integer> tagsIDs = tagsForSearch.stream().map(DBTag::getId).collect(Collectors.toList());
        Query query = session.createQuery(queryString);
        query.setParameter("tags", tagsIDs);
        List<DBArticle> articles = sortByTagsOrder(tags, query.getResultList());

        return articles.stream().map(Article::new).collect(Collectors.toList());
    }

    private List<DBArticle> sortByTagsOrder(List<aTag> tags, Collection<DBArticle> articles) {
        //внутренний класс для сортировки
        class ArticleContainer {
            DBArticle article;
            int rating;

            public ArticleContainer(DBArticle article) {
                this.article = article;
                this.rating = 0;
            }

            public DBArticle getArticle() {
                return article;
            }

            public void setRating(int rating) {
                this.rating = rating;
            }
        }
        //каждому тегу в запрашиваемой последовательности назначается рейтинг, пришёл первым - получил меньше(1,2,4,8...)
        Map<String, Integer> tagRating = new HashMap<>();
        int r = 128;//первые 7 тегов, остальные не будут влиять на порядок
        for (aTag tag : tags) {
            tagRating.put(tag.getTag(), r /= 2);
        }

        //заполняется лист для сортировки...
        List<ArticleContainer> sortable = articles.stream().map(ArticleContainer::new).collect(Collectors.toList());
        //...рассчитывается рейтинг каждой подходящей статьи
        sortable.forEach(container -> container.setRating(container
                .getArticle()
                .getTags()
                .stream()
                .map(tag -> tagRating.getOrDefault(tag.getTag(), 0))
                .reduce(0, Integer::sum))
        );

        //список сортируется
        Collections.sort(sortable, new Comparator<ArticleContainer>() {
            @Override
            public int compare(ArticleContainer o1, ArticleContainer o2) {
                return o2.rating - o1.rating;
            }
        });

        return sortable.stream().map(ArticleContainer::getArticle).collect(Collectors.toList());
    }


    @Override
    public Article getById(int id) {
        session.beginTransaction();
        DBArticle article = session.get(DBArticle.class, id);
        session.getTransaction().commit();
        try {
            Article result = new Article(article);
            return result;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        session.beginTransaction();
        DBArticle article = session.get(DBArticle.class, id);
        article.getTags().forEach(t -> t.getArticles().remove(article));
        article.getTags().forEach(session::update);
        session.delete(article);

        removeUnusedTags(session);

        session.getTransaction().commit();
        return true;
    }

    @Override
    public Article update(Article article) {
        int id = article.getId();
        session.beginTransaction();

        DBArticle article2Update = session.get(DBArticle.class, id);
        DBArticle articleFromFront = new DBArticle(article);
        article2Update.setHeader(articleFromFront.getHeader());
        article2Update.setText(articleFromFront.getText());
        article2Update.setTags(tagsAddIfExistReturn(articleFromFront.getTags()));
        article2Update.getTags().forEach(t -> t.getArticles().add(article2Update));
        session.update(article2Update);

        removeUnusedTags(session);

        session.getTransaction().commit();
        return new Article(article2Update);
    }

    //подробнее метод представлен в тестовом классе
    public Set<DBTag> tagsAddIfExistReturn(Set<DBTag> tagsToAdd) {
        Set<DBTag> result = new HashSet<>();

        String existingTagsQuery = "select t from TAGS t";
        List<DBTag> existing = session.createQuery(existingTagsQuery).getResultList();

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

    private void removeUnusedTags(Session session) {
        String usingTagsQuery = "select distinct a.tags from ARTICLES a";
        List<DBTag> usingTags = session.createQuery(usingTagsQuery).getResultList();
        List<DBTag> allTags = session.createCriteria(DBTag.class).list();
        allTags.stream().distinct().filter(Predicate.not((usingTags::contains))).forEach(session::delete);//ну такое...
    }

}
