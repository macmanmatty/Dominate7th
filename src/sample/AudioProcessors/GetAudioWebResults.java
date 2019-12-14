package sample.AudioProcessors;
import org.jaudiotagger.audio.AudioFile;
import sample.AcoustID.AcoustID;
import sample.AcoustID.AcoustIDJson.Result;
import sample.AcoustID.AcoustIDJson.Results;
import sample.AcoustID.AudioWebResult;
import sample.AcoustID.ChromaPrint;
import sample.AcoustID.MusicBrianzJson.MusicBrainzResult;
import sample.Library.AudioInformation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class GetAudioWebResults {
  private   String actionName;
  private   List<String> errorMessages= new ArrayList<>();
   private  AcoustID acoustID = new AcoustID();
   private List<AudioWebResult> audioWebResults = new ArrayList<>();
    public AudioWebResult getResult (AudioFile audioFile) { // returns a Audioweb result object for a given audio file
        return getResult(audioFile.getFile().getPath());
    }
    public AudioWebResult getResult(AudioInformation information) {
       return  getResult(information.getAudioFilePath());
    }
    private AudioWebResult getResult(String path){
        Results results=null;
        try {
            ChromaPrint chromaPrint = acoustID.getChromaPrint(path); // get chroma print
            if(chromaPrint!=null) {
                results = acoustID.getResults(chromaPrint);
            }
        }
        catch (IOException e) {
            errorMessages.add(e.getMessage());
            e.printStackTrace();
        }
        List<MusicBrainzResult> musicBrainzResults= new ArrayList<>();
        try {
            if(results!=null) { // get the music brainz result for the acoustID result id
                int size=results.getResults().size();
                for(int count=0; count<size; count++){
                MusicBrainzResult musicBrainzResult = acoustID.getMusicBrainzResult(results.getResults().get(count));
                if(musicBrainzResult!=null) {
                    musicBrainzResults.add(musicBrainzResult);
                }
                }
            }
        }
        catch (IOException e){
            errorMessages.add(e.getMessage());
            e.printStackTrace();
        }
            AudioWebResult webResult = new AudioWebResult(results, musicBrainzResults);
            audioWebResults.add(webResult);
            System.out.print("result Added");
            return  webResult;
    }
    public List<String> getErrorMessages() {
        return errorMessages;
    }
    public String getActionName() {
        return  actionName;
    }
    public List<AudioWebResult> getAudioWebResults() {
        return audioWebResults;
    }
}
