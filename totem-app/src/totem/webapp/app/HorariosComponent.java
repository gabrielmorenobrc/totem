package totem.webapp.app;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.util.ListModel;
import totem.service.DiaInfo;
import totem.service.HorarioInfo;
import totem.service.TotemService;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HorariosComponent extends Panel {
    private final Callback callback;
    @Inject
    private TotemService totemService;
    private ListModel<HorarioInfo> listModel;
    private WebMarkupContainer container;
    private Date fecha;
    private String selectedItemId;
    @Inject
    private SessionData sessionData;

    public interface Callback extends Serializable {
        void onSelected(AjaxRequestTarget ajaxRequestTarget);
    }

    public HorariosComponent(String id, Callback callback) {
        super(id);
        this.callback = callback;
        DiaInfo diaInfo = sessionData.getDiaInfo();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, diaInfo.getAnio());
        calendar.set(Calendar.MONTH, diaInfo.getMes() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, diaInfo.getDia());
        this.fecha = calendar.getTime();
        addActions();
        addCards();
        addProgressBar();
    }

    private void addActions() {

    }


    private void addProgressBar() {
        WebMarkupContainer progressBar = new WebMarkupContainer("ProgressBar");
        progressBar.setOutputMarkupId(true);
        progressBar.add(new ProgressBar("bar", "Horario"));
        add(progressBar);
    }

    private void addCards() {
        List<HorarioInfo> list = totemService.listHorarioInfo(sessionData.getTramiteInfo().getId(), sessionData.getSedeInfo().getId(), fecha, sessionData.getDiaInfo().getTokenTransito());
        listModel = new ListModel<>(list);
        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);
        ListView<HorarioInfo> listView = new ListView<HorarioInfo>("cell", listModel) {
            @Override
            protected void populateItem(ListItem<HorarioInfo> listItem) {
                listItem.setOutputMarkupId(true);
                HorarioInfo info = listItem.getModelObject();
                if (info.getOcupado()) {
                    listItem.add(new AttributeAppender("class", " ms10 column ocupado segment ui center"));
                    listItem.add(new AttributeModifier("onclick", ""));
                } else {
                    listItem.add(new AttributeAppender("class", " clickeable ms10 column segment ui center"));
                    listItem.add(new AjaxEventBehavior("onclick") {
                        @Override
                        protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
                            sessionData.setHorarioInfo(listItem.getModelObject());
                            callback.onSelected(ajaxRequestTarget);
                        }
                    });
                }
                listItem.add(new Label("horario", String.format("%02d:%02d", info.getHora(), info.getMinutos())));
            }
        };
        container.add(listView);
    }


}
