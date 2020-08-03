package totem.service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.Properties;

@ApplicationScoped
@Alternative
public class IdeTotemConfig implements TotemConfig {


    @Override
    public Properties getPersistenceProperties() {
        Properties prop = new Properties();
        prop.setProperty("javax.persistence.jdbc.url", "jdbc:postgresql://172.17.0.75:5432/totem");
        prop.setProperty("javax.persistence.jdbc.password", "tramites");
        prop.setProperty("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        prop.setProperty("javax.persistence.jdbc.user", "dbtramites.12");
        prop.setProperty("eclipselink.ddl-generation", "create-tables");
        //prop.setProperty("eclipselink.logging.level.sql", "FINE");
        //prop.setProperty("eclipselink.logging.parameters", "true");
        return prop;
    }

    @Override
    public String getErpBaseUrl() {
        return "http://servicios.externos.testing.gov.ar/UNSAM";
    }

    @Override
    public String getTurneroUriBase() {
        return "http://172.17.0.75:8089/turnero_ll/ws/totem";
    }

}
