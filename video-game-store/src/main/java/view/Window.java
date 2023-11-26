package view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Objects;

public class Window {
    private final Stage primaryStage;
    private final Scene loginScene;
    private final Scene customerScene;
    private final Scene employeeScene;
    private final Scene adminScene;

    public Window(Stage primaryStage, Scene loginScene, Scene customerScene, Scene employeeScene, Scene adminScene){
        this.primaryStage = primaryStage;
        this.loginScene = loginScene;
        this.customerScene = customerScene;
        this.employeeScene = employeeScene;
        this.adminScene = adminScene;

        initStage();

        primaryStage.show();
    }

    private void initStage(){
        primaryStage.setTitle("Video Game Store");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/game_store.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(customerScene);
    }

    public void setScene(int index){
        switch (index) {
            case 0 -> primaryStage.setScene(loginScene);
            case 1 -> primaryStage.setScene(customerScene);
            case 2 -> primaryStage.setScene(employeeScene);
            case 3 -> primaryStage.setScene(adminScene);
        }
    }
}
