package com.bsuir.khviasko.hotel.controller.admin;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProfitChartController {

    @FXML
    private Button backButton;

    @FXML
    private LineChart<String, Double> profitChart;

    @FXML
    private CategoryAxis dateX;

    @FXML
    private NumberAxis priceY;


    @FXML
    void initialize() {
        dateX.setLabel("Дата");
        priceY.setLabel("Прибыль");
        profitChart.setTitle("График прибыли");
        XYChart.Series series = new XYChart.Series();

        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setCommand("view_reservations");
        Gson gson = new Gson();
        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            JsonArray reservations = gson.fromJson(SocketService.readLine(), JsonArray.class);

            for (int i = 0; i < reservations.size(); i++) {

                final JsonObject jsonObject = reservations.get(i).getAsJsonObject().get("createDate").getAsJsonObject();
                final JsonObject jsonDate = jsonObject.get("date").getAsJsonObject();

                LocalDate date = LocalDate.of(jsonDate.get("year").getAsInt(), jsonDate.get("month").getAsInt(), jsonDate.get("day").getAsInt());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String formattedString = date.format(formatter);

                series.getData().add(new XYChart.Data(formattedString,
                        reservations.get(i).getAsJsonObject().get("totalPrice").getAsDouble()));
            }
            profitChart.getData().add(series);
        } catch (IOException e) {
            e.printStackTrace();
        }

        backButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/adminMenu.fxml"));
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
