package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.VideoGame;
import model.builder.VideoGameBuilder;
import model.validator.Notification;
import service.game.VideoGameService;
import service.order.OrderService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.EmployeeScene;
import view.Window;

import java.time.LocalDate;

public class EmployeeController extends CustomerController {
    private final Window window;
    private final EmployeeScene employeeScene;
    private final VideoGameService videoGameService;
    private final OrderService orderService;

    public EmployeeController(Window window, EmployeeScene employeeScene, AuthenticationService authenticationService,
                              VideoGameService videoGameService, OrderService orderService, UserService userService) {
        super(window, employeeScene, authenticationService, videoGameService, orderService, userService);

        this.window = window;
        this.employeeScene = employeeScene;
        this.videoGameService = videoGameService;
        this.orderService = orderService;

        this.employeeScene.addGamesItemListener(new GamesItemListener());
        this.employeeScene.addSalesReportItemListener(new SalesReportItemListener());
        this.employeeScene.addGameButtonListener(new AddGameButtonListener());
        this.employeeScene.addUpdateGameButtonListener(new UpdateGameButtonListener());
        this.employeeScene.addDeleteGameButtonListener(new DeleteGameButtonListener());
        this.employeeScene.addGenerateSalesReportButtonListener(new GenerateSalesReportButtonListener());
    }

    private class GamesItemListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            employeeScene.refreshGamePaneEmployee(videoGameService.findAll());
        }
    }

    private class SalesReportItemListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            employeeScene.refreshSalesPane(orderService.findAllEmployeeSales(window.getActiveUser().getId()));
        }
    }

    private class AddGameButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String name = employeeScene.getNameField();
            String description = employeeScene.getDescriptionField();
            Integer amount = employeeScene.getAmountField();
            Double price = employeeScene.getPriceField();
            LocalDate releasedDate = employeeScene.getReleasedDateField();

            VideoGame game = new VideoGameBuilder()
                    .setName(name)
                    .setDescription(description)
                    .setAmount(amount)
                    .setPrice(price)
                    .setReleaseDate(releasedDate)
                    .build();

            Notification<Boolean> addGameNotification = videoGameService.save(game);

            if (addGameNotification.hasErrors()) {
                employeeScene.setGameText(addGameNotification.getFormattedErrors());
            } else {
                employeeScene.setGameText("New game inserted!");
            }

            employeeScene.refreshGamePaneEmployee(videoGameService.findAll());
        }
    }

    private class UpdateGameButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            Long id = employeeScene.getIdField();
            String name = employeeScene.getNameField();
            String description = employeeScene.getDescriptionField();
            Integer amount = employeeScene.getAmountField();
            Double price = employeeScene.getPriceField();
            LocalDate releasedDate = employeeScene.getReleasedDateField();

            VideoGame game = new VideoGameBuilder()
                    .setId(id)
                    .setName(name)
                    .setDescription(description)
                    .setAmount(amount)
                    .setPrice(price)
                    .setReleaseDate(releasedDate)
                    .build();

            Notification<Boolean> updateGameNotification = videoGameService.update(game);

            if (updateGameNotification.hasErrors()) {
                employeeScene.setGameText(updateGameNotification.getFormattedErrors());
            } else {
                employeeScene.setGameText("Game updated!");
            }

            employeeScene.refreshGamePaneEmployee(videoGameService.findAll());
        }
    }

    private class DeleteGameButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            Long id = employeeScene.getIdField();

            videoGameService.deleteById(id);

            employeeScene.refreshGamePaneEmployee(videoGameService.findAll());
        }
    }

    private class GenerateSalesReportButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            //To do
        }
    }
}

