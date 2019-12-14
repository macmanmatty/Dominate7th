package sample.Windows;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.AudioProcessors.*;
import sample.Library.AudioInformation;
import sample.Library.Playlist;
import sample.Library.Settings;
import sample.MainAudioWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetTrackInfoFromWebWindow {

    private Stage stage= new Stage();
    private CheckBox automaticllySetBestResult;
    private CheckBox getAndAddAlbumArtworkToFile;
    private CheckBox embedArtWorkIntoFile;
    private CheckBox useMusicBrainzFirst;
   private List<AudioInformation> songs= new ArrayList<>();
   private  MainAudioWindow mainAudioWindow;
   private  Button getInfo;
   private  Playlist playlist;
   private  SongsToIdWindow songsToIdWindow;
   private VBox mainBox;
   private VBox progressBox= new VBox();

    public GetTrackInfoFromWebWindow(List<AudioInformation> songs, MainAudioWindow mainAudioWindow, Playlist playlist) {
        this.songs = songs;
        this.playlist=playlist;
        this.mainAudioWindow=mainAudioWindow;
        System.out.println("songs"+ songs.size());
        stage.setTitle("Get Track Tags Via AcoustID");


    }

    public void displayWindow(){
 mainBox= new VBox();
        automaticllySetBestResult= new CheckBox("Automatically  Select and  Set Best Result For The Given Songs");

        automaticllySetBestResult.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                getAndAddAlbumArtworkToFile.setVisible(automaticllySetBestResult.isSelected());
                useMusicBrainzFirst.setVisible(automaticllySetBestResult.isSelected());

            }
        });
        getAndAddAlbumArtworkToFile= new CheckBox("Attempt  To Find and Add Album Art Through The Cover Art Archive ");
        getAndAddAlbumArtworkToFile.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                embedArtWorkIntoFile.setVisible(getAndAddAlbumArtworkToFile.isSelected());
            }
        });
        embedArtWorkIntoFile= new CheckBox("Embed Album Art  Into Audio File");
        useMusicBrainzFirst= new CheckBox("Always Use MusicBrainz Result ");


        Label label= new Label("Get Tags For "+songs.size()+" Songs");
        getInfo=new Button("Get Tags For Songs");
        getInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(automaticllySetBestResult.isSelected()==true){

                    getBestResult();


                }
                else{

                    getResultsAndShowResultsWindow();
                }


            }
        });

        getAndAddAlbumArtworkToFile.setVisible(false);
        embedArtWorkIntoFile.setVisible(false);
        useMusicBrainzFirst.setVisible(false);
        mainBox.getChildren().add(label);
        mainBox.getChildren().add(automaticllySetBestResult);
        mainBox.getChildren().add(useMusicBrainzFirst);
        mainBox.getChildren().add(getAndAddAlbumArtworkToFile);
        mainBox.getChildren().add(embedArtWorkIntoFile);
        mainBox.getChildren().add(getInfo);
        mainBox.getChildren().add(progressBox);
        stage.setScene(new Scene(mainBox));
        stage.show();




    }

    private void getResultsAndShowResultsWindow() {


        songsToIdWindow= new SongsToIdWindow(songs, mainAudioWindow, playlist);
        GetAcoustIDResultsForSongs getAcoustIDResultsForSongs= new GetAcoustIDResultsForSongs();

        AudioInformationProcessor processor= new AudioInformationProcessor( getAcoustIDResultsForSongs);
        Settings settings=mainAudioWindow.getSettings();
        processor.setShowNotifications(settings.isShowProcessNotifications());
        processor.setShowNotifications(settings.isShowNotifcationOnProcessComplete());
        OnFinishAction action= new OnFinishAction() {
            @Override
            public void act() {
                if(playlist!=null) {
                    playlist.updatePlayListPane();
                }
                Runnable runnable= new Runnable() {
                    @Override
                    public void run() {
                        songsToIdWindow.setSongResults(getAcoustIDResultsForSongs.getSongResults());

                        songsToIdWindow.displayWindow();


                    }
                };
                Platform.runLater(runnable);


            }
        };
        processor.setOnFinishAction(action);
        ProgressWindow progressWindow= new ProgressWindow(processor);
        progressBox.getChildren().clear();
        progressBox.getChildren().add(progressWindow.getWindow());
        stage.hide();
       stage.show();
        List<AudioInformation> syncSongs= new ArrayList<>(songs);// make the list thread safe audio information processor runs on new thread(S)
       syncSongs=Collections.synchronizedList(syncSongs);
        processor.manipulateAudioInformation(syncSongs);
    }


   private  void getBestResult(){


        AudioInformationProcessor processor= new AudioInformationProcessor( new GetAndSaveBestAcoustIDResult(getAndAddAlbumArtworkToFile.isSelected(), embedArtWorkIntoFile.isSelected(),useMusicBrainzFirst.isSelected() ));
       Settings settings=mainAudioWindow.getSettings();
       processor.setShowNotifications(settings.isShowProcessNotifications());
       processor.setShowCompletedNotification(settings.isShowNotifcationOnProcessComplete());

        OnFinishAction action= new OnFinishAction() {
            @Override
            public void act() {
                if(playlist!=null) {
                    playlist.updatePlayListPane();
                }

                Runnable runnable= new Runnable() {
                    @Override
                    public void run() {

                    }
                };
                Platform.runLater(runnable);


            }
        };
        processor.setOnFinishAction(action);
        ProgressWindow progressWindow= new ProgressWindow(processor);
       mainBox.getChildren().add(progressWindow.getWindow());
       stage.hide();
       stage.show();
        List<AudioInformation> syncSongs= new ArrayList<>(songs);// make the list thread safe audio information processor runs on new thread(S)
       syncSongs=Collections.synchronizedList(syncSongs);

       processor.manipulateAudioInformation(syncSongs);


    }
}
