package totem.webapp.app;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.*;

public class AnuncioPanel extends TotemPanel {
    @Inject
    private SessionData sessionData;

    public AnuncioPanel(String id) {
        super(id);
        addProgressBar();
        String nombreCliente = sessionData.getAnuncioInfo().getNombreCliente();
        if (nombreCliente == null || nombreCliente.equals("null null")) {
            add(new Label("saludo", "COMPLETADO"));
        } else {
            add(new Label("saludo", "HOLA " + nombreCliente.toUpperCase()));
        }
        add(new Label("texto1", "Ha sido anunciado con el turno "));
        add(new Label("cod", sessionData.getAnuncioInfo().getNumeroAnuncio()));
        add(new Label("texto2", " para el trámite"));
        add(new Label("tramite", sessionData.getAnuncioInfo().getTramiteInfo().getDescripcion()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        String fecha = new SimpleDateFormat("yyyy'-'MM'-'dd'T'hh':'mm':'ss'-'03:00").format(new Date());
        String data = String.format("{\"categoria\":\"%s\", " +
                        "\"subcategoria\":\"%s\"," +
                        "\"tramite\":\"%s\"," +
                        "\"sede\":\"%s\"," +
                        "\"fecha\":\"%s\"," +
                        "\"numero\":\"%s\"}",
                sessionData.getAnuncioInfo().getCategoriaInfo().getDescripcion(),
                sessionData.getAnuncioInfo().getSubcategoriaInfo().getDescripcion(),
                sessionData.getAnuncioInfo().getTramiteInfo().getDescripcion(),
                sessionData.getAnuncioInfo().getSedeInfo().getDescripcion(),
                fecha,
                sessionData.getAnuncioInfo().getNumeroAnuncio());
        response.render(OnDomReadyHeaderItem.forScript("printTicket('" + data + "');"));


        Duration timeout = Duration.ofSeconds(30);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future handler = executor.submit(new Callable() {
            @Override
            public String call() throws Exception {

                RequestCycle.get().setResponsePage(TotemPage.class);
                return "ok";
            }
        });

        try {
            handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            handler.cancel(true);
        }

        executor.shutdownNow();
    }

    private void addProgressBar() {
        WebMarkupContainer progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        progressBar.add(new ProgressBar("bar", "Anunciado"));
        add(progressBar);
    }
}
