package view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Order;
import model.VideoGame;

import java.time.LocalDate;
import java.util.List;

public class CustomerScene extends Scene {
    protected MenuBar menuBar;
    protected Menu homeMenu;
    protected MenuItem homeItem;
    protected MenuItem logOutItem;
    protected Menu customerMenu;
    protected MenuItem buyGameItem;
    protected MenuItem customerOrdersItem;

    protected BorderPane mainPane;
    protected GridPane buyPane;
    private VBox userPane;

    protected TableView<VideoGame> gamesTable;
    protected TableView<Order> customerOrdersTable;

    protected Button buyButton;

    protected TextField buyGameIdTextField;
    protected TextField buyGameNameTextField;
    protected TextField buyAmountTextField;

    private Label userNameLabel;
    private Label moneyLabel;
    private Label userRoleLabel;

    protected Text buyText;

    public CustomerScene() {
        super(new StackPane());
        initializeMenuCustomer();
        initializeGamePane();
        initializeUserData();
        initializeOrderTable();

        mainPane = new BorderPane();
        mainPane.setTop(menuBar);
        mainPane.setCenter(new StackPane());
        setRoot(mainPane);
    }

    public void initializeMenuCustomer() {
        menuBar = new MenuBar();

        homeMenu = new Menu("Home");
        homeItem = new MenuItem("Home page");
        logOutItem = new MenuItem("LogOut");
        homeMenu.getItems().addAll(homeItem, logOutItem);

        customerMenu = new Menu("Customer");
        buyGameItem = new MenuItem("Buy Game...");
        customerOrdersItem = new MenuItem("My orders...");

        customerMenu.getItems().addAll(buyGameItem, customerOrdersItem);
        menuBar.getMenus().addAll(homeMenu, customerMenu);
    }

    private void initializeUserData() {
        userPane = new VBox(50);
        userPane.setAlignment(Pos.CENTER);

        userNameLabel = new Label("Name:");
        moneyLabel = new Label("Money:");
        userRoleLabel = new Label("Role:");

        userPane.getChildren().addAll(new Label("Welcome to our store!"), userNameLabel,
                moneyLabel, userRoleLabel);
    }

    private void initializeGamePane() {
        initializeGameTable();
        initializeBuyPane();
    }

    private void initializeGameTable() {
        gamesTable = new TableView<>();

        TableColumn<VideoGame, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<VideoGame, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<VideoGame, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(200);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<VideoGame, LocalDate> releasedDateColumn = new TableColumn<>("Released Date");
        releasedDateColumn.setMinWidth(200);
        releasedDateColumn.setCellValueFactory(new PropertyValueFactory<>("releasedDate"));
        TableColumn<VideoGame, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(50);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<VideoGame, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(70);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        gamesTable.getColumns().addAll(idColumn, nameColumn, descriptionColumn,
                releasedDateColumn, amountColumn, priceColumn);
        gamesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gamesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                buyGameIdTextField.setText(String.valueOf(newValue.getId()));
                buyGameNameTextField.setText(newValue.getName());
            }
        });
    }

    private void initializeOrderTable() {
        customerOrdersTable = new TableView<>();

        TableColumn<Order, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(130);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Order, Long> customerIdColumn = new TableColumn<>("Customer Id");
        customerIdColumn.setMinWidth(130);
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        TableColumn<Order, Long> employeeIdColumn = new TableColumn<>("Employee Id");
        employeeIdColumn.setMinWidth(130);
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        TableColumn<Order, Long> gameIdColumn = new TableColumn<>("Game Id");
        gameIdColumn.setMinWidth(130);
        gameIdColumn.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        TableColumn<Order, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(130);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Order, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setMinWidth(130);
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        customerOrdersTable.getColumns().addAll(idColumn, customerIdColumn, employeeIdColumn,
                gameIdColumn, amountColumn, totalPriceColumn);
        customerOrdersTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void initializeBuyPane() {
        buyPane = new GridPane(10, 10);
        buyPane.setAlignment(Pos.CENTER);
        buyPane.setPadding(new Insets(25, 25, 25, 25));

        HBox buyBox = new HBox(50);
        buyGameIdTextField = new TextField();
        buyGameIdTextField.setEditable(false);
        buyGameIdTextField.setMaxWidth(30);
        buyGameNameTextField = new TextField();
        buyGameNameTextField.setEditable(false);
        buyAmountTextField = new TextField();
        buyAmountTextField.setMaxWidth(30);
        buyButton = new Button("Buy");

        buyBox.getChildren().addAll(new HBox(new Label("Id:"), buyGameIdTextField),
                new HBox(new Label("Game:"), buyGameNameTextField),
                new HBox(new Label("Amount:"), buyAmountTextField, buyButton));

        buyText = new Text();
        buyText.setFill(Color.FIREBRICK);

        buyPane.add(buyBox, 0, 0);
        buyPane.add(buyText, 0, 1);
    }

    public void setUserData(String userName, String money, String role) {
        userNameLabel.setText("Name: " + userName);
        moneyLabel.setText("Money: " + money);
        userRoleLabel.setText("Role: " + role);
    }

    public void refreshHomePane() {
        clearPane();

        mainPane.setTop(menuBar);
        mainPane.setCenter(userPane);
    }

    public void refreshGamePane(List<VideoGame> games) {
        clearPane();

        gamesTable.setItems(FXCollections.observableList(games));
        mainPane.setTop(menuBar);
        mainPane.setCenter(gamesTable);
        mainPane.setBottom(buyPane);
    }

    public void refreshOrderPane(List<Order> orders) {
        clearPane();
        customerOrdersTable.setItems(FXCollections.observableList(orders));

        mainPane.setTop(menuBar);
        mainPane.setCenter(customerOrdersTable);
        buyGameIdTextField.clear();
        buyAmountTextField.clear();
    }

    public void clearPane() {
        mainPane.setTop(menuBar);
        mainPane.setBottom(new StackPane());
        mainPane.setCenter(new StackPane());
        mainPane.setRight(new StackPane());
        mainPane.setLeft(new StackPane());
    }

    public Long getSelectedGameId() {
        if (buyGameIdTextField.getText().isEmpty()) {
            setBuyResult("No product selected!");

            return 0L;
        }

        return Long.valueOf(buyGameIdTextField.getText());
    }

    public Integer getAmount() {
        if (buyAmountTextField.getText().isEmpty()) {
            setBuyResult("No amount selected!");

            return 0;
        }

        return Integer.valueOf(buyAmountTextField.getText());
    }

    public void setBuyResult(String result) {
        buyText.setText(result);
    }

    public void addBuyMenuButtonListener(EventHandler<ActionEvent> buyMenuButtonListener) {
        buyGameItem.setOnAction(buyMenuButtonListener);
    }

    public void addHomeItemListener(EventHandler<ActionEvent> homeItemListener) {
        homeItem.setOnAction(homeItemListener);
    }

    public void addCustomerOrdersItemListener(EventHandler<ActionEvent> customerOrdersMenuListener) {
        customerOrdersItem.setOnAction(customerOrdersMenuListener);
    }

    public void addLogOutItemListener(EventHandler<ActionEvent> logOutMenuListener) {
        logOutItem.setOnAction(logOutMenuListener);
    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener) {
        buyButton.setOnAction(buyButtonListener);
    }
}
