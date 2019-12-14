import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;
import sample.AcoustID.AudioWebResult;
import sample.AudioProcessors.GetAudioWebResults;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class GetAudioWebResultsTests {

    @Test
    public void getInformation() throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {

        GetAudioWebResults getAudioWebResults = new GetAudioWebResults();
        getAudioWebResults.getResult(AudioFileIO.read(new File("/Users/jessematty/Desktop/iams.mp3")));
        List<AudioWebResult> results= getAudioWebResults.getAudioWebResults();

        assertNotNull(results.get(0));
        assertEquals(results.get(0).getAcoustIdResult().getResults().get(0).getRecordings().get(0).getReleaseGroups().get(0).getTitle(), "Quadrophenia");

    }
}
