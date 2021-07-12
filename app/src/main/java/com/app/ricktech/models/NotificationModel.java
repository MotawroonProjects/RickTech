package com.app.ricktech.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private String id;
    private String title;
    private String desc;
    private String notification_date;
    private String action_type;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public String getAction_type() {
        return action_type;
    }
}
