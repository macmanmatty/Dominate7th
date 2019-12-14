package sample.Library;

import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import sample.Library.CueSheets.CueSheet;

import java.util.ArrayList;
import java.util.List;

public class Album {
    protected List<AudioInformation> tracks= new ArrayList<>();
    protected List<String> imagePaths= new ArrayList<>();
   
    protected String albumName="";
    protected String discID="";
    protected String genre="";
    protected String comment="";
    protected String year="";
    protected String discNo="";
    protected String discTotal="";
    protected String artist="";
    protected int numberOfTracks;
    protected long totalTime;
    protected String audioFileFormat;
    protected boolean remaster;
    protected boolean bounsTracks;


    public List<AudioInformation> getTracks() {
        return tracks;
    }

    public void setTracks(List<AudioInformation> tracks) {
        this.tracks = tracks;
    }
    

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getDiscID() {
        return discID;
    }

    public void setDiscID(String discID) {
        this.discID = discID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDiscNo() {
        return discNo;
    }

    public void setDiscNo(String discNo) {
        this.discNo = discNo;
    }

    public String getDiscTotal() {
        return discTotal;
    }

    public void setDiscTotal(String discTotal) {
        this.discTotal = discTotal;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }





    public boolean isRemaster() {
        return remaster;
    }

    public void setRemaster(boolean remaster) {
        this.remaster = remaster;
    }

    public boolean isBounsTracks() {
        return bounsTracks;
    }

    public void setBounsTracks(boolean bounsTracks) {
        this.bounsTracks = bounsTracks;
    }

    public String getAudioFileFormat() {
        return audioFileFormat;
    }

    public void setAudioFileFormat(String audioFileFormat) {
        this.audioFileFormat = audioFileFormat;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }
}
