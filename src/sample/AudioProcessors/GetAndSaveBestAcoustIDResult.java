package sample.AudioProcessors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import sample.AcoustID.AcoustID;
import sample.AcoustID.ChromaPrint;
import sample.AcoustID.MusicBrianzJson.MusicBrainzResult;
import sample.AcoustID.AcoustIDJson.Result;
import sample.Library.AudioInformation;
import sample.Windows.FileProgressBar;
import sample.Windows.UpdateLabel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetAndSaveBestAcoustIDResult implements  AudioFileAction, AudioInformationAction {

   private  CountDownLatch countDownLatch;
  private   int numberOfThreads;
  private   String actionName;
  private   List<String> errorMessages= new ArrayList<>();
   private  AcoustID acoustID = new AcoustID();
   private SaveMusicBrainzToAudioFile saveMusicBrainzToAudioFile= new SaveMusicBrainzToAudioFile();
   private boolean useMusicBrainz;
   private boolean getAlbumArt;
   private boolean embedAlbumArt;
    SaveAlbumArtFromWeb saveAlbumArtFromWeb;
    private boolean completed;
    private int currentErrors;
    private String fileName;
    private List<AudioInformation> tracks= new ArrayList<>();

    public GetAndSaveBestAcoustIDResult(  boolean useMusicBrainz, boolean getAlbumArt, boolean embedAlbumArt) {
        this.useMusicBrainz = useMusicBrainz;
        this.getAlbumArt = getAlbumArt;
        this.embedAlbumArt = embedAlbumArt;
        if(getAlbumArt==true) {
            saveAlbumArtFromWeb = new SaveAlbumArtFromWeb(  embedAlbumArt);
        }


    }

    @Override
    public void action(AudioFile audioFile) {
        currentErrors=0;
        completed=false;
        this.fileName=audioFile.getFile().getName();
        Result result=null;
        try {
            ChromaPrint chromaPrint = acoustID.getChromaPrint(audioFile.getFile().getAbsolutePath());
            if (chromaPrint != null) {
                result = acoustID.getBestResult(chromaPrint);
            }

            boolean artistSet = false;
            boolean titleSet = false;
            boolean albumSet = false;


            if (result != null) {
                Tag tag = audioFile.getTag();
                try {
                    try {
                        String titleName = result.getRecordings().get(0).getTitle();
                        if (titleName != null && !(titleName.isEmpty())) {

                            titleSet = true;
                            tag.setField(FieldKey.TITLE, titleName);

                        }
                    } catch (IndexOutOfBoundsException e) { // if anything is out of bounds we don't care  json has no data


                    } catch (NullPointerException e) { // if anything  is null we don't care  json has no data for us to add to the file tags

                    }

                    try {
                        String artistName = result.getRecordings().get(0).getArtists().get(0).getName();
                        if (artistName != null && !(artistName.isEmpty())) {

                            artistSet = true;
                            tag.setField(FieldKey.ARTIST, artistName);

                        }
                    } catch (IndexOutOfBoundsException e) {


                    } catch (NullPointerException e) {

                    }
                    try {
                        String albumName = result.getRecordings().get(0).getReleaseGroups().get(0).getTitle();
                        System.out.println(albumName);

                        if (albumName != null && !(albumName.isEmpty())) {
                            albumSet = true;
                            tag.setField(FieldKey.ALBUM, albumName);
                        }
                    } catch (IndexOutOfBoundsException e) {


                    } catch (NullPointerException e) {

                    }


                    tag.setField(FieldKey.ACOUSTID_ID, result.getId());

                    if (artistSet == true || albumSet == true || titleSet == true) {
                        AudioFileIO.write(audioFile);
                        System.out.println("fileWritten!!");

                    }


                    MusicBrainzResult musicBrainzResult = acoustID.getMusicBrainzResult(result);

                    if (musicBrainzResult != null) {


                    if (albumSet == false || artistSet == false || titleSet == false || useMusicBrainz == true || getAlbumArt == true) {


                            saveMusicBrainzToAudioFile.setResultToAudioFile(audioFile, musicBrainzResult);
                            errorMessages.addAll(saveMusicBrainzToAudioFile.getErrors());

                            String title = musicBrainzResult.getTitle();
                            if (title != null && !(title.isEmpty())) {

                                tag.setField(FieldKey.TITLE, title);
                            }
                            try {
                                String artistName = musicBrainzResult.getArtistCredit().get(0).getName();
                                if (artistName != null && !(artistName.isEmpty())) {
                                    tag.setField(FieldKey.ARTIST, artistName);
                                }
                            } catch (IndexOutOfBoundsException e) {


                            } catch (NullPointerException e) {

                            }

                            try {
                                String albumName = musicBrainzResult.getReleases().get(0).getTitle();

                                if (albumName != null && !(albumName.isEmpty())) {
                                    tag.setField(FieldKey.ALBUM, albumName);

                                }
                            } catch (IndexOutOfBoundsException e) {


                            } catch (NullPointerException e) {

                            }

                        }
                        try {
                            String year = musicBrainzResult.getReleases().get(0).getDate();

                            if (year != null && !(year.isEmpty())) {
                                tag.setField(FieldKey.YEAR, year);
                            }
                        } catch (IndexOutOfBoundsException e) {


                        } catch (NullPointerException e) {

                        }


                        try {
                            String country = musicBrainzResult.getReleases().get(0).getCountry();
                            if (country != null && !(country.isEmpty())) {
                                tag.setField(FieldKey.COUNTRY, country);
                            }
                        } catch (IndexOutOfBoundsException e) {


                        }
                        try {
                            String quality = musicBrainzResult.getReleases().get(0).getQuality();
                            if (quality != null && !(quality.isEmpty())) {
                                tag.setField(FieldKey.QUALITY, quality);
                            }
                        } catch (IndexOutOfBoundsException e) {


                        } catch (NullPointerException e) {

                        }




                    tag.setField(FieldKey.MUSICBRAINZ_WORK, result.getId());

                    AudioFileIO.write(audioFile);


                }

            } catch (FieldDataInvalidException e) {
                    e.printStackTrace();
                } catch (CannotWriteException e) {
                    e.printStackTrace();
                }
            }
        }

        catch(MalformedURLException e ){
                errorMessages.add(e.getMessage());
                currentErrors++;

            }
        catch(FileNotFoundException e){

                errorMessages.add(e.getMessage());
                currentErrors++;

            }

        catch(IOException e){
                errorMessages.add(e.getMessage());
                currentErrors++;
            }

        completed=true;





    }



    public void action(AudioInformation information) {

        try {
            String path = information.getAudioFilePath();
            if (path == null || path.isEmpty()) {
                return;
            }
            ChromaPrint chromaPrint = acoustID.getChromaPrint(path);
            Result result = acoustID.getBestResult(chromaPrint);
            if (result == null) {//  there is no data  exit out of function
                return;
            }
            boolean artistSet = false;
            boolean titleSet = false;
            boolean albumSet = false;


            AudioFile audioFile = information.getAudioFile();
            Tag tag = audioFile.getTag();
            try {
                try {
                    String titleName = result.getRecordings().get(0).getTitle();
                    if (titleName != null && !(titleName.isEmpty())) {

                        titleSet = true;
                        information.setTitle(titleName);

                    }
                } catch (IndexOutOfBoundsException e) { // if anything is out of bounds we don't json has no data


                } catch (NullPointerException e) { // if anything  is null we don't care  json has no data for us to add to the file tags

                }

                try {
                    String artistName = result.getRecordings().get(0).getArtists().get(0).getName();
                    if (artistName != null && !(artistName.isEmpty())) {

                        artistSet = true;
                        information.setArtist(artistName);
                    }
                } catch (IndexOutOfBoundsException e) {


                } catch (NullPointerException e) {

                }
                try {
                    String albumName = result.getRecordings().get(0).getReleaseGroups().get(0).getTitle();
                    System.out.println(albumName);

                    if (albumName != null && !(albumName.isEmpty())) {
                        albumSet = true;
                        information.setAlbum(albumName);
                    }
                } catch (IndexOutOfBoundsException e) {


                } catch (NullPointerException e) {

                }


                if (result != null) {
                    tag.setField(FieldKey.ACOUSTID_ID, result.getId());
                }


                if (artistSet == true || albumSet == true || titleSet == true) {
                    AudioFileIO.write(audioFile);
                    System.out.println("fileWritten!!");

                }


                MusicBrainzResult musicBrainzResult = acoustID.getMusicBrainzResult(result);


                if (musicBrainzResult != null) {
                    if (albumSet == false || artistSet == false || titleSet == false) {


                        saveMusicBrainzToAudioFile.setResultToAudioFile(audioFile, musicBrainzResult);
                        errorMessages.addAll(saveMusicBrainzToAudioFile.getErrors());

                        String title = musicBrainzResult.getTitle();
                        if (title != null && !(title.isEmpty())) {

                            information.setTitle(title);
                        }
                        try {
                            String artistName = musicBrainzResult.getArtistCredit().get(0).getName();
                            if (artistName != null && !(artistName.isEmpty())) {
                                information.setArtist(artistName);
                            }
                        } catch (IndexOutOfBoundsException e) {


                        } catch (NullPointerException e) {

                        }

                        try {
                            String albumName = musicBrainzResult.getReleases().get(0).getTitle();

                            if (albumName != null && !(albumName.isEmpty())) {
                                information.setAlbum(albumName);

                            }
                        } catch (IndexOutOfBoundsException e) {


                        } catch (NullPointerException e) {

                        }

                    }
                    try {
                        String year = musicBrainzResult.getReleases().get(0).getDate();

                        if (year != null && !(year.isEmpty())) {
                            information.setYear(year);
                        }
                    } catch (IndexOutOfBoundsException e) {


                    } catch (NullPointerException e) {

                    }


                    try {
                        String country = musicBrainzResult.getReleases().get(0).getCountry();
                        if (country != null && !(country.isEmpty())) {
                            information.setCountry(country);
                        }
                    } catch (IndexOutOfBoundsException e) {


                    } catch (NullPointerException e) { // if anything  is null we don't care  json has no data for us to add to the file tags

                    }
                    try {
                        String quality = musicBrainzResult.getReleases().get(0).getQuality();
                        if (quality != null && !(quality.isEmpty())) {
                            tag.setField(FieldKey.QUALITY, quality);
                        }
                    } catch (IndexOutOfBoundsException e) {


                    } catch (NullPointerException e) {

                    }


                    if (result != null) {
                        tag.setField(FieldKey.MUSICBRAINZ_WORK, result.getId());
                    }


                    AudioFileIO.write(audioFile);
                    if(getAlbumArt==true){

                        saveAlbumArtFromWeb.getAlbumArtFromWeb(result.getId(), information);

                    }



                }
            }

            catch(FieldDataInvalidException | CannotWriteException e){
                    errorMessages.add(e.getMessage());

                }


            }


        catch (MalformedURLException e ){
            errorMessages.add(e.getMessage());

        }
        catch (FileNotFoundException e){

            errorMessages.add(e.getMessage());

        }

        catch (IOException e) {
            errorMessages.add(e.getMessage());
        }


    }






    @Override
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    @Override
    public String getActionName() {
        return  actionName;

    }

    @Override
    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    @Override
    public void setCountDownLatch(CountDownLatch latch) {


        this.countDownLatch=latch;

    }

    @Override
    public void stopAction() {

    }

    @Override
    public int getCurrentErrors() {
        return currentErrors;
    }

    public boolean completed() {
        return completed;
    }

    @Override
    public String getFileName() {
        return  fileName;
    }

    @Override
    public void setProgressBar(FileProgressBar progressBar) {

    }

    @Override
    public void setUpdateLabel(UpdateLabel label) {

    }

    @Override
    public List<AudioInformation> getTracks() {
        return tracks;
    }
}
