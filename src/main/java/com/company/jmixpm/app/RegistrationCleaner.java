package com.company.jmixpm.app;

import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.DateTimeTransformations;
import io.jmix.core.TimeSource;
import io.jmix.core.security.SystemAuthenticator;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

public class RegistrationCleaner  implements Job {
    private static final Logger log = LoggerFactory.getLogger(RegistrationCleaner.class);

    private DataManager dataManager;
    private TimeSource timeSource;
    private DateTimeTransformations dateTimeTransformations;
    private SystemAuthenticator systemAuthenticator;

    public RegistrationCleaner(DataManager dataManager,
                               TimeSource timeSource,
                               DateTimeTransformations dateTimeTransformations, SystemAuthenticator systemAuthenticator) {
        this.dataManager = dataManager;
        this.timeSource = timeSource;
        this.dateTimeTransformations = dateTimeTransformations;
        this.systemAuthenticator = systemAuthenticator;
    }

    public String deleteOldNotActivatedUsers() {
        Date lastWeekDate = DateUtils.addDays(timeSource.currentTimestamp(), -7);
        Object threshold = dateTimeTransformations.transformToType(lastWeekDate, OffsetDateTime.class, null);

        List<User> oldUsers = dataManager.load(User.class)
                .query("select u from User u where u.createdDate < :threshold and u.needsActivation = true")
                .parameter("threshold", threshold)
                .list();
        for (User u: oldUsers) {
            dataManager.remove(u);
            log.info("Removing old not activated user " + u.getUsername());
        }
        return "Deleted " + oldUsers.size() + " users";
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String cleaningResult = systemAuthenticator.withSystem(this::deleteOldNotActivatedUsers);
        log.info("Scheduler result " + cleaningResult);
    }
}