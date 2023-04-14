package sample.AudioProcessors;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.images.Artwork;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.AudioProcessors.CueSheets.CueSheetReader;
import sample.Library.AudioInformation;
import sample.Library.CueSheets.CueSheet;
import sample.Utilities.AudioFileUtilities;
import sample.Utilities.FormatTime;
import sample.Windows.OptionPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ExtractAudioInformation {
    private ArrayList<AudioInformation> audioInformation = new ArrayList<>();
    private ArrayList<String> artists = new ArrayList<>(); // array list of artist names in the songs
    private ArrayList<String> albums = new ArrayList<>(); // array list of albums in the songs
    private SimpleStringProperty currentFile;
    private AudioFileUtilities utilities;
    private CueSheetReader cueSheetReader;
    private String fileSeperator;


    public ExtractAudioInformation() {
        utilities = new AudioFileUtilities();
        cueSheetReader = new CueSheetReader();
        fileSeperator=System.getProperty("file.separator");    }

    public ArrayList<AudioInformation> extractAudioInformationFromFile(List<AudioFile> audioFiles) { // extracts a list of of audio information from
        // a list of audio files
        int size = audioFiles.size();
        for (int count = 0; count < size; count++) {
            AudioFile file = audioFiles.get(count);
            AudioHeader header = file.getAudioHeader();
            Tag tag = file.getTagOrCreateAndSetDefault();
            if (tag == null) {
                tag = file.createDefaultTag();
            }
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            artists.add(artist);
            if (artist.isEmpty()) {
                albums.add(album);
            }
            List<String> imagePaths = new ArrayList<>();
            List<Artwork> albumArt = tag.getArtworkList();
            int size2 = albumArt.size();
            for (int count2 = 0; count2 < size2; count2++) {
                imagePaths.add(albumArt.get(count2).getImageUrl());
            }
            long trackLength = header.getTrackLength();
            String audioFilePath = file.getFile().getAbsolutePath();
            AudioInformation information = new AudioInformation(null, file.getFile().getName(), trackLength, audioFilePath, imagePaths, "" + trackLength, header.getBitRate(), artist, album, tag.getFirst(FieldKey.BPM), tag.getFirst(FieldKey.CATALOG_NO), tag.getFirst(FieldKey.COMMENT), tag.getFirst(FieldKey.COMPOSER), tag.getFirst(FieldKey.CONDUCTOR), tag.getFirst(FieldKey.COPYRIGHT), tag.getFirst(FieldKey.COUNTRY), tag.getFirst(FieldKey.CUSTOM1), tag.getFirst(FieldKey.CUSTOM2), tag.getFirst(FieldKey.DISC_NO), tag.getFirst(FieldKey.DISC_TOTAL), tag.getFirst(FieldKey.ENCODER), tag.getFirst(FieldKey.ENGINEER), tag.getFirst(FieldKey.ENSEMBLE), tag.getFirst(FieldKey.GENRE), tag.getFirst(FieldKey.GROUP), tag.getFirst(FieldKey.INSTRUMENT), tag.getFirst(FieldKey.LANGUAGE), tag.getFirst(FieldKey.LYRICIST), tag.getFirst(FieldKey.LYRICS), tag.getFirst(FieldKey.MEDIA), tag.getFirst(FieldKey.PRODUCER), tag.getFirst(FieldKey.ORIGINAL_ARTIST), tag.getFirst(FieldKey.RANKING), tag.getFirst(FieldKey.RATING), tag.getFirst(FieldKey.RECORD_LABEL), tag.getFirst(FieldKey.TEMPO), tag.getFirst(FieldKey.TITLE), tag.getFirst(FieldKey.TRACK), tag.getFirst(FieldKey.TRACK_TOTAL), tag.getFirst(FieldKey.YEAR), tag.getFirst(FieldKey.ACOUSTID_ID), tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));
            audioInformation.add(information);
            if (currentFile != null) {
                currentFile.set(audioFilePath);
            }
        }
        return audioInformation;
    }

    public ArrayList<AudioInformation> extractInformationAndLinkArt(List<AudioFile> audioFiles) {
        int size = audioFiles.size();
        LinkSongsToAlbumImages linkSongs = new LinkSongsToAlbumImages(false);
        for (int count = 0; count < size; count++) {
            AudioFile file = audioFiles.get(count);
            AudioHeader header = file.getAudioHeader();
            Tag tag = file.getTagOrCreateAndSetDefault();
            if (tag == null) {
                tag = file.createDefaultTag();
            }
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            artists.add(artist);
            if (artist.isEmpty()) {
                albums.add(album);
            }
            List<String> imagePaths = new ArrayList<>();
            List<Artwork> albumArt = tag.getArtworkList();
            int size2 = albumArt.size();
            for (int count2 = 0; count2 < size2; count2++) {
                imagePaths.add(albumArt.get(count2).getImageUrl());
            }
            long trackLength = header.getTrackLength();
            String audioFilePath = file.getFile().getAbsolutePath();
            AudioInformation information = new AudioInformation(null, file.getFile().getName(), trackLength, audioFilePath, imagePaths, "" + trackLength, header.getBitRate(), artist, album, tag.getFirst(FieldKey.BPM), tag.getFirst(FieldKey.CATALOG_NO), tag.getFirst(FieldKey.COMMENT), tag.getFirst(FieldKey.COMPOSER), tag.getFirst(FieldKey.CONDUCTOR), tag.getFirst(FieldKey.COPYRIGHT), tag.getFirst(FieldKey.COUNTRY), tag.getFirst(FieldKey.CUSTOM1), tag.getFirst(FieldKey.CUSTOM2), tag.getFirst(FieldKey.DISC_NO), tag.getFirst(FieldKey.DISC_TOTAL), tag.getFirst(FieldKey.ENCODER), tag.getFirst(FieldKey.ENGINEER), tag.getFirst(FieldKey.ENSEMBLE), tag.getFirst(FieldKey.GENRE), tag.getFirst(FieldKey.GROUP), tag.getFirst(FieldKey.INSTRUMENT), tag.getFirst(FieldKey.LANGUAGE), tag.getFirst(FieldKey.LYRICIST), tag.getFirst(FieldKey.LYRICS), tag.getFirst(FieldKey.MEDIA), tag.getFirst(FieldKey.PRODUCER), tag.getFirst(FieldKey.ORIGINAL_ARTIST), tag.getFirst(FieldKey.RANKING), tag.getFirst(FieldKey.RATING), tag.getFirst(FieldKey.RECORD_LABEL), tag.getFirst(FieldKey.TEMPO), tag.getFirst(FieldKey.TITLE), tag.getFirst(FieldKey.TRACK), tag.getFirst(FieldKey.TRACK_TOTAL), tag.getFirst(FieldKey.YEAR), tag.getFirst(FieldKey.ACOUSTID_ID), tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));
            linkSongs.linkFiles(file.getFile(), information); // link  song to album in art in folder
            audioInformation.add(information);
            if (currentFile != null) {
                currentFile.set(audioFilePath);
            }
        }
        return audioInformation;
    }

   public AudioInformation extractInformationUsingJAudioTagger(AudioFile file) {// extracts audio information  from a single audio file
        /*
        suports reading tags from
        Mp3, Mp4 (Mp4 audio, M4a and M4p audio), Ogg, Vorbis, Flac, Wav, Aiff, Dsf, and Wma
         */
        AudioHeader header = file.getAudioHeader();
        Tag tag = file.getTagOrCreateAndSetDefault();
        if (tag == null) {
            tag = file.createDefaultTag();
        }
        String artist = tag.getFirst(FieldKey.ARTIST);
        String album = tag.getFirst(FieldKey.ALBUM);
        artists.add(artist);
        if (artist.isEmpty()) {
            albums.add(album);
        }
        List<String> imagePaths = new ArrayList<>();
        List<Artwork> albumArt = tag.getArtworkList();
        int size2 = albumArt.size();
        for (int count2 = 0; count2 < size2; count2++) {
            imagePaths.add(albumArt.get(count2).getImageUrl());
        }
        long trackLength = header.getTrackLength();
        String audioFilePath = file.getFile().getAbsolutePath();
        AudioInformation information = new AudioInformation(null, file.getFile().getName(), trackLength, audioFilePath, imagePaths, "" + trackLength, header.getBitRate(), artist, album, tag.getFirst(FieldKey.BPM), tag.getFirst(FieldKey.CATALOG_NO), tag.getFirst(FieldKey.COMMENT), tag.getFirst(FieldKey.COMPOSER), tag.getFirst(FieldKey.CONDUCTOR), tag.getFirst(FieldKey.COPYRIGHT), tag.getFirst(FieldKey.COUNTRY), tag.getFirst(FieldKey.CUSTOM1), tag.getFirst(FieldKey.CUSTOM2), tag.getFirst(FieldKey.DISC_NO), tag.getFirst(FieldKey.DISC_TOTAL), tag.getFirst(FieldKey.ENCODER), tag.getFirst(FieldKey.ENGINEER), tag.getFirst(FieldKey.ENSEMBLE), tag.getFirst(FieldKey.GENRE), tag.getFirst(FieldKey.GROUP), tag.getFirst(FieldKey.INSTRUMENT), tag.getFirst(FieldKey.LANGUAGE), tag.getFirst(FieldKey.LYRICIST), tag.getFirst(FieldKey.LYRICS), tag.getFirst(FieldKey.MEDIA), tag.getFirst(FieldKey.PRODUCER), tag.getFirst(FieldKey.ORIGINAL_ARTIST), tag.getFirst(FieldKey.RANKING), tag.getFirst(FieldKey.RATING), tag.getFirst(FieldKey.RECORD_LABEL), tag.getFirst(FieldKey.TEMPO), tag.getFirst(FieldKey.TITLE), tag.getFirst(FieldKey.TRACK), tag.getFirst(FieldKey.TRACK_TOTAL), tag.getFirst(FieldKey.YEAR), tag.getFirst(FieldKey.ACOUSTID_ID), tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID));


        return information;
    }
    public AudioInformation extractAudioInformationFromFile(File file) {// extracts audio information  from a single audio file
        // attemps to use JAudioTagger to read the tags if Jaudiotagger cannot read them  tries using FFmpeg to read the tags
        AudioInformation audioInformation= new AudioInformation();
        audioInformation.setAudioFilePath(file.getAbsolutePath());
        audioInformation.setAudioFormat(utilities.getExtensionOfFile(file));
        try {

            AudioFile audioFile=AudioFileIO.read(file);
           audioInformation= extractInformationUsingJAudioTagger(audioFile);
            return  audioInformation;


        } catch (IOException | CannotReadException | ReadOnlyFileException | TagException | InvalidAudioFrameException e ) {
            e.printStackTrace();
            try {
              audioInformation=  extractInformationUsingFFmpeg(file);
            } catch (FrameGrabber.Exception ex) {
                ex.printStackTrace();
            }

        }
        // file is not a format that supports reading writing to and from tags so booleans to false;
        audioInformation.setTaggable(false);
        audioInformation.setWriteFieldsToFile(false);

        return  audioInformation;

    }




        private AudioInformation extractInformationUsingFFmpeg(File file) throws FrameGrabber.Exception {// extracts audio information  from a single audio file  that does NOT have supported  tag reding writing.
        AudioInformation information = new AudioInformation(false); // don't write fields as  it is not supported with other file formats
        String audioFilePath = file.getAbsolutePath();
        information.setAudioFilePath(audioFilePath);
        information.setAudioFormat(utilities.getExtensionOfFile(file));
        information.setTitle(audioFilePath);
        // set basic information with ffmpegFrame grabber
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
            grabber.start();

        int seconds = (int) (grabber.getLengthInTime() / 100000);
        information.setTrackLength(FormatTime.formatTimeWithColons(seconds));
        int bitRate = grabber.getAudioBitrate();
        information.setBitrate(bitRate + "");
        try {
            grabber.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();

            // don't care we are finsished any way
        }
        return information;
    }

    public List<AudioInformation> extractInformationFromCueFile(File file) throws CueSheetExeception {// extracts audio information  from a single audio file  that does NOT have supported  tag reding writing.
        CueSheet sheet = cueSheetReader.readCueSheet(file);
        List<AudioInformation> information = sheet.getTracks();
        return information;
    }

    public CueSheet getCueSheet(File file) throws CueSheetExeception {// extracts audio information  from a single audio file  that does NOT have supported  tag reding writing.
        CueSheet sheet = cueSheetReader.readCueSheet(file);
        return sheet;
    }

    public List<AudioInformation> createSongTracksFromCueSheet(File cueFile) throws CueSheetExeception {
        CueSheet sheet = cueSheetReader.readCueSheet(cueFile);
        return sheet.getTracks();
    }

    public List<AudioInformation> splitAudioFileWithCueSheet(AudioInformation track, File cueFile) throws CueSheetExeception {
        CueSheet sheet = cueSheetReader.readCueSheet(cueFile);
        String audioFilePath = track.getAudioFilePath();
        sheet.setAudioFilePath(audioFilePath);
        return sheet.getTracks();
    }

    public ArrayList<AudioInformation> getAudioInformation() {
        return audioInformation;
    }

    public ArrayList<String> getArtists() {
        return artists;
    }

    public ArrayList<String> getAlbums() {
        return albums;
    }

    public String getInformation(AudioInformation information, FieldKey key) {// returns the given information for specified Jaudio tagger field key and audio information instance
        switch (key) {
            case ALBUM:
                return information.getAlbum();
            case ARTIST:
                return information.getArtist();
            case BPM:
                return information.getBpm();
            case COMMENT:
                return information.getComment();
            case COMPOSER:
                return information.getComposer();
            case CONDUCTOR:
                return information.getConductor();
            case COPYRIGHT:
                return information.getCopyright();
            case COUNTRY:
                return information.getCountry();
            case CUSTOM1:
                return FormatTime.formatTimeWithColons(information.getCueStart());
            case DISC_NO:
                return information.getDiscNo();
            case DISC_TOTAL:
                return information.getDiscTotal();
            case ENCODER:
                return information.getEncoder();
            case ENGINEER:
                return information.getEngineer();
            case ENSEMBLE:
                return information.getEnsemble();
            case GENRE:
                return information.getGenre();
            case GROUP:
                return information.getGroup();
            case INSTRUMENT:
                return information.getInstruemnt();
            case LANGUAGE:
                return information.getLaugauge();
            case LYRICIST:
                return information.getLyricist();
            case LYRICS:
                return information.getLyrics();
            case MEDIA:
                return information.getMedia();
            case ORIGINAL_ARTIST:
                return information.getOriginalArtist();
            case PRODUCER:
                return information.getProducer();
            case RATING:
                return information.getRating();
            case RECORD_LABEL:
                return information.getRecordLabel();
            case TEMPO:
                return information.getTempo();
            case TITLE:
                return information.getTitle();
            case TRACK:
                return String.valueOf(information.getTrack());
            case TRACK_TOTAL:
                return information.getTrackTotal();
            case YEAR:
                return information.getYear();
        }
        return "";
    }

    public void setInformation(AudioInformation audioInformation, FieldKey key, String information) {
        if (information == null) {
            return;
        }
        switch (key) {
            case ALBUM:
                audioInformation.setAlbum(information);
                break;
            case ARTIST:
                audioInformation.setArtist(information);
                break;
            case COMMENT:
                audioInformation.setComment(information);
                break;
            case COMPOSER:
                audioInformation.setComposer(information);
                break;
            case CONDUCTOR:
                audioInformation.setConductor(information);
                break;
            case COPYRIGHT:
                audioInformation.setCopyright(information);
                break;
            case COUNTRY:
                audioInformation.setCountry(information);
                break;
            case DISC_NO:
                audioInformation.setDiscNo(information);
                break;
            case ENCODER:
                audioInformation.setEncoder(information);
                break;
            case ENGINEER:
                audioInformation.setEngineer(information);
                break;
            case ENSEMBLE:
                audioInformation.setEnsemble(information);
                break;
            case GENRE:
                audioInformation.setGenre(information);
                break;
            case GROUP:
                audioInformation.setGroup(information);
                break;
            case INSTRUMENT:
                audioInformation.setInstruemnt(information);
                break;
            case LANGUAGE:
                audioInformation.setLangauge(information);
                break;
            case LYRICIST:
                audioInformation.setLyricist(information);
                break;
            case LYRICS:
                audioInformation.setLyrics(information);
                break;
            case MEDIA:
                audioInformation.setMedia(information);
                break;
            case ORIGINAL_ARTIST:
                audioInformation.setOriginalArtist(information);
                break;
            case PRODUCER:
                audioInformation.setProducer(information);
                break;
            case RATING:
                audioInformation.setRating(information);
                break;
            case RECORD_LABEL:
                audioInformation.setRecordLabel(information);
                break;
            case TITLE:
                audioInformation.setTitle(information);
                break;
            case TRACK:
                audioInformation.setTrack(information);
                break;
            case TRACK_TOTAL:
                audioInformation.setTrackTotal(information);
                break;
            case YEAR:
                audioInformation.setYear(information);
                break;
        }
        audioInformation.clearAudioFile();
    }

    public boolean writeFieldsToFile(FieldKey key, String text, AudioFile audioFile) {// writes given string of text to given filed key
        // in a j audio tagger audio file returns true  file was written and false ifr it was not
        try {
            if (audioFile == null) {
                return false;

            }
            audioFile.getTag().setField(key, text);
            AudioFileIO.write(audioFile);

        } catch (FieldDataInvalidException e) {
            return false;
        } catch (CannotWriteException e) {
            return false;
        } catch (KeyNotFoundException e) {

            return false;

        }


        return true;


    }

    public File getPhysicalFile(AudioInformation information) {
    String audioFilePath=information.getAudioFilePath();

    File file = new File(audioFilePath);
        if(file.exists()==true){
            return file;

        }

        return null;


}
    public AudioFile getAudioFile(AudioInformation information){
        AudioFile audioFile=null;
            try {
              audioFile = AudioFileIO.read(new File(information.getAudioFilePath()));
            } catch (CannotReadException e) {
                new OptionPane().showOptionPane("File Is Corrupt "+e.getMessage(), "OK");
            } catch(FileNotFoundException e){
                new OptionPane().showLocateFilePane(information);
            } catch (IOException e) {
                new OptionPane().showOptionPane("File Cannot Be Played "+e.getMessage(), "OK");
            } catch (TagException e) {
                new OptionPane().showOptionPane("File Missing Tags "+e.getMessage(), "OK");
            } catch (ReadOnlyFileException e) {
                new OptionPane().showOptionPane("File Is Read Only Check File Permissions "+e.getMessage(), "OK");
            } catch (InvalidAudioFrameException e) {
                new OptionPane().showOptionPane("File Has Bad Frames "+e.getMessage(), "OK");
            }

        return audioFile;
    }





    public ArrayList<String> getAllInformation(AudioInformation audioInformation) {
        ArrayList<String> information = new ArrayList<>();
        information.add(audioInformation.getTrackLength());
        information.add(audioInformation.getBitRate());
        information.add(audioInformation.getArtist());
        information.add(audioInformation.getAlbum());
        information.add(audioInformation.getBpm());
        information.add(audioInformation.getCatalogNo());
        information.add(audioInformation.getComment());
        information.add(audioInformation.getComposer());
        information.add(audioInformation.getConductor());
        information.add(audioInformation.getCopyright());
        information.add(audioInformation.getCountry());
        information.add(audioInformation.getDiscNo());
        information.add(audioInformation.getDiscTotal());
        information.add(audioInformation.getEncoder());
        information.add(audioInformation.getEngineer());
        information.add(audioInformation.getEnsemble());
        information.add(audioInformation.getGenre());
        information.add(audioInformation.getGroup());
        information.add(audioInformation.getInstruemnt());
        information.add(audioInformation.getLaugauge());
        information.add(audioInformation.getLyricist());
        information.add(audioInformation.getLyrics());
        information.add(audioInformation.getMedia());
        information.add(audioInformation.getProducer());
        information.add(audioInformation.getOriginalArtist());
        information.add(audioInformation.getRanking());
        information.add(audioInformation.getRating());
        information.add(audioInformation.getRecordLabel());
        information.add(audioInformation.getTempo());
        information.add(audioInformation.getTitle());
        information.add(String.valueOf(audioInformation.getTrack()));
        information.add(audioInformation.getTrackTotal());
        information.add(audioInformation.getYear());
        return information;
    }
    public ArrayList<String> getInformation(List<FieldKey> keys, AudioInformation information) {
        ArrayList<String> strings = new ArrayList<>();
        int size = keys.size();
        for (int count = 0; count < size; count++) {
            strings.add(getInformation(information, keys.get(count)));
        }
        return strings;
    }
    public Image getFirstImage(AudioInformation information) {
        Tag tag = information.getAudioFile().getTag();
        if (tag != null) {
            List<Artwork> artworks = tag.getArtworkList();
            if (artworks.size() > 0) {
                byte[] b = artworks.get(0).getBinaryData();// get image data
                if (b != null) {
                    ByteArrayInputStream stream = new ByteArrayInputStream(b);
                    try {
                        BufferedImage image = ImageIO.read(stream);// make data into a bufferd image
                        if (image != null) {
                            Image fxImage = SwingFXUtils.toFXImage(image, null); // convert to fxImage
                            return fxImage;
                        }
                    } catch (IllegalArgumentException | IOException e) { // if cant read image set image to no image;
                        e.printStackTrace();
                    }
                }
                String path = artworks.get(0).getImageUrl();
                try {
                    Image image = new Image("file:" + path);
                    if (image != null) {
                        return image;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            //no image try using image url in  embedde into audio file
        }
        List<String> artworkPaths = information.getAlbumArtPaths();
        if (artworkPaths.size() > 0) {
            try {
                Image image = new Image("file:" + artworkPaths.get(0));
                if (image != null) {
                    return image;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return new Image("/noAlbumCover.jpg");
    }
}
