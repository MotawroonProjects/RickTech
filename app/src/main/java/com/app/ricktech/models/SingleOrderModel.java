package com.app.ricktech.models;

import java.io.Serializable;

public class SingleOrderModel extends StatusResponse implements Serializable {
    private OrderModel data;

    public OrderModel getData() {
        return data;
    }
}
