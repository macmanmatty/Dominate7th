package sample.PlaylistPanes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.AudioProcessors.*;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.Library.AudioInformation;
import sample.Library.Playlist;
import sample.MainAudioWindow;
import sample.Windows.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class AudioInformationContextMenu extends ContextMenu {
    private MenuItem editMutipleTracks;
   private  MenuItem linkSongsToAlbumImages;
  private   MenuItem showLyrics;
   private  MenuItem saveAlbumArtToFile;
   private  MenuItem getTrackkInfoFromWeb;
   private MenuItem getFileInfo;
   private MenuItem splitTrackWithCue;
   private Menu addSongs;
   private MenuItem importSelectedSongs;
   private MainAudioWindow mainAudioWindow;
   private List<AudioInformation> songs= new ArrayList<>();
   private PlaylistPane pane;
   private String fileSeperator;
    public AudioInformationContextMenu(MainAudioWindow window, PlaylistPane pane) {
        this.mainAudioWindow=window;
        this.pane=pane;
        this.fileSeperator=mainAudioWindow.getSystemInfo().getFileSeperator();
        hideOnEscapeProperty().setValue(true);
    }
   public void makeMenu(){
     editMutipleTracks= new MenuItem("Edit  Track Tags");
       editMutipleTracks.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               new MultipleAudioInformationWindow(pane).displayInformation(songs);
           }
       });
       linkSongsToAlbumImages= new MenuItem("Link Songs To Album Images ");
       linkSongsToAlbumImages.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               new LinkSongsToAlbumImages(true).linkSongsToAlbumArt(songs);
           }
       });
       saveAlbumArtToFile= new MenuItem("Save Embedded Album Art To File ");
       saveAlbumArtToFile.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
              Stage stage= new Stage();
               DirectoryChooser chooser= new DirectoryChooser();
             File file=  chooser.showDialog(stage);
               List<AudioInformation> syncSongs= Collections.synchronizedList(songs);// make the list thread safe
            new SaveAlbumArt(fileSeperator,true).saveAlbumArtToFile(syncSongs, file.getAbsolutePath());
           }
       });
       getFileInfo= new MenuItem("Get Audio File Info ");
       getFileInfo.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
              new AudioFileInformationWindow().displayFileInfo((AudioInformation) songs.get(0));
           }
       });
       getTrackkInfoFromWeb = new MenuItem("Get Track Information From MusicBrainz ");
       getTrackkInfoFromWeb.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               new GetTrackInfoFromWebWindow(songs, mainAudioWindow,pane.getPlaylist()).displayWindow();
           }
       });


       importSelectedSongs = new MenuItem("Import Selected Songs");
       importSelectedSongs.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               new ImportCDWindow(mainAudioWindow, songs).displayWindow();
           }
       });


       splitTrackWithCue= new MenuItem("Split Track With a Cue Sheet ");
       splitTrackWithCue.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               Stage stage= new Stage();
               FileChooser chooser= new FileChooser();
               File file=chooser.showOpenDialog(stage);
               ExtractAudioInformation extractAudioInformation= new ExtractAudioInformation();
               List<AudioInformation> informationList= new ArrayList<>();// read cue sheet
               try {
                   informationList = extractAudioInformation.splitAudioFileWithCueSheet(songs.get(0), file);
               } catch (CueSheetExeception cueSheetExeception) {
                   cueSheetExeception.printStackTrace();
               }
                    pane.getPlaylist().getAllSongs().remove(songs.get(0))  ;
                    pane.getPlaylist().addSongs(informationList);
           }
       });
       showLyrics= new MenuItem("Show Lyrics ");
       showLyrics.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               new LyricsWindow().showLyrics(songs.get(0));
           }
       });
      addSongs= new Menu("Add Songs To PlayList ");
      List<Playlist> playlists=mainAudioWindow.getLibrary().getRegularPlaylists();
      int size=playlists.size();
      for(int count=0; count<size; count++){
          Playlist playList=playlists.get(count);
          MenuItem playListItem= new MenuItem(playList.getName());
            playListItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    playList.addSongs(pane.getSelectedSongs());
                }
            });
            addSongs.getItems().add(playListItem);
      }
       getItems().add(editMutipleTracks);
       getItems().add(addSongs);
       getItems().add(splitTrackWithCue);
       getItems().add(linkSongsToAlbumImages);
       getItems().add(saveAlbumArtToFile);
       getItems().add(getTrackkInfoFromWeb);
       getItems().add(getFileInfo);
   }
   public  void updateMenuItems(){
        getItems().clear();
        addSongs.getItems().clear();
       List<Playlist> playlists=mainAudioWindow.getLibrary().getRegularPlaylists();
       int size=playlists.size();
       for(int count=0; count<size; count++){
           Playlist playList=playlists.get(count);
           MenuItem playListItem= new MenuItem(playList.getName());
           playListItem.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   playList.addSongs(songs);
               }
           });
           addSongs.getItems().add(playListItem);
       }
       if(songs.size()>1){
           getItems().add(editMutipleTracks);
           getItems().add(addSongs);
           getItems().add(linkSongsToAlbumImages);
           getItems().add(saveAlbumArtToFile);
           getItems().add(getTrackkInfoFromWeb);
           if(pane instanceof  CDPlaylistPane){
               getItems().add(importSelectedSongs);
           }
       }
       else{
           getItems().add(editMutipleTracks);
           getItems().add(addSongs);
           getItems().add(splitTrackWithCue);
           getItems().add(linkSongsToAlbumImages);
           getItems().add(saveAlbumArtToFile);
           getItems().add(showLyrics);
           getItems().add(getTrackkInfoFromWeb);
           getItems().add(getFileInfo);
           if(pane instanceof  CDPlaylistPane){
               getItems().add(importSelectedSongs);
           }
       }
   }
    public void addSingleTrackMenuItems(){
        getItems().add(showLyrics);
        getItems().add(getFileInfo);
    }
    public void removeSingleTracKMenuItems(){
       getItems().remove(showLyrics);
        getItems().remove(getFileInfo);
    }
    public List<AudioInformation> getSongs() {
        return songs;
    }
    public void setSongs(List<AudioInformation> songs) {
        this.songs= new ArrayList<>();// observable list may be passed  and we don't want one  so make a new list instead
        this.songs.addAll(songs);
    }
}
