package org.jasig.cas.ticket.registry.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

/**
 * @author SxL
 * Created on 9/25/2018 4:56 PM.
 */
public class JpaLockingStrategy implements LockingStrategy {
    public static final int DEFAULT_LOCK_TIMEOUT = 3600;
    @NotNull
    @PersistenceContext
    protected EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private String applicationId;
    @NotNull
    private String uniqueId;
    private int lockTimeout = 3600;

    public JpaLockingStrategy() {
    }

    public void setApplicationId(String id) {
        this.applicationId = id;
    }

    public void setUniqueId(String id) {
        this.uniqueId = id;
    }

    public void setLockTimeout(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Lock timeout must be non-negative.");
        } else {
            this.lockTimeout = seconds;
        }
    }

    @Transactional(
            readOnly = false
    )
    @Override
    public boolean acquire() {
        JpaLockingStrategy.Lock lock;
        try {
            lock = (JpaLockingStrategy.Lock)this.entityManager.find(JpaLockingStrategy.Lock.class, this.applicationId, LockModeType.PESSIMISTIC_WRITE);
        } catch (PersistenceException var4) {
            this.logger.debug("{} failed querying for {} lock.", new Object[]{this.uniqueId, this.applicationId, var4});
            return false;
        }

        boolean result = false;
        if (lock != null) {
            Date expDate = lock.getExpirationDate();
            if (lock.getUniqueId() == null) {
                this.logger.debug("{} trying to acquire {} lock.", this.uniqueId, this.applicationId);
                result = this.acquire(this.entityManager, lock);
            } else if (expDate != null && (new Date()).after(expDate)) {
                this.logger.debug("{} trying to acquire expired {} lock.", this.uniqueId, this.applicationId);
                result = this.acquire(this.entityManager, lock);
            }
        } else {
            this.logger.debug("Creating {} lock initially held by {}.", this.applicationId, this.uniqueId);
            result = this.acquire(this.entityManager, new JpaLockingStrategy.Lock());
        }

        return result;
    }

    @Transactional(
            readOnly = false
    )
    @Override
    public void release() {
        JpaLockingStrategy.Lock lock = (JpaLockingStrategy.Lock)this.entityManager.find(JpaLockingStrategy.Lock.class, this.applicationId, LockModeType.PESSIMISTIC_WRITE);
        if (lock != null) {
            String owner = lock.getUniqueId();
            if (this.uniqueId.equals(owner)) {
                lock.setUniqueId((String)null);
                lock.setExpirationDate((Date)null);
                this.logger.debug("Releasing {} lock held by {}.", this.applicationId, this.uniqueId);
                this.entityManager.persist(lock);
            } else {
                throw new IllegalStateException("Cannot release lock owned by " + owner);
            }
        }
    }

    @Transactional(
            readOnly = true
    )
    public String getOwner() {
        JpaLockingStrategy.Lock lock = (JpaLockingStrategy.Lock)this.entityManager.find(JpaLockingStrategy.Lock.class, this.applicationId);
        return lock != null ? lock.getUniqueId() : null;
    }

    @Override
    public String toString() {
        return this.uniqueId;
    }

    private boolean acquire(EntityManager em, JpaLockingStrategy.Lock lock) {
        lock.setUniqueId(this.uniqueId);
        if (this.lockTimeout > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(13, this.lockTimeout);
            lock.setExpirationDate(cal.getTime());
        } else {
            lock.setExpirationDate((Date)null);
        }

        boolean success = false;

        try {
            if (lock.getApplicationId() != null) {
                em.merge(lock);
            } else {
                lock.setApplicationId(this.applicationId);
                em.persist(lock);
            }

            success = true;
        } catch (PersistenceException var5) {
            success = false;
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("{} could not obtain {} lock.", new Object[]{this.uniqueId, this.applicationId, var5});
            } else {
                this.logger.info("{} could not obtain {} lock.", this.uniqueId, this.applicationId);
            }
        }

        return success;
    }

    @Entity
    @Table(
            name = "locks"
    )
    public static class Lock {
        @Id
        @Column(
                name = "application_id"
        )
        private String applicationId;
        @Column(
                name = "unique_id"
        )
        private String uniqueId;
        @Temporal(TemporalType.TIMESTAMP)
        @Column(
                name = "expiration_date"
        )
        private Date expirationDate;

        public Lock() {
        }

        public String getApplicationId() {
            return this.applicationId;
        }

        public void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }

        public String getUniqueId() {
            return this.uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public Date getExpirationDate() {
            return this.expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }
    }
}

