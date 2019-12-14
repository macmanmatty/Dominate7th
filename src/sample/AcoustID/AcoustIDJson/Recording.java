package sample.AcoustID.AcoustIDJson;

import java.util.ArrayList;
import java.util.List;

public class Recording {
       List<Artists> artists= new ArrayList<>();
    String id;

    List<ReleaseGroups> releasegroups= new ArrayList<>();
        String title;



        public String getId() {
            return id;
        }

    public List<Artists> getArtists() {
        return artists;
    }

    public String getTitle() {
        return title;
    }

    public List<ReleaseGroups> getReleaseGroups() {
        return releasegroups;
    }
}
