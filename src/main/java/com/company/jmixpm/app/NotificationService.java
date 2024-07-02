package com.company.jmixpm.app;


import com.company.jmixpm.entity.Notification;
import io.jmix.core.UnconstrainedDataManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NotificationService {

    @PersistenceContext
    private EntityManager entityManager;

    private UnconstrainedDataManager unconstrainedDataManager;

    public NotificationService(UnconstrainedDataManager unconstrainedDataManager) {
        this.unconstrainedDataManager = unconstrainedDataManager;
    }

    public void markAsReadWithUnconstrainedDM(Notification notification) {
        notification = unconstrainedDataManager.load(Notification.class)
                .id(notification.getId())
                .one();

        notification.setIsRead(true);

        unconstrainedDataManager.save(notification);
    }

    @Transactional
    public void markAsReadWithEntityManager(Notification notification) {
        notification = entityManager.find(Notification.class, notification.getId());
        notification.setIsRead(true);
    }
}