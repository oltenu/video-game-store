package view;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;

import java.util.Objects;

public class Window {
    private final Stage primaryStage;
    private final LoginScene loginScene;
    private final CustomerScene customerScene;
    private final EmployeeScene employeeScene;
    private final AdminScene adminScene;
    private User activeUser;

    public Window(Stage primaryStage, LoginScene loginScene, CustomerScene customerScene,
                  EmployeeScene employeeScene, AdminScene adminScene) {
        this.primaryStage = primaryStage;
        this.loginScene = loginScene;
        this.customerScene = customerScene;
        this.employeeScene = employeeScene;
        this.adminScene = adminScene;
        this.activeUser = null;

        initStage();

        primaryStage.show();
    }

    private void initStage() {
        primaryStage.setTitle("Video Game Store");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/game_store.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(employeeScene);
    }

    public void setScene(int index) {
        switch (index) {
            case 0 -> {
                primaryStage.setScene(loginScene);
                loginScene.refresh();
            }
            case 1 -> {
                primaryStage.setScene(customerScene);
                customerScene.initializeMenuCustomer();
            }
            case 2 -> {
                primaryStage.setScene(employeeScene);
                employeeScene.initializeMenuEmployee();
            }
            case 3 -> {
                primaryStage.setScene(adminScene);
                adminScene.initializeMenuAdmin();
            }
        }
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }
}
