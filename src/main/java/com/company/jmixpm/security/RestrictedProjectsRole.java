package com.company.jmixpm.security;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.User;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.security.model.RowLevelBiPredicate;
import io.jmix.security.model.RowLevelPolicyAction;
import io.jmix.security.role.annotation.PredicateRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;
import org.springframework.context.ApplicationContext;

@RowLevelRole(name = "Restrict projects for modification", code = RestrictedProjectsRole.CODE)
public interface RestrictedProjectsRole {
    String CODE = "restricted-projects";

    @PredicateRowLevelPolicy(entityClass = Project.class,
            actions = {RowLevelPolicyAction.UPDATE, RowLevelPolicyAction.DELETE}
    )
    default RowLevelBiPredicate<Project, ApplicationContext> allowOnlyManagerUpdateOrDeleteProject() {
        return (project, applicationContext) -> {
            User currentUser = (User) applicationContext.getBean(CurrentAuthentication.class).getUser();
            return currentUser.equals(project.getManager());
        };
    }
}
