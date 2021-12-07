package com.bsuir.khviasko.hotel.controller.admin;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUserController {
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
        createButton.setOnAction(event -> {
            String firstname = firstnameField.getText().trim();
            String surname = surnameField.getText().trim();
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();

            JsonObject accountJson = new JsonObject();

            validateFirstname(firstname, accountJson);
            validateSurname(surname, accountJson);
            validateLogin(login, accountJson);
            validatePassword(password, accountJson);

            boolean status = validateFirstname(firstname, accountJson) && validateSurname(surname, accountJson) &&
                    validateLogin(login, accountJson) && validatePassword(password, accountJson);
            if (!status) return;

            Gson queryGson = new Gson();
            QueryDTO query = new QueryDTO();
            query.setCommand("registration");

            try {
                SocketService.writeLine(queryGson.toJson(query));
                SocketService.writeLine(firstname);
                SocketService.writeLine(surname);
                SocketService.writeLine(login);
                SocketService.writeLine(password);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        backButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/users.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validateLogin(String login, JsonObject jsonObject) {
        String regex = "^.{5,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(login);
        if (login.isEmpty() || !matcher.matches()) {
            loginValidationField.setText("Введите корректный логин");
            return false;
        }
        jsonObject.addProperty("login", login);
        loginValidationField.setText("");
        return true;
    }

    private boolean validatePassword(String password, JsonObject jsonObject) {
        String regex = "^.{5,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (password.isEmpty() || !matcher.matches()) {
            passwordValidationField.setText("Введите корректный пароль");
            return false;
        }
        jsonObject.addProperty("password", password);
        passwordValidationField.setText("");
        return true;
    }

    private boolean validateFirstname(String firstname, JsonObject jsonObject) {
        String regex = "^[А-ЯA-Z][а-яa-z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(firstname);
        if (firstname.isEmpty() || !matcher.matches()) {
            firstnameValidationField.setText("Введите корректное имя");
            return false;
        }
        jsonObject.addProperty("firstname", firstname);
        firstnameValidationField.setText("");
        return true;
    }

    private boolean validateSurname(String surname, JsonObject jsonObject) {
        String regex = "^[А-ЯA-Z][а-яa-z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(surname);
        if (surname.isEmpty() || !matcher.matches()) {
            surnameValidationField.setText("Введите корректную фамилию");
            return false;
        }
        jsonObject.addProperty("surname", surname);
        surnameValidationField.setText("");
        return true;
    }

}
