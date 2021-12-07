package com.bsuir.khviasko.hotel.controller.admin;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.bsuir.khviasko.hotel.dto.ReservationDTO;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;

public class ReservationsController {
    @FXML
    private Button backButton;

    @FXML
    private Button approveButton;

    @FXML
    private Button declineButton;

    @FXML
    private TableView<ReservationDTO> reservationTable;

    @FXML
    private TableColumn<ReservationDTO, String> userFirstname;

    @FXML
    private TableColumn<ReservationDTO, String> userSurname;

    @FXML
    private TableColumn<ReservationDTO, String> roomNumber;

    @FXML
    private TableColumn<ReservationDTO, String> roomType;

    @FXML
    private TableColumn<ReservationDTO, Double> totalPrice;

    @FXML
    private TableColumn<ReservationDTO, LocalDate> createDate;

    @FXML
    private TableColumn<ReservationDTO, String> status;

    @FXML
    void initialize() {
        loadTable();

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

        approveButton.setOnAction(event -> {
            ReservationDTO reservationDTO = reservationTable.getSelectionModel().getSelectedItem();
            QueryDTO queryDTO = new QueryDTO();
            queryDTO.setReservationId(reservationDTO.getId());
            queryDTO.setCommand("approve_reservation");
            Gson gson = new Gson();
            try {
                SocketService.writeLine(gson.toJson(queryDTO));
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadTable();
        });

        declineButton.setOnAction(event -> {
            ReservationDTO reservationDTO = reservationTable.getSelectionModel().getSelectedItem();
            QueryDTO queryDTO = new QueryDTO();
            queryDTO.setReservationId(reservationDTO.getId());
            queryDTO.setCommand("decline_reservation");
            Gson gson = new Gson();
            try {
                SocketService.writeLine(gson.toJson(queryDTO));
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadTable();
        });
    }

    private void loadTable() {
        Gson gson = new Gson();
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setCommand("view_reservations");
        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            JsonArray reservations = gson.fromJson(SocketService.readLine(), JsonArray.class);
            ObservableList<ReservationDTO> reservationObservableArrayList = FXCollections.observableArrayList();

            if (reservations.size() == 0) {
                reservationTable.setItems(reservationObservableArrayList);
                return;
            }

            for (int i = 0; i < reservations.size(); i++) {
                reservationObservableArrayList.add(new ReservationDTO());

                reservationObservableArrayList.get(i).setId(reservations.get(i).getAsJsonObject().get("id").getAsLong());
                reservationObservableArrayList.get(i).setRoomNumber(reservations.get(i).getAsJsonObject().get("room").getAsJsonObject().get("roomNumber").getAsString());
                reservationObservableArrayList.get(i).setRoomType(reservations.get(i).getAsJsonObject().get("room").getAsJsonObject().get("roomType").getAsString());
                reservationObservableArrayList.get(i).setFirstname(reservations.get(i).getAsJsonObject().get("user").getAsJsonObject().get("firstname").getAsString());
                reservationObservableArrayList.get(i).setSurname(reservations.get(i).getAsJsonObject().get("user").getAsJsonObject().get("surname").getAsString());
                reservationObservableArrayList.get(i).setTotalPrice(reservations.get(i).getAsJsonObject().get("totalPrice").getAsDouble());
                reservationObservableArrayList.get(i).setStatus(reservations.get(i).getAsJsonObject().get("reservationStatus").getAsJsonObject().get("statusValue").getAsString());

                final JsonObject jsonObject = reservations.get(i).getAsJsonObject().get("createDate").getAsJsonObject();
                final JsonObject jsonDate = jsonObject.get("date").getAsJsonObject();

                LocalDate date = LocalDate.of(jsonDate.get("year").getAsInt(), jsonDate.get("month").getAsInt(), jsonDate.get("day").getAsInt());
                reservationObservableArrayList.get(i).setCreateDate(date);

                roomNumber.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("roomNumber"));
                roomType.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("roomType"));
                userFirstname.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("firstname"));
                userSurname.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("surname"));
                totalPrice.setCellValueFactory(new PropertyValueFactory<ReservationDTO, Double>("totalPrice"));
                status.setCellValueFactory(new PropertyValueFactory<ReservationDTO, String>("status"));
                createDate.setCellValueFactory(new PropertyValueFactory<ReservationDTO, LocalDate>("createDate"));

                reservationTable.setItems(reservationObservableArrayList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
