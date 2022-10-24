package ashev.flowers_calendar.db.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.sql.Blob;

@Slf4j
public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static SessionFactory sessionFactoryDatabaseCreation;

    public static SessionFactory initSessionFactoryDatabaseCreation() {
        if (sessionFactoryDatabaseCreation != null) {
            throw new RuntimeException("Initialization duplicates for db");
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug("Connection for db is opening...");
            }
            sessionFactoryDatabaseCreation = new Configuration()
                    .configure("hibernate-creation.cfg.xml")
                    .buildSessionFactory();
        } catch (Exception e) {
            shutdown();
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        return sessionFactoryDatabaseCreation;
    }

	public static SessionFactory initSessionFactory() {
        if (sessionFactory != null) {
            throw new RuntimeException("Initialization duplicates for db");
        }
        try {
            if (log.isDebugEnabled()) {
                log.debug("Connection for db is opening...");
            }
            sessionFactory = new Configuration()
            		.configure()
            		.buildSessionFactory();
        } catch (Exception e) {
            shutdown();
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        return sessionFactory;
    }
	
	public static synchronized SessionFactory getSessionFactory() {
        return sessionFactory;
    }
	
	 public static void shutdown() {
        if (log.isDebugEnabled()) {
            log.debug("Connection for db is closing...");
        }
        if (sessionFactory != null) {
        	sessionFactory.close();
        	sessionFactory = null;
        }
    }

    public static void shutdownDatabaseCreation() {
        if (log.isDebugEnabled()) {
            log.debug("Connection for db is closing...");
        }
        if (sessionFactoryDatabaseCreation != null) {
            sessionFactoryDatabaseCreation.close();
            sessionFactoryDatabaseCreation = null;
        }
    }
	
	 public static Blob createBlobFromFile(Session session, File file) throws Exception {
         final byte[] bytes = FileUtils.readFileToByteArray(file);
		 return createBlobFromBytes(session, bytes);
	 }
	 
	public static Blob createBlobFromBytes(Session session, byte[] bytes) {
		return Hibernate.getLobCreator(session).createBlob(bytes);
	}
	
	public static byte[] readBytesFromBlobWithFree(Blob blob) throws Exception {
		try {
			return blob.getBytes(1, (int) blob.length());
		} finally {
			blob.free();
		}
	}
	 
}
