package com.bsuir.khviasko.hotel.controller.user;

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
import javafx.scene.text.Text;

public class UserController {
    @FXML
    private Text firstname;

    @FXML
    private Text surname;

    @FXML
    private Text login;

    @FXML
    private Text role;

    @FXML
    private Button editButton;

    @FXML
    private Button backButton;

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

            firstname.setText(jsonObject.get("firstname").getAsString());
            surname.setText(jsonObject.get("surname").getAsString());
            login.setText(jsonObject.get("login").getAsString());
            role.setText(jsonObject.get("role").getAsJsonObject().get("roleValue").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        editButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/editUser.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        backButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/userMenu.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
