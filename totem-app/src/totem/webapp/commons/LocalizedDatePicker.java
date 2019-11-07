package totem.webapp.commons;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.convert.IConverter;
import totem.webapp.resources.Resources;

import java.util.Date;

public class LocalizedDatePicker extends DatePicker {

    private DateConverter converter;

    public LocalizedDatePicker(String id, IModel<Date> model) {
        super(id, model);
        this.converter = new DateConverter("dd/MM/yyyy");
        setOutputMarkupId(true);
    }

    public LocalizedDatePicker(String id) {
        this(id, new Options());
    }

    public LocalizedDatePicker(String id, Options options) {
        super(id, options);
        this.converter = new DateConverter("dd/MM/yyyy");
        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Resources.class, "datepicker-es.js")));
        String selector = "$( \"#" + getMarkupId() + "\" )";
        response.render(OnDomReadyHeaderItem.forScript(selector + ".datepicker( $.datepicker.regional[ \"es\" ]);"));
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>)converter;
    }
}

