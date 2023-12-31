package view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.JoinedOrder;
import model.Role;
import model.User;

import java.util.List;

public class AdminScene extends EmployeeScene {
    protected Menu adminMenu;
    protected MenuItem usersItem;
    protected MenuItem employeeReportItem;

    private TableView<User> usersTable;

    private TextField usernameField;
    private PasswordField passwordField;
    private TextField moneyField;
    private Long userId;

    private ChoiceBox<String> rolesChoice;
    private ChoiceBox<String> employeesChoice;

    private Button addUserButton;
    private Button updateUserButton;
    private Button deleteUserButton;
    private Button generateEmployeesReportButton;

    private Text usersText;
    private Text employeesText;

    private VBox crudUsersPane;
    private VBox employeesReportPane;

    public AdminScene() {
        initializeMenuAdmin();
        initializeUsersTable();
        initializeCrudUsersPane();
        initializeEmployeeReportPane();
    }

    public void initializeMenuAdmin() {
        adminMenu = new Menu("Admin");

        usersItem = new MenuItem("Users...");
        employeeReportItem = new MenuItem("Employee Report...");

        adminMenu.getItems().addAll(usersItem, employeeReportItem);
        menuBar.getMenus().add(adminMenu);
    }

    public void initializeCrudUsersPane() {
        crudUsersPane = new VBox(10);

        HBox fieldsBox = new HBox(20);
        fieldsBox.setAlignment(Pos.CENTER);
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        usersText = new Text();
        usersText.setFill(Color.FIREBRICK);
        usersText.setTextAlignment(TextAlignment.CENTER);

        usernameField = new TextField();
        Label usernameLabel = new Label("Email:");
        passwordField = new PasswordField();
        Label passwordLabel = new Label("Password");
        moneyField = new TextField();
        moneyField.setMaxWidth(60);
        Label moneyLabel = new Label("Money:");
        rolesChoice = new ChoiceBox<>();
        Label rolesLabel = new Label("Role:");
        fieldsBox.getChildren().addAll(new HBox(usernameLabel, usernameField),
                new HBox(passwordLabel, passwordField), new HBox(moneyLabel, moneyField), new HBox(rolesLabel, rolesChoice));


        addUserButton = new Button("Add");
        updateUserButton = new Button("Update");
        deleteUserButton = new Button("Delete");
        buttonBox.getChildren().addAll(addUserButton, updateUserButton, deleteUserButton);


        crudUsersPane.getChildren().addAll(fieldsBox, buttonBox, usersText);
        BorderPane.setMargin(crudUsersPane, new javafx.geometry.Insets(20));
    }

    public void initializeEmployeeReportPane() {
        employeesReportPane = new VBox(10);

        employeesText = new Text();

        HBox buttonBox = new HBox(50);
        buttonBox.setAlignment(Pos.CENTER);

        employeesChoice = new ChoiceBox<>();
        employeesChoice.setMinWidth(150);
        generateEmployeesReportButton = new Button("Generate");
        buttonBox.getChildren().addAll(employeesChoice, generateEmployeesReportButton);
        employeesReportPane.getChildren().addAll(buttonBox, employeesText);
        BorderPane.setMargin(employeesReportPane, new javafx.geometry.Insets(20));
    }

    private void initializeUsersTable() {
        usersTable = new TableView<>();

        TableColumn<User, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(200);
        idColumn.setMaxWidth(200);
        idColumn.setPrefWidth(200);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<User, String> usernameColumn = new TableColumn<>("Email");
        usernameColumn.setMinWidth(200);
        usernameColumn.setMaxWidth(200);
        usernameColumn.setPrefWidth(200);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, Double> moneyColumn = new TableColumn<>("Money");
        moneyColumn.setMinWidth(200);
        moneyColumn.setMaxWidth(200);
        moneyColumn.setPrefWidth(200);
        moneyColumn.setCellValueFactory(new PropertyValueFactory<>("money"));
        TableColumn<User, List<Role>> roleColumn = new TableColumn<>("Role");
        roleColumn.setMinWidth(200);
        roleColumn.setMaxWidth(200);
        roleColumn.setPrefWidth(200);
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        usersTable.getColumns().addAll(idColumn, usernameColumn, moneyColumn, roleColumn);
        usersTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        usersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userId = newValue.getId();
                usernameField.setText(newValue.getUsername());
                moneyField.setText(String.valueOf(newValue.getMoney()));
            }
        });
    }

    public void refreshEmployeesReportPanel(List<String> employees) {
        clearPane();
        employeesChoice.getItems().clear();

        mainPane.setTop(menuBar);
        mainPane.setCenter(ordersTable);
        mainPane.setBottom(employeesReportPane);

        employees.forEach(employee -> employeesChoice.getItems().add(employee));
        employeesChoice.setValue(employeesChoice.getItems().get(0));
    }

    public void refreshCrudUsersPanel(List<User> users, List<String> roles) {
        clearPane();
        usernameField.clear();
        passwordField.clear();
        moneyField.clear();
        rolesChoice.getItems().clear();

        usersTable.setItems(FXCollections.observableList(users));

        mainPane.setTop(menuBar);
        mainPane.setCenter(usersTable);
        mainPane.setBottom(crudUsersPane);

        roles.forEach(role -> rolesChoice.getItems().add(role));
        rolesChoice.setValue(rolesChoice.getItems().get(0));
    }

    public void setUsersText(String text, boolean good) {
        usersText.setFill(good ? Color.GREEN : Color.FIREBRICK);

        usersText.setText(text);
    }

    public void setEmployeesText(String text, boolean good) {
        employeesText.setFill(good ? Color.GREEN : Color.FIREBRICK);
        employeesText.setText(text);
    }

    public String getUsernameField() {
        return usernameField.getText();
    }

    public String getPasswordField() {
        return passwordField.getText();
    }

    public String getMoneyField() {
        return moneyField.getText();
    }

    public String getSelectedRole() {
        return rolesChoice.getValue();
    }

    public Long getUserId() {
        return userId;
    }

    public String getSelectedEmployee() {
        return employeesChoice.getValue();
    }

    public void setOrdersTable(List<JoinedOrder> orders) {
        ordersTable.setItems(FXCollections.observableList(orders));
    }

    public void addUsersItemListener(EventHandler<ActionEvent> usersItemListener) {
        usersItem.setOnAction(usersItemListener);
    }

    public void addEmployeesReportItemListener(EventHandler<ActionEvent> employeesReportItemListener) {
        employeeReportItem.setOnAction(employeesReportItemListener);
    }

    public void addUserListener(EventHandler<ActionEvent> addUserListener) {
        addUserButton.setOnAction(addUserListener);
    }

    public void addUpdateUserListener(EventHandler<ActionEvent> updateUserListener) {
        updateUserButton.setOnAction(updateUserListener);
    }

    public void addDeleteUserListener(EventHandler<ActionEvent> deleteUserListener) {
        deleteUserButton.setOnAction(deleteUserListener);
    }

    public void addGenerateEmployeesReportListener(EventHandler<ActionEvent> generateEmployeesReportListener) {
        generateEmployeesReportButton.setOnAction(generateEmployeesReportListener);
    }

    public void addEmployeeChoiceListener(EventHandler<ActionEvent> employeeChoiceListener) {
        employeesChoice.setOnAction(employeeChoiceListener);
    }
}
