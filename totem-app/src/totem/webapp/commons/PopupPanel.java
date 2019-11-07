package totem.webapp.commons;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;

public class PopupPanel extends Panel {
    private final HtmlComponent htmlComponent;

    public PopupPanel(String id) {
        super(id);
        add(new AttributeAppender("class", " ui custom popup top left transition hidden"));
        add(new AttributeAppender("style", " overflow: auto;"));
        htmlComponent = new HtmlComponent("content", "");
        htmlComponent.setOutputMarkupId(true);
        add(htmlComponent);
        setOutputMarkupId(true);
    }

    public void show(AjaxRequestTarget target, Component anchor, String content) {
        htmlComponent.setContent(content);
        target.add(htmlComponent);
        target.appendJavaScript("$('#" + anchor.getMarkupId() + "').popup({" +
                "    popup : $('#" + getMarkupId() + "')," +
                "    on    : 'click', onHidden: function(){$('#" + anchor.getMarkupId() + "').popup('destroy');}});");
        target.appendJavaScript("$('#" + anchor.getMarkupId() + "').popup('show');");
    }

}
