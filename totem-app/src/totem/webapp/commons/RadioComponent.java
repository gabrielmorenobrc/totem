package totem.webapp.commons;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.io.Serializable;

public class RadioComponent<T extends Serializable> extends Panel {

    private final Model<T> model;
    private final Callback<T> callback;

    public static abstract class Callback<T extends Serializable> implements Serializable {
        public abstract String getDisplayValue(T t);
        public abstract Object getId(T t);
    }

    public RadioComponent(String id, Model<T> model, ListModel<T> listModel, Callback<T> callback) {
        super(id);
        this.model = model;
        this.callback = callback;
        this.setOutputMarkupId(true);
        ListView<T> listView = new ListView<T>("radio", listModel) {
            @Override
            protected void populateItem(ListItem<T> listItem) {
                WebComponent radio = new WebComponent("input", new Model<>());
                radio.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
                        model.setObject(listItem.getModelObject());
                        ajaxRequestTarget.add(RadioComponent.this);
                        onChanged(ajaxRequestTarget);
                    }
                });
                radio.setOutputMarkupId(true);
                if (model.getObject() != null && callback.getId(listItem.getModelObject()).equals(callback.getId(model.getObject()))) {
                    listItem.add(new AttributeAppender("class", " checked"));
                    radio.add(new AttributeModifier("checked", "checked"));
                }
                listItem.add(radio);
                listItem.add(new Label("label", callback.getDisplayValue(listItem.getModelObject())));
            }
        };
        add(listView);
    }

    public void onChanged(AjaxRequestTarget ajaxRequestTarget) {

    }


}
