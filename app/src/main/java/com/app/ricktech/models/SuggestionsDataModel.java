package com.app.ricktech.models;

import java.io.Serializable;
import java.util.List;

public class SuggestionsDataModel extends StatusResponse implements Serializable {
    private List<SuggestionModel> data;

    public List<SuggestionModel> getData() {
        return data;
    }
}
