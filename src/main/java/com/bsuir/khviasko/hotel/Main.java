package com.bsuir.khviasko.hotel;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SocketService.init();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        StageConfig.stage.setTitle("Hotel");
        StageConfig.stage.setScene(new Scene(root, 800, 450));
        StageConfig.stage.setResizable(false);
        StageConfig.stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
