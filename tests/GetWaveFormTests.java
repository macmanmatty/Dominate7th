import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;
import sample.MusicPlayers.GetAudioWaveForm;

import java.io.File;
import java.io.IOException;

public class GetWaveFormTests {

    @Test
    public void getData() throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
        AudioFile f= AudioFileIO.read(new File("/Users/jessematty/Desktop/cc.mp3"));
        new GetAudioWaveForm().getData(f);



    }
}
