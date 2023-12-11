package com.epam.xstack.utils;

import com.epam.xstack.model.Training;
import com.epam.xstack.model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AppHealthIndicator implements HealthIndicator {
    private final SessionFactory sessionFactory;

    @Override
    public Health health() {
        int errorCode = checkDbConnection() * checkMemoryState() * checkDiskSpace();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }

        return Health.up().build();
    }

    private int checkDiskSpace() {
        File cDrive = new File("C:\\");
        long freeSpace = cDrive.getFreeSpace();
        long threshold = 1024 * 1024 * 100;
        return freeSpace >= threshold ? 1 : 0;
    }

    private int checkMemoryState() {
        Runtime runtime = Runtime.getRuntime();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        long threshold = 1024 * 1024 * 500;
        return memoryUsed < threshold ? 1 : 0;
    }

    private int checkDbConnection() {
        try (Session session = sessionFactory.openSession()) {
            var cb = session.getCriteriaBuilder();
            var cq = cb.createQuery(User.class);
            var root = cq.from(User.class);
            session.createQuery(cq);
            return 1;
        } catch (HibernateException e) {
            return 0;
        }
    }
}
