package com.company.jmixpm.view.timeentry;

import com.company.jmixpm.entity.TimeEntry;

import com.company.jmixpm.entity.User;
import com.company.jmixpm.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "timeEntries/:id", layout = MainView.class)
@ViewController("TimeEntry.detail")
@ViewDescriptor("time-entry-detail-view.xml")
@EditedEntityContainer("timeEntryDc")
public class TimeEntryDetailView extends StandardDetailView<TimeEntry> {
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private CurrentUserSubstitution currentUserSubstitution;

    @Subscribe
    public void onInitEntity(final InitEntityEvent<TimeEntry> event) {
        //final User user = (User) currentAuthentication.getUser(); admin assigned
        final User user = (User) currentUserSubstitution.getEffectiveUser();
        event.getEntity().setAssignee(user);
    }
    
    
}