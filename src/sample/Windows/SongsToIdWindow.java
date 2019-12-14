package sample.Windows;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.AudioProcessors.SaveAlbumArtFromWeb;
import sample.Library.AudioInformation;
import sample.Library.Playlist;
import sample.MainAudioWindow;
import sample.PlaylistPanes.MusicTable;

import java.util.ArrayList;
import java.util.List;

public class SongsToIdWindow {

    private List<List<AudioInformation>> songResults = new ArrayList<>();
    private List<AudioInformation> songsToId= new ArrayList<>();
    private Stage stage= new Stage();
   private  SongIDTable songIDTable;
   private  MainAudioWindow window;
   private  VBox songsToIdBox;
    protected  CheckBox lockSort;
    protected   CheckBox editTracks;
    protected Label totalTrackLabel;
    protected Label totalTrackDurationLabel;
    protected Label albumLabel;
    protected TextField searchBar;
    protected String artist="";
    protected String album="";
    protected int numberOfAlbums;
    protected int numberOfArtists;
    private CheckBox getAndAddAlbumArtworkToFile;
    private CheckBox embedArtWorkIntoFile;
    protected ChoiceBox<String> searchChoices= new ChoiceBox<>();
    protected Playlist playlist;


    public SongsToIdWindow(List<AudioInformation> songsToId, MainAudioWindow window, Playlist playlist) {
        this.songsToId = songsToId;
        this.window = window;
        this.playlist=playlist;
        stage.setTitle("ID Songs");

    }

    public SongsToIdWindow getSongsToIdWindow(){
        return this;

    }

    public void displayWindow(){

        songsToIdBox = new VBox();
        songsToIdBox.getStylesheets().add("css/songsToIdWindow.css");

        songIDTable=new SongIDTable(this);
        songIDTable.makeTable();
        Label title = new Label(songIDTable.getNumberOfResults()+" Songs  Out Of " +songsToId.size()+" With Matches Found Select Results ");
        songsToIdBox.getChildren().add(title);

        title.getStyleClass().add("title");
        searchBar= new TextField();
        albumLabel= new Label();
        searchChoices= new ChoiceBox<>();
        ArrayList <String> searchOperators= new ArrayList<>();
        searchOperators.add("And");
        searchOperators.add("Or");
        searchOperators.add("Exact Phrase");
        searchChoices.setItems(FXCollections.observableList(searchOperators));
        searchChoices.getSelectionModel().select(3);
        searchBar.setText("Search Playlist...");
        searchBar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text=searchBar.getText();
                if(text==null|| text.isEmpty()){

                    songIDTable.displayAllSongs();
                    updateTrackLabels();

                }
                else {

                    if (searchChoices.getSelectionModel().getSelectedIndex() == 0) {
                       songIDTable.search(text, true);
                    }
                    else if(searchChoices.getSelectionModel().getSelectedIndex()==1){
                        songIDTable.search(text, false);

                    }


                    else {

                        songIDTable.search(text);


                    }
                    updateTrackLabels();


                }

            }
        });
        getAndAddAlbumArtworkToFile= new CheckBox("Attempt  To Find and Add Album Art Through The Cover Art Archive ");
        getAndAddAlbumArtworkToFile.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                songIDTable.setGetAlbumArt(getAndAddAlbumArtworkToFile.isSelected());
                embedArtWorkIntoFile.setVisible(getAndAddAlbumArtworkToFile.isSelected());
            }
        });
        embedArtWorkIntoFile= new CheckBox("Embed Album Art  Into Audio File");
        embedArtWorkIntoFile.setVisible(false);


        embedArtWorkIntoFile.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                songIDTable.setEmbedAlbumArt(embedArtWorkIntoFile.isSelected());
            }
        });

        editTracks=new

                CheckBox("Edit Track Information");
        editTracks.selectedProperty().

                addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed (ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue){


                        songIDTable.setEditable(editTracks.isSelected());
                    }
                });
        lockSort=new

                CheckBox("Lock Track Sorting? ");
        lockSort.selectedProperty().

                addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed (ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue){

                        ObservableList<TableColumn>  columns= songIDTable.getColumns();
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
        header1.setSpacing(7);

        header1.getChildren().add(albumLabel);
        header1.getChildren().add(lockSort);
        header1.getChildren().add(editTracks);
        header1.getChildren().add(getAndAddAlbumArtworkToFile);
        header1.getChildren().add(embedArtWorkIntoFile);
        header.getChildren().add(header1);
        HBox searchBarbox= new HBox(searchChoices, searchBar);
        header.getChildren().add(searchBarbox);
        songsToIdBox.getChildren().add(header);
        songsToIdBox.getChildren().add(songIDTable);

        HBox footer= new HBox();
        footer.getChildren().add(totalTrackLabel);
        footer.getChildren().add(totalTrackDurationLabel);
        updateTrackLabels();
        songsToIdBox.getChildren().add(footer);


        double searchBarWidth=1000;
        searchBar.setPrefWidth(searchBarWidth);
        searchBar.setMaxWidth(searchBarWidth);
        searchBar.setMinWidth(searchBarWidth);
        stage.setScene(new Scene(songsToIdBox));
        stage.show();
    }

    public List<AudioInformation> getSongsToId() {
        return songsToId;
    }

    public void setSongsToId(List<AudioInformation> songsToId) {
        this.songsToId = songsToId;
    }

    public List<List<AudioInformation>> getSongResults() {
        return songResults;
    }

    public void setSongResults(List<List<AudioInformation>> songResults) {
        this.songResults = songResults;
    }

    public  void  updateTrackLabels(){ // updates the bottom labels to display  the run time and numbers of songs

        int tracks=songIDTable.getNumberOfSongs();
        long duration=songIDTable.getTotalDuration();
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
        return playlist;
    }
}
