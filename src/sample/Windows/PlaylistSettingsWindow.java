package sample.Windows;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.MainAudioWindow;
import sample.Library.Playlist;
import sample.Library.PlaylistSettings;
public class PlaylistSettingsWindow {
    Stage stage;
    MainAudioWindow window;
    CheckBox copyFilesToLibraryFolder;
    CheckBox addSongsTolibrary;
    Playlist playList;


    public PlaylistSettingsWindow(MainAudioWindow window) {
        this.stage = new Stage();
        this.window=window;
        playList=window.getCurrentPlaylistPane().getPlaylist();
    }
    public void showWindow() {
        VBox preferencePane= new VBox();
        PlaylistSettings settings= playList.getPlaylistSettings();

        HBox libraryBox= new HBox();

        copyFilesToLibraryFolder= new CheckBox("Copy Files to Library Folder on Adding Them");
        copyFilesToLibraryFolder.setSelected(settings.isMoveSongsToLibraryOnAdding());
        copyFilesToLibraryFolder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setMoveSongsToLibraryOnAdding(copyFilesToLibraryFolder.isSelected());
                settings.setAddSongsToLibraryOnAdding(copyFilesToLibraryFolder.isSelected());
            }
        });
        addSongsTolibrary= new CheckBox("Add Songs Library On Adding Them");
        addSongsTolibrary.setSelected(settings.isAddSongsToLibraryOnAdding());
        addSongsTolibrary.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setAddSongsToLibraryOnAdding(addSongsTolibrary.isSelected());
            }
        });
        preferencePane.getChildren().add(copyFilesToLibraryFolder);

        stage.setScene(new Scene(preferencePane));
        stage.show();

    }
}
