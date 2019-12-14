package sample;

import javafx.animation.AnimationTimer;
import sample.MusicPlayers.PlayerState;
import sample.Utilities.SystemInfo;

public interface PlayerWindow {
     void playNext();
     void repeatSong();
     AnimationTimer getSongTimer();
     SystemInfo getSystemInfo();
     void stopPlay();
     void increaseVolume();
     void decreaseVolume();
     void panLeft();
     void panRight();
     void panCenter();
     void pause();
     void play();
     void fastForward();
     void rewind();
     void mute();
     void unMute();
     boolean isMuted();
     PlayerState getPlayerState();
     void seekPlay(int seek);
     void reloadPlaylistTabs();







}
