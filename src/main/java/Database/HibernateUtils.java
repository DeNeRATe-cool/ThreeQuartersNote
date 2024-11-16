package Database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static final Configuration configuration;
    private static final SessionFactory sessionFactory;

    static{
        configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    /**
     * get new brand-new Session
     */
    public static Session openSession(){
        return sessionFactory.openSession();
    }

    /**
     * get Session of current thread tied
     * RECOMMEND!!!
     */
    public static Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    /**
     * turn of the global sessionFactory
     */
    public static void quit() {
        sessionFactory.close();
    }
}
