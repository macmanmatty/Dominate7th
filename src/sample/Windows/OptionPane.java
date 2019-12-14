package sample.Windows;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.AudioProcessors.GetAudioFiles;
import sample.MainAudioWindow;
import sample.Library.AudioInformation;
import sample.Library.MusicLibrary;
import sample.Library.Playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OptionPane {


    int option;

    public OptionPane(){}
    public void showOptionPane(String text, String buttonText){ // shows a new option pane with one message and  button much like a  joption pane  can be called from any thread
        Runnable  runnable=	new Runnable(){
            @Override
            public void run(){
                Stage dialogPane = new Stage();
                dialogPane.initModality(Modality.APPLICATION_MODAL);
                Button button = new Button(buttonText);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( ActionEvent k ) {
                        option=1;
                        dialogPane.hide();
                    }
                });
                VBox vbox = new VBox(new Text(text), button);
                vbox.setMinSize(300,300);
                vbox.setStyle("   -fx-padding: 15; -fx-spacing: 10;" );
                dialogPane.setScene(new Scene(vbox));
                dialogPane.showAndWait();
            }};
        Platform.runLater(runnable);
    }




    public void showOptionPane(String text, String buttonText, EventHandler<ActionEvent> buttonAction){ // shows a new option pane much like a  joption pane with one message two buttons
        // and an action to be performed on the other button click
        //  can be called from any thread
        Runnable  runnable=	new Runnable(){
            @Override
            public void run(){
                Stage dialogPane = new Stage();
                dialogPane.initModality(Modality.APPLICATION_MODAL);
                Button button = new Button(buttonText);
                button.setOnAction(buttonAction);
                Button close= new Button("No");
                close.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( ActionEvent k ) {
                        dialogPane.hide();
                    }
                });
                HBox hBox= new HBox(button, close);
                hBox.setSpacing(10);



                VBox vbox = new VBox(new Text(text), hBox);
                vbox.setMinSize(300,300);
                vbox.setStyle("   -fx-padding: 15; -fx-spacing: 10;" );
                dialogPane.setScene(new Scene(vbox));
                dialogPane.showAndWait();
            }};
        Platform.runLater(runnable);
    }


    public void showOptionPane( Stage stage, String text, String buttonText, EventHandler<ActionEvent> buttonAction, String button2Text, EventHandler<ActionEvent> button2Action){
        Runnable  runnable=	new Runnable(){
            @Override
            public void run(){
                Stage dialogPane =stage;
                dialogPane.initModality(Modality.APPLICATION_MODAL);
                Button button = new Button(buttonText);
                button.setOnAction(buttonAction);
                Button button2= new Button(button2Text);
                button2.setOnAction(button2Action);

                HBox hBox= new HBox(button,  button2);
                hBox.setSpacing(10);



                VBox vbox = new VBox(new Text(text), hBox);
                vbox.setMinSize(300,300);
                vbox.setStyle("   -fx-padding: 15; -fx-spacing: 10;" );
                dialogPane.setScene(new Scene(vbox));
                dialogPane.showAndWait();
            }};
        Platform.runLater(runnable);
    }



    public void  showLocateFilePane(AudioInformation information){
        Runnable  runnable=	new Runnable(){
            @Override
            public void run(){
                Stage dialogPane = new Stage();
                dialogPane.initModality(Modality.APPLICATION_MODAL);
                Button button = new Button("No");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( ActionEvent k ) {
                        option=1;
                        dialogPane.hide();

                    }
                });

                Button button2 = new Button("Yes");
                button2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( ActionEvent k ) {
                        option=2;
                        Stage stage = new Stage();
                        FileChooser directoryChooser = new FileChooser();
                        File file = directoryChooser.showOpenDialog(stage);
                        information.setAudioFilePath(file.getAbsolutePath());
                        dialogPane.hide();




                    }
                });
                VBox vbox = new VBox(new Text("File Not Found Would You like to Locate It?"), button, button2);
                vbox.setMinSize(300,300);
                vbox.setStyle("   -fx-padding: 15; -fx-spacing: 10;" );
                dialogPane.setScene(new Scene(vbox));
                dialogPane.showAndWait();
            }};
        Platform.runLater(runnable);


    }




       public void showDeleteSongsPane(MusicLibrary library , Playlist playlist, List<AudioInformation> information){ // option for the deletion of songs from playlsit
        Stage dialogPane = new Stage();
        dialogPane.initModality(Modality.APPLICATION_MODAL);
        Button button = new Button("No");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent k ) {
                option=1;
                dialogPane.hide();

            }
        });

        Button button2 = new Button("Yes");
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent k ) {
                option=2;

                showRemoveSongsFromLibraryPane(library,playlist, information);

                dialogPane.hide();


            }
        });
        VBox vbox = new VBox(new Text("Remove Songs From PlayList"), button, button2);
        vbox.setMinSize(300,300);
        vbox.setStyle("   -fx-padding: 15; -fx-spacing: 10;" );
        dialogPane.setScene(new Scene(vbox));
        dialogPane.showAndWait();

    }
    public void showRemoveSongsFromLibraryPane(MusicLibrary library, Playlist playlist, List<AudioInformation> information){ // option for the deletion of songs from playlsit
       List<AudioInformation> songs= new ArrayList<>();// songs are linked to  an observable list so me must create a new list to remove them
       songs.addAll(information);

        Stage dialogPane = new Stage();
        dialogPane.initModality(Modality.APPLICATION_MODAL);
        Button button = new Button("No");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent k ) {
                option=1;
                playlist.removeSongs(songs);

                dialogPane.hide();

            }
        });

        Button button2 = new Button("Yes");
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent k ) {
                option=2;
                playlist.removeSongs(songs);
                library.removeSongs(songs);
                dialogPane.hide();


            }
        });
        VBox vbox = new VBox(new Text("Remove Songs From Library As well"), button, button2);
        vbox.setMinSize(300,300);
        vbox.setStyle("   -fx-padding: 15; -fx-spacing: 10;" );
        dialogPane.setScene(new Scene(vbox));
        dialogPane.showAndWait();

    }

    public void showSetLibraryFolderPane( final MainAudioWindow window){ // option pane for setting the mian library folder

                Stage dialogPane = new Stage();
                dialogPane.initModality(Modality.WINDOW_MODAL);
                Button button = new Button("No");
        Label currentFile= new Label();

        button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( ActionEvent k ) {
                        option=1;
                        dialogPane.hide();
                        window.newPlaylist();

                    }
                });

                Button button2 = new Button("Yes");
                button2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( ActionEvent k ) {
                        option=2;



                                Stage stage = new Stage();

                                DirectoryChooser directoryChooser = new DirectoryChooser();
                                File file = directoryChooser.showDialog(stage);
                                String libraryPath = file.getAbsolutePath();
                                MusicLibrary library = window.getLibrary();
                                library.getSettings().setLibraryPath(libraryPath);

                                GetAudioFiles getAudioFiles = new GetAudioFiles(window.getSystemInfo(),window.getLibrary(), window.getCurrentPlaylistPane().getPlaylist());
                        getAudioFiles.setAddSongsToCurrentPlayList(true);
                        getAudioFiles.setAddSongsToLibrary(true);

                        getAudioFiles.setMoveSongsToLibrary(false);


                        getAudioFiles.loadFiles(file.getPath(), true);

                                dialogPane.hide();









                    }
                });
                VBox vbox = new VBox(new Text("No Main Music Library Folder Set Would You Like to Set One?"), button, button2, currentFile);
                vbox.setMinSize(300,300);
                vbox.setStyle("   -fx-padding: 15; -fx-spacing: 10;" );
                dialogPane.setScene(new Scene(vbox));
                dialogPane.showAndWait();

}



}
