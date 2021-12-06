package com.bsuir.khviasko.hotel.service;

public class Query {
    private static Long accountId;
    private static String role;
    private static String prevCommand;
    private static Long roomId;

    public static void setAccountId(Long accountId) {
        Query.accountId = accountId;
    }

    public static Long getAccountId() {
        return accountId;
    }

    public static void setRole(String role) {
        Query.role = role;
    }

    public static String getRole() {
        return role;
    }

    public static void setPrevCommand(String prevCommand) {
        Query.prevCommand = prevCommand;
    }

    public static String getPrevCommand() {
        return prevCommand;
    }

    public static void setRoomId(Long roomId) {
        Query.roomId = roomId;
    }

    public static Long getRoomId() {
        return roomId;
    }

    private Query(){}
}
