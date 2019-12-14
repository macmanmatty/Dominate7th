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
import sample.AudioProcessors.GetAudioFiles;
import sample.AudioProcessors.ImportCD;
import sample.Library.Playlist;
import sample.Library.PlaylistPaneKind;
import sample.Library.Settings;
import sample.MainAudioWindow;
import sample.Windows.ImportCDWindow;

import java.util.ArrayList;

public class CDPlaylistPane extends TablePlaylistPane{

    Button importCD= new Button();
    String cdPath;




    public CDPlaylistPane(Playlist playlist, MainAudioWindow window) {
        super(playlist, window);
    }


    public Node makePane(){
        playlistWindow= new VBox();
        playList.setPane(this);
        playList.setPlayListPaneKind(PlaylistPaneKind.TABLE);
        shuffleMode=playList.getShuffleMode();
        searchBar= new TextField();
        table= new MusicTable(this, window, false);
        table.setPlaylist(playList);
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
        importCD= new Button("Import CD");
        importCD.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {



                new ImportCDWindow(window, playList.getAllSongs()).displayWindow();
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
                        ObservableList<TableColumn> columns= table.getColumns();
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
        header1.setSpacing(7);
        header1.getChildren().add(lockSort);
        header1.getChildren().add(editTracks);
        header1.getChildren().add(importCD);
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

    public String getCdPath() {
        return cdPath;
    }

    public void setCdPath(String cdPath) {
        this.cdPath = cdPath;
    }


}
