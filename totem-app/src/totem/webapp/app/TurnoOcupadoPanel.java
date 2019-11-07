package totem.webapp.app;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import javax.inject.Inject;
import java.io.Serializable;

public class TurnoOcupadoPanel extends TotemPanel {
    private final Callback callback;
    private Label button;
    private String msg;
    @Inject
    private SessionData sessionData;

    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }

    public TurnoOcupadoPanel(String id, String msg, Callback callback) {
        super(id);
        this.msg = msg;
        addProgressBar();
        addMessage();
        addButton();
        this.callback = callback;
    }

    private void addProgressBar() {
        WebMarkupContainer progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        progressBar.add(new ProgressBar("bar", "Finalizar"));
        add(progressBar);
    }

    private void addMessage() {
        Label msgContainer = new Label("msg", this.msg);
        msgContainer.setOutputMarkupId(true);
        add(msgContainer);
    }

    private void addButton() {
        button = new Label("button", "Regresar");
        button.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
                callback.onSelected(ajaxRequestTarget);
            }
        });
        button.setOutputMarkupId(true);
        add(button);
    }
}
