package sample.Library.CueSheets;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import sample.Library.Album;
import sample.Library.AudioInformation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class CueSheet extends Album {
     private  String cueFilePath="";
     private String audioFilePath;
     private String fileName;
     private transient AudioFile audioFile;
     private boolean hasFile;
     private String   audioFileBitRate;
    public List<AudioInformation> getTracks() {
        return tracks;
    }
    public void setTracks(List<AudioInformation> tracks) {
        this.tracks = tracks;
    }
    public String getCueFilePath() {
        return cueFilePath;
    }
    public void setCueFilePath(String cueFilePath) {
        this.cueFilePath = cueFilePath;
    }
    public String getAudioFilePath() {
        return audioFilePath;
    }
    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
        System.out.println(audioFilePath);
        for(int count=0; count<numberOfTracks; count++){
            tracks.get(count).setAudioFilePath(audioFilePath);
        }
    }
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
        for(int count=0; count<numberOfTracks; count++){
            tracks.get(count).setAlbum(albumName);
        }
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
        for(int count=0; count<numberOfTracks; count++){
            tracks.get(count).setGenre(genre);
        }
    }
    public void setDiscNo(String discNo) {
        this.discNo=discNo;
        for(int count=0; count<numberOfTracks; count++){
            tracks.get(count).setDiscNo(discNo);
        }
    }
    public int getNumberOfTracks() {
        return numberOfTracks;
    }
    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
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
        for(int count=0; count<numberOfTracks; count++){
            tracks.get(count).setYear(year);
        }
    }
    public void setDiscTotal(String discTotal) {
        this.discTotal=discTotal;
        for(int count=0; count<numberOfTracks; count++){
            tracks.get(count).setDiscTotal(discTotal);
        }
    }
    public void setArtist(String artist) {
        this.discTotal=discTotal;
        for(int count=0; count<numberOfTracks; count++){
            tracks.get(count).setArtist(artist);
        }
    }
    public String getFileName() {
        return fileName;
    }
    public String getArtist() {
        return artist;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getDiscNo() {
        return discNo;
    }
    public String getDiscTotal() {
        return discTotal;
    }
    public long getTotalTime() {
        return totalTime;
    }
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
    public AudioFile getAudioFile() {
        if(audioFile==null) {
            try {
                audioFile = AudioFileIO.read(new File(audioFilePath));
            } catch (CannotReadException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;

            } catch (TagException e) {
                e.printStackTrace();
                return null;

            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
                return null;

            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
                return null;

            }
        }
        return  audioFile;
    }
    public File getPhysicalFile(){
        File file= new File (audioFilePath);
        if(file.exists()==true){
            return file;
        }
        return null;
    }
    public void clearAudioFile(){
        audioFile=null;
    }
    public void setAudioFile(AudioFile audioFile) {
        this.audioFile = audioFile;
        this.audioFilePath=audioFile.getFile().getAbsolutePath();
        this.totalTime=audioFile.getAudioHeader().getTrackLength();
    }
    public boolean hasAudioFile(){
        hasFile=new File(audioFilePath).exists();
        return hasFile;
    }
    public String getAudioFileBitRate() {
        return audioFileBitRate;
    }
    public void setAudioFileBitRate(String audioFileBitRate) {
        this.audioFileBitRate = audioFileBitRate;
    }
}
