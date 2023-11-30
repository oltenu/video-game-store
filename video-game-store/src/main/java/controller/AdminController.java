package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Order;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import service.game.VideoGameService;
import service.order.OrderService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminScene;
import view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static database.Constants.Roles.*;

public class AdminController extends EmployeeController {
    private final Window window;
    private final AdminScene adminScene;
    private final VideoGameService videoGameService;
    private final OrderService orderService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AdminController(Window window, AdminScene adminScene, VideoGameService videoGameService,
                           OrderService orderService, UserService userService, AuthenticationService authenticationService) {
        super(window, adminScene, authenticationService, videoGameService, orderService, userService);

        this.window = window;
        this.adminScene = adminScene;
        this.videoGameService = videoGameService;
        this.orderService = orderService;
        this.userService = userService;
        this.authenticationService = authenticationService;

        this.adminScene.addUsersItemListener(new UsersItemListener());
        this.adminScene.addEmployeesReportItemListener(new EmployeesReportItemListener());
        this.adminScene.addUserListener(new AddUserListener());
        this.adminScene.addUpdateUserListener(new UpdateUserListener());
        this.adminScene.addDeleteUserListener(new DeleteUserListener());
        this.adminScene.addGenerateEmployeesReportListener(new GenerateEmployeesReportListener());
        this.adminScene.addEmployeeChoiceListener(new EmployeeChoiceListener());
    }

    private class UsersItemListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            adminScene.refreshCrudUsersPanel(userService.findAll(), Arrays.asList(ROLES));
        }
    }

    private class EmployeesReportItemListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<User> employees = userService.findByRole(EMPLOYEE);
            List<User> admins = userService.findByRole(ADMINISTRATOR);
            employees.addAll(admins);
            List<String> employeesString = new ArrayList<>();
            employees.forEach(employee -> employeesString.add(
                    employee.getId() + ": " + employee.getUsername()));

            adminScene.refreshEmployeesReportPanel(employeesString);
        }
    }

    private class AddUserListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = adminScene.getUsernameField();
            String password = adminScene.getPasswordField();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                adminScene.setUsersText(registerNotification.getFormattedErrors());
            } else {
                adminScene.setUsersText("Added new user!");
            }

            adminScene.refreshCrudUsersPanel(userService.findAll(), Arrays.asList(ROLES));
        }
    }

    private class UpdateUserListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            Long id = adminScene.getUserId();
            String username = adminScene.getUsernameField();
            String password = adminScene.getPasswordField();
            Double money = Double.valueOf(adminScene.getMoneyField());
            String role = adminScene.getSelectedRole();
            List<Role> roles = new ArrayList<>();
            roles.add(userService.findRoleByTitle(role));

            User user = new UserBuilder()
                    .setId(id)
                    .setUsername(username)
                    .setPassword(password)
                    .setMoney(money)
                    .setRoles(roles)
                    .build();

            userService.update(user);

            adminScene.refreshCrudUsersPanel(userService.findAll(), Arrays.asList(ROLES));
        }
    }

    private class DeleteUserListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            Long id = adminScene.getUserId();

            userService.deleteById(id);

            adminScene.setUsersText("User deleted!");
            adminScene.refreshCrudUsersPanel(userService.findAll(), Arrays.asList(ROLES));
        }
    }

    private class GenerateEmployeesReportListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            //ToDo
        }
    }

    private class EmployeeChoiceListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String selectedEmployee = adminScene.getSelectedEmployee();
            Long employeeId = Long.parseLong(String.valueOf(selectedEmployee.charAt(0)));

            List<Order> orders = orderService.findAllEmployeeSales(employeeId);

            adminScene.setOrdersTable(orders);
        }
    }
}
