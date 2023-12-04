package controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.JoinedOrder;
import model.User;
import model.VideoGame;
import model.builder.VideoGameBuilder;
import model.validator.Notification;
import service.game.VideoGameService;
import service.order.OrderService;
import service.user.UserService;
import view.EmployeeScene;
import view.Window;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeController extends CustomerController {
    private final Window window;
    private final EmployeeScene employeeScene;
    private final VideoGameService videoGameService;
    private final OrderService orderService;

    public EmployeeController(Window window, EmployeeScene employeeScene,
                              VideoGameService videoGameService, OrderService orderService, UserService userService) {
        super(window, employeeScene, videoGameService, orderService, userService);

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
            Notification<Boolean> resultNotification;
            VideoGame game;
            int amount;
            double price;
            LocalDate releasedDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String name = employeeScene.getNameField();
            String description = employeeScene.getDescriptionField();
            String selectedAmount = employeeScene.getAmountField();
            String selectedPrice = employeeScene.getPriceField();
            String selectedDate = employeeScene.getReleasedDateField();

            resultNotification = validateGame(name, description, selectedAmount, selectedPrice,
                    selectedDate);

            if (resultNotification.hasErrors()) {
                employeeScene.setGameText(resultNotification.getFormattedErrors(), false);

                return;
            } else {
                amount = Integer.parseInt(selectedAmount);
                price = Double.parseDouble(selectedPrice);
                releasedDate = LocalDate.parse(selectedDate, formatter);

                game = new VideoGameBuilder()
                        .setName(name)
                        .setDescription(description)
                        .setAmount(amount)
                        .setPrice(price)
                        .setReleaseDate(releasedDate)
                        .build();
            }

            Notification<Boolean> addGameNotification = videoGameService.save(game);

            if (addGameNotification.hasErrors()) {
                employeeScene.setGameText(addGameNotification.getFormattedErrors(), false);
            } else {
                employeeScene.setGameText("New game inserted!", true);
            }

            employeeScene.refreshGamePaneEmployee(videoGameService.findAll());
        }
    }

    private class UpdateGameButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            Notification<Boolean> resultNotification;
            VideoGame game;
            long id;
            int amount;
            double price;
            LocalDate releasedDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String selectedGame = employeeScene.getIdField();
            String name = employeeScene.getNameField();
            String description = employeeScene.getDescriptionField();
            String selectedAmount = employeeScene.getAmountField();
            String selectedPrice = employeeScene.getPriceField();
            String selectedDate = employeeScene.getReleasedDateField();

            resultNotification = validateGame(name, description, selectedAmount, selectedPrice,
                    selectedDate);

            if (selectedGame == null) {
                resultNotification.addError("No game selected!");
                resultNotification.setResult(Boolean.FALSE);
            }

            if (resultNotification.hasErrors() || selectedGame == null) {
                employeeScene.setGameText(resultNotification.getFormattedErrors(), false);

                return;
            } else {
                id = Long.parseLong(selectedGame);
                amount = Integer.parseInt(selectedAmount);
                price = Double.parseDouble(selectedPrice);
                releasedDate = LocalDate.parse(selectedDate, formatter);

                game = new VideoGameBuilder()
                        .setId(id)
                        .setName(name)
                        .setDescription(description)
                        .setAmount(amount)
                        .setPrice(price)
                        .setReleaseDate(releasedDate)
                        .build();
            }

            Notification<Boolean> updateGameNotification = videoGameService.update(game);

            if (updateGameNotification.hasErrors()) {
                employeeScene.setGameText(updateGameNotification.getFormattedErrors(), false);
            } else {
                employeeScene.setGameText("Game updated!", true);
            }

            employeeScene.refreshGamePaneEmployee(videoGameService.findAll());
        }
    }

    private class DeleteGameButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String selectedGame = employeeScene.getSelectedGame();

            if (selectedGame.isEmpty()) {
                employeeScene.setGameText("No game selected!", false);
            } else {
                Long id = Long.valueOf(selectedGame);
                videoGameService.deleteById(id);
                employeeScene.refreshGamePaneEmployee(videoGameService.findAll());
                employeeScene.setGameText("Game deleted!", true);
            }
        }
    }

    private class GenerateSalesReportButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            User employee = window.getActiveUser();
            Long employeeId = employee.getId();
            String fileName = "src/main/resources/" + employee.getUsername() + "-sales-report.pdf";
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

                employeeScene.setSalesText("Failed to generate report!", false);
            }

            employeeScene.setSalesText("Report generated!", true);
        }
    }

    private Notification<Boolean> validateGame(String name, String description, String amount,
                                               String price, String date) {
        Notification<Boolean> result = new Notification<>();

        if (name.isEmpty()) {
            result.addError("Invalid name!");
            result.setResult(Boolean.FALSE);
        }

        if (description.isEmpty()) {
            result.addError("Invalid description!");
            result.setResult(Boolean.FALSE);
        }

        if (!Pattern.matches("^[1-9]\\d*$", amount)) {
            result.addError("Invalid amount!");
            result.setResult(Boolean.FALSE);
        }

        if (!Pattern.matches("^\\d*\\.?\\d+$", price)) {
            result.addError("Invalid price!");
            result.setResult(Boolean.FALSE);
        }

        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date)) {
            result.addError("Invalid released date!");
            result.setResult(Boolean.FALSE);
        }

        return result;
    }
}

