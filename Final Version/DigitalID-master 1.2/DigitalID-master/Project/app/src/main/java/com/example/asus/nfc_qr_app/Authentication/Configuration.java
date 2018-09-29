package com.example.asus.nfc_qr_app.Authentication;


public class Configuration {
    private static Configuration ourInstance = new Configuration();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
    }

    private String chatServerIp = "192.168.8.100";
    private String chatServerPort = "9000";

    public String getChatServerIp() {
        return chatServerIp;
    }

    public String getChatServerPort() {
        return chatServerPort;
    }
}
