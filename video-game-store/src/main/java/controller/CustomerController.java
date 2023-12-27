package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Role;
import model.User;
import model.validator.Notification;
import service.game.VideoGameService;
import service.order.OrderService;
import service.user.UserService;
import view.CustomerScene;
import view.Window;

import java.util.regex.Pattern;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class CustomerController {
    private final Window window;
    private final CustomerScene customerScene;
    private final VideoGameService videoGameService;
    private final OrderService orderService;
    private final UserService userService;

    public CustomerController(Window window, CustomerScene customerScene, VideoGameService videoGameService,
                              OrderService orderService, UserService userService) {
        this.window = window;
        this.userService = userService;
        this.customerScene = customerScene;
        this.videoGameService = videoGameService;
        this.orderService = orderService;

        this.customerScene.addLogOutItemListener(new LogOutButtonListener());
        this.customerScene.addBuyMenuButtonListener(new BuyMenuButtonListener());
        this.customerScene.addBuyButtonListener(new BuyButtonListener());
        this.customerScene.addCustomerOrdersItemListener(new CustomerOrdersItemListener());
        this.customerScene.addHomeItemListener(new HomeItemListener());
    }

    public void setUserData() {
        User user = userService.findById(window.getActiveUser().getId());

        String role = "Customer";
        if (user.getRoles().stream().map(Role::getRole).toList().contains(ADMINISTRATOR)) {
            role = "Administrator";
        } else if (user.getRoles().stream().map(Role::getRole).toList().contains(EMPLOYEE)) {
            role = "Employee";
        }

        customerScene.setUserData(user.getUsername(), user.getMoney().toString(), role);
    }

    private class HomeItemListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            setUserData();
            customerScene.refreshHomePane();
        }
    }

    private class LogOutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            window.setActiveUser(null);
            customerScene.clearPane();

            window.setScene(0);
        }
    }

    private class BuyMenuButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            customerScene.refreshGamePane(videoGameService.findAll());
        }
    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            Notification<Boolean> resultNotification = new Notification<>();
            Notification<Boolean> dataBaseNotification = new Notification<>();

            String selectedGame = customerScene.getSelectedGame();
            String amountString = customerScene.getAmount();
            int amount;

            if (selectedGame == null) {
                resultNotification.addError("No game selected!");
                resultNotification.setResult(Boolean.FALSE);
            }
            if (Pattern.matches("^[1-9]\\d*$", amountString)) {
                amount = Integer.parseInt(amountString);

                if (!resultNotification.hasErrors()) {
                    Long gameId = Long.valueOf(customerScene.getSelectedGame());
                    dataBaseNotification = orderService.buyGame(window.getActiveUser().getId(), gameId, amount);
                }
            } else {
                resultNotification.addError("Invalid amount!");
                resultNotification.setResult(Boolean.FALSE);
            }

            if (resultNotification.hasErrors() || dataBaseNotification.hasErrors()) {
                customerScene.setBuyResult(resultNotification.getFormattedErrors() +
                        dataBaseNotification.getFormattedErrors(), false);
                customerScene.refreshGamePane(videoGameService.findAll());
            } else {
                customerScene.setBuyResult("Purchased successfully!", true);
                customerScene.refreshGamePane(videoGameService.findAll());
            }
        }
    }

    private class CustomerOrdersItemListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            customerScene.refreshOrderPane(orderService.findAllCustomerOrders(window.getActiveUser().getId()));
        }
    }
}
