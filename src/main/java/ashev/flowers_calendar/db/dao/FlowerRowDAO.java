package ashev.flowers_calendar.db.dao;

import ashev.flowers_calendar.db.entity.FlowerRow;
import ashev.flowers_calendar.db.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

@Slf4j
public class FlowerRowDAO {

    private static final String SELECT_FLOWER_ROWS_ALL = "from " + FlowerRow.class.getSimpleName();

    public static List<FlowerRow> fetchAll() {
        if (log.isDebugEnabled()) {
            log.debug("START");
        }
        List<FlowerRow> flowerRows;
        final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            final Query<FlowerRow> query = session.createQuery(SELECT_FLOWER_ROWS_ALL, FlowerRow.class);
            flowerRows = query.getResultList();
        }
        if (log.isDebugEnabled()) {
            log.debug("END");
        }
        return flowerRows;
    }

}
