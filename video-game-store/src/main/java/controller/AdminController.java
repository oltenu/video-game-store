package controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.JoinedOrder;
import model.User;
import model.validator.Notification;
import service.game.VideoGameService;
import service.order.OrderService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminScene;
import view.Window;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static database.Constants.Roles.*;

public class AdminController extends EmployeeController {
    private final AdminScene adminScene;
    private final OrderService orderService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AdminController(Window window, AdminScene adminScene, VideoGameService videoGameService,
                           OrderService orderService, UserService userService, AuthenticationService authenticationService) {
        super(window, adminScene, videoGameService, orderService, userService);

        this.adminScene = adminScene;
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
            String money = adminScene.getMoneyField();
            String role = adminScene.getSelectedRole();

            if (password.isEmpty()) {
                adminScene.setUsersText("No password!", false);
            }

            Notification<Boolean> registerNotification = authenticationService.register(username, password, money,
                    role);

            if (registerNotification.hasErrors()) {
                adminScene.setUsersText(registerNotification.getFormattedErrors(), false);
            } else {
                adminScene.setUsersText("Added new user!", true);
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
            String money = adminScene.getMoneyField();
            String role = adminScene.getSelectedRole();

            if (id == null) {
                adminScene.setUsersText("No user selected!", false);
            }

            Notification<Boolean> resultNotification = userService.update(id, username, password, money, role);

            if (resultNotification.hasErrors()) {
                adminScene.setUsersText(resultNotification.getFormattedErrors(), false);
            } else {
                adminScene.setUsersText("User updated!", true);
            }

            adminScene.refreshCrudUsersPanel(userService.findAll(), Arrays.asList(ROLES));
        }
    }

    private class DeleteUserListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            Long id = adminScene.getUserId();

            userService.deleteById(id);

            adminScene.setUsersText("User deleted!", true);
            adminScene.refreshCrudUsersPanel(userService.findAll(), Arrays.asList(ROLES));
        }
    }

    private class GenerateEmployeesReportListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String selectedEmployee = adminScene.getSelectedEmployee();

            StringBuilder result = new StringBuilder();
            for (char c : selectedEmployee.toCharArray()) {
                if (Character.isDigit(c)) {
                    result.append(c);
                } else {
                    break;
                }
            }

            Long employeeId = Long.parseLong(String.valueOf(result));
            User employee = userService.findById(employeeId);
            String fileName = "src/main/resources/" + employee.getUsername() + "-employee-report.pdf";
            String titleUser = employee.getUsername() + " Sales\n\n";
            StringBuilder sales = new StringBuilder();
            List<JoinedOrder> employeeSales = orderService.findAllEmployeeSales(employeeId);

            int cnt = 1;
            for (JoinedOrder order : employeeSales) {
                sales.append(cnt++).append(": ").append("Game: ")
                        .append(order.getGameName()).append(" | Customer: ").append(order.getCustomerUsername())
                        .append(" | Amount: ").append(order.getAmount()).append(" | Total price: ")
                        .append(order.getTotalPrice()).append("\n");
            }

            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream(fileName));
                document.open();
                Font titleFont = new Font(Font.COURIER, 14, Font.BOLD);
                Font salesFont = new Font(Font.COURIER, 12);

                Chunk titleChunk = new Chunk(titleUser, titleFont);
                Chunk salesChunk = new Chunk(sales.toString(), salesFont);

                Paragraph paragraph = new Paragraph();
                paragraph.add(titleChunk);
                paragraph.add(salesChunk);

                document.add(paragraph);

                document.close();
            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();

                adminScene.setEmployeesText("Failed to generate report!", false);
            }

            adminScene.setEmployeesText("Report generated!", true);
        }
    }

    private class EmployeeChoiceListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String selectedEmployee = adminScene.getSelectedEmployee();

            StringBuilder result = new StringBuilder();
            for (char c : selectedEmployee.toCharArray()) {
                if (Character.isDigit(c)) {
                    result.append(c);
                } else {
                    break;
                }
            }

            Long employeeId = Long.parseLong(String.valueOf(result));

            List<JoinedOrder> orders = orderService.findAllEmployeeSales(employeeId);

            adminScene.setOrdersTable(orders);
        }
    }
}
