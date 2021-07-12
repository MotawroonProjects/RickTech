package com.app.ricktech.models;

import java.io.Serializable;

public class NotificationCount extends StatusResponse implements Serializable {
    private int data;

    public int getData() {
        return data;
    }
}
