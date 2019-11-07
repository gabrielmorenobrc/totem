package totem.webapp.commons;

import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateConverter implements IConverter<Date> {
    private static final Logger LOGGER = Logger.getLogger(DateConverter.class.getName());
    private final DateFormat dateFormat;

    public DateConverter(String format) {
        this.dateFormat = new SimpleDateFormat(format);
    }


    @Override
    public Date convertToObject(String s, Locale locale) throws ConversionException {
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error de conversión", e);
            return null;
        }
    }

    @Override
    public String convertToString(Date date, Locale locale) {
        return dateFormat.format(date);
    }
}
