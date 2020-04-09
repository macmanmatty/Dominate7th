package sample.MusicPlayers;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import sample.Windows.OptionPane;
import javax.sound.sampled.*;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
public class FFMpegPlayer implements Player {
    private  SourceDataLine line;
    private  AudioFormat format;
    private FFmpegFrameGrabber frameGrabber;
   private  final  MusicPlayer player;
   private volatile int playStartOffsetSeconds;// offset for playing for  playing of cue files
   private volatile int endSeconds;
    public FFMpegPlayer(MusicPlayer player) {
        this.player = player;
    }
    @Override
    public boolean play(File audioFile, int startSeconds, int playStartOffsetSeconds, int endSeconds, float volume, float pan) { // plays a song with a given  j audiotagger audio file
            frameGrabber = new FFmpegFrameGrabber(audioFile.getAbsolutePath());
            this.playStartOffsetSeconds =playStartOffsetSeconds;
            this.endSeconds = endSeconds;
        try {
            frameGrabber.start();
            format=new AudioFormat(frameGrabber.getSampleRate(), 16, frameGrabber.getAudioChannels(),true, true );
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line= (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            setPan(pan); // set line pan and volume
            setVolume(volume);
            line.start();
            double lengthInSeconds=frameGrabber.getLengthInTime()/1000000;
            double framesPerSecond=frameGrabber.getLengthInAudioFrames()/lengthInSeconds;
            System.out.println("FPS "+framesPerSecond);
            System.out.println("FPS " + framesPerSecond);
            System.out.println("start seconds " + startSeconds);
            System.out.println("play  start seconds " + playStartOffsetSeconds);
            System.out.println("end seconds " + endSeconds);


            if(endSeconds==0){
                endSeconds=(int)(lengthInSeconds);
            }
            double startFrame = ((startSeconds + playStartOffsetSeconds) * (framesPerSecond));// calculate start frame for ffmpeg.
            System.out.println("Frames " + frameGrabber.getLengthInAudioFrames() + " Start Frame " + startFrame);
            frameGrabber.setAudioFrameNumber((int) startFrame);// set the start frame.
           
            Thread thread=Thread.currentThread();
            int frames = (int) ((playStartOffsetSeconds)*framesPerSecond);// total number of frames being played
            int endFrame= (int) (endSeconds*framesPerSecond);// last frame number
            System.out.println("End Frame " + endFrame);

            player.startTime();// start the displayed time counter
            while (!thread.interrupted()) {
                Frame frame = frameGrabber.grabSamples();
                if (frame == null || player.getTime()>endSeconds ) {// frame is null or the end frame is reached terminate playing
                    System.out.println("Stopped " + frame);
                    break;
                }
                else if (frame.samples != null) {
                    ShortBuffer channelSamplesFloatBuffer = (ShortBuffer) frame.samples[0];
                    channelSamplesFloatBuffer.rewind();
                    ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesFloatBuffer.capacity() * 2);
                    for (int i = 0; i < channelSamplesFloatBuffer.capacity(); i++) {
                        short val = (short)((double) channelSamplesFloatBuffer.get(i) );
                        outBuffer.putShort(val);
                    }
                    if(line!=null) {
                        line.write(outBuffer.array(), 0, outBuffer.capacity());
                    }

                    outBuffer.clear();
                    ;
                }
                frames++;
            }
            frameGrabber.stop();
            frameGrabber.release();
            if(line!=null) {
                line.stop();
                line.close();
                line.drain();
            }

        } catch (FrameGrabber.Exception e) {
            new OptionPane().showOptionPane("The File: " +audioFile.getAbsolutePath()+"  Cannot Be Played"+e.getStackTrace(), "OK");
        } catch (LineUnavailableException e) {
            new OptionPane().showOptionPane("System  Audio Line Error"+e.getStackTrace(), "OK");
        }
        player.stopTime();
        return true;
    }
    @Override
    public void stop() {
        if(line!=null) {
            line.close();
            line.drain();
            line.stop();
        }
    }
    @Override
    public void close() {
        if(line!=null) {
            line.close();
            line.drain();
            line.stop();
        }
        line=null;
    }
    @Override
    public void resume() {
    }
    @Override
    public void setVolume(float volume) { // set the volume in Db's  for the source dataLine;
        if(line!=null && line.isOpen()) { // cant set volume until line is open
            FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN); // get the master gain for audio source
            float newGain = Math.min(Math.max(volume, control.getMinimum()), control.getMaximum()); // creates new gain level
            control.setValue(newGain); // set gain
        }
    }
    @Override
    public void setPan(float pan) {// sets the speaker  pan left or right -1=left 1=right 0=center for the java source dataline
        if(line!=null && line.isOpen()) { // cant set the pan until line is open
            FloatControl control = (FloatControl) line.getControl(FloatControl.Type.PAN); // get pan float control
            control.setValue(pan); // set pan
        }
    }
    @Override
    public void pause() {
    }
    @Override
    public void setPlayStart(int start) {
    }

    @Override
    public void seekPlay(int startSeconds) throws FrameGrabber.Exception {
        double lengthInSeconds=frameGrabber.getLengthInTime()/1000000;
        double framesPerSecond=frameGrabber.getLengthInAudioFrames()/lengthInSeconds;
        System.out.println("FPS "+framesPerSecond);
        System.out.println("FPS " + framesPerSecond);
        System.out.println("start seconds " + startSeconds);
        if(endSeconds==0){
            endSeconds=(int)(lengthInSeconds);
        }
        double startFrame = ((startSeconds + playStartOffsetSeconds) * (framesPerSecond));// calculate start frame for ffmpeg.
        System.out.println("Frames " + frameGrabber.getLengthInAudioFrames() + " Start Frame " + startFrame);
            frameGrabber.setAudioFrameNumber((int) startFrame);// set the start frame.
        }


}
