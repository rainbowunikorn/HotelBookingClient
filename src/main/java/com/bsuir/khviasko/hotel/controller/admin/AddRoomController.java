package com.bsuir.khviasko.hotel.controller.admin;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddRoomController {

    @FXML
    private TextField numberField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        addButton.setOnAction(event -> {
            String number = numberField.getText().trim();
            String type = typeField.getText().trim();
            String capacity = capacityField.getText().trim();
            String price = priceField.getText().trim();
            String description = descriptionField.getText().trim();

            Gson queryGson = new Gson();
            QueryDTO query = new QueryDTO();
            query.setCommand("add_room");

            try {
                SocketService.writeLine(queryGson.toJson(query));
                SocketService.writeLine(number);
                SocketService.writeLine(type);
                SocketService.writeLine(capacity);
                SocketService.writeLine(price);
                SocketService.writeLine(description);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        backButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/rooms.fxml"));
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
