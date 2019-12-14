package sample.AcoustID;

import sample.AcoustID.AcoustIDJson.Result;
import sample.AcoustID.AcoustIDJson.Results;
import sample.AcoustID.MusicBrianzJson.MusicBrainzResult;

import java.util.ArrayList;
import java.util.List;

public class AudioWebResult {
   private Results acoustIdResults;
   private List<MusicBrainzResult> musicBrainzResult= new ArrayList<>();
   private int numberOfAcoustIDResults;
    private int numberOfAcoustMusicBrainzReults;


    public AudioWebResult(Results acoustIdResult, List<MusicBrainzResult> results) {
        this.acoustIdResults = acoustIdResult;
        this.musicBrainzResult = results;
        if(acoustIdResults!=null) {
            numberOfAcoustMusicBrainzReults = results.size();
        }

        if(musicBrainzResult!=null) {
            numberOfAcoustIDResults = acoustIdResult.getResults().size();
        }

    }

    public AudioWebResult() {
    }

    public Results getAcoustIdResult() {
        return acoustIdResults;
    }

    public void setAcoustIdResult(Results acoustIdResult) {
        this.acoustIdResults = acoustIdResult;
        numberOfAcoustIDResults=acoustIdResult.getResults().size();

    }

    public List<MusicBrainzResult> getMusicBrainzResult() {
        return musicBrainzResult;
    }

    public void setMusicBrainzResults(List<MusicBrainzResult> musicBrainzResult) {
        this.musicBrainzResult = musicBrainzResult;
        numberOfAcoustMusicBrainzReults=musicBrainzResult.size();

    }

    public int getTotalResults(){
        return numberOfAcoustIDResults+numberOfAcoustMusicBrainzReults;

    }
}
