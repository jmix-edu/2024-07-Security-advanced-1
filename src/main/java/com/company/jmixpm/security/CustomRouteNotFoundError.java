package com.company.jmixpm.security;

import com.company.jmixpm.view.main.MainView;
import com.vaadin.flow.router.*;
import jakarta.servlet.http.HttpServletResponse;

//@ParentLayout(MainView.class)
public class CustomRouteNotFoundError extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {
        getElement().setText("My custom not found class!");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
