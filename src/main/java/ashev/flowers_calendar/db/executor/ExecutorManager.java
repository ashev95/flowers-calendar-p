package ashev.flowers_calendar.db.executor;

import ashev.flowers_calendar.Singleton;
import ashev.flowers_calendar.Utils;
import ashev.flowers_calendar.db.util.HibernateUtil;
import ashev.flowers_calendar.db.validation.AppValidationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ExecutorManager {
    public static synchronized void doInTransactionWithFormValidationAlert(Executor executor) {
        final Set<ConstraintViolation<?>> constraintViolations = doInTransaction(executor);
        if (constraintViolations != null && constraintViolations.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final ConstraintViolation<?> violation : constraintViolations) {
                stringBuilder.append("\n" + violation.getMessage());
            }
            Utils.showWarning(Singleton.getInstance().getAppBundle().getString("executor.manager.alert.title"),
                    Singleton.getInstance().getAppBundle().getString("executor.manager.alert.header_text"),
                    stringBuilder.toString());
            throw new AppValidationException("Form validation error");
        }
    }
    public static synchronized Set<ConstraintViolation<?>> doInTransaction(Executor executor) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            executor.execute(session);
            transaction.commit();
        } catch (javax.validation.ConstraintViolationException ve) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (log.isErrorEnabled()) {
                log.error(ve.getMessage(), ve);
            }
            return ve.getConstraintViolations();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            final ConstraintViolation<Object> unknownErrorConstraintViolation = ConstraintViolationImpl.forBeanValidation(null,
                    null, null, Singleton.getInstance().getAppBundle().getString("executor.manager.error"), null, null, null,
                    null, null, null, null, null);
            return new HashSet(Arrays.asList(unknownErrorConstraintViolation));
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return null;
    }
    public static synchronized <T> T find(Class<T> clazz, long id) {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        Object o;
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            o = session.find(clazz, id);
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return (T) o;
    }
}
