package com.company.jmixpm.security;

import com.company.jmixpm.entity.Task;
import com.company.jmixpm.entity.TimeEntry;
import com.company.jmixpm.entity.User;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.security.model.RowLevelBiPredicate;
import io.jmix.security.model.RowLevelPolicyAction;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.PredicateRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;
import org.springframework.context.ApplicationContext;

@RowLevelRole(name = "Restrictions for developer", code = DeveloperRowLevelRole.CODE)
public interface DeveloperRowLevelRole {
    String CODE = "developer-restrictions";

    @JpqlRowLevelPolicy(entityClass = Task.class,
            where = "{E}.assignee.id = :current_user_id"
    )
    void restrictTasks();

    @JpqlRowLevelPolicy(entityClass = TimeEntry.class,
            where = "{E}.assignee.id = :current_user_id"
    )
    void restrictTimeEntries();

    @PredicateRowLevelPolicy(entityClass = TimeEntry.class,
            actions = {RowLevelPolicyAction.CREATE, RowLevelPolicyAction.UPDATE, RowLevelPolicyAction.DELETE}
    )
    default RowLevelBiPredicate<TimeEntry, ApplicationContext> allowOnlyManagerUpdateOrDeleteProject() {
        return (entry, applicationContext) -> {
            User currentUser = (User) applicationContext.getBean(CurrentAuthentication.class).getUser();
            return currentUser.equals(entry.getAssignee());
        };
    }
}
