package sample.Windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.Library.AudioInformation;
import sample.Library.CueSheets.CueSheet;
import sample.Library.Playlist;
import sample.Library.Settings;
import sample.MainAudioWindow;
import sample.Utilities.AudioFileUtilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddSongsFromCueSheetWindow {
   private Stage stage = new Stage();
   private  FileChooser chooser= new FileChooser();
   private  File audioFile;
   private  File cueSheet;
   private  Button addSongs;
   private  Button audioFilePath;
   private  Button cueSheetPath;
 private  Label cueSheetPathLabel=new Label();
  private Label audioFilePathLabel= new Label();
 private  CheckBox moveFilesToLibrary;
 private  MainAudioWindow window;
  private VBox mainBox= new VBox();
  private boolean addToPlaylist;
  private AudioFileUtilities audioFileUtilities;
  private ExtractAudioInformation extractAudioInformation= new ExtractAudioInformation();
  private String fileSeperator;



    public AddSongsFromCueSheetWindow(MainAudioWindow window, boolean addToPlaylist) {
        this.window = window;
        this.addToPlaylist = addToPlaylist;
        this.fileSeperator=window.getSystemInfo().getFileSeperator();
        audioFileUtilities= new AudioFileUtilities();

    }

    public void  displayWindow(){
        addSongs= new Button("Add Songs With Cue Sheet");
        addSongs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                getFilesFromCueSheet();
                
            }
        });
        
        audioFilePath=new Button("Select Audio File Path");
        audioFilePath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage= new Stage();
                FileChooser.ExtensionFilter cueFilter= new FileChooser.ExtensionFilter("Cue Sheets", "*.cue");
                chooser.setSelectedExtensionFilter(cueFilter);
                audioFile=chooser.showOpenDialog(stage);
                audioFilePathLabel.setText(audioFile.getAbsolutePath());
                
            }
        });
       cueSheetPath=new Button("Select Cue Sheet Path");


       cueSheetPath.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               Stage stage= new Stage();
               cueSheet=chooser.showOpenDialog(stage);
               cueSheetPathLabel.setText(cueSheet.getAbsolutePath());

           }
       });
       HBox cueSheetBox= new HBox(cueSheetPath, cueSheetPathLabel);

       HBox audioFileBox= new HBox(audioFilePath, audioFilePathLabel);
       mainBox.getChildren().add(cueSheetBox);
       mainBox.getChildren().add(audioFileBox);
       mainBox.getChildren().add(addSongs);
        mainBox.setSpacing(7);

        stage.setScene(new Scene(mainBox));
       stage.show();





   }


   public void getFilesFromCueSheet(){
       CueSheet sheet = null;
       try {
           sheet = extractAudioInformation.getCueSheet( cueSheet);
       } catch (CueSheetExeception cueSheetExeception) {
           cueSheetExeception.printStackTrace();
       }
       sheet.setAudioFilePath(audioFile.getAbsolutePath());
       List<AudioInformation> tracks=sheet.getTracks();
       Playlist playlist=window.getCurrentPlaylistPane().getPlaylist();
       if(addToPlaylist==true){

           playlist.addSongs(tracks);

       }
       if(playlist.getPlaylistSettings().isAddSongsToLibraryOnAdding() || addToPlaylist==false){
           window.getLibrary().addSongs(tracks);
       }
       if(tracks.size()>0) {

           String bitRate ="none";
           try {
               AudioFile audioFile2 = AudioFileIO.read(audioFile);
               bitRate = audioFile2.getAudioHeader().getBitRate();
           } catch (CannotReadException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (TagException e) {
               e.printStackTrace();
           } catch (ReadOnlyFileException e) {
               e.printStackTrace();
           } catch (InvalidAudioFrameException e) {
               e.printStackTrace();
           }
           Settings settings = window.getSettings();

           String movePath = settings.getLibraryPath();

           String artist = tracks.get(0).getArtist();
           movePath=movePath+fileSeperator+artist;
           if(settings.isSortLibraryByFileType()) {
               String kind = audioFileUtilities.getExtensionOfFile(audioFile);
               movePath=movePath+fileSeperator+kind;
           }
           if(settings.isSortLibraryByBitRate()) {
               movePath=movePath+fileSeperator+bitRate;
           }
           File directory = new File(movePath);


           if (settings.isMoveFilesToLibraryFolder()) {
               if (settings.isCopyFilesToLibraryFolder()) {

                   try {
                       audioFileUtilities.copyThenMoveFile(audioFile, directory);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   try {
                       audioFileUtilities.copyThenMoveFile(cueSheet, directory);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }


               } else {
                   try {
                       FileUtils.moveToDirectory(cueSheet, directory, true);

                       FileUtils.moveToDirectory(audioFile, directory, true);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }


           }
       }

   }
}
