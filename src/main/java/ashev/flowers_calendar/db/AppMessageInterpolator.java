package ashev.flowers_calendar.db;

import ashev.flowers_calendar.UTF8Control;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
import org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator;

import javax.el.ExpressionFactory;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppMessageInterpolator extends AbstractMessageInterpolator {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("hibernate-bundle" + File.separatorChar + "ValidationMessages", new UTF8Control());

    public AppMessageInterpolator() {
        super(locale -> BUNDLE);
    }

    @Override
    public String interpolate(Context context, Locale locale, String term) {
        final InterpolationTerm expression = new InterpolationTerm(term, locale, ExpressionFactory.newInstance());
        return expression.interpolate( context );
    }

}
