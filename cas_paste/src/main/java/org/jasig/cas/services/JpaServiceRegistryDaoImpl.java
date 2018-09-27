package org.jasig.cas.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 3:16 PM.
 */
public final class JpaServiceRegistryDaoImpl implements ServiceRegistryDao {
    @NotNull
    @PersistenceContext
    private EntityManager entityManager;

    public JpaServiceRegistryDaoImpl() {
    }

    @Override
    public boolean delete(RegisteredService registeredService) {
        if (this.entityManager.contains(registeredService)) {
            this.entityManager.remove(registeredService);
        } else {
            this.entityManager.remove(this.entityManager.merge(registeredService));
        }

        return true;
    }

    @Override
    public List<RegisteredService> load() {
        return this.entityManager.createQuery("select r from AbstractRegisteredService r", RegisteredService.class).getResultList();
    }

    @Override
    public RegisteredService save(RegisteredService registeredService) {
        boolean isNew = registeredService.getId() == -9223372036854775807L;
        RegisteredService r = (RegisteredService)this.entityManager.merge(registeredService);
        if (!isNew) {
            this.entityManager.persist(r);
        }

        return r;
    }

    @Override
    public RegisteredService findServiceById(long id) {
        return (RegisteredService)this.entityManager.find(AbstractRegisteredService.class, id);
    }
}

