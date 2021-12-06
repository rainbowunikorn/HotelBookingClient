package com.bsuir.khviasko.hotel.controller.menu;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.bsuir.khviasko.hotel.service.Query;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class AdminMenuController {

    @FXML
    private Button usersButton;
    @FXML
    private Button roomsButton;
    @FXML
    private Button reservationsButton;
    @FXML
    private Button profitChartButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button backButton;

    @FXML
    void initialize() {

        usersButton.setOnAction(event -> {
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

        roomsButton.setOnAction(event -> {
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

        reservationsButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/reservations.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        profitChartButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/profitChart.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        reportButton.setOnAction(event -> {
            File file = new File("Order_report(" + new Date().toString().replace(':', '-') + ").txt");
            try {
                PrintWriter printWriter = new PrintWriter(file);
                Gson gson = new Gson();
                QueryDTO queryDTO = new QueryDTO();
                queryDTO.setAccountId(Query.getAccountId());
                queryDTO.setCommand("view_reservations");

                SocketService.writeLine(gson.toJson(queryDTO));
                JsonArray contracts = gson.fromJson(SocketService.readLine(), JsonArray.class);

                printWriter.println("Отчёт по заказам");
                printWriter.println("Общее количество заказов: " + contracts.size());
                printWriter.println("Заказы:");
                printWriter.println();
                for (int i = 0; i < contracts.size(); i++) {
                    printWriter.println(
                            "Заказ ID:" + contracts.get(i).getAsJsonObject().get("id").getAsLong());
                    printWriter.println(
                            "Пользователь ID: " + contracts.get(i).getAsJsonObject().get("user").getAsJsonObject().get("id").getAsString());
                    printWriter.println(
                            "Имя пользователя: " + contracts.get(i).getAsJsonObject().get("user").getAsJsonObject().get("firstname").getAsString());
                    printWriter.println(
                            "Фамилия пользователя: " + contracts.get(i).getAsJsonObject().get("user").getAsJsonObject().get("surname").getAsString());
                    printWriter.println(
                            "Номер комнаты: " + contracts.get(i).getAsJsonObject().get("room").getAsJsonObject().get("id").getAsString());
                    printWriter.println(
                            "Тип комнаты: " + contracts.get(i).getAsJsonObject().get("room").getAsJsonObject().get("roomType").getAsString());
                    printWriter.println(
                            "Стоимость: " + contracts.get(i).getAsJsonObject().get("totalPrice").getAsString());
                    printWriter.println(
                            "Статус: " + contracts.get(i).getAsJsonObject().get("reservationStatus").getAsJsonObject().get("statusValue").getAsString());

                    printWriter.println();
                }
                printWriter.print("Конец отчёта");
                printWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        backButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
                Query.setRole("");
                Query.setAccountId(0L);
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
