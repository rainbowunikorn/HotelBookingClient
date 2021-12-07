package com.bsuir.khviasko.hotel.controller.admin;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.bsuir.khviasko.hotel.dto.RoomDTO;
import com.bsuir.khviasko.hotel.dto.UserDTO;
import com.bsuir.khviasko.hotel.service.Query;
import com.bsuir.khviasko.hotel.stageConfig.StageConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

public class RoomsController {
    @FXML
    private Button backButton;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<RoomDTO> roomTable;

    @FXML
    private TableColumn<RoomDTO, String> number;

    @FXML
    private TableColumn<RoomDTO, String> type;

    @FXML
    private TableColumn<RoomDTO, String> capacity;

    @FXML
    private TableColumn<RoomDTO, String> price;

    @FXML
    private TableColumn<RoomDTO, String> description;

    @FXML
    void initialize() {
        loadTable();

        backButton.setOnAction(event -> {
            try {
                Parent root;
                if(Constants.ADMIN_ROLE.equalsIgnoreCase(Query.getRole())){
                    root = FXMLLoader.load(getClass().getResource("/fxml/adminMenu.fxml"));
                }else {
                    root = FXMLLoader.load(getClass().getResource("/fxml/userMenu.fxml"));
                }
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/addRoom.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        editButton.setOnAction(event -> {
            try {
                RoomDTO roomDTO = roomTable.getSelectionModel().getSelectedItem();
                Query.setRoomId(roomDTO.getId());
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/editRoom.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteButton.setOnAction(event -> {
            RoomDTO roomDTO = roomTable.getSelectionModel().getSelectedItem();
            QueryDTO queryDTO = new QueryDTO();
            queryDTO.setRoomId(roomDTO.getId());
            queryDTO.setCommand("delete_room");
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
        queryDTO.setCommand("view_rooms");
        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            JsonArray rooms = gson.fromJson(SocketService.readLine(), JsonArray.class);
            ObservableList<RoomDTO> roomObservableArrayList = FXCollections.observableArrayList();

            if(rooms.size() ==0 ){
                roomTable.setItems(roomObservableArrayList);
                return;
            }

            for (int i = 0; i < rooms.size(); i++) {
                roomObservableArrayList.add(new RoomDTO());

                roomObservableArrayList.get(i).setId(rooms.get(i).getAsJsonObject().get("id").getAsLong());
                roomObservableArrayList.get(i).setRoomNumber(rooms.get(i).getAsJsonObject().get("roomNumber").getAsString());
                roomObservableArrayList.get(i).setRoomType(rooms.get(i).getAsJsonObject().get("roomType").getAsString());
                roomObservableArrayList.get(i).setCapacity(rooms.get(i).getAsJsonObject().get("capacity").getAsInt());
                roomObservableArrayList.get(i).setPrice(rooms.get(i).getAsJsonObject().get("price").getAsDouble());
                roomObservableArrayList.get(i).setDescription(rooms.get(i).getAsJsonObject().get("description").getAsString());

                number.setCellValueFactory(new PropertyValueFactory<RoomDTO, String>("roomNumber"));
                type.setCellValueFactory(new PropertyValueFactory<RoomDTO, String>("roomType"));
                capacity.setCellValueFactory(new PropertyValueFactory<RoomDTO, String>("capacity"));
                price.setCellValueFactory(new PropertyValueFactory<RoomDTO, String>("price"));
                description.setCellValueFactory(new PropertyValueFactory<RoomDTO, String>("description"));

                roomTable.setItems(roomObservableArrayList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
