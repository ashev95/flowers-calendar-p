package ashev.flowers_calendar.db.dao;

import ashev.flowers_calendar.db.entity.LightType;
import ashev.flowers_calendar.db.executor.ExecutorManager;
import ashev.flowers_calendar.db.executor.RemoveExecutor;
import ashev.flowers_calendar.db.executor.SaveExecutor;
import ashev.flowers_calendar.db.executor.UpdateExecutor;
import ashev.flowers_calendar.db.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

@Slf4j
public class LightTypeDAO {

    private static final String SELECT_LIGHT_TYPE_ALL = "from " + LightType.class.getSimpleName();

    public static List<LightType> fetchAll() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        List<LightType> lightTypes;
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            final Query<LightType> query = session.createQuery(SELECT_LIGHT_TYPE_ALL, LightType.class);
            lightTypes = query.getResultList();
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return lightTypes;
    }

    public static LightType save(LightType lightType) {
        ExecutorManager.doInTransactionWithFormValidationAlert(new SaveExecutor(lightType));
        return lightType;
    }

    public static LightType update(LightType lightType) {
        ExecutorManager.doInTransactionWithFormValidationAlert(new UpdateExecutor(lightType));
        return lightType;
    }

    public static void remove(LightType lightType) {
        ExecutorManager.doInTransaction(new RemoveExecutor(lightType));
    }

}
