import dto.DBArticle;
import dto.DBTag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.util.List;
import java.util.Set;

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
    public void closeTransactionAndSession(){
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
        DBArticle artilce = session.get(DBArticle.class, 4);
        System.out.println("|||||||------->>>>>>> " + artilce);

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
        Criteria criteria = session.createCriteria(DBArticle.class);
//        criteria.setMaxResults(100);
        List<DBArticle> articles = criteria.list();

        articles.forEach(System.out::println);
    }

    @Test
    public void testDelete() {
        session.delete(session.get(DBArticle.class, 4));
        session.flush();
        System.out.println("-----------delete complite");

        //код из метода testGetAllViaJPQL
        List<DBArticle> articles = session.createQuery("select a from ARTICLES a", DBArticle.class).getResultList();
        articles.forEach(System.out::println);

        List<DBTag> tags = session.createQuery("select t from TAGS t", DBTag.class).getResultList();
        tags.forEach(System.out::println);
    }

    @Test
    public void updateArticle(){
        List<DBArticle> articles = session.createQuery("select a from ARTICLES a", DBArticle.class).getResultList();
        articles.forEach(System.out::println);

        DBArticle article = session.get(DBArticle.class, 4);
        article.setText("There is new text of article " + article.getHeader());
        session.save(article);

        articles = session.createQuery("select a from ARTICLES a", DBArticle.class).getResultList();
        articles.forEach(System.out::println);
    }

    private void prepareDB(Session session){
        DBTag tag0 = new DBTag();
        tag0.setTag("Tag0");
        DBTag tag1 = new DBTag();
        tag1.setTag("Tag1");
        DBTag tag2 = new DBTag();
        tag2.setTag("Tag2");

        DBArticle artilce1 = new DBArticle();
        artilce1.setHeader("Article1");
        artilce1.setText("Article 1 text is here");
        artilce1.setTags(Set.of(tag0, tag1));

        DBArticle artilce2 = new DBArticle();
        artilce2.setHeader("Article2");
        artilce2.setText("Article 2 text is here");
        artilce2.setTags(Set.of(tag0, tag2));

        session.save(tag0);
        session.save(tag1);
        session.save(tag2);
        session.save(artilce1);
        session.save(artilce2);
    }

}
