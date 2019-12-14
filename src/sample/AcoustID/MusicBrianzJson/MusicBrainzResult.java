package sample.AcoustID.MusicBrianzJson;

import java.util.ArrayList;
import java.util.List;

public class MusicBrainzResult {
    String id;
    boolean video;
    int length;
    String disambiguation;
    String title;
    List<Artist> artistcredit= new ArrayList<>();
    List<Release> releases= new ArrayList<>();

    public String getId() {
        return id;
    }

    public boolean isVideo() {
        return video;
    }

    public int getLength() {
        return length;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public String getTitle() {
        return title;
    }

    public List<Artist> getArtistCredit() {
        return artistcredit;
    }

    public List<Release> getReleases() {
        return releases;
    }
}
