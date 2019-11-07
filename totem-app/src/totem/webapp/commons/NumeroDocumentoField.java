package totem.webapp.commons;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NumeroDocumentoField extends TextField<Long> {
    private final Converter converter = new Converter();
    private static final Logger LOGGER = Logger.getLogger(NumeroDocumentoField.class.getName());

    public NumeroDocumentoField(String id) {
        super(id);
    }


    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>)converter;
    }


    class Converter implements IConverter<Long> {


        @Override
        public Long convertToObject(String s, Locale locale) throws ConversionException {
            try {
                if (s.length() > String.valueOf(Long.MAX_VALUE).length()) {
                    return null;
                }
                Long l = new Long(s);
                if (l < 1) {
                    return null;
                }
                return l;
            } catch (Throwable e) {
                LOGGER.log(Level.WARNING, "Error de conversión", e);
                return null;
            }
        }

        @Override
        public String convertToString(Long aLong, Locale locale) {
            return aLong.toString();
        }

    }



}
