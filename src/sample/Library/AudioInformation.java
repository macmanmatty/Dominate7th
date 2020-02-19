package sample.Library;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.Utilities.FormatTime;
import sample.Windows.OptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class AudioInformation { // plain old Data  class  that has simple string  properties  for use with javafx table
    private transient AudioFile audioFile;
    private transient File physicalFile;
    private String audioFilePath;
    private SimpleStringProperty trackLength = new SimpleStringProperty("");
    private SimpleStringProperty bitrate = new SimpleStringProperty("");
    private SimpleStringProperty artist = new SimpleStringProperty("");
    private SimpleStringProperty album = new SimpleStringProperty("");
    private SimpleStringProperty bpm = new SimpleStringProperty("");
    private SimpleStringProperty catalogNo = new SimpleStringProperty("");
    private SimpleStringProperty comment = new SimpleStringProperty("");
    private SimpleStringProperty composer = new SimpleStringProperty("");
    private SimpleStringProperty conductor = new SimpleStringProperty("");
    private SimpleStringProperty copyright = new SimpleStringProperty("");
    private SimpleStringProperty country = new SimpleStringProperty("");
    private SimpleStringProperty custom1 = new SimpleStringProperty("");
    private SimpleStringProperty custom2 = new SimpleStringProperty("");
    private SimpleStringProperty discNo = new SimpleStringProperty("");
    private SimpleStringProperty discTotal = new SimpleStringProperty("");
    private SimpleStringProperty encoder = new SimpleStringProperty("");
    private SimpleStringProperty engineer = new SimpleStringProperty("");
    private SimpleStringProperty ensemble = new SimpleStringProperty("");
    private SimpleStringProperty genre = new SimpleStringProperty("");
    private SimpleStringProperty group = new SimpleStringProperty("");
    private SimpleStringProperty instruemnt = new SimpleStringProperty("");
    private SimpleStringProperty laugauge = new SimpleStringProperty("");
    private SimpleStringProperty lyricist = new SimpleStringProperty("");
    private SimpleStringProperty lyrics = new SimpleStringProperty("");
    private SimpleStringProperty media = new SimpleStringProperty("");
    private SimpleStringProperty producer = new SimpleStringProperty("");
    private SimpleStringProperty originalArtist = new SimpleStringProperty("");
    private SimpleStringProperty ranking = new SimpleStringProperty("");
    private SimpleStringProperty rating = new SimpleStringProperty("");
    private SimpleStringProperty recordLabel = new SimpleStringProperty("");
    private SimpleStringProperty tempo = new SimpleStringProperty("");
    private SimpleStringProperty title = new SimpleStringProperty("");
    private SimpleStringProperty track = new SimpleStringProperty("");
    private SimpleStringProperty trackTotal = new SimpleStringProperty("");
    private SimpleStringProperty year = new SimpleStringProperty("");
    private SimpleStringProperty musicBrainzId= new SimpleStringProperty("");
    private  SimpleStringProperty acoustIDId= new SimpleStringProperty("");
    private SimpleIntegerProperty cueStart= new SimpleIntegerProperty( 0);
    private boolean hasCue;
    private long trackLengthNumber;
    private int plays;
    private String audioFormat;
    private List<String> albumArtPaths = new ArrayList<>();
    private boolean writeFieldsToFile;
    private long numberOfFrames;
    private boolean usesFrames;
    private boolean isTaggable=true;
    public AudioInformation() {
        writeFieldsToFile=true;
    }
    public AudioInformation(boolean writeFieldsToFile) {
        this.writeFieldsToFile = writeFieldsToFile;
        this.isTaggable=writeFieldsToFile;

    }
    private transient ExtractAudioInformation extractAudioInformation= new ExtractAudioInformation();
    public AudioInformation(AudioFile audioFile, String fileName, long time, String filePath, List<String> imagePaths, String trackLength, String bitrate, String artist, String album, String bpm, String catalogNo, String comment, String composer, String conductor, String copyright, String country, String custom1, String custom2, String discNo, String discTotal, String encoder, String engineer, String ensemble, String genre, String group, String instruemnt, String laugauge, String lyricist, String lyrics, String media, String producer, String originalArtist, String ranking, String rating, String recordLabel, String tempo, String title, String track, String trackTotal, String year, String musicBrainzId, String acoustIdId) {
        this.audioFile = audioFile;
        this.albumArtPaths = imagePaths;
        writeFieldsToFile=true;
        this.trackLengthNumber = time;
        this.audioFilePath = filePath;
        String[] fileNameParts = fileName.split("\\.");
        int length = fileNameParts.length;
        audioFormat = fileNameParts[length - 1];
        try {
            this.trackLength = new SimpleStringProperty(FormatTime.formatTimeWithColons(trackLengthNumber));
        } catch (NumberFormatException n) {
            this.trackLength = new SimpleStringProperty(trackLength);
        }
        this.bitrate = new SimpleStringProperty(bitrate);
        this.artist = new SimpleStringProperty(artist);
        this.album = new SimpleStringProperty(album);
        this.bpm = new SimpleStringProperty(bpm);
        this.catalogNo = new SimpleStringProperty(catalogNo);
        this.comment = new SimpleStringProperty(comment);
        this.composer = new SimpleStringProperty(composer);
        this.conductor = new SimpleStringProperty(conductor);
        this.copyright = new SimpleStringProperty(copyright);
        this.country = new SimpleStringProperty(country);
        this.custom1 = new SimpleStringProperty(custom1);
        this.custom2 = new SimpleStringProperty(custom2);
        this.discNo = new SimpleStringProperty(discNo);
        this.discTotal = new SimpleStringProperty(discTotal);
        this.encoder = new SimpleStringProperty(encoder);
        this.engineer = new SimpleStringProperty(engineer);
        this.ensemble = new SimpleStringProperty(ensemble);
        this.genre = new SimpleStringProperty(genre);
        this.group = new SimpleStringProperty(group);
        this.instruemnt = new SimpleStringProperty(instruemnt);
        this.laugauge = new SimpleStringProperty(laugauge);
        this.lyricist = new SimpleStringProperty(lyricist);
        this.lyrics = new SimpleStringProperty(lyrics);
        this.media = new SimpleStringProperty(media);
        this.producer = new SimpleStringProperty(producer);
        this.originalArtist = new SimpleStringProperty(originalArtist);
        this.ranking = new SimpleStringProperty(ranking);
        this.rating = new SimpleStringProperty(rating);
        this.recordLabel = new SimpleStringProperty(recordLabel);
        this.tempo = new SimpleStringProperty(tempo);
        this.title = new SimpleStringProperty(title);
        this.musicBrainzId= new SimpleStringProperty(musicBrainzId);
        this.acoustIDId= new SimpleStringProperty(acoustIdId);
        if (this.title.get().isEmpty()) {
            this.title.set(fileName);
        }
        this.track = new SimpleStringProperty(track);
        this.trackTotal = new SimpleStringProperty(trackTotal);
        this.year = new SimpleStringProperty(year);
        clearAudioFile();
    }
    public boolean loadAudioFile() {
        File file = new File(audioFilePath);
        try {
            audioFile = AudioFileIO.read(file);
        } catch (CannotReadException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (TagException e) {
            e.printStackTrace();
            return false;
        } catch (ReadOnlyFileException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public String getBitRate() {
        return bitrate.get();
    }
    public String getArtist() {
        return artist.get();
    }
    public void setArtist(String artist) {
        if (writeFieldsToFile == true) {

            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.ARTIST, artist, getAudioFile());
            if(written==true) {
                this.artist.set(artist);
            }


        }
            else{
                this.artist.set(artist);
            }
    }
    public String getAlbum() {
        return album.get();
    }
    public void setAlbum(String album) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.ALBUM, album, getAudioFile());
            if(written==true) {
                this.album.set(album);
            }
        }
        else{
            this.album.set(album);
        }
    }
    public String getBpm() {
        return bpm.get();
    }
    public String getCatalogNo() {
        return catalogNo.get();
    }
    public void setCatalogNo(String catalogNo) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.CATALOG_NO, catalogNo, getAudioFile());
            if(written==true) {
                this.catalogNo.set(catalogNo);
            }
        }
        else{
            this.catalogNo.set(catalogNo);
        }
    }
    public String getComment() {
        return comment.get();
    }
    public void setComment(String comment) {
            if (writeFieldsToFile == true) {
                boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.COMMENT, comment, getAudioFile());
                if(written==true) {
                    this.comment.set(comment);
                }


        }
        else{
            this.comment.set(comment);
        }
    }
    public String getComposer() {
        return composer.get();
    }
    public void setComposer(String composer) {
            if (writeFieldsToFile == true) {
                boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.COMPOSER, composer, getAudioFile());
                if(written==true) {
                    this.composer.set(composer);
                }
            }

        else{
            this.composer.set(composer);
        }
    }
    public String getConductor() {
        return conductor.get();
    }
    public void setConductor(String conductor) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.CONDUCTOR, conductor, getAudioFile());
            if(written==true) {
                this.conductor.set(conductor);
            }
        }
        else{
            this.conductor.set(conductor);
        }
    }
    public String getCopyright() {
        return copyright.get();
    }
    public void setCopyright(String copyright) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.COPYRIGHT, copyright, getAudioFile());
            if(written==true) {
                this.copyright.set(copyright);
            }
        }
        else{
            this.copyright.set(copyright);
        }
    }
    public String getCountry() {
        return country.get();
    }
    public void setCountry(String country) {
        if (writeFieldsToFile == true) {
            if (writeFieldsToFile == true) {
                boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.COUNTRY, country, getAudioFile());
                if(written==true) {
                    this.country.set(country);
                }
            }
        }
        else{
        this.country.set(country);
        }
    }

    public String getDiscNo() {
        return discNo.get();
    }
    public void setDiscNo(String discNo) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.DISC_NO, discNo, getAudioFile());
            if(written==true) {
                this.discNo.set(discNo);
            }

        }
        else{
            this.discNo.set(discNo);
        }
    }
    public String getDiscTotal() {
        return discTotal.get();
    }
    public void setDiscTotal(String discTotal) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.DISC_TOTAL, discTotal, getAudioFile());
            if(written==true) {
                this.discTotal.set(discTotal);
            }
        }
        else{
            this.discTotal.set(discTotal);
        }
    }
    public String getEncoder() {
        return encoder.get();
    }
    public void setEncoder(String encoder) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.ENCODER, encoder, getAudioFile());
            if(written==true) {
                this.encoder.set(encoder);
            }
        }
        else{
            this.encoder.set(encoder);
        }
    }
    public String getEngineer() {
        return engineer.get();
    }
    public void setEngineer(String engineer) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.ENGINEER, engineer, getAudioFile());
            if(written==true) {
                this.encoder.set(engineer);
            }
        }
        else{
            this.engineer.set(engineer);
        }
    }
    public String getEnsemble() {
        return ensemble.get();
    }
    public void setEnsemble(String ensemble) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.ENSEMBLE, ensemble, getAudioFile());
            if(written==true) {
                this.ensemble.set(ensemble);
            }
        }
        else{
            this.ensemble.set(ensemble);
        }
    }
    public String getGenre() {
        return genre.get();
    }
    public void setGenre(String genre) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.GENRE, genre, getAudioFile());
            if(written==true) {
                this.genre.set(genre);
            }

        }
            else{
              this.genre.set(genre);
            }
    }
    public String getGroup() {
        return group.get();
    }
    public void setGroup(String group) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.GROUP, group, getAudioFile());
            if(written==true) {
                this.group.set(group);
            }
        }
        else{
            this.group.set(group);
        }
    }
    public String getInstruemnt() {
        return instruemnt.get();
    }
    public void setInstruemnt(String instrument) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.INSTRUMENT, instrument, getAudioFile());
            if(written==true) {
                this.instruemnt.set(instrument);
            }
        }
        else{
            this.instruemnt.set(instrument);
        }
    }
    public String getLaugauge() {
        return laugauge.get();
    }
    public void setLangauge(String language) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.LANGUAGE, language, getAudioFile());
            if(written==true) {
                this.laugauge.set(language);
            }
        }
        else{
            this.laugauge.set(language);
        }
    }
    public String getLyricist() {
        return lyricist.get();
    }
    public void setLyricist(String lyricist) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.LYRICIST, lyricist, getAudioFile());
            if(written==true) {
                this.lyricist.set(lyricist);
            }
        }
        else{
            this.lyricist.set(lyricist);
        }
    }
    public String getLyrics() {
        return lyrics.get();
    }
    public void setLyrics(String lyrics) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.LYRICS, lyrics, getAudioFile());
            if(written==true) {
                this.lyrics.set(lyrics);
            }
        }
        else{
            this.lyrics.set(lyrics);
        }
    }
    public String getMedia() {
        return media.get();
    }
    public void setMedia(String media) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.MEDIA, media, getAudioFile());
            if(written==true) {
                this.media.set(media);
            }
        }
        else{
            this.media.set(media);
        }
    }
    public String getProducer() {
        return producer.get();
    }
    public void setProducer(String producer) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.PRODUCER, producer, getAudioFile());
            if(written==true) {
                this.producer.set(producer);
            }
        }
        else{
            this.producer.set(producer);
        }
    }
    public String getOriginalArtist() {
        return originalArtist.get();
    }
    public void setOriginalArtist(String originalArtist) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.ORIGINAL_ARTIST,originalArtist, getAudioFile());
            if(written==true) {
                this.originalArtist.set(originalArtist);
            }
        }
        else{
            this.originalArtist.set(originalArtist);
        }
    }
    public String getRanking() {
        return ranking.get();
    }
    public void setRanking(String ranking) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.RANKING, ranking, getAudioFile());
            if(written==true) {
                this.ranking.set(ranking);
            }
        }
        else{
            this.ranking.set(ranking);
        }
    }
    public String getRating() {
        return rating.get();
    }
    public void setRating(String rating) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.RATING, rating, getAudioFile());
            if(written==true) {
                this.rating.set(rating);
            }
        }
        else{
            this.rating.set(rating);
        }
    }
    public String getRecordLabel() {
        return recordLabel.get();
    }
    public void setRecordLabel(String recordLabel) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.RECORD_LABEL, recordLabel, getAudioFile());
            if(written==true) {
                this.recordLabel.set(recordLabel);
            }
        }
        else{
            this.recordLabel.set(recordLabel);
        }
    }
    public String getTempo() {
        return tempo.get();
    }
    public String getTitle() {
        String songTitle=title.get();
        if(songTitle.isEmpty()==false){
            return songTitle;
        }
        return  audioFilePath;
    }
    public void setTitle(String title) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.TITLE, title, getAudioFile());
            if(written==true) {
                this.title.set(title);
            }
        }
        else{
            this.title.set(title);
        }
    }
    public String getTrack() {
        return track.get();
    }
    public void setTrack(String track) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.TRACK, track, getAudioFile());
            if(written==true) {
                this.track.set(track);
            }
        }
        else{
            this.track.set(track);
        }
    }
    public String getTrackTotal() {
        return trackTotal.get();
    }
    public void setTrackTotal(String trackTotal) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.TRACK_TOTAL, trackTotal, getAudioFile());
            if(written==true) {
                this.trackTotal.set(trackTotal);
            }
        }
        else{
            this.trackTotal.set(trackTotal);
        }
    }
    public String getYear() {
        return year.get();
    }
    public void setYear(String year) {
        if (writeFieldsToFile == true) {
            boolean written=extractAudioInformation.writeFieldsToFile(FieldKey.YEAR, year, getAudioFile());
            if(written==true) {
                this.year.set(year);
            }
        }
        else{
            this.year.set(year);
        }
}
    public String getAudioFilePath() {
        return audioFilePath;
    }
    public String getTrackLength() {
        return trackLength.get();
    }
    public void setBitrate(String bitrate) {
        this.bitrate.set(bitrate);
    }
    public void setBpm(String bpm) {
        this.bpm.set(bpm);
    }
    public void setTempo(String tempo) {
        this.tempo.set(tempo);
    }
    public AudioFile getAudioFile() {
        if(audioFile==null) {
           audioFile= extractAudioInformation.getAudioFile(this);

        }

        return audioFile;
    }
    public File getPhysicalFile(){


        return extractAudioInformation.getPhysicalFile(this);
    }



    public void clearAudioFile(){ // clears the audio file so it won't take up memory
        audioFile=null;
    }
    public void setAudioFilePath(String audioFilePath) {
           this.audioFilePath = audioFilePath;
    }
   public void  increasePLays(){
        plays++;
   }
   public  void resetPlays(){
        plays=0;
   }
    public List<String> getAlbumArtPaths() {
        return albumArtPaths;
    }
    public void setAlbumArtPaths(List<String> albumArtPaths) {
        this.albumArtPaths = albumArtPaths;
    }
    public int getPlays() {
        return plays;
    }
    public void setPlays(int plays) {
        this.plays = plays;
    }
    public String getAudioFormat() {
        String [] fileNameParts=audioFilePath.split("\\.");
        int length=fileNameParts.length;
        audioFormat=fileNameParts[length-1];
        return audioFormat;
    }
    public boolean isWriteFieldsToFile() {
        return writeFieldsToFile;
    }
    public void setWriteFieldsToFile(boolean writeFieldsToFile) {
        this.writeFieldsToFile = writeFieldsToFile;
    }
    @Override
    public String toString() {
        String title=this.title.getName();
        if(title!=null && !(title.isEmpty())){
                return title;
            }
            else if (audioFilePath!=null){
                return audioFilePath;
            }
                else {
            return "Song";
        }
    }
    public void setTrackLengthNumber(long trackLengthNumber) {
        this.trackLengthNumber = trackLengthNumber;
        this.trackLength.set(FormatTime.formatTimeWithColons(trackLengthNumber));
    }
    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }
    public void setTrackLength(String trackLength) {
        this.trackLength.set(trackLength);
    }
    public String getBitrate() {
        return bitrate.get();
    }
    public String getMusicBrainzId() {
        return musicBrainzId.get();
    }
    public void setMusicBrainzId(String musicBrainzId) {
        this.musicBrainzId.set(musicBrainzId);
    }
    public String getAcoustIDId() {
        return acoustIDId.get();
    }
    public void setAcoustIDId(String acoustIDId) {
        this.acoustIDId.set(acoustIDId);
    }
    public int getCueStart() {
        return cueStart.get();
    }
    public String getCueStartString() {
        return  FormatTime.formatTimeWithColons(cueStart.get());
    }
    public void setCueStart(int cueStart) {
        this.cueStart.set(cueStart);
    }
    public boolean isHasCue() {
        return hasCue;
    }
    public void setHasCue(boolean hasCue) {
        this.hasCue = hasCue;
    }
    public long getTrackLengthNumber() {
        if(trackLengthNumber<=0){

                    if(audioFile!=null) {
                        audioFile = getAudioFile();
                    }

                    if(audioFile!=null){
           trackLengthNumber= audioFile.getAudioHeader().getTrackLength();

                    }

        }
        return trackLengthNumber;
    }
    public void setStartFrame(long numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }
    public long getStartFrame() {
        return numberOfFrames;
    }
    public boolean isUsesFrames() {
        return usesFrames;
    }
    public void setUsesFrames(boolean usesFrames) {
        this.usesFrames = usesFrames;
    }

    public boolean isTaggable() {
        return isTaggable;
    }

    public void setTaggable(boolean taggable) {
        isTaggable = taggable;
    }
}
