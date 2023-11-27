package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Role;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.LoginScene;
import view.Window;

import java.util.List;

import static database.Constants.Roles.*;

public class LoginController {

    private final LoginScene loginScene;
    private final AuthenticationService authenticationService;
    private final Window window;


    public LoginController(LoginScene loginScene, AuthenticationService authenticationService,
                           Window window) {
        this.window = window;
        this.loginScene = loginScene;
        this.authenticationService = authenticationService;

        this.loginScene.addLoginButtonListener(new LoginButtonListener());
        this.loginScene.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginScene.getUsername();
            String password = loginScene.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()) {
                loginScene.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                loginScene.setActionTargetText("LogIn Successful!");
                User user = loginNotification.getResult();
                window.setActiveUser(user);
                List<Role> roles = user.getRoles();
                List<String> rolesTitle = roles.stream().map(Role::getRole).toList();

                if (rolesTitle.contains(ADMINISTRATOR)) {
                    window.setScene(3);
                } else if (rolesTitle.contains(EMPLOYEE)) {
                    window.setScene(2);
                } else {
                    window.setScene(1);
                }
            }

        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginScene.getUsername();
            String password = loginScene.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginScene.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginScene.setActionTargetText("Register successful!");
            }
        }
    }
}