package sample.AudioProcessors;

import sample.AcoustID.AcoustIDJson.*;
import sample.AcoustID.AudioWebResult;
import sample.AcoustID.MusicBrianzJson.MusicBrainzResult;
import sample.AcoustID.MusicBrianzJson.Release;
import sample.Library.AudioInformation;

import java.util.ArrayList;
import java.util.List;

public class GetAudioInformationFromWebResults {


   public List<AudioInformation> getAlbumData(AudioWebResult result){ // pasrses the JSON acoustID result and uses  that acoustID result ID
       //to get  a musicbrainz result and then parse the JSON result generated from music brainz.
       Results results=result.getAcoustIdResult();
       List<Result> recordings=results.getResults();
       List<AudioInformation> allAlbums= new ArrayList<>();
       int size=recordings.size();
       for(int count=0; count<size; count++){

           List<Recording> resultRecordings=recordings.get(count).getRecordings();
           int size2=resultRecordings.size();
           for(int count2=0; count2<size2; count2++){
               Recording recording=resultRecordings.get(count2);
               String title=recording.getTitle();
               String acoustIDId=recording.getId();
               List<Artists> artists= resultRecordings.get(count2).getArtists();
               List<ReleaseGroups> releaseGroups=resultRecordings.get(count2).getReleaseGroups();
               List<AudioInformation> albumData= new ArrayList<>();
               String artist="";
               if(artists.size()>0) {
                   artist = artists.get(0).getName();
               }

               int size3=releaseGroups.size();
               for(int count3=0; count3<size3; count3++) {
                    String album=releaseGroups.get(count3).getTitle();
                    AudioInformation information= new AudioInformation(false);// create new audio information but, don't write fields as there no audio file to write to
                    information.setAlbum(album);
                    information.setTitle(title);
                    information.setArtist(artist);
                    albumData.add(information);
                    information.setAcoustIDId(acoustIDId);
               }
               allAlbums.addAll(albumData);


               }


       }

       List<MusicBrainzResult> musicBrainzResults=result.getMusicBrainzResult();
      size=musicBrainzResults.size();
       for(int count=0; count<size; count++) {
           String title=musicBrainzResults.get(count).getTitle();
           String artist=musicBrainzResults.get(count).getArtistCredit().get(0).getName();

           List<Release> releases=musicBrainzResults.get(count).getReleases();
           int size2=releases.size();

           List<AudioInformation> albumData= new ArrayList<>();

           for(int count2=0; count2<size2; count2++) {
               Release release=releases.get(count2);
               String album=release.getTitle();
               String country=release.getCountry();
               String year=release.getDate();
               String musicBrainzId=release.getId();
               AudioInformation information= new AudioInformation(false);
               information.setTitle(title);
               information.setArtist(artist);
               information.setAlbum(album);
               information.setCountry(country);
               information.setYear(year);
               information.setMusicBrainzId(musicBrainzId);
               albumData.add(information);

           }
           allAlbums.addAll(albumData);
           }

       return allAlbums;

   }
}
