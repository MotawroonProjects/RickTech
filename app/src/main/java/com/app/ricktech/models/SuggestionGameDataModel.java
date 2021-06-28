package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SuggestionGameDataModel extends StatusResponse implements Serializable {
    private List<SuggestionGameModel> data;

    public List<SuggestionGameModel> getData() {
        return data;
    }
}
