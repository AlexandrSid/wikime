import org.aleksid.wikime.dto.DBArticle;
import org.aleksid.wikime.dto.DBTag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Ignore
public class HibernateTest2dir {

    private static SessionFactory sessionFactory = null;
    private Session session = null;

    @BeforeClass
    public static void setup() throws Exception {
        sessionFactory = new Configuration().configure("db/hibernate_test.cfg.xml").buildSessionFactory();
        System.out.println("Session Factory SETUP");
    }

    @AfterClass
    public static void teardown() throws Exception {
        sessionFactory.close();
        System.out.println("Session Factory TEARDOWN");
    }

    @Before
    public void beginTransactionPrepareDB() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        prepareDB(session);
    }

    @After
    public void closeTransactionAndSession() {
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testSave() {
        DBTag tag0 = new DBTag();
        tag0.setTag("Tag00");
        DBTag tag1 = new DBTag();
        tag1.setTag("Tag1");
        DBTag tag2 = new DBTag();
        tag2.setTag("Tag2");

        DBArticle article1 = new DBArticle();
        article1.setHeader("Article01");
        article1.setText("Article 01 text is here");
        article1.setTags(tagsAddIfExistReturn(tag0, tag1));
        article1.getTags().forEach(t -> t.getArticles().add(article1));

        DBArticle artilce2 = new DBArticle();
        artilce2.setHeader("Article02");
        artilce2.setText("Article 02 text is here");
        artilce2.setTags(tagsAddIfExistReturn(tag0, tag2));
        article1.getTags().forEach(t -> t.getArticles().add(artilce2));

        session.save(article1);
        session.save(artilce2);
    }

    @Test
    public void testGetArticleByID() {
        DBArticle dbArticle = session.get(DBArticle.class, 1);
        System.out.println(dbArticle);
    }

    @Test
    public void testGetTagByID() {
        DBTag tag = session.get(DBTag.class, 3);
        System.out.println(tag);
        System.out.println(showArticlesWORecursion(tag.getArticles()));
    }

    @Test
    public void testGetAllArticles(){
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<DBArticle> criteriaQuery = criteriaBuilder.createQuery(DBArticle.class);
        Root<DBArticle> rootEntry = criteriaQuery.from(DBArticle.class);
        CriteriaQuery<DBArticle> all = criteriaQuery.select(rootEntry);
        TypedQuery<DBArticle> allQuery = session.createQuery(all);
        List<DBArticle> articles =allQuery.getResultList();

        articles.forEach(System.out::println);
    }

    @Test
    public void testGetAllTags(){
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<DBTag> criteriaQuery = criteriaBuilder.createQuery(DBTag.class);
        Root<DBTag> rootEntry = criteriaQuery.from(DBTag.class);
        CriteriaQuery<DBTag> all = criteriaQuery.select(rootEntry);
        TypedQuery<DBTag> allQuery = session.createQuery(all);
        List<DBTag> tags =allQuery.getResultList();

        tags.forEach(System.out::println);
    }

    @Test
    public void testArticleUpdate(){
        DBArticle article = session.get(DBArticle.class, 3);
        article.setText("There is new text of article " + article.getHeader());
        article.setHeader("There is new Header of article");
        article.setTags(Set.copyOf(tagsAddIfExistReturn(new DBTag("completely_new_tag"), new DBTag("Tag2"), new DBTag("Tag1"))));
        article.getTags().forEach(t->t.getArticles().add(article));
        session.update(article);

        testGetAllTags();
    }

    @Test
    public void testDeleteArticle(){
        final DBArticle article = session.get(DBArticle.class, 4);
        article.getTags().forEach(t->t.getArticles().remove(article));
        article.getTags().forEach(session::update);
        session.delete(article);

        removeUnusedTags(session);
    }



    @Test
    public void testGetArticleByTags(){
        List <String> tags = List.of("Tag1");

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<DBTag> criteriaQuery = criteriaBuilder.createQuery(DBTag.class);
        Root<DBTag> rootEntry = criteriaQuery.from(DBTag.class);
        CriteriaQuery<DBTag> all = criteriaQuery.select(rootEntry);
        TypedQuery<DBTag> allQuery = session.createQuery(all);
        List<DBTag> list =allQuery.getResultList();

//        list.stream().map(t->t.getArticles() + " " + t.getTag()).forEach(System.out::println);
//        list.forEach(System.out::println);

        final List<DBArticle> collect = list.stream().distinct()
                .filter(t -> tags.contains(t.getTag()))
                .flatMap(dbTag -> dbTag.getArticles().stream())
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void getArticlesWithJPQL(){
        String queryString = "select distinct a from ARTICLES a join a.tags tag where tag.id in :tags";
        Set<DBTag> tagsForSearch = tagsAddIfExistReturn(new DBTag("Tag4"), new DBTag("Tag2"));
        List<Integer> tagsIDs = tagsForSearch.stream().map(t -> t.getId().intValue()).collect(Collectors.toList());
        Query query = session.createQuery(queryString);
        query.setParameter("tags", tagsIDs);
        List<DBArticle> articles = query.getResultList();
        articles.forEach(System.out::println);
    }


    @Test
    public void removeUnusedTags(){
        String usedTags = "select distinct a.tags from ARTICLES a";
        List<DBTag> resultList = session.createQuery(usedTags).getResultList();

        List<DBTag> allTags = session.createCriteria(DBTag.class).list();

        resultList.forEach(System.out::println);
        System.out.println("-------------");
        allTags.stream().distinct().forEach(System.out::println);
        System.out.println("-------------");

        List<DBTag> notUsed = allTags.stream().distinct().filter(Predicate.not((resultList::contains))).collect(Collectors.toList());
        notUsed.forEach(System.out::println);

        notUsed.forEach(session::delete);

        allTags = session.createCriteria(DBTag.class).list();
        allTags.stream().distinct().forEach(System.out::println);

    }



    private void prepareDB(Session session) {
        DBTag tag1 = new DBTag();
        tag1.setTag("Tag1");
        DBTag tag2 = new DBTag();
        tag2.setTag("Tag2");
        DBTag tag3 = new DBTag();
        tag3.setTag("Tag3");
        DBTag tag4 = new DBTag();
        tag4.setTag("Tag4");

        DBTag tag42 = new DBTag();
        tag42.setTag("Tag42");
        session.save(tag42);

        DBTag tag43 = new DBTag();
        tag43.setTag("Tag43");
        session.save(tag43);

        DBArticle article1 = new DBArticle();
        article1.setHeader("Article1");
        article1.setText("Article 1 text is here");
        article1.setTags(tagsAddIfExistReturn(tag1, tag2));
        article1.getTags().forEach(t -> t.getArticles().add(article1));

        DBArticle article2 = new DBArticle();
        article2.setHeader("Article2");
        article2.setText("Article 2 text is here");
        article2.setTags(tagsAddIfExistReturn(tag1, tag3));
        article2.getTags().forEach(t -> t.getArticles().add(article2));

        DBArticle article3 = new DBArticle();
        article3.setHeader("Article3");
        article3.setText("Article 3 text is here");
        article3.setTags(tagsAddIfExistReturn(tag1, tag2, tag3));
        article3.getTags().forEach(t -> t.getArticles().add(article3));

        DBArticle article4 = new DBArticle();
        article4.setHeader("Article4");
        article4.setText("Article 4 text is here");
        article4.setTags(tagsAddIfExistReturn(tag2, tag4));
        article4.getTags().forEach(t -> t.getArticles().add(article4));

        session.persist(article1);
        session.persist(article2);
        session.persist(article3);
        session.persist(article4);
        //ToDo N+1 проблема во всей красе. Как впихнуть Join Fetch?

        session.getTransaction().commit();
        session.beginTransaction();
    }

    private Set<DBTag> tagsAddIfExistReturn(DBTag... args) {
        Set<DBTag> result = new HashSet<>();// это засунуть в новую или редактируемую статью при добавлении/обновлении

        List<DBTag> tagsToAdd = List.of(args);
        Criteria criteria = session.createCriteria(DBTag.class);
        List<DBTag> existing = criteria.list();

        if (existing.isEmpty()) {
            tagsToAdd.forEach(session::persist);
            result.addAll(tagsToAdd);
        } else {
            List<String> toAddBodies = tagsToAdd.stream().map(DBTag::getTag).collect(Collectors.toList());
            List<String> existingBodies = existing.stream().map(DBTag::getTag).collect(Collectors.toList());

            //теперь надо разделить toAddBodies на новые и имеющиеся.
            Map<Boolean, List<String>> newAndExisting = toAddBodies.stream().collect(Collectors.partitioningBy(existingBodies::contains));
//            newAndExisting.get(true);//existing
//            newAndExisting.get(false);//new
            final List<DBTag> newTagsToAdd = newAndExisting.get(false).stream().map(DBTag::new).collect(Collectors.toList());
            newTagsToAdd.forEach(session::persist);
            List<DBTag> existingPartToAdd = existing.stream().filter(dbt -> newAndExisting.get(true).contains(dbt.getTag())).collect(Collectors.toList());

            result.addAll(existingPartToAdd);
            result.addAll(newTagsToAdd);
        }
//        result.forEach(bdT -> System.out.format("    ----    Persisted tag with DB_ID %d and Body of %s \n", bdT.getId(), bdT.getTag()));
        return result;
    }

    private String showArticlesWORecursion(Set<DBArticle> articles) {
        return articles.stream().map(a -> String.format("   Article ID = %d and Header = %s;", a.getId(), a.getHeader())).collect(Collectors.joining("\n"));
    }

    private void removeUnusedTags(Session session) {
    }




}
