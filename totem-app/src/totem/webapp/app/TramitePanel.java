package totem.webapp.app;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import javax.inject.Inject;
import java.io.Serializable;

public class TramitePanel extends Panel {
    private WebMarkupContainer progressBar;
    private final Callback callback;
    @Inject
    private SessionData sessionData;
    private Integer cantColumns;

    public interface Callback extends Serializable {
        void onTramiteSeleccionado(AjaxRequestTarget ajaxRequestTarget);
    }

    public TramitePanel(String id, Callback callback) {
        super(id);
        this.callback = callback;
        progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        this.cantColumns = sessionData.getTipoPaso().equals("anunciarseSinTurno") ? 6 : 8;
        addCategorias();
        setOutputMarkupId(true);
    }

    private void addProgressBar(String titulo) {
        ProgressBar progressBarAux = new ProgressBar("bar", titulo);
        progressBar.addOrReplace(progressBarAux);
        add(progressBar);
    }

    private void addCategorias() {
        Integer percent;
        if (cantColumns == 8) {
            percent = 24;
        } else {
            percent = 54;
        }
        addProgressBar("Categoría");
        addOrReplace(new SeleccionCategoriaPanel("contenido", new SeleccionCategoriaPanel.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                addSubcategorias();
                ajaxRequestTarget.appendJavaScript("initProgressBar(" + cantColumns + ", " + percent + ")");
                ajaxRequestTarget.add(TramitePanel.this);
            }
        }));
    }

    private void addSubcategorias() {
        Integer percent;
        if (cantColumns == 8) {
            percent = 36;
        } else {
            percent = 70;
        }
        addProgressBar("Subcategoría");
        addOrReplace(new SeleccionSubcategoriaPanel("contenido", new SeleccionSubcategoriaPanel.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                addTramites();
                ajaxRequestTarget.appendJavaScript("initProgressBar(" + cantColumns + ", " + percent + ")");
                ajaxRequestTarget.add(TramitePanel.this);
            }
        }));
    }

    private void addTramites() {
        Integer percent;
        if (sessionData.getTipoPaso().equals("turnoOnline")) {
            percent = 48;
        } else {
            percent = 100;
        }
        addProgressBar("Trámite");
        addOrReplace(new SeleccionTramitePanel("contenido", new SeleccionTramitePanel.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                ajaxRequestTarget.appendJavaScript("initProgressBar(" + cantColumns + ", " + percent + ")");
                callback.onTramiteSeleccionado(ajaxRequestTarget);
            }
        }));
    }
}
