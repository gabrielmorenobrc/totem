package totem.webapp.app;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import javax.inject.Inject;

public class TurnoConfirmadoPanel extends TotemPanel {
    @Inject
    private SessionData sessionData;
    private ProgressBar progressBar;



    public TurnoConfirmadoPanel(String id) {
        super(id);
        addProgressBar();
        add(new Label("tramite", sessionData.getTramiteInfo().getDescripcion()));
        add(new Label("tipoDocumento", sessionData.getTipoDocumentoInfo()));
        add(new Label("numeroDocumento", sessionData.getNumeroDocumento()));
        add(new Label("fecha",  sessionData.getDiaInfo().getDia().toString() + "/" + sessionData.getDiaInfo().getMes().toString() + "/" + sessionData.getDiaInfo().getAnio().toString()));
        add(new Label("hora", sessionData.getHorarioInfo().getHora().toString()  + ":" + (sessionData.getHorarioInfo().getMinutos() == 0 ? "00" : sessionData.getHorarioInfo().getMinutos().toString())));
        add(new Label("sede", sessionData.getSedeInfo().getDescripcion()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(OnDomReadyHeaderItem.forScript("printTicketOnline();"));
    }

    private void addProgressBar(){
        WebMarkupContainer progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        progressBar.add( new ProgressBar("bar", "Finalizar"));
        add(progressBar);
    }
}
