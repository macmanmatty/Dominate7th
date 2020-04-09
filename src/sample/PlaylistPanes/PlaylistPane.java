package sample.PlaylistPanes;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.jaudiotagger.tag.FieldKey;
import sample.Library.AudioInformation;
import sample.Library.Playlist;
import sample.MusicPlayers.ShuffleMode;

import java.util.List;

public interface PlaylistPane {



    abstract void updatePane();
    abstract Node getPlaylistWindow();
    abstract AudioInformation getNextSong();
    void  addSongs(List<AudioInformation> songs);
    void setSongs(List<AudioInformation> songs);

    void  removeSongs(List<AudioInformation> songs);


    abstract List<FieldKey> getShownKeys();
    abstract void changeColumn(FieldKey key);
    abstract Playlist getPlaylist();
    Node makePane();
     void setEditTracks(boolean edit);
     boolean isEditTracks();
     boolean isLockTrackSorting();
     void setLockTrackSorting(boolean sort);
     void addShuffleKey(FieldKey key);
     void removeShuffleKey(FieldKey key);
     void setShuffle(boolean shuffle);
     boolean isShuffle();
     List<FieldKey> getShuffleKeys();
     List<AudioInformation> getSelectedSongs();
















}
