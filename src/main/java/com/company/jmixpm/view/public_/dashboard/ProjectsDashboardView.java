package com.company.jmixpm.view.public_.dashboard;

import com.company.jmixpm.app.DashboardService;
import com.company.jmixpm.entity.dashboard.DashboardProject;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@AnonymousAllowed
@Route(value = "projects-dashboard")
@ViewController("ProjectsDashboardView")
@ViewDescriptor("projects-dashboard.xml")
public class ProjectsDashboardView extends StandardView {
    @ViewComponent
    private Div managerCardsContainer;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private MessageBundle messageBundle;

    @Install(to = "dashboardProjectsDl", target = Target.DATA_LOADER)
    private List<DashboardProject> dashboardProjectsDlLoadDelegate(final LoadContext<DashboardProject> loadContext) {
        return dashboardService.fetchProjects();
    }

    @Subscribe(id = "dashboardProjectsDc", target = Target.DATA_CONTAINER)
    public void onDashboardProjectsDcCollectionChange(final CollectionContainer.CollectionChangeEvent<DashboardProject> event) {
        updateManagerCards(event.getSource().getItems());
    }

    private void updateManagerCards(List<DashboardProject> projects) {
        managerCardsContainer.removeAll();

        List<Component> components = projects.stream().map(p -> {
            VerticalLayout card = uiComponents.create(VerticalLayout.class);
            card.addClassName("dashboard-manager-card");
            card.setWidth("20em");
            card.setHeight("20em");

            Image image = new Image();
            image.addClassName(LumoUtility.AlignSelf.CENTER);
            image.setWidth("12em");
            image.setHeight("12em");
            image.setSrc(String.format("public/images/%s.png", p.getManagerUsername()));
            card.add(image);

            Span firstNameSpan = uiComponents.create(Span.class);
            firstNameSpan.setText(messageBundle.formatMessage("firstName", p.getManagerFirstName()));
            card.add(firstNameSpan);
            Span lastNameSpan = uiComponents.create(Span.class);
            lastNameSpan.setText(messageBundle.formatMessage("lastName", p.getManagerLastName()));
            card.add(lastNameSpan);

            Span efficiencySpan = uiComponents.create(Span.class);
            efficiencySpan.setText(p.getEfficiency() + "%");
            card.add(efficiencySpan);

            return card;
        }).collect(Collectors.toList());

        managerCardsContainer.add(components);
    }
}