package totem.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.cdi.ConversationPropagation;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.serialize.ISerializer;
import totem.webapp.app.TotemPage;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

public class TotemWebApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();
        BeanManager beanManager = CDI.current().getBeanManager();
        new CdiConfiguration(beanManager).setPropagation(ConversationPropagation.NONE).configure(this);
        getFrameworkSettings().setSerializer(new ISerializer() {
            @Override
            public byte[] serialize(Object o) {
                return new byte[0];
            }

            @Override
            public Object deserialize(byte[] bytes) {
                return null;
            }
        });
        getPageSettings().setVersionPagesByDefault(false);
   }

    @Override
    public RuntimeConfigurationType getConfigurationType() {
        RuntimeConfigurationType result = RuntimeConfigurationType.DEPLOYMENT;
        if (isDevelopmentEnvironment()) {
            result = RuntimeConfigurationType.DEVELOPMENT;
        }
        return result;
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return TotemPage.class;
    }

    private boolean isDevelopmentEnvironment() {
        String development = System.getProperty("development");
        return Boolean.parseBoolean(development);
    }


}
