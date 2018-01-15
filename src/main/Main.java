package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final String GAME_TITLE= "The game of life";

    @Override
    public void start(Stage stage) throws IOException {
        initializeStage(stage);
    }

    private void initializeStage(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
        Scene scene = initializeScene(root);

        stage.setTitle(GAME_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    private Scene initializeScene(Parent parent){
        Scene scene = new Scene(parent, 1280, 1024);
        String cssStylePath = this.getClass().getResource("gameStyles.css").toExternalForm();
        scene.getStylesheets().add(cssStylePath);
        return scene;
    }
}
