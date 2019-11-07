package totem.webapp.app;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.util.ListModel;
import totem.service.TotemService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ProgressBar extends Panel {
    @Inject
    private TotemService totemService;
    private WebMarkupContainer container;
    private ListModel<Paso> listModel;
    @Inject
    private SessionData sessionData;

    public ProgressBar(String id, String titulo) {
        super(id);
        setOutputMarkupId(true);
        addTitulo(titulo);
    }
    private void addTitulo(String _titulo){
        Label titulo = new Label("titulo",_titulo);
        container = new WebMarkupContainer("cantidadColumnas");
        container.setOutputMarkupId(true);
        container.add(new AttributeModifier("class", resolveColumnClass()));
        add(container);
        List<Paso> list = resolvePasos();
        listModel = new ListModel<>(list);
        ListView<Paso> listView = new ListView<Paso>("column", listModel) {
            @Override
            protected void populateItem(ListItem<Paso> listItem) {
                listItem.setOutputMarkupId(true);
                Paso paso = listItem.getModelObject();
                listItem.add(new Label("title", paso.getTitulo()));
            }
        };
        container.add(listView);
        add(titulo);
    }

    public List<Paso> resolvePasos(){
        ArrayList<Paso> list = new ArrayList<>();
        ArrayList<String> listTitles = new ArrayList<>();
        String tipoPaso = sessionData.getTipoPaso();
        if(tipoPaso == "anunciarseConTurno"){
            listTitles.add("Ingresar");
            listTitles.add("Turno");
            listTitles.add("Finalizar");
        } else if (tipoPaso == "anunciarseSinTurno"){
            listTitles.add("Ingresar");
            listTitles.add("Turno");
            listTitles.add("Categoría");
            listTitles.add("Subcategoría");
            listTitles.add("Trámite");
            listTitles.add("Finalizar");
        } else if (tipoPaso == "turnoOnline"){
            listTitles.add("Ingresar");
            listTitles.add("Categoría");
            listTitles.add("Subcategoría");
            listTitles.add("Trámite");
            listTitles.add("Lugar");
            listTitles.add("Día");
            listTitles.add("Horario");
            listTitles.add("Finalizar");
        }
        for (int i = 0; i < listTitles.size(); i++) {
            Paso info = new Paso();
            info.setTitulo(listTitles.get(i));
            list.add(info);
        }
        return list;
    }

    private String resolveColumnClass(){
        String styleClass = "";
        String tipoPaso = sessionData.getTipoPaso();
        if(tipoPaso == "anunciarseConTurno"){
            styleClass = "three column row paddTop0";
        } else if (tipoPaso == "anunciarseSinTurno"){
            styleClass = "six column row paddTop0";
        } else if (tipoPaso == "turnoOnline"){
            styleClass = "eight column row paddTop0";
        }
        return styleClass;
    }


}
