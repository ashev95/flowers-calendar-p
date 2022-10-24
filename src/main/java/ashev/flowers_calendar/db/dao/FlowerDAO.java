package ashev.flowers_calendar.db.dao;

import ashev.flowers_calendar.db.entity.Flower;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

@Slf4j
public class FlowerDAO {

    public static Flower save(Flower flower) {
        ExecutorManager.doInTransactionWithFormValidationAlert(new SaveExecutor(flower));
        return flower;
    }

    public static Flower update(Flower flower) {
        ExecutorManager.doInTransactionWithFormValidationAlert(new UpdateExecutor(flower));
        return flower;
    }

    public static Flower find(long id) {
        return ExecutorManager.find(Flower.class, id);
    }

    public static void remove(Flower flower) {
        ExecutorManager.doInTransaction(new RemoveExecutor(flower));
    }

    public static boolean hasByLightType(LightType lightType) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        boolean res;
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            final CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Boolean> cQuery = cBuilder.createQuery(Boolean.class);
            final Root<Flower> root = cQuery.from(Flower.class);
            cQuery.select(cBuilder.literal(Boolean.TRUE));
            final Path<?> attributePath = root.get("lightType");
            cQuery.where(cBuilder.equal(attributePath, lightType.getId()));
            final Query query = session.createQuery(cQuery);
            query.setMaxResults(1);
            res = query.getResultList().size() > 0;
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return res;
    }

}
