package sample.MusicPlayers;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FrameGrabber;
import sample.PlayerWindow;
import sample.Utilities.AudioFileUtilities;

import java.io.File;
import java.io.IOException;

public   class MusicPlayer {  // class for playing music files
    private PlayerState playerState =PlayerState.NOTSTARTED; // the stat of the player IE playing paused stopped ect.
    private  int start; // start time in trackTimerSeconds  for the sing
    private  int end;  // end time in trackTimerSeconds
    private Player player; // the  actual class the plays  decodes and plays the music files
    private File file; // the j tagger  audiofile class
    private PlayerWindow window;// the main window
    private int trackLength; // length of teh song being played
    private Thread musicPlayingThread; // the thread the player runs on
    private  volatile  float volume; // audio volume
    private  volatile float pan; // audio pan
    private PlayMode playMode=PlayMode.REPEAT_PLAYLIST; // play mode repeat song repeat playlist stop on finsihed.
    private String kind; //the kind of audio file
    private boolean stopped; // is the player stopped
    private int startOffset;// the time in seconds to start playing the song
    private boolean fileChanged;
    private boolean onCD;//whether or not the track is ona cd
    private String fileSeperator;
    private File cachedFile;

    public MusicPlayer(PlayerWindow window) {
        this.window=window;
        this.fileSeperator=window.getSystemInfo().getFileSeperator();

    }
    public boolean pause() { // pauses the player by suspending the thread
            if (playerState == PlayerState.PLAYING) {
                playerState = PlayerState.PAUSED;
                if(player!=null) {
                    player.pause();
                }
                System.out.println("pause");
                musicPlayingThread.suspend();
            }
            return playerState == PlayerState.PAUSED;

    }

    public void seekPlay(int seconds){

        try {
            player.seekPlay(seconds);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
            player.stop();
            player.close();
            close();
        }
    }
    public void stop() {
            playerState = PlayerState.FINISHED;
            stopped=true;
            start=0;
            if(player!=null) {
                player.stop();
                end=trackLength;
            }

    }
    public  void setVolume(float volume){ // set the volume for the player
        this.volume=volume;
            if(player!=null) {
                player.setVolume(volume);
            }

    }
    public void mute(){ // mutes the player
            if(player!=null) {
                player.setVolume(-80);
            }

    }
    public void unMute(){ // unmutes the player
            if(player!=null) {
                player.setVolume(volume);
            }

    }


    public void setPan(float pan){
        this.pan=pan;
            if(player!=null) {
                player.setPan(pan);
            }

    }
    public void play(File file) {
        this.file = file;
        if(cachedFile!=null){
            cachedFile.delete();
        }
        play();
    }
    public void play(){
        stopped=false;
            if(playerState==PlayerState.NOTSTARTED) {
                player = new FFMpegPlayer(this);
            }
            if(playerState ==PlayerState.FINISHED || playerState ==PlayerState.NOTSTARTED) {
                final Runnable runnable = new Runnable() {
                    public void run() {
                        playInternal();
                    }
                };
                if(musicPlayingThread !=null){
                    musicPlayingThread.interrupt();
                }
                musicPlayingThread = new Thread(runnable);
                musicPlayingThread.setPriority(Thread.MAX_PRIORITY);
                playerState = PlayerState.PLAYING;
                musicPlayingThread.start();
            }
            if(playerState ==PlayerState.PAUSED) {
                resume();
            }

    }
    /**
     * Resumes playback. Returns true if the new state is PLAYING.
     */
    public boolean resume() {
        stopped=false;
            if (playerState == PlayerState.PAUSED) {
                playerState = PlayerState.PLAYING;
                musicPlayingThread.resume();
                if(player!=null) {
                    player.resume();
                }
            }
            return playerState == PlayerState.PLAYING;

    }
    public void playInternal() {
        onCD=new AudioFileUtilities().isOnCD(file);
        if(onCD==true && cachedFile==null){
            cacheFile(file);
        }


        if (!player.play(file, start , startOffset,  end,volume, pan)) {// play song
            close();
        }
        if(stopped==false) {
            switch (playMode) {
                case STOP_WHEN_FINISHED:{
                    window.stopPlay();
                    break;}
                case REPEAT_SONG: {
                    window.repeatSong();
                    break;
                }
                case REPEAT_PLAYLIST: {
                    window.playNext();
                    break;
                }
            }
        }
    }

    private void cacheFile(File fileToCache) {




                String path = fileSeperator + "Users" + fileSeperator + "AudioApp" + fileSeperator + file.getName();
                 cachedFile = new File(path);
                try {
                    FileUtils.copyFile(fileToCache, cachedFile);
                    file=cachedFile;
                } catch (IOException e) {

                }




    }

    public void startTime(){
        window.getSongTimer().start();
    }
    public void stopTime(){
        window.getSongTimer().stop();
    }
    /**
     * Closes the player, regardless of current state.
     */
    public void close() {
            playerState = PlayerState.FINISHED;
            try {
                player.close();
                musicPlayingThread.interrupt();
            } catch (final Exception e) {
                // ignore, we are terminating anyway
            }

    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public PlayerState getPlayerState() {
        return playerState;
    }
    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }
    public PlayMode getPlayMode() {
        return playMode;
    }
    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }
    public int getStartOffset() {
        return startOffset;
    }
    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getTime(){
        return  window.getTime();
    }


}
