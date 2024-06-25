package totem.webapp.app;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.string.StringValue;
import plataforma1.wicket.semantic.NotifierProvider;
import plataforma1.wicket.semantic.SemanticResourceReference;
import totem.service.SedeInfo;
import totem.service.TotemService;
import totem.webapp.resources.Resources;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TotemPage extends WebPage {
    @Inject
    private TotemService totemService;
    @Inject
    private SessionData sessionData;
    private WebMarkupContainer contenedor;
    private MenuPanel menuPanel;
    private AbstractDefaultAjaxBehavior callbackBehavior;
    private Model<String> nombreSedeModel;
    private Label nombreSedeLabel;
    @Inject
    private NotifierProvider notifierProvider;

    public TotemPage(PageParameters pageParameters){
        setVersioned(false);
        String idSede = pageParameters.get("idSede").toString();
        sessionData.setIdSede(idSede);
        SedeInfo sedeInfo = totemService.findSedeInfo(sessionData.getIdSede());
        nombreSedeModel = new Model<>("");
        nombreSedeModel.setObject(sedeInfo.getDescripcion());
        notifierProvider.createNotifier(this, "notifier");

        StringValue onlineParam = pageParameters.get("online");

        addSetup();
        addTitle();
        addMenu();
        addContenido();

        if (onlineParam.toBoolean(false)) {
            sessionData.setSelectedMenu("online");
            addOnline();
        } else {
            sessionData.setSelectedMenu("atencion");
            addAtencion();
        }

    }

    private void addTitle() {
        nombreSedeLabel = new Label("nombreSede", nombreSedeModel);
        nombreSedeLabel.setOutputMarkupId(true);
        add(nombreSedeLabel);
    }

    private void addSetup() {
        callbackBehavior = new AbstractDefaultAjaxBehavior() {
            @Override
            protected void respond(AjaxRequestTarget ajaxRequestTarget) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest();
                try {
                    String error = httpServletRequest.getParameter("error");
                    if (error == null) {
                        String idSede = httpServletRequest.getParameter("idSede");
                        sessionData.setIdSede(idSede);
                        SedeInfo sedeInfo = totemService.findSedeInfo(sessionData.getIdSede());
                        nombreSedeModel.setObject(sedeInfo.getDescripcion());
                    } else {
                        Logger.getLogger(TotemPage.class.getName()).log(Level.WARNING, error);
                        nombreSedeModel.setObject("ERROR");
                    }

                } catch (Exception e) {
                    Logger.getLogger(TotemPage.class.getName()).log(Level.WARNING, "", e);
                    nombreSedeModel.setObject("ERROR");
                }
                ajaxRequestTarget.add(nombreSedeLabel);
            }
        };
        add(callbackBehavior);
    }

    private void addAtencion() {
        AtencionPanel panel = new AtencionPanel("contenido");
        contenedor.addOrReplace(panel);
    }

    private void addMenu() {
        menuPanel = new MenuPanel("menu", new MenuPanel.Callback() {
            @Override
            public void onSelected(AjaxRequestTarget ajaxRequestTarget) {
                String nombre = sessionData.getSelectedMenu();
                if (nombre.equals("atencion")) {
                    addAtencion();
                } else if (nombre.equals("online")) {
                    addOnline();
                } else if (nombre.equals("guia")) {
                    addGuia();
                }
                ajaxRequestTarget.add(menuPanel);
                ajaxRequestTarget.add(contenedor);
            }
        });
        menuPanel.setOutputMarkupId(true);
        add(menuPanel);

    }

    private void addGuia() {
        GuiaTramitesPanel panel = new GuiaTramitesPanel("contenido");
        contenedor.addOrReplace(panel);
    }

    private void addOnline() {
        TurnoOnlinePanel panel = new TurnoOnlinePanel("contenido");
        contenedor.addOrReplace(panel);
    }

    private void addContenido() {
        contenedor = new WebMarkupContainer("contenedor");
        contenedor.setOutputMarkupId(true);
        add(contenedor);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(Resources.class, "totem.css")));
        response.render(JavaScriptHeaderItem.forReference(SemanticResourceReference.get()));
        response.render(CssHeaderItem.forReference(new CssResourceReference(Resources.class, "semantic_override.css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(Resources.class, "totem.js")));
        response.render(CssHeaderItem.forReference(new CssResourceReference(Resources.class, "clan.css")));
        response.render(JavaScriptHeaderItem.forScript("initGuiaTramites();", null));
        String callbackUrl = String.valueOf(callbackBehavior.getCallbackUrl());
        response.render(OnDomReadyHeaderItem.forScript("setupConfig('" + callbackUrl + "');"));
        response.render(OnDomReadyHeaderItem.forScript("updateScrollingAdvice();"));
    }


}
