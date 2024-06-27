package com.company.jmixpm.view.notification;

import com.company.jmixpm.entity.Notification;
import com.company.jmixpm.entity.User;
import com.company.jmixpm.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.*;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "notifications", layout = MainView.class)
@ViewController("Notification.list")
@ViewDescriptor("notification-list-view.xml")
@LookupComponent("notificationsDataGrid")
@DialogMode(width = "64em")
public class NotificationListView extends StandardListView<Notification> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<Notification> notificationsDc;

    @ViewComponent
    private InstanceContainer<Notification> notificationDc;

    @ViewComponent
    private InstanceLoader<Notification> notificationDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe
    public void onInit(final InitEvent event) {
        updateControls(false);
    }

    @Subscribe("notificationsDataGrid.create")
    public void onNotificationsDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        Notification entity = dataContext.create(Notification.class);
        entity.setSender((User) currentAuthentication.getUser());
        notificationDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("notificationsDataGrid.edit")
    public void onNotificationsDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveBtn")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.save();
        notificationsDc.replaceItem(notificationDc.getItem());
        updateControls(false);
    }

    @Subscribe("cancelBtn")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        notificationDl.load();
        updateControls(false);
    }

    @Subscribe(id = "notificationsDc", target = Target.DATA_CONTAINER)
    public void onNotificationsDcItemChange(final InstanceContainer.ItemChangeEvent<Notification> event) {
        Notification entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            notificationDl.setEntityId(entity.getId());
            notificationDl.load();
        } else {
            notificationDl.setEntityId(null);
            notificationDc.setItem(null);
        }
    }

    private void updateControls(boolean editing) {
        form.getChildren().forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
    }
}