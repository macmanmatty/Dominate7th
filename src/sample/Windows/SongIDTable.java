package sample.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.Utilities.FieldKeyOperations;
import sample.Library.*;
import sample.MainAudioWindow;
import sample.SearchAudio.Sorter;
import sample.Utilities.CopyAudioInformation;
import java.util.ArrayList;
import java.util.List;
public class SongIDTable extends TableView {
    private int clicks;
  private  ArrayList<FieldKey> fieldKeysToShow = new ArrayList<>(FieldKey.values().length);// the Jaudio tagger field Keys to show on the table
  private BidiMap<FieldKey, TableColumn> columnKeySet= new DualHashBidiMap<>();
    private SongsToIdWindow songsToIdWindow; // the  play window containg the table
   private Sorter sorter= new Sorter(); // sorter and search for songs
    private ObservableList<AudioInformation> displayList; // the songs currently displayed
    private AudioInformation selectedSong; // the selected song from the table to play , delete ect.
    private int selectedSongPosition; // numerical position of the selected song.
    private ExtractAudioInformation extract = new ExtractAudioInformation();
    private List<List<AudioInformation>> songResults;
    private Playlist playlist;
    private int numberOfResults;
    private boolean getAlbumArt;
    private boolean embedAlbumArt;
    public SongIDTable(SongsToIdWindow songsToIdWindow) {
        fieldKeysToShow.add(FieldKey.ALBUM);
        fieldKeysToShow.add(FieldKey.ARTIST);
        fieldKeysToShow.add(FieldKey.TITLE);
        fieldKeysToShow.add(FieldKey.CUSTOM4);
     playlist=songsToIdWindow.getPlaylist();
        this.songsToIdWindow = songsToIdWindow;
        List<AudioInformation> songsToId=songsToIdWindow.getSongsToId();
        System.out.println("Songs "+songsToId.size());
        songResults=songsToIdWindow.getSongResults();
        // set information to best result
        int size=songResults.size();
        displayList= FXCollections.observableList(new ArrayList<>());
        int count=0;
        size=songsToId.size();
        for(count=0; count<size; count++){
            List<AudioInformation> informationList=songResults.get(count);
            if(informationList.size()>0) {
                CopyAudioInformation.copyAudioInformation(songsToId.get(count), informationList.get(0));
                displayList.add(songsToId.get(count));
                numberOfResults++;
            }
            else{
                displayList.add(songsToId.get(count));
            }
        }
    }
    public void updateTable(){
        refresh();
        if(playlist!=null){
            playlist.updatePlayListPane();
        }
    }
    public void makeTable(){
        getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { // mouse  click  listener  for displaying the tables context menu
            @Override
            public void handle(MouseEvent t) {
            }
        });
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    IdSelectionMenu idSelectionMenu=new IdSelectionMenu(songResults, getSongIdTable(), getAlbumArt, embedAlbumArt);
                    idSelectionMenu.createMenu(getSelectionModel().getSelectedIndex());
                    idSelectionMenu.show(getTable(), event.getScreenX(), event.getScreenY());
                }
                if(getSelectionModel()!=null) {
                }
            }
        });
        IdSelectionMenu idSelectionMenu =new IdSelectionMenu(songResults, this, getAlbumArt, embedAlbumArt);
        setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                IdSelectionMenu idSelectionMenu=new IdSelectionMenu(songResults, getSongIdTable(),getAlbumArt, embedAlbumArt);
                idSelectionMenu.createMenu(getSelectionModel().getSelectedIndex());
                idSelectionMenu.show(getTable(),  event.getScreenX(), event.getScreenY());
            }
        });
        int size= fieldKeysToShow.size();
        for(int count=0; count<size; count++){
            FieldKey key= fieldKeysToShow.get(count);
            TableColumn column= new TableColumn(FieldKeyOperations.getFieldKeyDisplayName(key));
            column.setEditable(FieldKeyOperations.getEditable(key));
            column.setPrefWidth(200);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            PropertyValueFactory factory=new PropertyValueFactory<AudioInformation,String>(FieldKeyOperations.getFieldKeyGetter(key));
            column.setCellValueFactory( factory);
            column.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<AudioInformation, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<AudioInformation,String> event) {
                            AudioInformation information=((AudioInformation)event.getTableView().getItems().get(
                                    event.getTablePosition().getRow()));
                            extract.setInformation( information, key, event.getNewValue());
                        }
                    });
            getColumns().add(column);
            columnKeySet.put(key, column);
        }
        TableColumn durationColumn= new TableColumn("Duration");
        durationColumn.setCellValueFactory( new PropertyValueFactory<AudioInformation,String>("trackLength"));
        getColumns().add(durationColumn);
        setItems(displayList);
    }

    public void changeColumn(FieldKey key){ // if  a column for given field key exists removes it if column does not exist it adds it to the table.
       boolean columnExists= columnExists(key);
       if(columnExists){
           getColumns().remove(columnKeySet.get(key));
           fieldKeysToShow.remove(key);
           columnKeySet.remove(key);
       }
       else {
           TableColumn column= new TableColumn(FieldKeyOperations.getFieldKeyDisplayName(key));
           column.setEditable(FieldKeyOperations.getEditable(key));
           column.setPrefWidth(250);
           column.setCellFactory(TextFieldTableCell.forTableColumn());
           PropertyValueFactory factory=new PropertyValueFactory<AudioInformation,String>(FieldKeyOperations.getFieldKeyGetter(key));
           column.setCellValueFactory( factory);
           column.setOnEditCommit(
                   new EventHandler<TableColumn.CellEditEvent<AudioInformation, String>>() {
                       @Override
                       public void handle(TableColumn.CellEditEvent<AudioInformation,String> event) {
                           AudioInformation information=((AudioInformation)event.getTableView().getItems().get(
                                   event.getTablePosition().getRow()));
                           extract.setInformation( information, key, event.getNewValue());
                           refresh();
                       }
                   });
           getColumns().add(column);
           columnKeySet.put(key, column);
           refresh();
           fieldKeysToShow.add(key);
       }
    }
    private boolean columnExists(FieldKey key){
        int size= fieldKeysToShow.size();
        for(int count=0; count<size; count++) {
            if(fieldKeysToShow.get(count)==key){
                return true;
            }
        }
        return false;
        }
    public AudioInformation getNextSong(){ // gets the next song to play  if there is no next song start from song 0 again
      selectedSongPosition++;
        if(selectedSongPosition>=getItems().size()){
            return(AudioInformation) getItems().get(0);
        }
        return (AudioInformation) getItems().get(selectedSongPosition);
        }
    public String getFieldKeyDisplayName(FieldKey key){ // turns a filed key the variable name for the javafx table
        String name=key.toString();
        name.toLowerCase();
        String [] splitName= name.split("_");
        int size=splitName.length;
        String fullName=splitName[0];
        for(int count=0; count<size; count++){
            fullName=fullName+splitName[count].substring(0, 1).toUpperCase() + splitName[count].substring(1);
        }
        return fullName;
    }

    public boolean addKey(FieldKey key){
        int size= fieldKeysToShow.size();
        for(int count=0; count<size; count++){
            FieldKey fKey= fieldKeysToShow.get(count);
            if(fKey==key){
                return true;
            }
        }
        fieldKeysToShow.add(key);
    return false;
    }
    public boolean removeKey(FieldKey key){
        int size= fieldKeysToShow.size();
        for(int count=0; count<size; count++){
            FieldKey fKey= fieldKeysToShow.get(count);
            if(fKey==key){
                fieldKeysToShow.add(key);
                return true;
            }
        }
        return false;
    }
    public void setSongs(List<AudioInformation> songs) { // sets the playlist and creates  a new observable list based on the song in that play list
        this.displayList=FXCollections.observableList(songs);
    }
    public ArrayList<FieldKey> getFieldKeysToShow() {
        return fieldKeysToShow;
    }
    public void setFieldKeysToShow(ArrayList<FieldKey> fieldKeysToShow) {
        this.fieldKeysToShow = fieldKeysToShow;
    }
    public void search(String text, boolean and){ // searchs for given string of text   in all field keys and display all songs that match by setting tables list to the display list .
        List<AudioInformation> list= sorter.search(text, songsToIdWindow.getSongsToId(), fieldKeysToShow, and );
        displayList=FXCollections.observableList(list);
        setItems(displayList);
        refresh();
    }
    public void search(String text){ // searchs for given string of text in all field keys and display all songs that match by setting tables list to the display list .
        List<AudioInformation> list= sorter.search(text, songsToIdWindow.getSongsToId(), fieldKeysToShow );
        displayList=FXCollections.observableList(list);
        setItems(displayList);
        refresh();
    }
    public void search(FieldKey key, String text){ //searchs for a given string of text in given field key and displays all songs that match unless text equals all  or is empty then does nothing
        List<AudioInformation> list= sorter.search(key, text, songsToIdWindow.getSongsToId());
        displayList=FXCollections.observableList(list);
        setItems(displayList);
        refresh();
    }
    public void displayAllSongs() { // displays All the songs in the play list
        List<AudioInformation> list= songsToIdWindow.getSongsToId();
        displayList=FXCollections.observableList(list);
        setItems(displayList);
        refresh();
    }
    public int getNumberOfSongs(){
        return displayList.size();
    }
    public long getTotalDuration(){
        long totalRunTime=0;
        int size=displayList.size();
        for(int count=0;  count<size; count++){
            totalRunTime= (long) (totalRunTime+displayList.get(count).getTrackLengthNumber());
        }
        return totalRunTime;
    }
    public ObservableList<AudioInformation> getDisplayList() {
        return displayList;
    }
    public List<String> getArtists(){
         return sorter.getMatchingKeys(songsToIdWindow.getSongsToId(), FieldKey.ARTIST);
    }
    public List<String> getAlbums(){
        return sorter.getMatchingKeys(songsToIdWindow.getSongsToId(), FieldKey.ALBUM);
    }
    public List<String> getAlbums(String artist){
        return sorter.getMatchingKeys(songsToIdWindow.getSongsToId(), FieldKey.ARTIST, artist, FieldKey.ALBUM);
    }
    public List<String> getNames(ArrayList<AudioInformation> information, FieldKey key){
        return sorter.getMatchingKeys(information, key);
    }
    public  TableView getTable(){
        return this;
    }
    public void setDisplayList(ObservableList<AudioInformation> displayList) {
        this.displayList = displayList;
        getTable().setItems(displayList);
    }
    public  SongIDTable getSongIdTable(){
        return this;
    }
    public int getNumberOfResults() {
        return numberOfResults;
    }
    public boolean isGetAlbumArt() {
        return getAlbumArt;
    }
    public void setGetAlbumArt(boolean getAlbumArt) {
        this.getAlbumArt = getAlbumArt;
    }
    public boolean isEmbedAlbumArt() {
        return embedAlbumArt;
    }
    public void setEmbedAlbumArt(boolean embedAlbumArt) {
        this.embedAlbumArt = embedAlbumArt;
    }
}
