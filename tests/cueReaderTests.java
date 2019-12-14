import org.junit.Test;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.AudioProcessors.CueSheets.CueSheetReader;
import sample.Library.CueSheets.CueSheet;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

public class cueReaderTests {


    @Test

    public void getCueSheetTest() throws CueSheetExeception {
        File file= new File("/Users/jessematty/Desktop/Dr. John - Discography [FLAC img + CUE]/Official/[1968] Gris-Gris.cue");
        CueSheetReader cueSheetReader= new CueSheetReader();
        CueSheet sheet=cueSheetReader.readCueSheet(file);
        assertEquals(sheet.getTracks().size(), 7);
        assertEquals(sheet.getTracks().get(0).getGenre(), "Rock");
        assertEquals(sheet.getAlbumName(), "Gris-Gris");

        assertEquals(sheet.getTracks().get(0).getCueStart(), 1);
        assertEquals(sheet.getTracks().get(1).getCueStart(), 337);
        assertEquals(sheet.getTracks().get(0).getTrackLengthNumber(),336);
        assertEquals(sheet.getTracks().get(0).getAudioFilePath(),  "/Users/jessematty/Desktop/Dr. John - Discography [FLAC img + CUE]/Official/[1968] Gris-Gris.flac");








    }

    public void getDurationTest() throws CueSheetExeception {
        File file= new File("/Users/jessematty/Desktop/splittest/[1995] Afterglow.cue");
        CueSheetReader cueSheetReader= new CueSheetReader();
        CueSheet sheet=cueSheetReader.readCueSheet(file);
        assertEquals(sheet.getTotalTime(), 2732);
        assertEquals(sheet.getAudioFilePath(),"/Users/jessematty/Desktop/splittest/[1995] Afterglow.flac" );








    }


}
