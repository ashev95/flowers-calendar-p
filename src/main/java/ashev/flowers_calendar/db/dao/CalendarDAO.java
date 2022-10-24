package ashev.flowers_calendar.db.dao;

import ashev.flowers_calendar.db.entity.Calendar;
import ashev.flowers_calendar.db.entity.Flower;
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
import java.util.List;

@Slf4j
public class CalendarDAO {

    public static Calendar save(Calendar calendar) {
        ExecutorManager.doInTransactionWithFormValidationAlert(new SaveExecutor(calendar));
        return calendar;
    }

    public static Calendar update(Calendar calendar) {
        ExecutorManager.doInTransactionWithFormValidationAlert(new UpdateExecutor(calendar));
        return calendar;
    }

    public static Calendar find(long id) {
        return ExecutorManager.find(Calendar.class, id);
    }

    public static void remove(Calendar calendar) {
        ExecutorManager.doInTransaction(new RemoveExecutor(calendar));
    }

    public static boolean hasByFlower(Flower flower) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        boolean res;
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            final CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Boolean> cQuery = cBuilder.createQuery(Boolean.class);
            final Root<Calendar> root = cQuery.from(Calendar.class);
            cQuery.select(cBuilder.literal(Boolean.TRUE));
            final Path<?> attributePath = root.get("flower");
            cQuery.where(cBuilder.equal(attributePath, flower.getId()));
            final Query query = session.createQuery(cQuery);
            query.setMaxResults(1);
            res = query.getResultList().size() > 0;
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return res;
    }

    public static List<Calendar> fetchByFlower(Flower flower) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        List<Calendar> calendarRows;
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            final CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Calendar> cQuery = cBuilder.createQuery(Calendar.class);
            final Root<Calendar> root = cQuery.from(Calendar.class);
            final Path<?> attributePath = root.get("flower");
            cQuery.where(cBuilder.equal(attributePath, flower.getId()));
            final Query query = session.createQuery(cQuery);
            calendarRows = query.getResultList();
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return calendarRows;
    }

}
