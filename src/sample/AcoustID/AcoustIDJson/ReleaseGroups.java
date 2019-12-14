package sample.AcoustID.AcoustIDJson;

import java.util.ArrayList;
import java.util.List;

public class ReleaseGroups {
    List<Artists> artists= new ArrayList<>();
    String id;
    String title;
    String type;

    public List<Artists> getArtists() {
        return artists;
    }

    public String getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
