package ashev.flowers_calendar.db.executor;

import org.hibernate.Session;

public interface Executor {

    void execute(Session session);

}
