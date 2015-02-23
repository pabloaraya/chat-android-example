package com.pabloaraya.client;

/**
 * Created by pablo on 2/22/15.
 */
public class MessageModel {
    private String message;
    private String name;

    public MessageModel(String message, String name){
        this.setMessage(message);
        this.setName(name);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
