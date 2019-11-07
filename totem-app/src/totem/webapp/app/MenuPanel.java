package totem.webapp.app;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.Panel;
import totem.webapp.commons.ClassRemover;

import javax.inject.Inject;
import java.io.Serializable;

public class MenuPanel extends Panel {
    @Inject
    private SessionData sessionData;
    private final Callback callback;

    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }

    public MenuPanel(String id, Callback callback) {
        super(id);
        this.callback = callback;
        addItems();
        updateActive();
    }

    private void addItems() {
        addItem("atencion");
        addItem("online");
        addItem("guia");
    }

    private void addItem(String id) {
        add(new AjaxLink<Void>(id) {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                if (!id.equals("guia")) {
                    ajaxRequestTarget.appendJavaScript("hideDimmer();");
                }
                sessionData.setSelectedMenu(getId());
                updateActive();
                callback.onSelected(ajaxRequestTarget);
            }
        });
    }

    private void updateActive() {
        String selectedId = sessionData.getSelectedMenu();
        for (int i = 0; i < size(); i++) {
            Component c = get(i);
            if (c.getId().equals(selectedId)) {
                c.add(new AttributeAppender("class", " teal"));
            } else {
                c.add(new ClassRemover("teal"));
            }
        }
    }

}
