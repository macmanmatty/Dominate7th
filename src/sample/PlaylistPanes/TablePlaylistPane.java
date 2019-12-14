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
import sample.*;
import sample.Library.AudioInformation;
import sample.Library.Playlist;
import sample.Library.PlaylistPaneKind;
import sample.MusicPlayers.ShuffleMode;
import sample.SearchAudio.Shuffle;
import sample.SearchAudio.Sorter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TablePlaylistPane implements PlaylistPane {
    protected  CheckBox lockSort;
    protected   CheckBox editTracks;
    protected boolean sortLocked;
    protected VBox playlistWindow;
    protected Label totalTrackLabel;
    protected Label totalTrackDurationLabel;
    protected Label albumLabel= new Label();
    protected Playlist playList;
    protected MainAudioWindow window;
    protected TextField searchBar;
   protected MusicTable table ;
    protected List<AudioInformation> allTracks;
    protected String artist="All Artists";
    protected String album="All Albums";
    protected AudioInformation previousSong;

    protected ShuffleMode shuffleMode;
    protected String previousPlayedArtist;
    protected String previousPlayedAlbum;
    protected ChoiceBox<String> searchChoices= new ChoiceBox<>();
    protected List<FieldKey> shuffleKeys=new ArrayList<>();
    protected boolean shuffle;
    protected Shuffle shuffleObject= new Shuffle();
    protected ObservableList<String> artists= FXCollections.observableList(new ArrayList<>());
    protected ObservableList<String> albums=FXCollections.observableList(new ArrayList<>());

    public TablePlaylistPane(Playlist playlist, MainAudioWindow window) {
        this.window = window;
        this.playList=playlist;
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
        ArrayList <String> searchOperators= new ArrayList<>();
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
    editTracks=new CheckBox("Edit Track Information");
        editTracks.selectedProperty().
    addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed (ObservableValue < ? extends Boolean > observable, Boolean oldValue, Boolean newValue){
            setEditable(editTracks.isSelected());
        }
    });
    lockSort=new CheckBox("Lock Track Sorting? ");
       lockSort.selectedProperty().
    addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed (ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue){
          ObservableList<TableColumn>  columns= table.getColumns();
          int size=columns.size();
          for(int count=0;  count<size; count++){
              columns.get(count).setSortable(!lockSort.isSelected());
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
        playList.getShuffleKeys().remove(key);
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


        updateAristsAndAlbums();
        updateTrackLabels();
        table.refresh();
        System.out.println("Done");
    }

    private void updateAristsAndAlbums(){

        table.updateArtistsAndAlbums();


        if(artist==null|| artist.isEmpty() || artist.equalsIgnoreCase("All")) {
            albumLabel.setText(" # of Artists: " + artists.size() + "  # of Albums: " + albums.size());
        }
        else{
            albumLabel.setText("Artist: " + artist + "  # of Albums: " + albums.size());
        }
    }




    private void updateArtistBoxesWithSelection() {
        List<String> albums = table.getAlbums(artist);
        System.out.println("Searching artist: "+artist);
        ObservableList<String> albumsO=FXCollections.observableArrayList(albums);
        Collections.sort(albumsO);
        albumsO.add(0, "All Albums");
        if(artist==null|| artist.isEmpty()) {
            albumLabel.setText(" # of Artists: " + 1 + "  # of Albums: " + albums.size());
        }
        else{
            albumLabel.setText("Artist: " + artist + "  # of Albums: " + albums.size());
        }
    }
    protected void updateAlbumBoxeWithSelection() {
            albumLabel.setText("Artist: " + artist + "  # of Albums: " + 1);
    }
    public void setFieldsToShow(ArrayList<FieldKey> keys){
        table.setFieldKeysToShow(keys);
        table.makeTable();
    }
    @Override
    public void changeColumn(FieldKey key){
        table.changeColumn(key);
        window.saveLibrary();
    }
    public List<AudioInformation> getAllTracks() {
        return playList.getAllSongs();
    }
    public VBox getPlaylistWindow() {
        return playlistWindow;
    }
    @Override
   public AudioInformation getNextSong() {
        if(shuffle==true){
          shuffleObject.setPreviousSong(previousSong);
            return shuffleObject.getShuffleSong( shuffleKeys, table.getTable().getItems());
        }
        else{
            return table.getNextSong();
        }
    }

    @Override
    public void addSongs(List<AudioInformation> songs) {
        table.addSongs(songs);
        table.refresh();

    }

    @Override
    public void setSongs(List<AudioInformation> songs) {
        table.setDisplayList( FXCollections.observableList(songs));

    }

    @Override
    public void removeSongs(List<AudioInformation> songs) {
        table.removeSongs(songs);
        table.refresh();

    }

    public void setAudioInformation(AudioInformation information){
        String album=information.getAlbum();
        String artist=information.getArtist();
        window.setAudioFile(information.getPhysicalFile());
        window.setCurrentSong(information);
        previousPlayedArtist=artist;
        previousPlayedAlbum=album;
        previousSong=information;
    }
    @Override
    public List<FieldKey> getShownKeys() {
         return table.getFieldKeysToShow();
    }
    private boolean inPlayList(AudioInformation information){
        File file=information.getAudioFile().getFile();
        int size=allTracks.size();
        for(int count=0; count<size;  count++){
            File file2= allTracks.get(count).getAudioFile().getFile();
            if(file.equals(file2)){
                return false;
            }
        }
        return true;
    }
    public MusicTable getTable() {
        return table;
    }
   public  void  updateTrackLabels(){ // updates the bottom labels to display  the run time and numbers of songs
        int tracks=table.getNumberOfSongs();
        long duration= (long) table.getTotalDuration();
        long years=(long)(duration/ 31536000);
        long partialYears=duration%31536000;
        long days=partialYears/86400;
        long partialDays=partialYears%86400;
        long hours=partialDays/3600;
        long partialHours=partialDays%3600;
        long minutes=partialHours/60;
        long seconds=minutes%60;
        totalTrackDurationLabel.setText(" Total Time: Years: "+years+" Days: "+days+" Hours: "+hours+" Minutes: "+minutes+" Seconds: "+seconds);
        totalTrackLabel.setText("Number Of Tracks: "+tracks);
        if(searchBar.getText().isEmpty()){
            searchBar.setText("Search Playlist...");
        }
   }
    public Playlist getPlaylist() {
        return playList;
    }
    public void setPlayList(Playlist playList) {
        this.playList = playList;
    }
    public MainAudioWindow getMainAudioWindow() {
        return window;
    }

    public List<String> getArtists() {
        return artists;
    }

    public List<String> getAlbums() {
        return albums;
    }
}
