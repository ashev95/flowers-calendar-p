import ashev.flowers_calendar.db.entity.Calendar;
import ashev.flowers_calendar.db.entity.Flower;
import ashev.flowers_calendar.db.entity.FlowerRow;
import ashev.flowers_calendar.db.entity.LightType;
import ashev.flowers_calendar.db.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DbTests {

	private static final File TEST_TEMP_DIR = new File("src/test/resources/tmp");

	@Test
	@Disabled("Activate before creation and then start db-server.bat")
	@Order(1)
	@DisplayName("Create database")
	public void createDatabaseTest() {
		try {
			final SessionFactory sessionFactory = HibernateUtil.initSessionFactoryDatabaseCreation();
			assertNotNull(sessionFactory);
		} finally {
			HibernateUtil.shutdownDatabaseCreation();
		}
	}

	@Test
	@Order(2)
	@DisplayName("Create connection")
	public void createConnectionTest() throws Exception {
		try {
			final SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
			assertNotNull(sessionFactory);
		} finally {
			HibernateUtil.shutdown();
		}
	}

	@Test
	@Order(3)
	@DisplayName("Create user for debug")
	public void createUserForDebugTest() {
		try {
			final SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
			Session session = null;
			Transaction transaction = null;
			try {
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				session.createSQLQuery("CREATE USER IF NOT EXISTS DEBUG PASSWORD '12345' ADMIN").executeUpdate();
				transaction.commit();
				final Query query = session.createSQLQuery("SELECT * FROM INFORMATION_SCHEMA.USERS u WHERE USER_NAME = 'DEBUG'");
				assertEquals(1, query.getResultList().size());
			} finally {
				if (session != null) {
					session.close();
				}
			}
		} finally {
			HibernateUtil.shutdown();
		}
	}

	@Test
	@Order(4)
	@DisplayName("Create LightType")
	public void createLightTypeTest() throws Exception {
		try {
			final SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
			Session session = null;
			Transaction transaction = null;
			try {
				session = sessionFactory.openSession();
				final CriteriaQuery<LightType> cQuery = session.getCriteriaBuilder().createQuery(LightType.class);
				cQuery.from(LightType.class);
				final Query query = session.createQuery(cQuery).setMaxResults(1);
				if (query.getResultList().size() == 0) {
					final LightType lightType = new LightType("Яркий");
					final LightType lightType2 = new LightType("Рассеянный");
					transaction = session.beginTransaction();
					session.save(lightType);
					session.save(lightType2);
					transaction.commit();
					if (log.isInfoEnabled()) {
						log.info("LightType id: {}", lightType.getId());
					}
					final LightType lightType1 = session.find(LightType.class, lightType.getId());
					assertNotNull(lightType1);
					final LightType lightType3 = session.find(LightType.class, lightType2.getId());
					assertNotNull(lightType3);
				} else {
					if (log.isInfoEnabled()) {
						log.info("LightTypes are created yet");
					}
				}
				final CriteriaQuery<LightType> cQuery1 = session.getCriteriaBuilder().createQuery(LightType.class);
				cQuery1.from(LightType.class);
				Query query1 = session.createQuery(cQuery1);
				if (log.isInfoEnabled()) {
					log.info("LightType's count: {}", query1.getResultList().size());
				}
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				throw e;
			} finally {
				if (session != null) {
					session.close();
				}
			}
		} finally {
			HibernateUtil.shutdown();
		}
	}
	
	@Test
	@Order(5)
	@DisplayName("Create Flower")
	public void createFlowerTest() throws Exception {
		try {
			FileUtils.cleanDirectory(TEST_TEMP_DIR.getAbsoluteFile());
			SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
			Session session = null;
			Transaction transaction = null;
			try {
				session = sessionFactory.openSession();
				final CriteriaQuery<Flower> cQuery = session.getCriteriaBuilder().createQuery(Flower.class);
				cQuery.from(Flower.class);
				Query query = session.createQuery(cQuery).setMaxResults(1);
				if (query.getResultList().size() == 0) {
					LightType lightType = session.find(LightType.class, 1L);
					assertNotNull(lightType);
					URL url = Thread.currentThread().getContextClassLoader().getResource("test.jpg");
					File file = new File(url.getPath());
					Flower flower = new Flower("Гладиолус",
							"Растение с прямым длинным стеблем 0,5 – 1,5 метра в высоту",
							"10 л/квм",
							"Цветы для композиции",
							lightType,
							HibernateUtil.createBlobFromFile(session, file),
							FilenameUtils.getExtension(file.getAbsolutePath()));
					Flower flower1 = new Flower("Фиалка душистая",
							"Растение с укороченными стеблями",
							"Умеренный",
							"Для приготовления настойки",
							lightType,
							HibernateUtil.createBlobFromFile(session, file),
							FilenameUtils.getExtension(file.getAbsolutePath()));
					transaction = session.beginTransaction();
					session.save(flower);
					session.save(flower1);
					transaction.commit();
					if (log.isInfoEnabled()) {
						log.info("Flower's id: {}", flower1.getId());
					}
					Flower flower2 = session.find(Flower.class, flower.getId());
					assertNotNull(flower2);
					Flower flower3 = session.find(Flower.class, flower1.getId());
					assertNotNull(flower3);
				} else {
					if (log.isInfoEnabled()) {
						log.info("Flowers are created yet");
					}
				}
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				throw e;
			} finally {
				try {
					final CriteriaQuery<Flower> cQuery = session.getCriteriaBuilder().createQuery(Flower.class);
					cQuery.from(Flower.class);
					Query query = session.createQuery(cQuery);
					List<Flower> resultList = query.getResultList();
					if (log.isInfoEnabled()) {
						log.info("Flower's count: {}", resultList.isEmpty());
					}
					if (resultList.size() > 0) {
						for (Flower flower : resultList) {
							if (log.isInfoEnabled()) {
								log.info("Flower's id: {}", flower.getId());
								log.info("Flower's image length: {}", flower.getImage().length());
							}
							byte[] imageBinary = HibernateUtil.readBytesFromBlobWithFree(flower.getImage());
							File newFile = new File(TEST_TEMP_DIR, flower.getId() + "." + flower.getImageExt());
							FileUtils.writeByteArrayToFile(newFile, imageBinary);
							assertTrue(newFile.exists());
							assertTrue(newFile.length() > 0);
						}
					}
				} finally {
					if (session != null) {
						session.close();
					}
				}
			}
		} finally {
			HibernateUtil.shutdown();
		}
	}

	@Test
	@Order(6)
	@DisplayName("Create Calendar rows")
	public void createCalendarRowsTest() {
		try {
			SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
			Session session = null;
			Transaction transaction = null;
			try {
				session = sessionFactory.openSession();
				final CriteriaQuery<Calendar> cQuery = session.getCriteriaBuilder().createQuery(Calendar.class);
				cQuery.from(Calendar.class);
				Query query = session.createQuery(cQuery).setMaxResults(1);
				if (query.getResultList().size() == 0) {
					Flower flower = session.find(Flower.class, 1L);
					assertNotNull(flower);
					Calendar calendar = new Calendar(flower, new Date(), "Заметки 1");
					Calendar calendar1 = new Calendar(flower, new Date(), "Заметки 2");
					transaction = session.beginTransaction();
					session.save(calendar);
					session.save(calendar1);
					transaction.commit();
					if (log.isInfoEnabled()) {
						log.info("Calendar's id: {}", calendar.getId());
					}
					Calendar calendar2 = session.find(Calendar.class, calendar.getId());
					assertNotNull(calendar2);
					Calendar calendar3 = session.find(Calendar.class, calendar1.getId());
					assertNotNull(calendar3);
				} else {
					if (log.isInfoEnabled()) {
						log.info("Calendars are created yet");
					}
				}
				final CriteriaQuery<Calendar> cQuery1 = session.getCriteriaBuilder().createQuery(Calendar.class);
				cQuery1.from(Calendar.class);
				Query query1 = session.createQuery(cQuery1);
				List<Calendar> resultList = query1.getResultList();
				if (log.isInfoEnabled()) {
					log.info("Calendar's count: {}", resultList.size());
				}
			} catch (Exception e) {
				if (transaction != null) {
					transaction.rollback();
				}
				throw e;
			} finally {
				if (session != null) {
					session.close();
				}
			}
		} finally {
			HibernateUtil.shutdown();
		}
	}

	@Test
	@Order(7)
	@DisplayName("Retrieve specific columns")
	public void retrieveSpecificColumnsTest() {
		try {
			SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
			try (Session session = sessionFactory.openSession()) {
				final Query<FlowerRow> query = session.createQuery("select new ashev.flowers_calendar.db.entity.FlowerRow(f.id as id, f.name, MAX(c.pourDate) as pourDate) from Flower f, Calendar c " +
						"WHERE f.id = c.flower.id GROUP BY f.id", FlowerRow.class);
				assertTrue(query.getResultList().size() > 0);
				final CriteriaQuery<Calendar> cQuery = session.getCriteriaBuilder().createQuery(Calendar.class);
				cQuery.from(Calendar.class);
				Query query1 = session.createQuery(cQuery);
				List<Calendar> resultList = query1.getResultList();
				if (log.isInfoEnabled()) {
					log.info("Calendar's count: {}", resultList.size());
				}
			}
		} finally {
			HibernateUtil.shutdown();
		}
	}

	@Test
	@Order(8)
	@DisplayName("Retrieve view data")
	public void retrieveViewDataTest() {
		try {
			SessionFactory sessionFactory = HibernateUtil.initSessionFactory();
			try (Session session = sessionFactory.openSession()) {
				final Query<FlowerRow> query = session.createQuery("from " + FlowerRow.class.getSimpleName(), FlowerRow.class);
				assertTrue(query.getResultList().size() > 0);
				if (log.isInfoEnabled()) {
					final CriteriaQuery<Calendar> cQuery = session.getCriteriaBuilder().createQuery(Calendar.class);
					cQuery.from(Calendar.class);
					Query query1 = session.createQuery(cQuery);
					List<Calendar> resultList = query1.getResultList();
					log.info("Calendar's count: {}", resultList.size());
				}
			}
		} finally {
			HibernateUtil.shutdown();
		}
	}

}
