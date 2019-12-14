package sample.AcoustID.MusicBrianzJson;

        import java.util.ArrayList;
        import java.util.List;

public class Release {
    String title;
    String packagingid;
    String barcode;
    String status;
    String country;
    String Date;
    String id;
    List<ReleaseEvent> releaseevents= new ArrayList<>();
    String statusId;
  TextRepresentation textrepresentation;
    String quality;

    public String getTitle() {
        return title;
    }

    public String getPackagingId() {
        return packagingid;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getStatus() {
        return status;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
        return Date;
    }

    public String getId() {
        return id;
    }

    public List<ReleaseEvent> getReleaseEvents() {
        return releaseevents;
    }

    public String getStatusId() {
        return statusId;
    }

    public TextRepresentation getTextRepresentation() {
        return textrepresentation;
    }

    public String getQuality() {
        return quality;
    }
}
