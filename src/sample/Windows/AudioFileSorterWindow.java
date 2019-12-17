package sample.Windows;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.AudioProcessors.AudioFileSorter;
import sample.AudioProcessors.FileProcessor;
import sample.Library.Settings;
import sample.MainAudioWindow;
import sample.Utilities.AudioFileUtilities;

import java.io.File;
public class AudioFileSorterWindow {
   private  DirectoryChooser directoryChooser= new DirectoryChooser();
   private Button chooseInputFiles= new Button();
    private   Button choooseOutputFiles= new Button();
    private  Button sortFiles= new Button();
    private String  inputPath;
    private  String  outputPath;
    private Label inputDirectory;
    private Label outputDirectory;
    private   CheckBox sortByFileType;
    private  CheckBox sortTypeByBitRate;
    private CheckBox copyFilesOnMoving;
    private AudioFileSorter audioFileSorter;
    private  int numberOfMusicFiles;
    private  volatile boolean sorted=true;
    private Label error= new Label();
    private UpdateLabel updateLabel = new UpdateLabel();
    private FileProgressBar progressBar= new FileProgressBar();
    private VBox mainBox;
    private FileProcessor audioProcess;

    private CheckBox copyDuplicateFiles;
    private CheckBox getAudioInformationFromMusicBrainz;
    private MainAudioWindow window;
    private VBox progressWindow= new VBox();

    Stage stage;
    public AudioFileSorterWindow(Stage stage, MainAudioWindow window) {
        this.stage = stage;
        stage.setTitle("Audio File Sorter");
        this.window=window;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(audioProcess!=null) {
                    audioProcess.setShowCompletedNotification(true);
                }
                stage.hide();
            }
        });
    }
    public void showWindow(){
        chooseInputFiles.setText("Select Folder To Sort ");
        choooseOutputFiles.setText("Select Output Folder");
        sortByFileType= new CheckBox("Sort Files By Audio File Type?");
        sortTypeByBitRate= new CheckBox("Sort File Type By Bit Rate?");
        copyDuplicateFiles= new CheckBox("Move Duplicate Files?");
        copyFilesOnMoving= new CheckBox("Copy Files Before Moving Them");
        getAudioInformationFromMusicBrainz= new CheckBox("Get Missing Tags From MusicBrainz Database?");
        sortFiles.setText("Sort Files");
        sortFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sort();
            }

                        
        });
        choooseOutputFiles.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( final ActionEvent e) {
                        File file = directoryChooser.showDialog(stage);
                        if (file != null) {
                            outputPath=file.getAbsolutePath();
                            outputDirectory.setText("Output Path: "+outputPath);
                        }
                    }
                });
        chooseInputFiles.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle( final ActionEvent e) {
                        File file = directoryChooser.showDialog(stage);
                        if (file != null) {
                            inputPath=file.getAbsolutePath();
                            inputDirectory.setText("Input Path: "+ inputPath);
                        }
                    }
                });
        inputDirectory= new Label( "Input Path: "+ inputPath);
        outputDirectory= new Label("Output Path: "+outputPath);
      mainBox= new VBox();
        mainBox.getChildren().add(inputDirectory);
        mainBox.getChildren().add(outputDirectory);
        mainBox.getChildren().add(sortByFileType);
        mainBox.getChildren().add(sortTypeByBitRate);
        mainBox.getChildren().add(copyDuplicateFiles);
        mainBox.getChildren().add(copyFilesOnMoving);
        mainBox.getChildren().add(getAudioInformationFromMusicBrainz);
        mainBox.getChildren().add(progressWindow);
        HBox buttonBox= new HBox();
        buttonBox.getChildren().add(chooseInputFiles);
        buttonBox.getChildren().add(choooseOutputFiles);
        buttonBox.getChildren().add(sortFiles);
        mainBox.getChildren().add(buttonBox);
        if(error!=null){
            mainBox.getChildren().add(error);
        }
        mainBox.setSpacing(7);

        stage.setScene(new Scene(mainBox));
        stage.show();

    }


    private void sort(){
        error.setText("");
        if(inputDirectory==null){
            error.setText("Input directory is Null");
            return;
        }
        if(outputDirectory==null){
            error.setText("output directory is Null");
            return;
        }



        audioFileSorter = new AudioFileSorter( window.getSystemInfo().getFileSeperator(),outputPath, sortByFileType.isSelected(), sortTypeByBitRate.isSelected(), copyDuplicateFiles.isSelected(), getAudioInformationFromMusicBrainz.isSelected(), copyFilesOnMoving.isSelected(), false);
        audioProcess= new FileProcessor(audioFileSorter, updateLabel, progressBar);
        Settings settings=window.getSettings();
        audioProcess.setShowCompletedNotification(settings.isShowNotifcationOnProcessComplete());
        ProgressWindow window= new ProgressWindow(audioProcess);
        progressWindow.getChildren().clear();
        progressWindow.getChildren().add(window.getWindow());
        stage.hide();// refresh stage so added ui  nodes  show
        stage.show();
        audioProcess.manipulateFiles(inputPath);
        sorted = true;




    }
}
