package com.bsuir.khviasko.hotel.controller.user;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.bsuir.khviasko.hotel.dto.RoomDTO;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.io.IOException;

public class RoomChartController {
    @FXML
    private Button backButton;

    @FXML
    private BarChart<String, Double> roomChart;

    @FXML
    private CategoryAxis nameX;

    @FXML
    private NumberAxis nameY;

    @FXML
    void initialize() {
        nameX.setLabel("Тип");
        nameY.setLabel("Средняя стоимость");

        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setCommand("view_rooms");
        Gson gson = new Gson();


        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            JsonArray rooms = gson.fromJson(SocketService.readLine(), JsonArray.class);

            XYChart.Series series = new XYChart.Series();

            for (int i = 0; i < rooms.size(); i++) {
                series.getData().add(new XYChart.Data(rooms.get(i).getAsJsonObject().get("roomType").getAsString(),
                        rooms.get(i).getAsJsonObject().get("price").getAsDouble()));
            }

            roomChart.getData().add(series);

        } catch (IOException e) {
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
