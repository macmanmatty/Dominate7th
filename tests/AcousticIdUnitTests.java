import junit.framework.TestCase;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;
import sample.AcoustID.AcoustID;
import sample.AcoustID.ChromaPrint;
import sample.Utilities.HttpReader;
import sample.AcoustID.MusicBrianzJson.MusicBrainzResult;
import sample.AcoustID.AcoustIDJson.Result;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AcousticIdUnitTests {



    @Test

    public void getMuiscBrainzTags() throws IOException {


        HttpReader json= new HttpReader();

        String url="https://musicbrainz.org/ws/2/recording/cd2e7c47-16f5-46c6-a37c-a1eb7bf599ff?inc=artists+releases&fmt=json";
        MusicBrainzResult results= json.getJSONFromUrl(url, MusicBrainzResult.class);

        TestCase.assertEquals(results.getTitle(), "Lower Your Eyelids to Die With the Sun");





    }
    @Test

    public void getAcousticIdJson() throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException {

       AcoustID getAudioInformationFromWeb= new AcoustID();
        ChromaPrint print=getAudioInformationFromWeb.getChromaPrint("/Users/jessematty/Desktop/iams.mp3");
        Result result=getAudioInformationFromWeb.getBestResult(print);

        TestCase.assertEquals(result.getRecordings().get(0).getReleaseGroups().get(0).getTitle(), "Quadrophenia");
    }







}


