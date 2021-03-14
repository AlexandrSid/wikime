import dto.DBArticle;
import dto.DBTag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

public class HibernateTest {

    private static SessionFactory sessionFactory = null;
    private Session session = null;

    @BeforeClass
    public static void setup() throws Exception {
        sessionFactory = new Configuration().configure("db/hibernate.cfg.xml").buildSessionFactory();
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
        tag1.setTag("Tag01");
        DBTag tag2 = new DBTag();
        tag2.setTag("Tag02");

        DBArticle artilce1 = new DBArticle();
        artilce1.setHeader("Article01");
        artilce1.setText("Article 01 text is here");
        artilce1.setTags(Set.of(tag0, tag1));

        DBArticle artilce2 = new DBArticle();
        artilce2.setHeader("Article02");
        artilce2.setText("Article 02 text is here");
        artilce2.setTags(Set.of(tag0, tag2));

        session.save(tag0);
        session.save(tag1);
        session.save(tag2);
        session.save(artilce1);
        session.save(artilce2);
    }

    @Test
    public void testGet() {
        DBArticle article = session.get(DBArticle.class, 4);
        System.out.println("|||||||------->>>>>>> " + article);

        DBTag tag = session.get(DBTag.class, 2);
        tag.getArticles().forEach(System.out::println);
    }

    @Test
    public void testGetAllViaJPQL() {
        List<DBArticle> articles = session.createQuery("select a from ARTICLES a", DBArticle.class).getResultList();
        articles.forEach(System.out::println);
    }

    @Test
    public void testGetAllViaCriteria() {
//        //жонглирование какое-то
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        CriteriaQuery<DBArticle> criteriaQuery = criteriaBuilder.createQuery(DBArticle.class);
//        Root<DBArticle> rootEntry = criteriaQuery.from(DBArticle.class);
//        CriteriaQuery<DBArticle> all = criteriaQuery.select(rootEntry);
//        TypedQuery<DBArticle> allQuery = session.createQuery(all);
//        List<DBArticle> articles =allQuery.getResultList();

        //а этот способ deprecated
        //upd прочитал почему https://ru.stackoverflow.com/questions/965018/hibernate-%D1%80%D0%B0%D0%B7%D0%BD%D0%B8%D1%86%D0%B0-%D0%BC%D0%B5%D0%B6%D0%B4%D1%83-session-%D0%B8-entitymanager
        Criteria criteria = session.createCriteria(DBArticle.class);
//        criteria.setMaxResults(100);
        List<DBArticle> articles = criteria.list();

        articles.forEach(System.out::println);
    }

    @Test
    public void testGetByTags(){
        session.getTransaction().commit();
        session.beginTransaction();
        List <String> tags = List.of("Tag0", "Tag2");

        Criteria criteria = session.createCriteria(DBTag.class);
        List<DBTag> list = criteria.list();

        list.stream().map(t->t.getArticles() + " " + t.getTag()).forEach(System.out::println);
        list.forEach(System.out::println);

        final List<DBArticle> collect = list.stream().distinct()
                .filter(t -> tags.contains(t.getTag()))
                .flatMap(dbTag -> dbTag.getArticles().stream())
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void updateArticle() {
        List<DBArticle> articles = session.createQuery("select a from ARTICLES a", DBArticle.class).getResultList();
        articles.forEach(System.out::println);

        DBArticle article = session.get(DBArticle.class, 4);
        article.setText("There is new text of article " + article.getHeader());
        article.setHeader("There is new Header of article");
        article.setTags(Set.copyOf(tagsAddIfExistReturn(new DBTag("completly_new_tag"), new DBTag("Tag0"))));
        session.update(article);
        session.flush();

        articles = session.createQuery("select a from ARTICLES a", DBArticle.class).getResultList();
        articles.forEach(System.out::println);
    }

    @Test
    public void tagsAddIfNotExists() {
        List<DBTag> result = new ArrayList<>();// это засунуть в новую или редактируемую статью при добавлении/обновлении

        List<DBTag> tagsToAdd = List.of(new DBTag("Tag0"), new DBTag("Tag2"));
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
        result.forEach(bdT -> System.out.format("    ----    Persisted tag with DB_ID %d and Body of %s \n", bdT.getId(), bdT.getTag()));
    }

    @Test
    public void testDelete() {
        DBArticle o = session.get(DBArticle.class, 14);
        System.out.println(o);
        Set<DBTag> tags1 = o.getTags();

        //TODO это должно лечиться выставлением правильного CascadeType как мне кажется
        o.setTags(Collections.emptySet());
        tags1.forEach(t -> t.getArticles().remove(o));

        session.delete(o);
        session.flush();
        System.out.println("-----------delete complite");

        //код из метода testGetAllViaJPQL
        List<DBArticle> articles = session.createQuery("select a from ARTICLES a", DBArticle.class).getResultList();
        articles.forEach(System.out::println);

        List<DBTag> tags = session.createQuery("select t from TAGS t", DBTag.class).getResultList();
        tags.forEach(System.out::println);
    }

    @Test
    public void getArticlesWithJPQL(){
        String queryString = "select distinct a from ARTICLES a join a.tags tag where tag.id in :tags";
        Set<DBTag> tagsForSearch = tagsAddIfExistReturn(new DBTag("Tag4"), new DBTag("Tag2"));
        List<Integer> tagsIDs = tagsForSearch.stream().map(DBTag::getId).collect(Collectors.toList());
        Query query = session.createQuery(queryString);
        query.setParameter("tags", tagsIDs);
        List<DBArticle> articles = query.getResultList();
        articles.forEach(System.out::println);
    }

    private void prepareDB(Session session) {
        DBTag tag0 = new DBTag();
        tag0.setTag("Tag0");
        DBTag tag1 = new DBTag();
        tag1.setTag("Tag1");
        DBTag tag2 = new DBTag();
        tag2.setTag("Tag2");

        DBArticle artilce1 = new DBArticle();
        artilce1.setHeader("Article1");
        artilce1.setText("Article 1 text is here");
        artilce1.setTags(tagsAddIfExistReturn(tag0, tag1, tag2));

        DBArticle artilce2 = new DBArticle();
        artilce2.setHeader("Article2");
        artilce2.setText("Article 2 text is here");
        artilce2.setTags(tagsAddIfExistReturn(tag0, tag2));

        session.save(artilce1);
        session.save(artilce2);
    }

    public Set<DBTag> tagsAddIfExistReturn(DBTag... args) {
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

}
