package com.bsuir.khviasko.hotel.controller.admin;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.constants.Constants;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
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

public class UsersController {
    @FXML
    private Button backButton;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, String> firstname;

    @FXML
    private TableColumn<UserDTO, String> surname;

    @FXML
    private TableColumn<UserDTO, String> login;

    @FXML
    private TableColumn<UserDTO, String> role;

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

        addButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/addUser.fxml"));
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
                UserDTO user = userTable.getSelectionModel().getSelectedItem();
                Query.setUserId(user.getId());
                Query.setPrevCommand("view_users");
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/editUser.fxml"));
                StageConfig.stage.setTitle(Constants.HOTEL_TITLE);
                StageConfig.stage.setScene(new Scene(root, 800, 450));
                StageConfig.stage.setResizable(false);
                StageConfig.stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteButton.setOnAction(event -> {
            UserDTO user = userTable.getSelectionModel().getSelectedItem();
            QueryDTO queryDTO = new QueryDTO();
            queryDTO.setUserId(user.getId());
            queryDTO.setCommand("delete_user");
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
        queryDTO.setUserId(Query.getUserId());
        queryDTO.setCommand("view_users");
        try {
            SocketService.writeLine(gson.toJson(queryDTO));
            JsonArray users = gson.fromJson(SocketService.readLine(), JsonArray.class);
            ObservableList<UserDTO> accountObservableArrayList = FXCollections.observableArrayList();

            if(users.size() ==0 ){
                userTable.setItems(accountObservableArrayList);
                return;
            }

            for (int i = 0; i < users.size(); i++) {
                accountObservableArrayList.add(new UserDTO());

                accountObservableArrayList.get(i).setId(users.get(i).getAsJsonObject().get("id").getAsLong());
                accountObservableArrayList.get(i).setFirstname(users.get(i).getAsJsonObject().get("firstname").getAsString());
                accountObservableArrayList.get(i).setSurname(users.get(i).getAsJsonObject().get("surname").getAsString());
                accountObservableArrayList.get(i).setLogin(users.get(i).getAsJsonObject().get("login").getAsString());
                accountObservableArrayList.get(i).setRole(users.get(i).getAsJsonObject().get("role").getAsJsonObject().get("roleValue").getAsString());

                firstname.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstname"));
                surname.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("surname"));
                login.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("login"));
                role.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("role"));

                userTable.setItems(accountObservableArrayList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
