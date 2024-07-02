package com.company.jmixpm.view.public_.register;

import com.company.jmixpm.app.RegistrationService;
import com.company.jmixpm.entity.User;

import com.company.jmixpm.view.login.LoginView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.jmix.email.EmailException;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route(value = "register")
@ViewController("UserRegistrationView")
@ViewDescriptor("user-registration-view.xml")
public class UserRegistrationView extends StandardView {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationView.class);

    @Autowired
    private ViewValidation viewValidation;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private Notifications notifications;

    @ViewComponent
    private FormLayout form;
    @ViewComponent
    private TypedTextField<String> emailField;
    @ViewComponent
    private TypedTextField<String> firstNameField;
    @ViewComponent
    private TypedTextField<String> lastNameField;
    @ViewComponent
    private JmixButton registerBtn;
    @Autowired
    private ViewNavigators viewNavigators;

    @Subscribe(id = "registerBtn", subject = "clickListener")
    public void onRegisterBtnClick(final ClickEvent<JmixButton> event) {
        if (!validateFields()) {
            return;
        }

        User user = registrationService.registerNewUser(emailField.getValue(), firstNameField.getValue(), lastNameField.getValue());

        String activationToken = registrationService.generateRandomActivationToken();

        registrationService.saveActivationToken(user, activationToken);

        try {
            registrationService.sendActivationEmail(user);
        } catch (EmailException e) {
            log.error("Error", e);
            notifications.create("Failed to send registration email. Sorry for inconvenience.")
                    .withType(Notifications.Type.ERROR)
                    .show();
            return;
        }

        notifications.show("User registered successfully. Check your email inbox.");

        form.setEnabled(false);
        registerBtn.setEnabled(false);
    }

    @Subscribe(id = "backToLoginBtn", subject = "clickListener")
    public void onBackToLoginBtnClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(LoginView.class)
                .navigate();
    }

    private boolean validateFields() {
        ValidationErrors validationErrors = viewValidation.validateUiComponents(getContent());
        if (!validationErrors.isEmpty()) {
            viewValidation.showValidationErrors(validationErrors);
            return false;
        }

        String email = emailField.getValue();

        if (registrationService.checkUserAlreadyExist(email)) {
            notifications.create("User with this email already exists")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return false;
        }

        return true;
    }
}