package com.company.jmixpm.app;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.Task;
import com.company.jmixpm.entity.dashboard.DashboardProject;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final DataManager dataManager;

    public DashboardService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<DashboardProject> fetchProjects() {
        List<Project> projects = dataManager.load(Project.class)
                .all()
                .fetchPlan(builder -> builder.addFetchPlan(FetchPlan.BASE)
                        .add("manager", FetchPlan.BASE)
                        .add("tasks", tasksBuilder -> tasksBuilder.addFetchPlan(FetchPlan.BASE)))
                .list();

        List<DashboardProject> dProjects = projects.stream()
                .map(project -> {
                    DashboardProject dProject = dataManager.create(DashboardProject.class);
                    dProject.setId(project.getId());
                    dProject.setName(project.getName());
                    dProject.setManagerUsername(project.getManager().getUsername());
                    dProject.setManagerFirstName(project.getManager().getFirstName());
                    dProject.setManagerLastName(project.getManager().getLastName());
                    dProject.setEfficiency(RandomUtils.nextInt(5, 100));

                    List<Task> tasks = project.getTasks();
                    dProject.setTaskCount(tasks == null ? 0 : tasks.size());

                    if (tasks != null) {
                        Integer plannedEfforts = tasks.stream()
                                .map(t -> t.getEstimatedEfforts() == null ? 0 : t.getEstimatedEfforts())
                                .reduce(0, Integer::sum);
                        dProject.setPlannedEfforts(plannedEfforts);
                    }

                    dProject.setActualEfforts(getActualEfforts(project.getId()));
                    dProject.setCompleteness(calculateCompleteness(tasks));
                    return dProject;
                }).collect(Collectors.toList());
        return dProjects;
    }

    private Integer getActualEfforts(UUID projectId) {
        return dataManager.loadValue("select sum(te.timeSpent) from TimeEntry te " +
                        "where te.task.project.id = :projectId", Integer.class)
                .parameter("projectId", projectId)
                .optional().orElse(0);
    }

    private Float calculateCompleteness(List<Task> tasks) {
        if (CollectionUtils.isEmpty(tasks)) {
            return 100f;
        }
        float closedTasks = tasks.stream()
                .filter(Task::getClosed)
                .toList()
                .size();
        return (closedTasks / tasks.size()) * 100;
    }
}
