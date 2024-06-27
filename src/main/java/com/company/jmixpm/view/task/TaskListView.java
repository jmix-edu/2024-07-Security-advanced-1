package com.company.jmixpm.view.task;

import com.company.jmixpm.entity.Task;

import com.company.jmixpm.view.main.MainView;

import com.company.jmixpm.view.timeentry.quickaddtimeentry.QuickAddTimeEntryDialog;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "tasks", layout = MainView.class)
@ViewController("Task_.list")
@ViewDescriptor("task-list-view.xml")
@LookupComponent("tasksDataGrid")
@DialogMode(width = "64em")
public class TaskListView extends StandardListView<Task> {
    @ViewComponent
    private DataGrid<Task> tasksDataGrid;
    @Autowired
    private DialogWindows dialogWindows;

    @Subscribe("tasksDataGrid.addSpentTime")
    public void onTasksTableAddSpentTime(ActionPerformedEvent event) {
        Task task = tasksDataGrid.getSingleSelectedItem();
        if (task == null) {
            return;
        }
        dialogWindows.view(this, QuickAddTimeEntryDialog.class)
                .withViewConfigurer(dialog -> dialog.setTask(task))
                .open();
    }
}