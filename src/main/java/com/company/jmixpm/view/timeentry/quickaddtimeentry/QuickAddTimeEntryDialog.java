package com.company.jmixpm.view.timeentry.quickaddtimeentry;


import com.company.jmixpm.entity.Task;
import com.company.jmixpm.entity.TimeEntry;
import com.company.jmixpm.entity.User;
import com.company.jmixpm.view.main.MainView;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "quick-add-time-entry-dialog", layout = MainView.class)
@ViewController("QuickAddTimeEntry")
@ViewDescriptor("quick-add-time-entry-dialog.xml")
@DialogMode(width = "30em")
public class QuickAddTimeEntryDialog extends StandardView {
    @ViewComponent
    private InstanceContainer<TimeEntry> timeEntryDc;
    @ViewComponent
    private FormLayout timeEntryForm;
    @ViewComponent
    private DataContext dataContext;

    @Autowired
    private DataManager dataManager;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private ViewValidation viewValidation;
    @Autowired
    private Notifications notifications;
    @Autowired
    private MessageBundle messageBundle;

    private Task task;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        User user = (User) currentAuthentication.getUser();
        user = dataManager.load(User.class).id(user.getId()).one();

        TimeEntry entry = dataContext.create(TimeEntry.class);
        entry.setTask(task);
        entry.setAssignee(user);
        timeEntryDc.setItem(entry);
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Subscribe("saveBtn")
    public void onSaveBtnClick(ClickEvent<JmixButton> event) {
        ValidationErrors validationErrors = viewValidation.validateUiComponents(timeEntryForm);
        if (!validationErrors.isEmpty()) {
            viewValidation.showValidationErrors(validationErrors);
            return;
        }
        dataContext.save();
        close(StandardOutcome.SAVE);
        notifications.create(messageBundle.getMessage("timeEntrySaved"))
                .withPosition(Notification.Position.TOP_END)
                .withType(Notifications.Type.SUCCESS)
                .show();
    }
}