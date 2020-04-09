package sample.PlaylistPanes;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.stage.Stage;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.FieldKey;
import sample.*;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.AudioProcessors.GetAudioFiles;
import sample.Library.*;
import sample.SearchAudio.Sorter;
import sample.Utilities.FieldKeyOperations;
import sample.Windows.OptionPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicTable extends TableView {

    private int clicks;

  private  ArrayList<FieldKey> fieldKeysToShow = new ArrayList<>(FieldKey.values().length);// the Jaudio tagger field Keys to show on the table
  private BidiMap<FieldKey, TableColumn> columnKeySet= new DualHashBidiMap<>();
    private  AudioFile audioFile;// the selected audio file
    private TablePlaylistPane tablePlayListPane; // the  play window containg the table
   private Sorter sorter= new Sorter(); // sorter and search for songs
    private ObservableList<AudioInformation> displayList; // the songs currently displayed
    private AudioInformation selectedSong; // the selected song from the table to play , delete ect.
    private int selectedSongPosition; // numerical position of the selected song.
    private Playlist playlist; // the play list liked to the table.
    private AudioInformationContextMenu contextMenu; // the tables context menu
    private ExtractAudioInformation extract = new ExtractAudioInformation();
    private MainAudioWindow window;
    private boolean fileAddable=true;

    public MusicTable(TablePlaylistPane tablePlayListPane, MainAudioWindow window) {



        this.tablePlayListPane = tablePlayListPane;
        this.window=window;

    }
    public MusicTable(TablePlaylistPane tablePlayListPane, MainAudioWindow window, boolean fileAddable) {


        this.fileAddable=fileAddable;


        this.tablePlayListPane = tablePlayListPane;
        this.window=window;

    }
    public AudioFile getAudioFile() {
        return audioFile;
    }

    public void makeTable(){


        getTable().setMinSize(400,200);
       prefHeightProperty().bind(tablePlayListPane.getMainAudioWindow().getStage().heightProperty());
       prefWidthProperty().bind(tablePlayListPane.getMainAudioWindow().getStage().widthProperty());





       if(fileAddable==true) {
           setOnKeyPressed(new EventHandler<KeyEvent>() // new key handler for pressing delete to remove songs from playlist
           {
               @Override
               public void handle(final KeyEvent keyEvent) {
                   final List<AudioInformation> selectedItems = getSelectionModel().getSelectedItems();

                   if (selectedItems.size() > 0) {
                       if (keyEvent.getCode().equals(KeyCode.DELETE)) {

                           if (!(playlist instanceof SmartPlaylist)) {


                               new OptionPane().showDeleteSongsPane(window.getLibrary(), playlist, selectedItems);
                           } else {
                               Stage stage = new Stage();
                               EventHandler<ActionEvent> deleteSongs = new EventHandler<ActionEvent>() {
                                   @Override
                                   public void handle(ActionEvent event) {
                                       ArrayList<AudioInformation> information = new ArrayList<>();
                                       information.addAll(getSelectionModel().getSelectedItems());
                                       window.getLibrary().removeSongs(information);
                                       playlist.getAllSongs().removeAll(information);
                                       displayList.removeAll(information);
                                       stage.close();
                                       refresh();


                                   }
                               };

                               EventHandler<ActionEvent> no = new EventHandler<ActionEvent>() {
                                   @Override
                                   public void handle(ActionEvent event) {


                                   }
                               };
                               new OptionPane().showOptionPane(stage, "Delete Songs From Library?", "Yes!", deleteSongs, "No", no);


                           }

                       }
                   }
               }
           });


           setOnSort(new EventHandler<SortEvent<TableView>>() {
               @Override
               public void handle(SortEvent<TableView> event) {

                   saveSortOrder();

               }
           });


           getColumns().addListener(new ListChangeListener<TableColumn<AudioInformation, String>>() {
               @Override
               public void onChanged(ListChangeListener.Change<? extends TableColumn<AudioInformation, String>> change) {
                   List<TableColumn> columns=getColumns();
                   int size=columns.size();
                   List<FieldKey> fieldKeys= new ArrayList<>();
                   for(int count=0; count<size; count++){
                       if(columnKeySet.getKey(columns.get(count))!=null) {
                           fieldKeys.add(columnKeySet.getKey(columns.get(count)));
                       }

                   }



                   playlist.setShowFields(fieldKeys);


               }
           });

           setOnDragEntered(new EventHandler<DragEvent>() { // drag listener for adding songs to the play list

               @Override
               public void handle(DragEvent event) {

                   Dragboard db = event.getDragboard();
                   if  (db.hasFiles()) {
                       List<File> files=db.getFiles();
                       System.out.println("DRag DRopped!!! files "+files.size());

                       GetAudioFiles getAudioFiles;
                       Settings settings = window.getLibrary().getSettings();

                       getAudioFiles = new GetAudioFiles( window.getSystemInfo(), window.getLibrary(), playlist);
                       getAudioFiles.setAddSongsToCurrentPlayList(true);
                       getAudioFiles.setAddSongsToLibrary(settings.isAddAllAddedSongsToLibrary());
                       getAudioFiles.setSortByBitRate(settings.isSortLibraryByBitRate());
                       getAudioFiles.setMoveSongsToLibrary(settings.isMoveFilesToLibraryFolder());
                       getAudioFiles.setCopyFilesThenMove(settings.isCopyFilesToLibraryFolder());
                       getAudioFiles.setSortByFileType(settings.isSortLibraryByFileType());
                       getAudioFiles.setGetMissingTags(settings.isGetMissingTagsOnAddingSongsToLibrary());
                       List<AudioInformation> songs=getAudioFiles.loadFiles(files, settings.isDisplayProgressWindowsForAddingSongs());
                       System.out.println("Songs in table "+songs.size());
                       playlist.addSongs(songs);
                       if(settings.isAddAllAddedSongsToLibrary()){
                           window.getLibrary().addSongs(songs);
                       }



                   }
                   refresh();
                   event.consume();
               }
           });

       }

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

                    contextMenu.setSongs(getSelectionModel().getSelectedItems());
                    contextMenu.updateMenuItems();

                    contextMenu.show(getTable(), event.getScreenX(), event.getScreenY());
                }

                if(getSelectionModel()!=null) {
                    if(getSelectionModel().isEmpty()==false ) {
                        AudioInformation information= (AudioInformation) getSelectionModel().getSelectedItems().get(0);
                        if(selectedSong!=null&& selectedSong==information) {
                            tablePlayListPane.setAudioInformation(information);

                        }
                        else{

                            selectedSong=information;
                            selectedSongPosition=getSelectionModel().getSelectedIndex();
                        }
                    }

                }

            }
        });




        fieldKeysToShow.addAll(playlist.getShowFields());
        if(fieldKeysToShow.size()==0){
            fieldKeysToShow.add(FieldKey.ALBUM);
            fieldKeysToShow.add(FieldKey.ARTIST);
            fieldKeysToShow.add(FieldKey.TITLE);
            fieldKeysToShow.add(FieldKey.TRACK);

        }
        contextMenu=new AudioInformationContextMenu(window, tablePlayListPane);
        contextMenu.makeMenu();
        setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {

                contextMenu.setSongs(getSelectionModel().getSelectedItems());
                contextMenu.updateMenuItems();
                contextMenu.show(getTable(),  event.getScreenX(), event.getScreenY());

            }
        });

        int size= fieldKeysToShow.size();


        for(int count=0; count<size; count++){

            FieldKey key= fieldKeysToShow.get(count);

            TableColumn column= new TableColumn(FieldKeyOperations.getFieldKeyDisplayName(key));
            column.setEditable(FieldKeyOperations.getEditable(key));

            System.out.println("Column width "+playlist.getColumnWidth(key));

            column.setPrefWidth(playlist.getColumnWidth(key));
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
            if(key==FieldKey.TRACK || key==FieldKey.DISC_NO){

                column.setComparator(FieldKeyOperations.getNumberStringComparartor());
            }


            getColumns().add(column);
            columnKeySet.put(key, column);

        }

        TableColumn durationColumn= new TableColumn("Duration");
        durationColumn.setCellValueFactory( new PropertyValueFactory<AudioInformation,String>("trackLength"));
        durationColumn.setEditable(false);
        durationColumn.setComparator(FieldKeyOperations.getTimeDurationComparartor());

        getColumns().add(durationColumn);

        setItems(displayList);

    }


    public void saveSortOrder(){
        ObservableList<TableColumn> column =getSortOrder();
        List<FieldKey> columsKeys= new ArrayList<>();
        int size=column.size();
        for(int count=0; count<size; count++){
            columsKeys.add(columnKeySet.getKey(column));



        }

        playlist.setSortOrder(columsKeys);


    }


    public void setSortOrder(List<FieldKey> sortKeys){
        int size=sortKeys.size();
        for(int count=0; count<size; count++){
            getSortOrder().add(columnKeySet.get((sortKeys.get(count))));




        }



    }



    public void changeColumn(FieldKey key){ // if  a column for given field key exists removes it if column does not exist it adds it to the table.

       boolean columnExists= columnExists(key);


       if(columnExists){

           getColumns().remove(columnKeySet.get(key));
           fieldKeysToShow.remove(key);
           columnKeySet.remove(key);
           playlist.getShowFields().remove(key);


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
           if(key==FieldKey.TRACK || key==FieldKey.DISC_NO){

               column.setComparator(FieldKeyOperations.getNumberStringComparartor());
           }


           getColumns().add(column);
           columnKeySet.put(key, column);

           refresh();
           playlist.getShowFields().add(key);
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

    @Override
    public boolean resizeColumn(TableColumn column, double delta) {
       FieldKey key= columnKeySet.getKey(column);
        playlist.setColumnWidth(key, delta+column.getWidth());


        return super.resizeColumn(column, delta);
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

    public void setTablePlayListPane(TablePlaylistPane tablePlayListPane) {
        this.tablePlayListPane = tablePlayListPane;
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
    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) { // sets the playlist and creates  a new observable list based on the song in that play list
        this.playlist = playlist;

        this.displayList=FXCollections.observableList(playlist.getAllSongs());

    }

    public ArrayList<FieldKey> getFieldKeysToShow() {
        return fieldKeysToShow;
    }

    public void setFieldKeysToShow(ArrayList<FieldKey> fieldKeysToShow) {
        this.fieldKeysToShow = fieldKeysToShow;
    }

    public void search(String text, boolean and){ // searchs for given string of text   in all field keys and display all songs that match by setting tables list to the display list .

        List<AudioInformation> list= sorter.search(text, playlist.getAllSongs(), fieldKeysToShow, and );
        displayList=FXCollections.observableList(list);
        setItems(displayList);
        refresh();
    }

    public void search(String text){ // searchs for given string of text in all field keys and display all songs that match by setting tables list to the display list .

        List<AudioInformation> list= sorter.search(text, playlist.getAllSongs(), fieldKeysToShow );
        displayList=FXCollections.observableList(list);
        setItems(displayList);
        refresh();


    }

    public void search(FieldKey key, String text){ //searchs for a given string of text in given field key and displays all songs that match unless text equals all  or is empty then does nothing

        List<AudioInformation> list= sorter.search(key, text, playlist.getAllSongs());

        displayList=FXCollections.observableList(list);


        setItems(displayList);
        refresh();


    }
    public void displayAllSongs() { // displays All the songs in the play list

        List<AudioInformation> list= playlist.getAllSongs();
        displayList=FXCollections.observableList(list);
        setItems(displayList);
        refresh();

    }


    public int getNumberOfSongs(){

        return displayList.size();
    }
    public double getTotalDuration(){

        double totalRunTime=0;

        int size=displayList.size();

        for(int count=0;  count<size; count++){

            totalRunTime=totalRunTime+displayList.get(count).getTrackLengthNumber();

        }

        return totalRunTime;

    }

    public List<String> getArtists(){

         return sorter.getMatchingKeys(playlist.getAllSongs(), FieldKey.ARTIST);

    }

    public List<String> getAlbums(){

        return sorter.getMatchingKeys(playlist.getAllSongs(), FieldKey.ALBUM);

    }

    public List<String> getAlbums(String artist){

        return sorter.getMatchingKeys(playlist.getAllSongs(), FieldKey.ARTIST, artist, FieldKey.ALBUM);

    }

    public List<String> getNames(ArrayList<AudioInformation> information, FieldKey key){

        return sorter.getMatchingKeys(information, key);

    }

    public  TableView getTable(){

        return this;

    }

    public void updateArtistsAndAlbums(){
        List<String> artists=getArtists();
        List<String> albums=getAlbums();
        sorter.addNewStrings(tablePlayListPane.getArtists(), artists);
        sorter.addNewStrings(tablePlayListPane.getAlbums(), albums);



    }

    public void setDisplayList(ObservableList<AudioInformation> displayList) {
        this.displayList = displayList;
       setItems(displayList);
    }

    public void addSongs(List<AudioInformation> songs) {
        this.displayList.addAll(songs);
        setItems(displayList);

    }
    public void removeSongs(List<AudioInformation> songs) {
        this.displayList.removeAll(songs);
        setItems(displayList);
    }


}
