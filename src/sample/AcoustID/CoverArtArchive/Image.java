package sample.AcoustID.CoverArtArchive;

import java.util.ArrayList;
import java.util.List;

public class Image {
    private List<String> types= new ArrayList<>();
    private  boolean front;
    private boolean back;
    private long edit;
    private String image;
    private String comment;
    private boolean approved;
    private String id;
    private List<String> thumbnails= new ArrayList<>();

    public List<String> getTypes() {
        return types;
    }

    public boolean isFront() {
        return front;
    }

    public boolean isBack() {
        return back;
    }

    public long getEdit() {
        return edit;
    }

    public String getImage() {
        return image;
    }

    public String getComment() {
        return comment;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getId() {
        return id;
    }

    public List<String> getThumbnails() {
        return thumbnails;
    }
}
