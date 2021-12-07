package com.bsuir.khviasko.hotel.controller.common;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.bsuir.khviasko.hotel.service.Query;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.Gson;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserController {
    @FXML
    private TextField firstnameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createButton;

    @FXML
    private Button backButton;

    @FXML
    private Text loginValidationField;

    @FXML
    private Text passwordValidationField;

    @FXML
    private Text firstnameValidationField;

    @FXML
    private Text surnameValidationField;

    @FXML
    void initialize() {
        Gson gson = new Gson();
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setUserId(Query.getUserId());
        queryDTO.setCommand("view_user");
        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            String response = SocketService.getReader().readLine();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            firstnameField.setText(jsonObject.get("firstname").getAsString());
            surnameField.setText(jsonObject.get("surname").getAsString());
            loginField.setText(jsonObject.get("login").getAsString());
            passwordField.setText(jsonObject.get("password").getAsString());


            createButton.setOnAction(event -> {
                boolean status = validateFirstname(firstnameField.getText()) && validateSurname(surnameField.getText()) &&
                        validateLogin(loginField.getText()) && validatePassword(passwordField.getText());
                if (!status) return;


                jsonObject.remove("firstname");
                jsonObject.remove("surname");
                jsonObject.remove("login");
                jsonObject.remove("password");

                jsonObject.addProperty("firstname", firstnameField.getText());
                jsonObject.addProperty("surname", surnameField.getText());
                jsonObject.addProperty("login", loginField.getText());
                jsonObject.addProperty("password", passwordField.getText());


                QueryDTO queryDTO2 = new QueryDTO();
                queryDTO2.setUserId(Query.getUserId());
                queryDTO2.setCommand("edit_user");
                try {
                    SocketService.writeLine(gson.toJson(queryDTO2));
                    SocketService.writeLine(jsonObject.toString());
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/editUser.fxml"));
                    StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                    StageConfig.stage.setScene(new Scene(root, 800, 450));
                    StageConfig.stage.setResizable(false);
                    StageConfig.stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

        }catch (Exception e){
            e.printStackTrace();
        }

        backButton.setOnAction(event -> {
            try {
                Parent root;
                if ("view_users".equalsIgnoreCase(Query.getPrevCommand())) {
                    root = FXMLLoader.load(getClass().getResource("/fxml/users.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/fxml/user.fxml"));
                }
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validateLogin(String login) {
        String regex = "^.{5,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(login);
        if (login.isEmpty() || !matcher.matches()) {
            loginValidationField.setText("Введите корректный логин");
            return false;
        }
        loginValidationField.setText("");
        return true;
    }

    private boolean validatePassword(String password) {
        String regex = "^.{5,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (password.isEmpty() || !matcher.matches()) {
            passwordValidationField.setText("Введите корректный пароль");
            return false;
        }
        passwordValidationField.setText("");
        return true;
    }

    private boolean validateFirstname(String firstname) {
        String regex = "^[А-ЯA-Z][а-яa-z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(firstname);
        if (firstname.isEmpty() || !matcher.matches()) {
            firstnameValidationField.setText("Введите корректное имя");
            return false;
        }
        firstnameValidationField.setText("");
        return true;
    }

    private boolean validateSurname(String surname) {
        String regex = "^[А-ЯA-Z][а-яa-z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(surname);
        if (surname.isEmpty() || !matcher.matches()) {
            surnameValidationField.setText("Введите корректную фамилию");
            return false;
        }
        surnameValidationField.setText("");
        return true;
    }

}
