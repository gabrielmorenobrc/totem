package totem.webapp.app;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;

public class GuiaTramitesPanel extends TotemPanel {

    public GuiaTramitesPanel(String id) {
        super(id);
        WebComponent iframe = new WebComponent("iframe");
        add(iframe);
        iframe.add(new AttributeModifier("src", "http://domo.nupatec.ml:8037/guiatramites2/"));
    }


}
