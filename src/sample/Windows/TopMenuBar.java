package sample.Windows;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.MainAudioWindow;
import sample.Utilities.FieldKeyOperations;
import sample.AudioProcessors.GetAudioFiles;
import sample.Library.*;
import sample.PlaylistPanes.PlaylistPane;
import sample.PlaylistPanes.TablePlaylistPane;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TopMenuBar {
   private  MenuBar menuBar;
    private MainAudioWindow window;
    private Map<String, MenuItem> menuItems= new HashMap<>();
    private List<CheckMenuItem> fieldKeysToDisplay= new ArrayList<>();
    private List<CheckMenuItem> shuffleKeysToShow= new ArrayList<>();
    private ToggleGroup shuffleModeGroup= new ToggleGroup();
    private ToggleGroup playlistPaneDisplayGroup = new ToggleGroup();
    private BidiMap <Toggle, FieldKey> shuffleModes= new DualHashBidiMap<>();
    private RadioMenuItem albumPlaylistPane;
    private RadioMenuItem tablePlaylistPane;
    public TopMenuBar(MainAudioWindow window) {
        menuBar= new MenuBar();
        this.window=window;
        Menu dominateSeventh= new Menu("Dominant Seventh");
        MenuItem aboutDominateSeventh= new MenuItem("About Dominate Seventh");
        aboutDominateSeventh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
       dominateSeventh.getItems().add(aboutDominateSeventh);
       menuItems.put(" About Dominant Seventh", aboutDominateSeventh);
        MenuItem exit= new MenuItem("Exit " );
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.closeApp();
            }
        });
        dominateSeventh.getItems().add(exit);
        menuItems.put(" Exit", aboutDominateSeventh);
        Menu libraryMenu= new Menu("Music Library");
        MenuItem openFile= new MenuItem("Open File");
        openFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                FileChooser directoryChooser = new FileChooser();
                File file = directoryChooser.showOpenDialog(stage);
               AudioInformation information=new ExtractAudioInformation().extractAudioInformationFromFile(file);
               window.getCurrentPlaylistPane().getPlaylist().addSong(information);
               window.setCurrentSong(information);
               window.startPlay();
            }
        });
        menuItems.put("Open File", openFile);
        MenuItem sortMusicFolder= new MenuItem("Sort Music folder");
        sortMusicFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage= new Stage();
                new AudioFileSorterWindow(stage, window).showWindow();
            }
        });
        menuItems.put("Sort Music Folder", sortMusicFolder);
        MenuItem preferences= new MenuItem("Library Settings");
        preferences.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LibrarySettingsWindow( window).showWindow();
            }
        });
        menuItems.put("Save Library", preferences);

        MenuItem saveLibrary= new MenuItem("Save Library");
        saveLibrary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.saveLibrary();
            }
        });
        menuItems.put("Library Settings", preferences);
        MenuItem importCd= new MenuItem("Import CD");
        importCd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new ImportCDWindow(window, window.getCurrentPlaylistPane().getPlaylist().getAllSongs()).displayWindow();
            }
        });


        menuItems.put("Import CD", importCd);
        MenuItem addSongsToLibrary= new MenuItem("Add Songs To Library");
        addSongsToLibrary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(stage);
                MusicLibrary musicLibrary=window.getLibrary();
                Settings settings=musicLibrary.getSettings();
                GetAudioFiles getAudioFiles;
                getAudioFiles = new GetAudioFiles( window.getSystemInfo(),window.getLibrary(), window.getCurrentPlaylistPane().getPlaylist());
                getAudioFiles.setAddSongsToCurrentPlayList(false);
                getAudioFiles.setAddSongsToLibrary(true);
                getAudioFiles.setSortByBitRate(settings.isSortLibraryByBitRate());
                getAudioFiles.setMoveSongsToLibrary(settings.isMoveFilesToLibraryFolder());
                getAudioFiles.setCopyFilesThenMove(settings.isCopyFilesToLibraryFolder());
                getAudioFiles.setSortByFileType(settings.isSortLibraryByFileType());
                getAudioFiles.setGetMissingTags(settings.isGetMissingTagsOnAddingSongsToLibrary());
                getAudioFiles.loadFiles(file.getPath(), true);
            }
        });

        menuItems.put("Add Songs To Library", addSongsToLibrary);

        MenuItem encodeAudioWithFFmpeg= new MenuItem("Encode Audio With FFmpeg");
        encodeAudioWithFFmpeg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new FFMpegEnocderWindow(window).displayWindow();
            }
        });
        menuItems.put("Encode Audio With FFmpeg", encodeAudioWithFFmpeg);



        MenuItem refresh= new MenuItem("Reload Playlist Tabs");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.reloadPlaylistTabs();
            }
        });
        menuItems.put("Reload Playlist Tabs", refresh);
        MenuItem linkSongsToAlbums= new MenuItem("Link Song Files To Album Images");
        linkSongsToAlbums.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LinkSongsToAlbumImagesWindow(window).displayWindow();
            }
        });
        menuItems.put("Link Song Files To Album Images", linkSongsToAlbums);

        libraryMenu.getItems().add(openFile);
        libraryMenu.getItems().add(sortMusicFolder);
        libraryMenu.getItems().add(importCd);
        libraryMenu.getItems().add(saveLibrary);
        libraryMenu.getItems().add(addSongsToLibrary);
        libraryMenu.getItems().add(encodeAudioWithFFmpeg);
        libraryMenu.getItems().add(preferences);
        libraryMenu.getItems().add(refresh);
        libraryMenu.getItems().add(linkSongsToAlbums);
        Menu playlistMenu= new Menu("Playlist");
        MenuItem playlistSettings= new MenuItem("Playlist Settings");
        playlistSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new PlaylistSettingsWindow(window).showWindow();
            }
        });
        playlistSettings.setDisable(true);
        menuItems.put("Playlist Settings", playlistSettings);



        MenuItem editPlayList2= new MenuItem("Edit PlayList");
        editPlayList2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new PlaylistCreatorWindow(window, window.getCurrentPlaylistPane().getPlaylist()).displayWindow();
            }
        });
        menuItems.put("Edit Playlist", editPlayList2);
        MenuItem addSongsToPlaylist= new MenuItem("Add Songs To Playlist");
        addSongsToPlaylist.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(stage);
                MusicLibrary musicLibrary=window.getLibrary();
                Settings settings=musicLibrary.getSettings();
                PlaylistSettings playListSettings=window.getCurrentPlaylistPane().getPlaylist().getPlaylistSettings();
                GetAudioFiles getAudioFiles;


                getAudioFiles = new GetAudioFiles( window.getSystemInfo(),window.getLibrary() , window.getCurrentPlaylistPane().getPlaylist());
                getAudioFiles.setAddSongsToCurrentPlayList(true);
                getAudioFiles.setAddSongsToLibrary(playListSettings.isMoveSongsToLibraryOnAdding());
                getAudioFiles.setSortByBitRate(settings.isSortLibraryByBitRate());
                getAudioFiles.setMoveSongsToLibrary(settings.isMoveFilesToLibraryFolder());
                getAudioFiles.setCopyFilesThenMove(settings.isCopyFilesToLibraryFolder());
                getAudioFiles.setSortByFileType(settings.isSortLibraryByFileType());
                getAudioFiles.setGetMissingTags(settings.isGetMissingTagsOnAddingSongsToLibrary());
                getAudioFiles.loadFiles(file.getPath(), true);            }
        });
        menuItems.put("Add Songs To Playlist", addSongsToPlaylist);

        MenuItem newPlaylist= new MenuItem("New Playlist");
        newPlaylist.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new PlaylistCreatorWindow(window).displayWindow();
            }
        });
        menuItems.put("New Playlist", newPlaylist);
        Menu shuffleMode= new Menu("Shuffle");
        CheckMenuItem none= new CheckMenuItem("No Shuffling Of Playlist");
        none.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PlaylistPane pane=window.getCurrentPlaylistPane();
                if(pane!=null) {
                   pane.setShuffle(false);
                   int size=shuffleKeysToShow.size();
                    for(int count=1; count<size; count++){
                        shuffleKeysToShow.get(count).setSelected(false);
                    }
                }
            }
        });
        shuffleMode.getItems().add(none);
        shuffleKeysToShow.add(none);
        List<FieldKey> keys=window.getSettings().usedFieldKeys;
            int size=keys.size();
            for(int count=0; count<size; count++) {
                CheckMenuItem item = new CheckMenuItem(FieldKeyOperations.getFieldKeyDisplayName(keys.get(count)));
                shuffleKeysToShow.add(item);
                int finalCount1 = count;
                item.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        PlaylistPane pane=window.getCurrentPlaylistPane();
                        pane.setShuffle(true);
                        shuffleKeysToShow.get(0).setSelected(false);
                        if(item.isSelected()==true) {
                            pane.addShuffleKey(keys.get(finalCount1));
                        }
                        else{
                            pane.removeShuffleKey(keys.get(finalCount1));
                        }
                        if(pane.getShuffleKeys().size()==0){
                            pane.setShuffle(false);
                            shuffleKeysToShow.get(0).setSelected(true);

                        }
                    }
                });
                shuffleMode.getItems().add(item);
        }
        menuItems.put("Shuffle Keys", shuffleMode);
        Menu displayKeys= new Menu("Display");
       size=keys.size();
        for(int count=0; count<size; count++) {
            CheckMenuItem item = new CheckMenuItem(FieldKeyOperations.getFieldKeyDisplayName(keys.get(count)));
            fieldKeysToDisplay.add(item);
            int finalCount = count;
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    window.getCurrentPlaylistPane().changeColumn(keys.get(finalCount));
                }
            });
            displayKeys.getItems().add(item);
        }
        menuItems.put("Display", displayKeys);
        Menu playlistPaneStyle= new Menu("Playlist Window Style");
         tablePlaylistPane= new RadioMenuItem("Table Window");
        tablePlaylistPane.setToggleGroup(playlistPaneDisplayGroup);
        tablePlaylistPane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PlaylistPane pane= new TablePlaylistPane(window.getCurrentPlaylistPane().getPlaylist(),window);
                pane.makePane();
                window.updateTab(window.getCurrentPlaylistPane().getPlaylist());
            }
        });
     albumPlaylistPane= new RadioMenuItem("Album Playlist Pane");
        albumPlaylistPane.setToggleGroup(playlistPaneDisplayGroup);
        albumPlaylistPane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        albumPlaylistPane.setDisable(true);
        playlistPaneStyle.getItems().add(tablePlaylistPane);
        playlistPaneStyle.getItems().add(albumPlaylistPane);
        menuItems.put("Playlist Pane Style", playlistPaneStyle);

        playlistMenu.getItems().add(editPlayList2);
        playlistMenu.getItems().add(newPlaylist);
        playlistMenu.getItems().add(addSongsToPlaylist);
        playlistMenu.getItems().add(shuffleMode);
        playlistMenu.getItems().add(playlistPaneStyle);
        playlistMenu.getItems().add(displayKeys);
        menuBar.getMenus().add(dominateSeventh);
        menuBar.getMenus().add(libraryMenu);
        menuBar.getMenus().add(playlistMenu);
        if(window.getCurrentPlaylistPane()!=null){
            updateMenusToPlaylistPane(window.getCurrentPlaylistPane());
        }
    }
    public MenuBar getMenuBar() {
        return menuBar;
    }
    public MenuItem getMenuItem(String name){
        return menuItems.get(name);
    }
    public void updateMenusToPlaylistPane(PlaylistPane pane){
        if(pane!=null) {
             Playlist playlist=pane.getPlaylist();
             if(playlist instanceof  SmartPlaylist){
                 setSmartPlaylist();
             }
             else if(playlist.isCDPlaylist()){

                 setCDPlaylist();

             }
             else{
                 setRegularPlaylist();
             }
            List<FieldKey> shownKeys = pane.getShownKeys();
            List<FieldKey> keys=window.getSettings().usedFieldKeys;
            int size=keys.size();
            for(int count=0; count<size; count++){
                fieldKeysToDisplay.get(count).setSelected(false);
            }
            for(int count=0; count<size; count++) {
                int size2 = shownKeys.size();
                for (int count2 = 0; count2 < size2; count2++) {
                    if (keys.get(count) == shownKeys.get(count2)) {
                        fieldKeysToDisplay.get(count).setSelected(true);
                    }
                }
            }
            setSelectedShuffleMode(pane);
            setSelectedShuffleMode(pane);
            setSelectedPlayPaneDisplay(pane.getPlaylist());
        }
    }
    private  void setSelectedPlayPaneDisplay(Playlist playlist){
      PlaylistPaneKind paneKind= playlist.getPlayListPaneKind();
      switch(paneKind){
          case TABLE:
              tablePlaylistPane.setSelected(true);
              break;
          case ALBUM:
              albumPlaylistPane.setSelected(true);
              break;
      }
    }
    private void setSelectedShuffleMode(PlaylistPane pane) {
        if (pane != null) {
            pane = window.getCurrentPlaylistPane();

            List<FieldKey> shuffleKeys = pane.getShuffleKeys();
            List<FieldKey> keys = window.getSettings().usedFieldKeys;
            int size=keys.size();
            // one more menu option so size plus one
            for(int count=1; count<size+1; count++){
                shuffleKeysToShow.get(count).setSelected(false);
            }
            if(!(pane.isShuffle())){ // if pane is not in shuffle mode
                shuffleKeysToShow.get(0).setSelected(true);
                return;
            }
            // first menu item is no shuffle so ignore it.
            for (int count = 1; count < size+1; count++) {
                int size2 = shuffleKeys.size();
                for (int count2 = 0; count2 < size2; count2++) {
                    if (keys.get(count) == shuffleKeys.get(count2)) { // compare keys
                        shuffleKeysToShow.get(count).setSelected(true);
                    }
                }
            }
        }
    }
    private void setSmartPlaylist(){
           getMenuItem("Add Songs To Playlist").setDisable(true);
        getMenuItem("Import CD").setDisable(true);
    }
        private void setRegularPlaylist(){
            getMenuItem("Add Songs To Playlist").setDisable(false);
            getMenuItem("Import CD").setDisable(true);

        }

    private void setCDPlaylist(){
        getMenuItem("Add Songs To Playlist").setDisable(true);
        getMenuItem("Edit Playlist").setDisable(true);
        getMenuItem("Import CD").setDisable(false);

    }
}
