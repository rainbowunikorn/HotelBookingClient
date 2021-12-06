package com.bsuir.khviasko.hotel.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketService {
    private static Socket socket;
    private static BufferedWriter writer;
    private static BufferedReader reader;

    public static void init() {
        try {
            socket = new Socket("localhost", 8080);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Server is off");
        }
    }

    public static void close() {
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedWriter getWriter() {
        return writer;
    }

    public static BufferedReader getReader() {
        return reader;
    }

    public static void writeLine(String line) throws IOException {
        writer.write(line + "\n");
        writer.flush();
    }

    public static String readLine() throws IOException {
        return reader.readLine();
    }
}
