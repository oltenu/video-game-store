package launcher;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ComponentFactory.getInstance(false, primaryStage);
    }
}