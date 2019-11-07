package totem.webapp.commons;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;

import java.util.Date;

public class DateLabel extends Label {

    private DateConverter dateConverter;

    public DateLabel(String id, String format) {
        super(id);
        dateConverter = new DateConverter(format);
    }

    public DateLabel(String id, Date date, String format) {
        super(id, date);
        dateConverter = new DateConverter(format);
    }

    public DateLabel(String id, Model<Date> model, String format) {
        super(id, model);
        dateConverter = new DateConverter(format);
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        if (type.equals(Date.class)) {
            return (IConverter<C>)dateConverter;
        } else {
            return super.getConverter(type);
        }
    }

}
