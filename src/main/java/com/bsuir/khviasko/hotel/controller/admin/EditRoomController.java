package com.bsuir.khviasko.hotel.controller.admin;

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
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditRoomController {
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
    private Button editButton;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        Gson gson = new Gson();
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setRoomId(Query.getRoomId());
        queryDTO.setCommand("view_room");
        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            String response = SocketService.getReader().readLine();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            numberField.setText(jsonObject.get("roomNumber").getAsString());
            typeField.setText(jsonObject.get("roomType").getAsString());
            capacityField.setText(jsonObject.get("capacity").getAsString());
            priceField.setText(jsonObject.get("price").getAsString());
            descriptionField.setText(jsonObject.get("description").getAsString());


            editButton.setOnAction(event -> {

                jsonObject.remove("roomNumber");
                jsonObject.remove("roomType");
                jsonObject.remove("capacity");
                jsonObject.remove("price");
                jsonObject.remove("description");

                jsonObject.addProperty("roomNumber", numberField.getText());
                jsonObject.addProperty("roomType", typeField.getText());
                jsonObject.addProperty("capacity", capacityField.getText());
                jsonObject.addProperty("price", priceField.getText());
                jsonObject.addProperty("description", descriptionField.getText());


                QueryDTO queryDTO2 = new QueryDTO();
                queryDTO2.setRoomId(Query.getRoomId());
                queryDTO2.setCommand("edit_room");
                try {
                    SocketService.writeLine(gson.toJson(queryDTO2));
                    SocketService.writeLine(jsonObject.toString());
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/editRoom.fxml"));
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
