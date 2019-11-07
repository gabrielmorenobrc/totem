package totem.webapp.commons;

import org.apache.wicket.AttributeModifier;

import java.io.Serializable;

public class ClassRemover extends AttributeModifier {

    public ClassRemover(Serializable value) {
        super("class", value);
    }

    @Override
    protected String newValue(String currentValue, String replacementValue) {
        return currentValue.replaceAll(replacementValue, "");
    }
}
