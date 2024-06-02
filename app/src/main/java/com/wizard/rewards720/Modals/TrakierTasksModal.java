package com.wizard.rewards720.Modals;

public class TrakierTasksModal {

    String taskCount, taskName;
    boolean isTaskCompleted;

    public TrakierTasksModal(String taskCount, String taskName, boolean isTaskCompleted) {
        this.taskCount = taskCount;
        this.taskName = taskName;
        this.isTaskCompleted = isTaskCompleted;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isTaskCompleted() {
        return isTaskCompleted;
    }
}
