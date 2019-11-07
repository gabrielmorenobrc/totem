package totem.service;

import plataforma1.services.PU;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

public class TotemEntityManagerProducer {

    @Inject
    private TotemConfig totemConfig;
    private static EntityManagerFactory emf;

    private void checkEMF() {
        if (emf == null) {
            Properties properties = totemConfig.getPersistenceProperties();
            if (properties == null) {
                emf = Persistence.createEntityManagerFactory(TotemConstants.PU_NAME);
            } else {
                emf = Persistence.createEntityManagerFactory(TotemConstants.PU_NAME, properties);
            }
        }
    }

    @Produces
    @PU(puName = TotemConstants.PU_NAME)
    @RequestScoped
    EntityManager createEntityManager() {
        checkEMF();
        return emf.createEntityManager();
    }

    void close(@Disposes @PU(puName = TotemConstants.PU_NAME) EntityManager entityManager) {
        entityManager.close();
    }
}
