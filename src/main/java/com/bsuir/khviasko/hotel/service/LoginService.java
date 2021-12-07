package com.bsuir.khviasko.hotel.service;

import com.bsuir.khviasko.hotel.configuration.SocketService;
import com.bsuir.khviasko.hotel.dto.QueryDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LoginService {
    public String login(String login, String password) {
        try {
            Gson gson = new Gson();
            QueryDTO query = new QueryDTO();
            query.setCommand("login");
            SocketService.writeLine(gson.toJson(query));
            SocketService.writeLine(login);
            SocketService.writeLine(password);

            String response = SocketService.getReader().readLine();
            if (response.contains("REJECTED")) {
                return "REJECTED";
            }
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            Query.setUserId(jsonObject.get("id").getAsLong());
            Query.setRole(jsonObject.get("role").getAsJsonObject().get("roleValue").getAsString());
        } catch (Exception e) {
            System.out.println("Error: server disconnected");
        }
        return "SUCCESS";
    }
}
