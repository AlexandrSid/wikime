import dto.DBArtilce;
import dto.DBTag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

public class HibernateTest{

    private static SessionFactory sessionFactory = null;

    @BeforeClass
    public static void setup() throws Exception {
        sessionFactory = new Configuration().configure("db/hibernate.cfg.xml").buildSessionFactory();
        System.out.println("Session Factory SETUP");
    }

    @AfterClass
    public static void teardown() throws Exception{
        sessionFactory.close();
        System.out.println("Session Factory TEARDOWN");
    }

    @Test
    public void testSave(){
        DBTag tag0 = new DBTag();
        tag0.setTag("Tag0");
        DBTag tag1 = new DBTag();
        tag1.setTag("Tag1");
        DBTag tag2 = new DBTag();
        tag2.setTag("Tag2");

        DBArtilce artilce1 = new DBArtilce();
        artilce1.setHeader("Article1");
        artilce1.setText("Article 1 text is here");
        artilce1.setTags(Set.of(tag0, tag1));

        DBArtilce artilce2 = new DBArtilce();
        artilce2.setHeader("Article2");
        artilce2.setText("Article 2 text is here");
        artilce2.setTags(Set.of(tag0, tag2));

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(tag0);
        session.save(tag1);
        session.save(tag2);
        session.save(artilce1);
        session.save(artilce2);

        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testGet(){
        testSave();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        DBArtilce artilce = session.get(DBArtilce.class, 4);
        System.out.println(artilce);

        session.getTransaction().commit();
        session.close();
    }

}
