package sample.AudioProcessors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import sample.AcoustID.AcoustID;
import sample.AcoustID.AudioWebResult;
import sample.Library.AudioInformation;
import sample.Utilities.AudioFileUtilities;
import sample.Windows.FileProgressBar;
import sample.Windows.UpdateLabel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetAcoustIDResultsForSongs implements  AudioFileAction, AudioInformationAction {

   private  CountDownLatch countDownLatch;
  private   int numberOfThreads;
  private   String actionName;
  private   List<String> errorMessages= new ArrayList<>();
   private  AcoustID acoustID = new AcoustID();
   private GetAudioWebResults getAudioWebResults = new GetAudioWebResults();
   private List<List<AudioInformation>> songResults= new ArrayList<>();// the results form music brainz and acoustID
   private AudioFileUtilities audioFileUtilities= new AudioFileUtilities();
   private boolean completed;
   private int currentErrors;
   private String fileName;




    @Override
    public void action(AudioFile audioFile) {
        this.fileName=audioFile.getFile().getName();



        AudioWebResult audioWebResult = getAudioWebResults.getResult(audioFile);
        getInformation(audioWebResult, audioFile);
    }

    public void action(AudioInformation information) {
        AudioWebResult audioWebResult = getAudioWebResults.getResult(information);
        getInformation(audioWebResult, information.getAudioFile());



    }

    // gets the album results
    private void getInformation(AudioWebResult audioWebResult, AudioFile audioFile){

        currentErrors=0;
        completed=false;

             List<AudioInformation> data=new GetAudioInformationFromWebResults().getAlbumData(audioWebResult);
             int size=data.size();
             for(int count=0; count<size; count++){ // set basic file information  to the results
                 File file=audioFile.getFile();
                 data.get(count).setAudioFilePath(file.getAbsolutePath());
                 AudioHeader  header= audioFile.getAudioHeader();
                 data.get(count).setBitrate(header.getBitRate());
                 data.get(count).setAudioFormat(audioFileUtilities.getExtensionOfFile(file));

             }
             songResults.add(data);
             completed=true;


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

    public List<List<AudioInformation>> getSongResults() {
        return songResults;
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
        return fileName;
    }

    @Override
    public void setProgressBar(FileProgressBar progressBar) {

    }

    @Override
    public void setUpdateLabel(UpdateLabel label) {

    }

    @Override
    public List<AudioInformation> getTracks() {
        return new ArrayList<>();
    }


}
