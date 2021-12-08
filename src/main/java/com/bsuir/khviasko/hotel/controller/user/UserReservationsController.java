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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserReservationsController {
    @FXML
    private Text number;

    @FXML
    private Text type;

    @FXML
    private Text capacity;

    @FXML
    private Text status;

    @FXML
    private Text date;

    @FXML
    private Text price;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        Gson gson = new Gson();
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setUserId(Query.getUserId());
        queryDTO.setCommand("view_user_reservation");
        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            String response = SocketService.getReader().readLine();


            if (response.contains("EMPTY")) {
                number.setText("");
                type.setText("");
                capacity.setText("");
                status.setText("");
                price.setText("");
                date.setText("");
            } else {

                JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                number.setText(jsonObject.get("room").getAsJsonObject().get("roomNumber").getAsString());
                type.setText(jsonObject.get("room").getAsJsonObject().get("roomType").getAsString());
                capacity.setText(jsonObject.get("room").getAsJsonObject().get("capacity").getAsString());
                status.setText(jsonObject.get("reservationStatus").getAsJsonObject().get("statusValue").getAsString());
                price.setText(jsonObject.get("totalPrice").getAsString());

                final JsonObject jsonDate = jsonObject.get("createDate").getAsJsonObject().get("date").getAsJsonObject();
                LocalDate localDate = LocalDate.of(jsonDate.get("year").getAsInt(), jsonDate.get("month").getAsInt(), jsonDate.get("day").getAsInt());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String formattedString = localDate.format(formatter);
                date.setText(formattedString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
