package sample.PlaylistPanes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jaudiotagger.tag.FieldKey;
import sample.Library.Playlist;
import sample.Library.PlaylistPaneKind;
import sample.MainAudioWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiskPlaylistPane extends TablePlaylistPane {


    public DiskPlaylistPane(Playlist playlist, MainAudioWindow window) {
        super(playlist, window);
    }
    public Node makePane(){
        playlistWindow= new VBox();

        playList.setPane(this);
        playList.setPlayListPaneKind(PlaylistPaneKind.TABLE);
        shuffleMode=playList.getShuffleMode();
        searchBar= new TextField();
        table= new MusicTable(this, window);
        table.setPlaylist(playList);
        albumLabel= new Label();


        searchChoices= new ChoiceBox<>();
        ArrayList<String> searchOperators= new ArrayList<>();
        searchOperators.add("And");
        searchOperators.add("Or");
        searchOperators.add("Exact Phrase");
        searchChoices.setItems(FXCollections.observableList(searchOperators));
        searchChoices.getSelectionModel().select(2);

        searchBar.setText("Search Playlist...");
        searchBar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text=searchBar.getText();
                if(text==null|| text.isEmpty()){
                    table.displayAllSongs();
                    updateTrackLabels();
                }
                else {
                    if (searchChoices.getSelectionModel().getSelectedIndex() == 0) {
                        table.search(text, true);
                    }
                    else if(searchChoices.getSelectionModel().getSelectedIndex()==1){
                        table.search(text, false);
                    }
                    else {
                        table.search(text);
                    }
                    updateTrackLabels();
                }
            }
        });
        editTracks=new
                CheckBox("Edit Track Information");
        editTracks.selectedProperty().
                addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed (ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue){
                        setEditable(editTracks.isSelected());
                    }
                });
        lockSort=new
                CheckBox("Lock Track Sorting? ");
        lockSort.selectedProperty().
                addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed (ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue){
                        ObservableList<TableColumn>  columns= table.getColumns();
                        int size=columns.size();
                        for(int count=0;  count<size; count++){
                            columns.get(count).setSortable(lockSort.isSelected());
                        }
                    }
                });
        totalTrackLabel= new Label();
        totalTrackDurationLabel=new Label();
        VBox header= new VBox();
        HBox header1= new HBox();
        table.makeTable();
        table.setSortOrder(playList.getSortOrder());
        VBox artist= new VBox();
        Label artistLanel= new Label("Select an Artist to Show");
        artist.getChildren().add(artistLanel);
        VBox album= new VBox();
        Label albumLanel= new Label("Select an Album to Show");
        album.getChildren().add(albumLanel);
        header1.setSpacing(7);
        header1.getChildren().add(albumLabel);
        header1.getChildren().add(lockSort);
        header1.getChildren().add(editTracks);
        header.getChildren().add(header1);
        HBox searchBarbox= new HBox(searchChoices, searchBar);
        header.getChildren().add(searchBarbox);
        playlistWindow.getChildren().add(header);
        playlistWindow.getChildren().add(table);
        HBox footer= new HBox();
        footer.getChildren().add(totalTrackLabel);
        footer.getChildren().add(totalTrackDurationLabel);
        updateTrackLabels();
        playlistWindow.getChildren().add(footer);
        searchBar.setMinWidth(1000);
        searchBar.prefWidthProperty().bind(window.getWindow().prefWidthProperty());
        return playlistWindow;
    }

    @Override
    public void setEditTracks(boolean edit) {
        this.editTracks.setSelected(true);
    }
    @Override
    public boolean isEditTracks() {
        return editTracks.isSelected();
    }
    @Override
    public boolean isLockTrackSorting() {
        return lockSort.isSelected();
    }
    @Override
    public void setLockTrackSorting(boolean sort) {
        lockSort.setSelected(sort);
    }
    @Override
    public void addShuffleKey(FieldKey key) {
        shuffleKeys.add(key);
        playList.getShuffleKeys().add(key);
    }
    @Override
    public void removeShuffleKey(FieldKey key) {
        shuffleKeys.remove(key);
        playList.getShuffleKeys().add(key);
    }
    @Override
    public void setShuffle(boolean shuffle) {
        this.shuffle=shuffle;
    }
    public boolean isShuffle() {
        return shuffle;
    }
    @Override
    public List<FieldKey> getShuffleKeys() {
        return shuffleKeys;
    }
    public  void searchAlbum(String album) {
        if(album.equalsIgnoreCase("All Albums")==false) {
            System.out.println("Searching Album " + album);
            table.search(FieldKey.ALBUM, album);
            updateAlbumBoxeWithSelection();
            updateTrackLabels();
        }
        else{
            updateArtistBoxesWithSelection();
        }
    }
    public void searchArtist(String artist){
        if(artist.equalsIgnoreCase("All Artists")==false) {
            table.search(FieldKey.ARTIST, artist);
            updateArtistBoxesWithSelection();
            updateTrackLabels();
        }
        else{
            table.displayAllSongs();
        }
    }
    public void setEditable(boolean edit){
        table.setEditable(edit);
    }
    public boolean isSortLocked() {
        return sortLocked;
    }
    public void setSortLocked(boolean sortLocked) {
        this.sortLocked = sortLocked;
    }
    public void updatePane(){
        System.out.println("Updating Pane...");
        //updateBoxes();
        updateTrackLabels();
        table.refresh();
        System.out.println("Done");
    }

    private void updateArtistBoxesWithSelection() {
        List<String> albums = table.getAlbums(artist);
        System.out.println("Searching artist: "+artist);
        ObservableList<String> albumsO= FXCollections.observableArrayList(albums);
        Collections.sort(albumsO);
        albumsO.add(0, "All Albums");
        if(artist==null|| artist.isEmpty()) {
            albumLabel.setText(" # of Artists: " + 1 + "  # of Albums: " + albums.size());
        }
        else{
            albumLabel.setText("Artist: " + artist + "  # of Albums: " + albums.size());
        }
    }

}
