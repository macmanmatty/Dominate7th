import org.junit.Test;
import sample.Utilities.AudioFileUtilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class AudioFileUtilitiesTests {

    @Test

    public void fileExtensionTest(){
        AudioFileUtilities utilities= new AudioFileUtilities();
        String extension= utilities.getExtensionOfFile(new File("/Users/jessematty/Desktop/cc.mp3"));
        assertEquals(extension, "mp3");



    }


    @Test

    public void fileCompareTest(){


        File file= new File("/Users/jessematty/Desktop/cc.mp3");
        File file2= new File("/Users/jessematty/Desktop/cc1.mp3");
        AudioFileUtilities utilities= new AudioFileUtilities();
        boolean copy= utilities.isAudioCopy(file, file2);
        assertEquals(copy, true);



    }

    @Test

    public void fileCompareTest2(){


        File file= new File("/Users/jessematty/Desktop/cc.mp3");
        File file2= new File("/Users/jessematty/Desktop/cc2.mp3");
        AudioFileUtilities utilities= new AudioFileUtilities();
        boolean copy= utilities.isAudioCopy(file, file2);
        assertEquals(copy, false);



    }

    @Test
    public void  fileCopyMoveTest() throws IOException {

        File file= new File("/Users/jessematty/Desktop/iams.mp3");

                String targetPath="/Users/jessematty/Desktop/copy/iams.mp3";
                AudioFileUtilities audioFileUtilities= new AudioFileUtilities();
                File newFile=audioFileUtilities.copyThenMoveFile(file, targetPath);
                boolean exists=newFile.exists();



        assertEquals(exists, true);


    }

    @Test
    public void  fileCopyFileMoveTest() throws IOException {

        File file= new File("/Users/jessematty/Desktop/iams.mp3");
        File directory= new File("/Users/jessematty/Desktop/encode test/boo/hello");

        AudioFileUtilities audioFileUtilities= new AudioFileUtilities();
        File newFile=audioFileUtilities.copyThenMoveFile(file, directory);
        boolean exists=newFile.exists();



        assertEquals(exists, true);


    }




}
