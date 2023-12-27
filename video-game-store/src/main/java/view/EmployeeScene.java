package view;

import javafx.collections.FXCollections;
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
import model.JoinedOrder;
import model.VideoGame;

import java.util.List;

public class EmployeeScene extends CustomerScene {
    private MenuItem gamesItem;
    private MenuItem salesReportItem;

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
        Menu employeeMenu = new Menu("Employee");
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

        fieldsBox.getChildren().addAll(new HBox(nameLabel, nameField),
                new HBox(descriptionLabel, descriptionField), new HBox(amountLabel, amountField),
                new HBox(priceLabel, priceField), new HBox(releasedDateLabel, releasedDateField));

        gameText = new Text();
        gameText.setFill(Color.FIREBRICK);
        gameText.setTextAlignment(TextAlignment.CENTER);

        crudGamePane = new VBox(10);
        crudGamePane.getChildren().addAll(fieldsBox, buttonBox, gameText);
        BorderPane.setMargin(crudGamePane, new javafx.geometry.Insets(20));
        gamesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGame = newValue.getId().toString();
                nameField.setText(newValue.getName());
                descriptionField.setText(newValue.getDescription());
                amountField.setText(String.valueOf(newValue.getAmount()));
                priceField.setText(String.valueOf(newValue.getPrice()));
                releasedDateField.setText(String.valueOf(newValue.getReleasedDate()));
            }
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
        selectedGame = null;
        nameField.clear();
        descriptionField.clear();
        amountField.clear();
        priceField.clear();
        releasedDateField.clear();
    }

    public void refreshSalesPane(List<JoinedOrder> orders) {
        clearPane();
        ordersTable.getItems().clear();
        ordersTable.setItems(FXCollections.observableList(orders));

        mainPane.setTop(menuBar);
        mainPane.setCenter(ordersTable);
        mainPane.setBottom(salesReportPane);
    }

    public String getIdField() {
        return selectedGame;
    }

    public String getNameField() {
        return nameField.getText();
    }

    public String getDescriptionField() {
        return descriptionField.getText();
    }

    public String getAmountField() {
        return amountField.getText();
    }

    public String getPriceField() {
        return priceField.getText();
    }

    public String getReleasedDateField() {
        return releasedDateField.getText();
    }

    public void setGameText(String text, boolean good) {
        gameText.setFill(good ? Color.GREEN : Color.FIREBRICK);

        gameText.setText(text);
    }

    public void setSalesText(String text, boolean good) {
        salesText.setFill(good ? Color.GREEN : Color.FIREBRICK);

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
