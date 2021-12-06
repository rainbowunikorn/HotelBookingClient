package com.bsuir.khviasko.hotel.controller.common;

import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.service.LoginService;
import com.bsuir.khviasko.hotel.service.Query;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Text badCredTextLabel;

    @FXML
    void initialize() {
        loginButton.setOnAction(
                actionEvent -> {
                    String login = loginField.getText().trim();
                    String password = passwordField.getText().trim();

                    LoginService loginService = new LoginService();
                    String response = loginService.login(login, password);

                    if (response.equals("REJECTED")) {
                        badCredTextLabel.setText("Неправильно введенные данные!");
                        return;
                    }
                    if (response.equals("SUCCESS")) {

                        try {
                            Parent root = null;

                            if (Constants.ADMIN_ROLE.equalsIgnoreCase(Query.getRole())) {
                                root = FXMLLoader.load(getClass().getResource("/fxml/adminMenu.fxml"));
                            } else {
                                root = FXMLLoader.load(getClass().getResource("/fxml/userMenu.fxml"));
                            }

                            StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                            StageConfig.stage.setScene(new Scene(root, 800, 450));
                            StageConfig.stage.setResizable(false);
                            StageConfig.stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        registerButton.setOnAction(
                actionEvent -> {
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/fxml/registration.fxml"));
                        StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                        StageConfig.stage.setScene(new Scene(root, 800, 450));
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("", "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
