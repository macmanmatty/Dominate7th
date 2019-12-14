package sample.AcoustID;

import com.google.gson.Gson;
import sample.*;
import sample.AcoustID.AcoustIDJson.Result;
import sample.AcoustID.AcoustIDJson.Results;
import sample.AcoustID.MusicBrianzJson.MusicBrainzResult;
import sample.Library.AudioInformation;
import sample.Library.Settings;
import sample.Utilities.HttpReader;
import sample.Windows.OptionPane;

import java.io.*;

public class AcoustID {
    private MainAudioWindow window;
    private Settings settings;
    private   String fpcalcLocation="fpcalc";
    private final String acosIDLookUpUrl ="https://api.acoustid.org/v2/lookup?client=DPBREMyuby&meta=recordings+releasegroups+compress";
    private final String acousIDApiKey="DPBREMyuby";
    private final String musicBrianzUrl="https://musicbrainz.org/ws/2/recording/";
    public AcoustID( MainAudioWindow window) {
        this.window=window;
        this.settings=window.getLibrary().getSettings();
        String fpCalcPath=settings.getFpCalcPath();
        if(fpCalcPath!=null && !(fpCalcPath.isEmpty())){// sets the user defined fpcalc locationif there is one

            fpcalcLocation=fpCalcPath;

        }

    }

    public AcoustID() {
    }

    public ChromaPrint getChromaPrint(AudioInformation information) throws IOException {
        ChromaPrint chromaPrint=getChromaPrint(information.getAudioFilePath());

        return chromaPrint;
    }

    // gets A ChromaPrint results using the FPCalc command line program
    public ChromaPrint getChromaPrint(String filePath) throws IOException {
        File file= new File(filePath);
        if(file.exists()==false){
            return new ChromaPrint("","");

        }
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(fpcalcLocation, null);

        processBuilder.redirectErrorStream(true);
        processBuilder.command().set(1, filePath);
        final Process fpcalcProc = processBuilder.start();
        final BufferedReader br = new BufferedReader(new InputStreamReader(fpcalcProc.getInputStream()));
        String line;
        String chromaprint = null;
        String duration = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("FINGERPRINT=")) {
                chromaprint = line.substring("FINGERPRINT=".length());
            } else if (line.startsWith("DURATION=")) {
                duration = line.substring("DURATION=".length());
            }
        }
            return new ChromaPrint(chromaprint, duration);

        }
        catch (FileNotFoundException e){ // can't locate fpcalc so quit process

            e.printStackTrace();
            new OptionPane().showOptionPane("Cannot Locate FPCalc, check the path for FpCalc the settings", "ok");
            Thread.currentThread().interrupt();// can't find fp calc so stop process.

        }

        return new ChromaPrint("","");

    }

    // gets the best Acoustid result for a given chroma print
    public Result getBestResult(ChromaPrint chromaPrint) throws IOException {
            String urlString=acosIDLookUpUrl+"&duration="+chromaPrint.getDuration()+"&fingerprint="+chromaPrint.getChromaprint();
            Results results=new HttpReader().getJSONFromUrl(urlString, Results.class);
            if(results!=null) {
                Result result = getBestResult(results);
                return result;
            }
            return null;
    }


    // get all Acoustid Results for a given chroma print.
    public Results getResults(ChromaPrint chromaPrint) throws IOException {
        String urlString=acosIDLookUpUrl+"&duration="+chromaPrint.getDuration()+"&fingerprint="+chromaPrint.getChromaprint();
        Results results=new HttpReader().getJSONFromUrl(urlString, Results.class);

        return results;
    }
    // gets the best result from a results object containing all Acoustid results
    public  Result getBestResult(Results results) {
        if (results.getResults().size() > 0) {
            Result bestResult = results.getResults().get(0);
            double currentScore = Double.parseDouble(bestResult.getScore());
            for (final Result result : results.getResults()) {
                final double score = Double.parseDouble(result.getScore());
                if (score > currentScore) {
                    bestResult = result;
                    currentScore = score;
                }
            }
            return bestResult;
        } else {
            return null;
        }
    }
    private Results getResults(String data) {

        Gson gson= new Gson();
        Results results=gson.fromJson(data, Results.class);
        return results;

    }

    public MusicBrainzResult getMusicBrainzResult(Result result) throws IOException {
        if(result==null){

            return null;
        }
        String urlString=musicBrianzUrl+result.getId()+"?inc=artists+releases&fmt=json";

        MusicBrainzResult musicBrainzJson=new HttpReader().getJSONFromUrl(urlString, MusicBrainzResult.class);


        return musicBrainzJson;

    }

}
