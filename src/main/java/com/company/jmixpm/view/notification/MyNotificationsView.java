package com.company.jmixpm.view.notification;


import com.company.jmixpm.entity.Notification;
import com.company.jmixpm.entity.User;
import com.company.jmixpm.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "my-notifications", layout = MainView.class)
@ViewController("MyNotifications")
@ViewDescriptor("my-notifications-view.xml")
public class MyNotificationsView extends StandardView {
    @ViewComponent
    private DataGrid<Notification> notificationsDataGrid;
    @ViewComponent
    private CollectionLoader<Notification> notificationsDl;

    @Autowired
    private DataManager dataManager;

    @Subscribe("notificationsDataGrid.markAsRead")
    public void onNotificationsTableMarkAsRead(ActionPerformedEvent event) {
        // take item from the table
        Notification item = notificationsDataGrid.getSingleSelectedItem();
        if (item == null) {
            return;
        }

        // set isRead
        item.setIsRead(true);
        // save changes
        dataManager.save(item);

        // reload the table
        notificationsDl.load();
    }
}