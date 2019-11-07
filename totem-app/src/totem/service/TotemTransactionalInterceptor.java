package totem.service;

import plataforma1.services.PU;
import plataforma1.services.Transactional;

import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.persistence.EntityManager;
import java.io.Serializable;

@Interceptor
@Transactional
public class TotemTransactionalInterceptor extends plataforma1.services.TransactionalInterceptor implements Serializable {

    @Inject
    @PU(puName = TotemConstants.PU_NAME)
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
