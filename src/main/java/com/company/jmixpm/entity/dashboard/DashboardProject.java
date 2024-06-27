package com.company.jmixpm.entity.dashboard;

import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.UUID;

@JmixEntity
public class DashboardProject {
    @JmixId
    private UUID id;

    @InstanceName
    private String name;

    private Float completeness;

    private Integer taskCount;

    private Integer plannedEfforts;

    private Integer actualEfforts;

    private String managerFirstName;

    private String managerLastName;

    private String managerUsername;

    private Integer efficiency;

    public void setEfficiency(Integer efficiency) {
        this.efficiency = efficiency;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public String getManagerLastName() {
        return managerLastName;
    }

    public void setManagerLastName(String managerLastName) {
        this.managerLastName = managerLastName;
    }

    public String getManagerFirstName() {
        return managerFirstName;
    }

    public void setManagerFirstName(String managerFirstName) {
        this.managerFirstName = managerFirstName;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public Integer getActualEfforts() {
        return actualEfforts;
    }

    public void setActualEfforts(Integer actualEfforts) {
        this.actualEfforts = actualEfforts;
    }

    public void setCompleteness(Float completeness) {
        this.completeness = completeness;
    }

    public Float getCompleteness() {
        return completeness;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public Integer getPlannedEfforts() {
        return plannedEfforts;
    }

    public void setPlannedEfforts(Integer plannedEfforts) {
        this.plannedEfforts = plannedEfforts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}