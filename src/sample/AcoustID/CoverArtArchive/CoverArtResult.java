package sample.AcoustID.CoverArtArchive;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class CoverArtResult {
  private  List<Image> images=  new ArrayList();
   private String release;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}
