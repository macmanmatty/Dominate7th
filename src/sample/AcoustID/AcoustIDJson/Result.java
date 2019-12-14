package sample.AcoustID.AcoustIDJson;


import java.util.ArrayList;
import java.util.List;

public class Result {
    String id;
    List<Recording> recordings = new ArrayList<>();
    String score;

    public String getId() {
        return id;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public String getScore() {
        return score;
    }
}

