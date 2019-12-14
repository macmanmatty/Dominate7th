package sample.AudioProcessors.Encoders;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import sample.AudioProcessors.AudioFileAction;
import sample.AudioProcessors.AudioInformationAction;
import sample.AudioProcessors.FileAction;
import sample.Library.AudioInformation;
import sample.Library.CueSheets.CueSheet;
import sample.Library.MusicLibrary;
import sample.Utilities.AudioFileUtilities;
import sample.Utilities.CopyAudioInformation;
import sample.Windows.FileProgressBar;
import sample.Windows.UpdateLabel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public abstract class Encoder implements AudioFileAction, FileAction, AudioInformationAction {
   protected  List<String> errorMessages= new ArrayList<>();
   protected int songsEncoded;
   protected CountDownLatch countDownLatch;
   protected int numberOfThreads;
   protected boolean splitAndEnocdeCueFiles;
   protected AudioFileUtilities utilities;
   protected UpdateLabel updateLabel = new UpdateLabel();
   protected FileProgressBar fileProgressBar= new FileProgressBar();
   protected int tracksEncoded;
   protected int tracksToEncode;
   protected boolean useFileInputStream;
   protected boolean useFileOutputStream;
   protected MusicLibrary library;
   protected boolean addToLibrary=true;
    public abstract  File encode(File file );

    public abstract void splitAndEncode(File cueSheet);
    public   abstract   void setBitrate(int bitrate);
    protected String fileSepeator;

    public Encoder(MusicLibrary library) {
        this.library = library;
    }

    public Encoder( String fileSeperator, MusicLibrary library, boolean addToLibrary) {
        this.library = library;
        this.addToLibrary = addToLibrary;
        this.fileSepeator=fileSeperator;
        utilities= new AudioFileUtilities();
    }

    @Override
    public void action(AudioFile audioFile) {
         File encodedFile=encode(audioFile.getFile());
         if(encodedFile!=null) {
             setTags(audioFile.getFile(), encodedFile);
         }


    }
    @Override
    public void action(File file) {
        if(utilities.isAudioFile(file)) {
            File encodedFile=encode(file);
            if(encodedFile!=null) {
                setTags(file, encodedFile);
            }

        }
        else  if( splitAndEnocdeCueFiles==true &&  utilities.getExtensionOfFile(file).equalsIgnoreCase("cue")){
            splitAndEncode(file);
        }
    }
    @Override
    public void action(AudioInformation information) {
       
         File encodedFile=encode(information.getAudioFile().getFile());

         if(encodedFile!=null){

            AudioInformation newSong= setTags(information, encodedFile);
            if(addToLibrary==true) {
                library.addSong(newSong);
            }


         }



    }
    public AudioInformation setTags(AudioInformation oldSong, File file) { // set the tags  for the  generated  converted (encoded)  file  to the same ones  as converted audio information class
        AudioInformation newSong=new AudioInformation(false);
        System.out.println("File Path: " + file.getAbsolutePath());

        CopyAudioInformation.copyAudioInformation(oldSong, newSong);
        newSong.setAudioFilePath(file.getAbsolutePath());
        newSong.setAudioFormat(utilities.getExtensionOfFile(file));
        AudioFile newFile = null; // read the audio header with J audio tagger
        try {
            newFile = AudioFileIO.read(file);
            System.out.println("Audio File Path: " + newFile.getFile().getAbsolutePath());

            AudioHeader header=newFile.getAudioHeader();
            newSong.setBitrate(header.getBitRate());
            newSong.setTrackLengthNumber(header.getTrackLength());

        Tag newFileTag = newFile.getTagOrCreateAndSetDefault(); // get the tag
        System.out.println(newFileTag);
        // set Field keys
            System.out.println("Artist: "+oldSong.getArtist());
            System.out.println("Artist: "+newSong.getArtist());


            newFileTag.setField(FieldKey.ARTIST, oldSong.getArtist());
        newFileTag.setField(FieldKey.ALBUM, oldSong.getAlbum());
        newFileTag.setField(FieldKey.COMPOSER, oldSong.getComposer());
        newFileTag.setField(FieldKey.ENCODER, "FFmpeg");
        newFileTag.setField(FieldKey.BPM, oldSong.getBpm());
        newFileTag.setField(FieldKey.COMMENT, oldSong.getComment());
        newFileTag.setField(FieldKey.COPYRIGHT, oldSong.getCopyright());
        newFileTag.setField(FieldKey.CATALOG_NO, oldSong.getCatalogNo());
        newFileTag.setField(FieldKey.COUNTRY, oldSong.getCountry());
        newFileTag.setField(FieldKey.DISC_NO, oldSong.getDiscNo());
        newFileTag.setField(FieldKey.DISC_TOTAL, oldSong.getDiscNo());
        newFileTag.setField(FieldKey.ENGINEER, oldSong.getEngineer());
        newFileTag.setField(FieldKey.ENSEMBLE, oldSong.getEnsemble());
        newFileTag.setField(FieldKey.GENRE, oldSong.getGenre());
        newFileTag.setField(FieldKey.GROUP, oldSong.getGroup());
        newFileTag.setField(FieldKey.INSTRUMENT, oldSong.getInstruemnt());
        newFileTag.setField(FieldKey.LANGUAGE, oldSong.getLaugauge());
        newFileTag.setField(FieldKey.LYRICIST, oldSong.getLyricist());
        newFileTag.setField(FieldKey.LYRICS, oldSong.getLyricist());
        newFileTag.setField(FieldKey.MEDIA, oldSong.getMedia());
        newFileTag.setField(FieldKey.PRODUCER, oldSong.getProducer());
        newFileTag.setField(FieldKey.ORIGINAL_ARTIST, oldSong.getOriginalArtist());
        newFileTag.setField(FieldKey.RANKING, oldSong.getRanking());
        newFileTag.setField(FieldKey.RATING, oldSong.getRating());
        newFileTag.setField(FieldKey.RECORD_LABEL, oldSong.getRecordLabel());
        newFileTag.setField(FieldKey.TEMPO, oldSong.getTempo());
        newFileTag.setField(FieldKey.TITLE, oldSong.getTitle());
        newFileTag.setField(FieldKey.TRACK, String.valueOf(oldSong.getTrack()));
        newFileTag.setField(FieldKey.TRACK_TOTAL, oldSong.getTrackTotal());
        newFileTag.setField(FieldKey.YEAR, oldSong.getYear());

            AudioFileIO.write(newFile);

        }
    catch (CannotReadException e) {
        e.printStackTrace();
        errorMessages.add(e.getMessage());

    } catch (IOException e) {
        e.printStackTrace();
            errorMessages.add(e.getMessage());

        } catch (TagException e) {
        e.printStackTrace();
            errorMessages.add(e.getMessage());

        } catch (ReadOnlyFileException e) {
        e.printStackTrace();
            errorMessages.add(e.getMessage());

        } catch (InvalidAudioFrameException e) {
        e.printStackTrace();
            errorMessages.add(e.getMessage());

        } catch (CannotWriteException e) {
            e.printStackTrace();
            errorMessages.add(e.getMessage());

        }

        return newSong;

    }

    public void setTags(File audioFile, File file) { // set the tags  for the  generated  converted (encoded)  file  to the same ones  as the source audio  file
        try {
            AudioFile newFile= AudioFileIO.read(file); // read the audio header with J audio tagger
            Tag newFileTag= newFile.getTagOrCreateAndSetDefault(); // get the tag
            AudioFile oldAudioFile=AudioFileIO.read(audioFile);
            Tag originalFileTag= oldAudioFile.getTag();
            System.out.println(newFileTag);
            // set Field keys
            newFileTag.setField(FieldKey.ARTIST, originalFileTag.getFirst(FieldKey.ARTIST));
            newFileTag.setField(FieldKey.ALBUM, originalFileTag.getFirst(FieldKey.ALBUM));
            newFileTag.setField(FieldKey.COMPOSER, originalFileTag.getFirst(FieldKey.COMPOSER));
            newFileTag.setField(FieldKey.ENCODER, "FFmpeg");
            newFileTag.setField(FieldKey.BPM, originalFileTag.getFirst(FieldKey.BPM));
            newFileTag.setField(FieldKey.COMMENT, originalFileTag.getFirst(FieldKey.COMMENT));
            newFileTag.setField(FieldKey.COPYRIGHT, originalFileTag.getFirst(FieldKey.COPYRIGHT));
            newFileTag.setField(FieldKey.CATALOG_NO, originalFileTag.getFirst(FieldKey.CATALOG_NO));
            newFileTag.setField(FieldKey.COUNTRY, originalFileTag.getFirst(FieldKey.COUNTRY));
            newFileTag.setField(FieldKey.CUSTOM1, originalFileTag.getFirst(FieldKey.CUSTOM1));
            newFileTag.setField(FieldKey.CUSTOM2, originalFileTag.getFirst(FieldKey.CUSTOM2));
            newFileTag.setField(FieldKey.DISC_NO, originalFileTag.getFirst(FieldKey.DISC_NO));
            newFileTag.setField(FieldKey.DISC_TOTAL, originalFileTag.getFirst(FieldKey.DISC_TOTAL));
            newFileTag.setField(FieldKey.ENGINEER, originalFileTag.getFirst(FieldKey.ENGINEER));
            newFileTag.setField(FieldKey.ENSEMBLE, originalFileTag.getFirst(FieldKey.ENSEMBLE));
            newFileTag.setField(FieldKey.GENRE, originalFileTag.getFirst(FieldKey.ARTIST));
            newFileTag.setField(FieldKey.GROUP, originalFileTag.getFirst(FieldKey.GROUP));
            newFileTag.setField(FieldKey.INSTRUMENT, originalFileTag.getFirst(FieldKey.INSTRUMENT));
            newFileTag.setField(FieldKey.LANGUAGE, originalFileTag.getFirst(FieldKey.LANGUAGE));
            newFileTag.setField(FieldKey.LYRICIST, originalFileTag.getFirst(FieldKey.LYRICIST));
            newFileTag.setField(FieldKey.LYRICS, originalFileTag.getFirst(FieldKey.LYRICS));
            newFileTag.setField(FieldKey.MEDIA, originalFileTag.getFirst(FieldKey.MEDIA));
            newFileTag.setField(FieldKey.PRODUCER, originalFileTag.getFirst(FieldKey.PRODUCER));
            newFileTag.setField(FieldKey.ORIGINAL_ARTIST, originalFileTag.getFirst(FieldKey.ORIGINAL_ARTIST));
            newFileTag.setField(FieldKey.RANKING, originalFileTag.getFirst(FieldKey.RANKING));
            newFileTag.setField(FieldKey.RATING, originalFileTag.getFirst(FieldKey.RATING));
            newFileTag.setField(FieldKey.RECORD_LABEL, originalFileTag.getFirst(FieldKey.RECORD_LABEL));
            newFileTag.setField(FieldKey.TEMPO, originalFileTag.getFirst(FieldKey.TEMPO));
            newFileTag.setField(FieldKey.TITLE, originalFileTag.getFirst(FieldKey.TITLE));
            newFileTag.setField(FieldKey.TRACK, originalFileTag.getFirst(FieldKey.TRACK));
            newFileTag.setField(FieldKey.TRACK_TOTAL, originalFileTag.getFirst(FieldKey.TRACK_TOTAL));
            newFileTag.setField(FieldKey.YEAR, originalFileTag.getFirst(FieldKey.YEAR));
            List<Artwork> artwork=originalFileTag.getArtworkList();
            int size=artwork.size();
            for(int count=0; count<size; count++){
                newFileTag.setField(artwork.get(count));
            }
            AudioFileIO.write(newFile);
        } catch (CannotReadException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (IOException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (TagException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (ReadOnlyFileException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (InvalidAudioFrameException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (CannotWriteException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        }
    }
    public void setTagsFromCue(File audioFile, CueSheet cueSheet, int trackNumber,  File file) { // set the tags  for the  generated  converted (encoded)  file  to the same ones  as the in the cue sheet
        try {
            AudioFile newFile= AudioFileIO.read(file); // read the audio header with J audio tagger
            AudioFile oldAudioFile=AudioFileIO.read(audioFile);
            Tag newFileTag= newFile.getTagOrCreateAndSetDefault(); // get the tag
            Tag originalFileTag= oldAudioFile.getTag();
            System.out.println(newFileTag);
            String artist="";
            String album="";
            String composer="";
            String genre="";
            String year="";
            
            
            
            
            List<AudioInformation> tracks=cueSheet.getTracks();
            String title="";
            if(tracks.size()>trackNumber) {
                AudioInformation track = tracks.get(trackNumber);
                // get title and artist from track section of cue sheet
                title = track.getTitle();
                artist = track.getArtist();
                // try to get tags from cue sheet
                if (artist == null || artist.isEmpty()) {
                    artist = cueSheet.getArtist();
                }
                album = track.getAlbum();
                if (album == null || album.isEmpty()) {
                    album = cueSheet.getAlbumName();
                }
                genre = track.getGenre();
                if (genre == null || genre.isEmpty()) {
                    genre = cueSheet.getGenre();
                }
                composer = track.getComposer();
                year = track.getYear();
                if (year == null || year.isEmpty()) {
                    year = cueSheet.getYear();
                }
            }
            // tags are still empty try  reading tags from audio file
            if (year == null || year.isEmpty()) {
                year = originalFileTag.getFirst(FieldKey.YEAR);
            }
            newFileTag.setField(FieldKey.ARTIST,album );
            if (composer == null || composer.isEmpty()) {
                composer = originalFileTag.getFirst(FieldKey.COMPOSER);
            }
            if (genre == null || genre.isEmpty()) {
                genre = originalFileTag.getFirst(FieldKey.GENRE);
            }
            if (album == null || album.isEmpty()) {
                album = originalFileTag.getFirst(FieldKey.ALBUM);
            }
            if (artist == null || artist.isEmpty()) {
                artist = originalFileTag.getFirst(FieldKey.ARTIST);
            }
            newFileTag.setField(FieldKey.ALBUM, album);
            newFileTag.setField(FieldKey.ARTIST ,artist);
            newFileTag.setField(FieldKey.COMPOSER, composer);
            newFileTag.setField(FieldKey.ENCODER, "FFmpeg");
            newFileTag.setField(FieldKey.BPM, originalFileTag.getFirst(FieldKey.BPM));
            newFileTag.setField(FieldKey.COMMENT, originalFileTag.getFirst(FieldKey.COMMENT));
            newFileTag.setField(FieldKey.COPYRIGHT, originalFileTag.getFirst(FieldKey.COPYRIGHT));
            newFileTag.setField(FieldKey.CATALOG_NO, originalFileTag.getFirst(FieldKey.CATALOG_NO));
            newFileTag.setField(FieldKey.COUNTRY, originalFileTag.getFirst(FieldKey.COUNTRY));
            newFileTag.setField(FieldKey.CUSTOM1, originalFileTag.getFirst(FieldKey.CUSTOM1));
            newFileTag.setField(FieldKey.CUSTOM2, originalFileTag.getFirst(FieldKey.CUSTOM2));
            newFileTag.setField(FieldKey.DISC_NO, originalFileTag.getFirst(FieldKey.DISC_NO));
            newFileTag.setField(FieldKey.DISC_TOTAL, originalFileTag.getFirst(FieldKey.DISC_TOTAL));
            newFileTag.setField(FieldKey.ENGINEER, originalFileTag.getFirst(FieldKey.ENGINEER));
            newFileTag.setField(FieldKey.ENSEMBLE, originalFileTag.getFirst(FieldKey.ENSEMBLE));
            newFileTag.setField(FieldKey.GENRE, genre);
            newFileTag.setField(FieldKey.GROUP, originalFileTag.getFirst(FieldKey.GROUP));
            newFileTag.setField(FieldKey.INSTRUMENT, originalFileTag.getFirst(FieldKey.INSTRUMENT));
            newFileTag.setField(FieldKey.LANGUAGE, originalFileTag.getFirst(FieldKey.LANGUAGE));
            newFileTag.setField(FieldKey.LYRICIST, originalFileTag.getFirst(FieldKey.LYRICIST));
            newFileTag.setField(FieldKey.LYRICS, originalFileTag.getFirst(FieldKey.LYRICS));
            newFileTag.setField(FieldKey.MEDIA, originalFileTag.getFirst(FieldKey.MEDIA));
            newFileTag.setField(FieldKey.PRODUCER, originalFileTag.getFirst(FieldKey.PRODUCER));
            newFileTag.setField(FieldKey.ORIGINAL_ARTIST, originalFileTag.getFirst(FieldKey.ORIGINAL_ARTIST));
            newFileTag.setField(FieldKey.RANKING, originalFileTag.getFirst(FieldKey.RANKING));
            newFileTag.setField(FieldKey.RATING, originalFileTag.getFirst(FieldKey.RATING));
            newFileTag.setField(FieldKey.RECORD_LABEL, originalFileTag.getFirst(FieldKey.RECORD_LABEL));
            newFileTag.setField(FieldKey.TEMPO, originalFileTag.getFirst(FieldKey.TEMPO));
            newFileTag.setField(FieldKey.TITLE, title);
            newFileTag.setField(FieldKey.TRACK, trackNumber+"");
            newFileTag.setField(FieldKey.TRACK_TOTAL, tracks.size()+"");
            newFileTag.setField(FieldKey.YEAR, year);
           List<Artwork> artwork=originalFileTag.getArtworkList();
            int size=artwork.size();
            for(int count=0; count<size; count++){
                newFileTag.setField(artwork.get(count));
            }
            AudioFileIO.write(newFile);
        } catch (CannotReadException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());
        } catch (IOException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (TagException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (ReadOnlyFileException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (InvalidAudioFrameException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        } catch (CannotWriteException e) {
            e.getStackTrace();
            errorMessages.add(e.getMessage());

        }
    }
    @Override
    public List<String> getErrorMessages() {
        return errorMessages;
    }
    public int getSongsEncoded() {
        return songsEncoded;
    }
    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }
    @Override
    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
    @Override
    public int getNumberOfThreads() {
        return numberOfThreads;
    }
    public boolean isSplitAndEnocdeCueFiles() {
        return splitAndEnocdeCueFiles;
    }
    public void setSplitAndEnocdeCueFiles(boolean splitAndEnocdeCueFiles) {
        this.splitAndEnocdeCueFiles = splitAndEnocdeCueFiles;
    }
    public UpdateLabel getUpdateLabel() {
        return updateLabel;
    }
    @Override
    public List<AudioInformation> getTracks() {
        return new ArrayList<>();
    }
    public void setUpdateLabel(UpdateLabel label) {
        this.updateLabel = label;
    }
    public FileProgressBar getProgressBar() {
        return fileProgressBar;
    }
    public void setProgressBar(FileProgressBar fileProgressBar) {
        this.fileProgressBar = fileProgressBar;
    }

    public boolean isUseFileInputStream() {
        return useFileInputStream;
    }

    public void setUseFileInputStream(boolean useFileStream) {
        this.useFileInputStream = useFileStream;
    }

    public boolean isUseFileOutputStream() {
        return useFileOutputStream;
    }

    public void setUseFileOutputStream(boolean useFileOutputStream) {
        this.useFileOutputStream = useFileOutputStream;
    }
}
