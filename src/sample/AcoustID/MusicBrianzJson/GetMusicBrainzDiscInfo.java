package sample.AcoustID.MusicBrianzJson;

import org.musicbrainz.discid.JMBDiscId;

public class GetMusicBrainzDiscInfo {
    JMBDiscId discId= new JMBDiscId();
    String discPath;



    public void getDiscInfo(String discPath){
       String lookUpUrl= discId.getDiscIdLookupUrl(discPath);



    }
}
