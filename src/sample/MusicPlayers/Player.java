package sample.MusicPlayers;

import org.bytedeco.javacv.FrameGrabber;
import org.jaudiotagger.audio.AudioFile;

import java.io.File;

public interface Player {


    abstract boolean play(File audioFile, int start, int playStart, int end, float volume, float pan);
    abstract void stop();
    abstract void close();
    abstract void resume();
    abstract void setVolume(float volume);
    abstract void setPan(float pan);


    abstract void pause();

    abstract void setPlayStart(int start);


    void seekPlay(int seconds) throws FrameGrabber.Exception;
}
