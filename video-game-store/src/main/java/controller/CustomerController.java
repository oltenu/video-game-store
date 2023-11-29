package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Role;
import model.validator.Notification;
import service.game.VideoGameService;
import service.order.OrderService;
import service.user.AuthenticationService;
import view.CustomerScene;
import view.Window;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class CustomerController {
    private final Window window;
    private final CustomerScene customerScene;
    private final AuthenticationService authenticationService;
    private final VideoGameService videoGameService;
    private final OrderService orderService;

    public CustomerController(Window window, CustomerScene customerScene, AuthenticationService authenticationService,
                              VideoGameService videoGameService, OrderService orderService) {
        this.window = window;
        this.customerScene = customerScene;
        this.authenticationService = authenticationService;
        this.videoGameService = videoGameService;
        this.orderService = orderService;

        this.customerScene.addLogOutItemListener(new LogOutButtonListener());
        this.customerScene.addBuyMenuButtonListener(new BuyMenuButtonListener());
        this.customerScene.addBuyButtonListener(new BuyButtonListener());
        this.customerScene.addCustomerOrdersItemListener(new CustomerOrdersItemListener());
        this.customerScene.addHomeItemListener(new HomeItemListener());
    }

    public void setUserData() {
        String role = "Customer";
        if (window.getActiveUser().getRoles().stream().map(Role::getRole).toList().contains(ADMINISTRATOR)) {
            role = "Administrator";
        } else if (window.getActiveUser().getRoles().stream().map(Role::getRole).toList().contains(EMPLOYEE)) {
            role = "Employee";
        }

        customerScene.setUserData(window.getActiveUser().getUsername(), window.getActiveUser().getMoney().toString(), role);
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
            authenticationService.logout(window.getActiveUser());
            window.setActiveUser(null);

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
            Long gameId = customerScene.getSelectedGameId();
            Integer amount = customerScene.getAmount();

            if (gameId == 0 || amount == 0) {
                return;
            }

            Notification<Boolean> result = orderService.buyGame(window.getActiveUser().getId(), gameId, amount);

            if (result.hasErrors()) {
                customerScene.setBuyResult(result.getFormattedErrors());
                customerScene.refreshGamePane(videoGameService.findAll());
            } else {
                customerScene.setBuyResult("Purchased successfully!");
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
