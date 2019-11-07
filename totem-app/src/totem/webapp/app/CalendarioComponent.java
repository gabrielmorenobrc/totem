package totem.webapp.app;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import totem.service.DiaInfo;
import totem.service.TotemService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarioComponent extends Panel {
    private final Callback callback;
    @Inject
    private TotemService totemService;
    private ListModel<DiaInfo> listModel;
    private WebMarkupContainer container;
    private int mes;
    private int anio;
    private Label mesLabel;
    private Label anioLabel;
    @Inject
    private SessionData sessionData;


    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }

    public CalendarioComponent(String id, Callback callback) {
        super(id);
        addProgressBar();
        addActions();
        addCards();
        this.callback = callback;
    }

    private void addProgressBar() {
        WebMarkupContainer progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        progressBar.add(new ProgressBar("bar", "Día"));
        add(progressBar);
    }

    private void addActions() {
        add(new AjaxLink<Void>("bck") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                mes--;
                if (mes == 0) {
                    mes = 12;
                    anio--;
                }
                listModel.setObject(totemService.listDiaInfo(sessionData.getTramiteInfo().getId(), sessionData.getSedeInfo().getId(), mes, anio));
                ajaxRequestTarget.add(container);
                ajaxRequestTarget.add(mesLabel);
                ajaxRequestTarget.add(anioLabel);
            }
        });
        add(new AjaxLink<Void>("fwd") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                mes++;
                if (mes == 13) {
                    mes = 1;
                    anio++;
                }
                listModel.setObject(totemService.listDiaInfo(sessionData.getTramiteInfo().getId(), sessionData.getSedeInfo().getId(), mes, anio));
                ajaxRequestTarget.add(container);
                ajaxRequestTarget.add(mesLabel);
                ajaxRequestTarget.add(anioLabel);
            }
        });
        mesLabel = new Label("mes", new PropertyModel<>(this, "mes"));
        mesLabel.setOutputMarkupId(true);
        add(mesLabel);
        anioLabel = new Label("anio", new PropertyModel<>(this, "anio"));
        anioLabel.setOutputMarkupId(true);
        add(anioLabel);
    }

    private void addCards() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        DiaInfo diaInfo = sessionData.getDiaInfo();
        if (diaInfo == null) {
            mes = calendar.get(Calendar.MONTH) + 1;
            anio = calendar.get(Calendar.YEAR);
        } else {
            mes = diaInfo.getMes();
            anio = diaInfo.getAnio();
        }
        List<DiaInfo> list = totemService.listDiaInfo(sessionData.getTramiteInfo().getId(), sessionData.getSedeInfo().getId(), mes, anio);
        listModel = new ListModel<>(list);
        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);
        ListView<DiaInfo> listView = new ListView<DiaInfo>("cell", listModel) {
            @Override
            protected void populateItem(ListItem<DiaInfo> listItem) {
                listItem.setOutputMarkupId(true);
                DiaInfo diaInfo = listItem.getModelObject();
                if (diaInfo.getMes() != mes) {
                    listItem.add(new AttributeModifier("class", " clickeable calendar-block otroMes column segment ui"));
                }
                if (diaInfo.getDeshabilitado()) {
                    listItem.add(new AttributeModifier("class", " calendar-block column grey segment ui"));
                    listItem.add(new AttributeModifier("onclick", ""));
                }
                if (diaInfo.getOcupado()) {
                    listItem.add(new AttributeModifier("class", " calendar-block column ocupado segment ui"));
                    listItem.add(new AttributeModifier("onclick", ""));
                }
                if (!diaInfo.getOcupado() && !diaInfo.getDeshabilitado()) {
                    listItem.add(new AjaxEventBehavior("onclick") {
                        @Override
                        protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
                            try {
                                sessionData.setDiaInfo(diaInfo);
                                callback.onSelected(ajaxRequestTarget);
                            } finally {
                                ajaxRequestTarget.appendJavaScript("hideDimmer();");
                            }
                        }
                    });
                }
                listItem.add(new Label("numeroDia", diaInfo.getDia().toString()));
            }
        };
        container.add(listView);
    }

}
