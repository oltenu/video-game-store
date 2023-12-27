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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.JoinedOrder;
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
    protected TableView<JoinedOrder> ordersTable;

    protected Button buyButton;

    protected String selectedGame;
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
        Label welcomeLabel = new Label("Welcome to our store!");
        welcomeLabel.setFont(Font.font("Courier", FontWeight.BOLD, 20));

        userPane.getChildren().addAll(welcomeLabel, userNameLabel,
                moneyLabel, userRoleLabel);
    }

    private void initializeGamePane() {
        initializeGameTable();
        initializeBuyPane();
    }

    private void initializeGameTable() {
        gamesTable = new TableView<>();

        TableColumn<VideoGame, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setPrefWidth(0);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<VideoGame, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(195);
        nameColumn.setMaxWidth(195);
        nameColumn.setPrefWidth(195);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<VideoGame, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(195);
        descriptionColumn.setMaxWidth(195);
        descriptionColumn.setPrefWidth(195);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<VideoGame, LocalDate> releasedDateColumn = new TableColumn<>("Released Date");
        releasedDateColumn.setMinWidth(195);
        releasedDateColumn.setMaxWidth(195);
        releasedDateColumn.setPrefWidth(195);
        releasedDateColumn.setCellValueFactory(new PropertyValueFactory<>("releasedDate"));
        TableColumn<VideoGame, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(100);
        amountColumn.setMaxWidth(100);
        amountColumn.setPrefWidth(100);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<VideoGame, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setMaxWidth(100);
        priceColumn.setPrefWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        gamesTable.getColumns().addAll(idColumn, nameColumn, descriptionColumn,
                releasedDateColumn, amountColumn, priceColumn);
        gamesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gamesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGame = newValue.getId().toString();
                buyGameNameTextField.setText(newValue.getName());
            }
        });
    }

    private void initializeOrderTable() {
        ordersTable = new TableView<>();

        TableColumn<JoinedOrder, String> gameColumn = new TableColumn<>("Game");
        gameColumn.setMinWidth(195);
        gameColumn.setMaxWidth(195);
        gameColumn.setPrefWidth(195);
        gameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        TableColumn<JoinedOrder, String> customerColumn = new TableColumn<>("Customer");
        customerColumn.setMinWidth(195);
        customerColumn.setMaxWidth(195);
        customerColumn.setPrefWidth(195);
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerUsername"));
        TableColumn<JoinedOrder, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setMinWidth(195);
        employeeColumn.setMaxWidth(195);
        employeeColumn.setPrefWidth(195);
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeUsername"));
        TableColumn<JoinedOrder, Integer> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(100);
        amountColumn.setMaxWidth(100);
        amountColumn.setPrefWidth(100);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<JoinedOrder, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setMinWidth(100);
        totalPriceColumn.setMaxWidth(100);
        totalPriceColumn.setPrefWidth(100);
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        ordersTable.getColumns().addAll(gameColumn, customerColumn,
                employeeColumn, amountColumn, totalPriceColumn);
        ordersTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void initializeBuyPane() {
        buyPane = new GridPane(10, 10);
        buyPane.setAlignment(Pos.CENTER);
        buyPane.setPadding(new Insets(25, 25, 25, 25));

        HBox buyBox = new HBox(50);
        buyGameNameTextField = new TextField();
        buyGameNameTextField.setEditable(false);
        buyAmountTextField = new TextField();
        buyAmountTextField.setMaxWidth(30);
        buyButton = new Button("Buy");

        buyBox.getChildren().addAll(new HBox(new Label("Game:"), buyGameNameTextField),
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

        mainPane.setCenter(userPane);
    }

    public void refreshGamePane(List<VideoGame> games) {
        clearPane();

        gamesTable.getItems().clear();
        buyGameNameTextField.clear();
        buyAmountTextField.clear();
        selectedGame = null;
        gamesTable.setItems(FXCollections.observableList(games));
        mainPane.setTop(menuBar);
        mainPane.setCenter(gamesTable);
        mainPane.setBottom(buyPane);
    }

    public void refreshOrderPane(List<JoinedOrder> joinedOrders) {
        clearPane();
        ordersTable.getItems().clear();
        ordersTable.setItems(FXCollections.observableList(joinedOrders));

        mainPane.setTop(menuBar);
        mainPane.setCenter(ordersTable);
    }

    public void clearPane() {
        mainPane.setTop(menuBar);
        mainPane.setBottom(new StackPane());
        mainPane.setCenter(new StackPane());
        mainPane.setRight(new StackPane());
        mainPane.setLeft(new StackPane());
    }

    public String getSelectedGame() {
        return selectedGame;
    }

    public String getAmount() {
        return buyAmountTextField.getText();
    }

    public void setBuyResult(String result, boolean good) {
        buyText.setFill(good ? Color.GREEN : Color.FIREBRICK);

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
