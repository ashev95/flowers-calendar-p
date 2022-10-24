package ashev.flowers_calendar.db.executor;

import lombok.AllArgsConstructor;
import org.hibernate.Session;

@AllArgsConstructor
public class UpdateExecutor implements Executor {
    private Object o;
    @Override
    public void execute(Session session) {
        session.update(o);
    }
}
