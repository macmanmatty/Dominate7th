package sample.MusicPlayers;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import org.jaudiotagger.audio.AudioFile;
import sample.Library.AudioInformation;

import javax.sound.sampled.*;
import java.io.IOException;

public class GetAudioWaveForm {
    private Pane waveformPane = new Pane();
    private Line trackBar = new Line();
    AudioFormat format;


    public byte[] getData(AudioFile audioFile) {

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile.getFile());
            AudioFormat audioFormat = audioStream.getFormat();


            long numBytes = audioStream.getFrameLength();

            int[] data = new int[(int) numBytes];
            int k = 0;
            int count = 0;
            while (audioStream.read() != -1) {
                data[count] = audioStream.read();
                count++;

            }



        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];


    }


        public  void makeWaveformPane(byte [] data, int width, int height, Color color){

        long length=data.length;
        int sections=(int)(length/width);

        int [] lines= new int [sections];
        for(int count=0; count<sections; count++){
            int start=count*width;
            int end=(count+1)*width;
           int average=(int)((data[start]+data[end])/2);
           int startLine=(int)((width-average)/2);





            Line line= new Line( count, startLine, count, average);



        }



    }

    public Pane getWaveformPane() {
        return waveformPane;
    }
}
