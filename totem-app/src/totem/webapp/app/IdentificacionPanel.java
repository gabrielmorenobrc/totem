package totem.webapp.app;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.util.ListModel;
import totem.service.TipoDocumentoInfo;
import totem.service.TotemService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentificacionPanel extends Panel {
    private final Boolean online;
    private PopupPanel popup;
    @Inject
    private SessionData sessionData;
    private String titulo;
    private final Callback callback;
    private WebMarkupContainer tiposContainer;
    private Label button;
    @Inject
    private TotemService totemService;

    public interface Callback extends Serializable {
        void onBuscar(AjaxRequestTarget ajaxRequestTarget);
    }

    public IdentificacionPanel(String id, String titulo,Boolean online,  Callback callback) {
        super(id);
        this.titulo = titulo;
        this.callback = callback;
        popup = new PopupPanel("popup");
        popup.setOutputMarkupId(true);
        add(popup);
        this.online= online;
        addForm();
    }


    private void addTitulo(Form<ModelObject> form){
        Label header = new Label("titulo", this.titulo);
        form.add(header);
    }

    private void addForm() {
        CompoundPropertyModel<ModelObject> model = new CompoundPropertyModel<>(new ModelObject());
        Form<ModelObject> form = new Form<>("form", model);
        addTitulo(form);
        tiposContainer = new WebMarkupContainer("tipos");
        tiposContainer.setOutputMarkupId(true);
        form.add(tiposContainer);
        ListModel<TipoDocumentoInfo> tiposModel = new ListModel<>(totemService.listTipoDocumentoInfo());
        ListView<TipoDocumentoInfo> tiposView = new ListView<TipoDocumentoInfo>("item", tiposModel) {
            @Override
            protected void populateItem(ListItem<TipoDocumentoInfo> listItem) {
                listItem.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
                        model.getObject().setTipoDocumentoInfo(listItem.getModelObject());
                        ajaxRequestTarget.add(tiposContainer);
                    }
                });
                button = new Label("label", listItem.getModelObject().toString());
                listItem.add(button);
                if (listItem.getModelObject().equals(model.getObject().getTipoDocumentoInfo())) {
                    button.add(new AttributeAppender("class", " teal"));
                    listItem.add(button);
                }
            }
        };
        tiposContainer.add(tiposView);
        form.add(new TextField<String>("numero"));
        form.add(new AjaxSubmitLink("buscar") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    ModelObject modelObject = model.getObject();
                    if (modelObject.getTipoDocumentoInfo() == null) {
                        popup.show(target, this, "Debe seleccionar el tipo de documento");
                    } else if (modelObject.getNumero() == null) {
                        popup.show(target, this, "Debe ingresar el número de documento");
                    } else if (modelObject.getNumero().isEmpty()) {
                        popup.show(target, this, "Debe ingresar el número de documento");
                    } else if (modelObject.getNumero().length() > modelObject.getTipoDocumentoInfo().getCantidadCaracteres()) {
                        popup.show(target, this, "El número de documento no peude superar los " +
                                modelObject.getTipoDocumentoInfo().getCantidadCaracteres() + " caracteres");
                    }else if(modelObject.getTipoDocumentoInfo().getMinimoCaracteres() > 0 && modelObject.getTipoDocumentoInfo().getMinimoCaracteres()  > modelObject.getNumero().length() ){
                        popup.show(target, this, "El número de documento debe superar los " +
                                modelObject.getTipoDocumentoInfo().getMinimoCaracteres() + " caracteres");
                    } else if(online && !totemService.documentoValido(modelObject.getTipoDocumentoInfo().getCodigo(),modelObject.getNumero())){
                        popup.show(target, this, "El numero de documento ingresado no esta registrado en la base de datos, por favor solicite el turno mediante la WEB");
                    } else {
                        sessionData.setTipoDocumentoInfo(model.getObject().tipoDocumentoInfo);
                        sessionData.setNumeroDocumento(model.getObject().numero);
                        callback.onBuscar(target);
                    }
                } catch (Throwable t) {
                    Logger.getLogger(IdentificacionPanel.class.getName()).log(Level.WARNING, "Error", t);
                    popup.show(target, this, "Error inesperado");
                } finally {
                    target.appendJavaScript("hideDimmer();");
                }
            }
        }.setOutputMarkupId(true));
        form.add(new TecladoDocumentoPanel("pad"));
        add(form);
    }

    static class ModelObject implements Serializable {
        private TipoDocumentoInfo tipoDocumentoInfo;
        private String numero;

        public TipoDocumentoInfo getTipoDocumentoInfo() {
            return tipoDocumentoInfo;
        }

        public void setTipoDocumentoInfo(TipoDocumentoInfo tipoDocumento) {
            this.tipoDocumentoInfo = tipoDocumento;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }
    }


}
