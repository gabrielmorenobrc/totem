package totem.webapp.app;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.Response;
import plataforma1.util.StringUtils;


public class HtmlComponent extends WebComponent {

    private String content;

    public HtmlComponent(String id, String content) {
        super(id);
        setEscapeModelStrings(true);
        this.content = StringUtils.stripNonPrintable(content);
    }

    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        Response response = getRequestCycle().getResponse();
        if (content != null) {
            response.write(content);
        }
    }

    public void setContent(String content) {
        this.content = content;
    }


}
