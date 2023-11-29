package view;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Order;
import model.VideoGame;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeScene extends CustomerScene {
    private Menu employeeMenu;
    private MenuItem gamesItem;
    private MenuItem salesReportItem;

    private TextField idField;
    private TextField nameField;
    private TextField descriptionField;
    private TextField amountField;
    private TextField priceField;
    private TextField releasedDateField;

    private Button addGameButton;
    private Button updateGameButton;
    private Button deleteGameButton;
    private Button generateSalesReportButton;

    private Text gameText;
    private Text salesText;

    private VBox crudGamePane;
    private VBox salesReportPane;

    public EmployeeScene() {
        initializeMenuEmployee();
        initializeCrudGamePane();
        initializeSalesReportPane();
    }

    public void initializeMenuEmployee() {
        employeeMenu = new Menu("Employee");
        gamesItem = new MenuItem("Games...");
        salesReportItem = new MenuItem("Sales...");

        employeeMenu.getItems().addAll(gamesItem, salesReportItem);
        menuBar.getMenus().add(employeeMenu);
    }

    private void initializeCrudGamePane() {
        HBox fieldsBox = new HBox(20);
        fieldsBox.setAlignment(Pos.CENTER);
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        addGameButton = new Button("Add");
        addGameButton.setAlignment(Pos.CENTER);
        updateGameButton = new Button("Update");
        updateGameButton.setAlignment(Pos.CENTER);
        deleteGameButton = new Button("Delete");
        deleteGameButton.setAlignment(Pos.CENTER);

        buttonBox.getChildren().addAll(addGameButton, updateGameButton, deleteGameButton);

        idField = new TextField();
        idField.setMaxWidth(50);
        Label idLabel = new Label("Id:");
        idLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        nameField = new TextField();
        nameField.setMaxWidth(80);
        Label nameLabel = new Label("Name");
        nameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        descriptionField = new TextField();
        descriptionField.setMaxWidth(80);
        Label descriptionLabel = new Label("Description");
        descriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        amountField = new TextField();
        amountField.setMaxWidth(50);
        Label amountLabel = new Label("Amount:");
        amountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        priceField = new TextField();
        priceField.setMaxWidth(50);
        Label priceLabel = new Label("Price:");
        priceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        releasedDateField = new TextField();
        releasedDateField.setMaxWidth(80);
        Label releasedDateLabel = new Label("Date:");
        releasedDateLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        fieldsBox.getChildren().addAll(new HBox(idLabel, idField), new HBox(nameLabel, nameField),
                new HBox(descriptionLabel, descriptionField), new HBox(amountLabel, amountField),
                new HBox(priceLabel, priceField), new HBox(releasedDateLabel, releasedDateField));

        gameText = new Text();
        gameText.setFill(Color.FIREBRICK);
        gameText.setTextAlignment(TextAlignment.CENTER);

        crudGamePane = new VBox(10);
        crudGamePane.getChildren().addAll(fieldsBox, buttonBox, gameText);
        BorderPane.setMargin(crudGamePane, new javafx.geometry.Insets(20));

        gamesTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<VideoGame>) selected -> {
            selected.next();
            ObservableList<VideoGame> selectedGameList = (ObservableList<VideoGame>) selected.getList();
            VideoGame selectedGame = selectedGameList.get(0);
            idField.setText(String.valueOf(selectedGame.getId()));
            nameField.setText(selectedGame.getName());
            descriptionField.setText(selectedGame.getDescription());
            amountField.setText(String.valueOf(selectedGame.getAmount()));
            priceField.setText(String.valueOf(selectedGame.getPrice()));
            releasedDateField.setText(String.valueOf(selectedGame.getReleasedDate()));
        });
    }

    private void initializeSalesReportPane() {
        BorderPane generateBox = new BorderPane();

        salesText = new Text();
        salesText.setFill(Color.FIREBRICK);
        salesText.setTextAlignment(TextAlignment.CENTER);

        generateSalesReportButton = new Button("Generate PDF");

        generateBox.setCenter(salesText);
        generateBox.setRight(generateSalesReportButton);

        salesReportPane = new VBox(50);
        salesReportPane.setAlignment(Pos.CENTER);
        salesReportPane.getChildren().add(generateBox);
        BorderPane.setMargin(salesReportPane, new javafx.geometry.Insets(20));
    }

    public void refreshGamePaneEmployee(List<VideoGame> games) {
        super.refreshGamePane(games);

        mainPane.setBottom(crudGamePane);
        idField.clear();
        nameField.clear();
        descriptionField.clear();
        amountField.clear();
        priceField.clear();
        releasedDateField.clear();
    }

    public void refreshSalesPane(List<Order> orders) {
        clearPane();
        customerOrdersTable.setItems(FXCollections.observableList(orders));

        mainPane.setTop(menuBar);
        mainPane.setCenter(customerOrdersTable);
        mainPane.setBottom(salesReportPane);
    }

    public Long getIdField() {
        return Long.valueOf(idField.getText());
    }

    public String getNameField() {
        return nameField.getText();
    }

    public String getDescriptionField() {
        return descriptionField.getText();
    }

    public Integer getAmountField() {
        return Integer.valueOf(amountField.getText());
    }

    public Double getPriceField() {
        return Double.valueOf(priceField.getText());
    }

    public LocalDate getReleasedDateField() {
        String date = releasedDateField.getText();

        String regexPattern = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(date);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (matcher.matches()) {
            return LocalDate.parse(date, formatter);
        } else {
            setSalesText("Invalid date. Setting a default date!");
            return LocalDate.now();
        }
    }

    public void setGameText(String text) {
        gameText.setText(text);
    }

    public void setSalesText(String text) {
        salesText.setText(text);
    }

    public void addGamesItemListener(EventHandler<ActionEvent> gamesItemListener) {
        gamesItem.setOnAction(gamesItemListener);
    }

    public void addSalesReportItemListener(EventHandler<ActionEvent> salesReportItemListener) {
        salesReportItem.setOnAction(salesReportItemListener);
    }

    public void addGameButtonListener(EventHandler<ActionEvent> addGameButtonListener) {
        addGameButton.setOnAction(addGameButtonListener);
    }

    public void addUpdateGameButtonListener(EventHandler<ActionEvent> updateGameButtonListener) {
        updateGameButton.setOnAction(updateGameButtonListener);
    }

    public void addDeleteGameButtonListener(EventHandler<ActionEvent> deleteGameButtonListener) {
        deleteGameButton.setOnAction(deleteGameButtonListener);
    }

    public void addGenerateSalesReportButtonListener(EventHandler<ActionEvent> generateSalesReportButtonListener) {
        generateSalesReportButton.setOnAction(generateSalesReportButtonListener);
    }
}
