package sample.Windows;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.AudioProcessors.LinkSongsToAlbumImages;
import sample.Library.Settings;
import sample.MainAudioWindow;
import java.io.File;
public class LinkSongsToAlbumImagesWindow {
  private   Stage stage= new Stage();
    private   File file;
    private   Button linkFiles;
    private  Button selectFiles;
    private  Label directoryLabel= new Label("Location To Link: ");
    private   Label finished= new Label();
    private UpdateLabel updateLabel= new UpdateLabel();
    private FileProgressBar progressBar= new FileProgressBar();
    private LinkSongsToAlbumImages linkSongsToAlbumImages;
   private  VBox mainBox;
   private MainAudioWindow window;
   private VBox progressBox= new VBox();

    public LinkSongsToAlbumImagesWindow(MainAudioWindow window) {
        this.window=window;
        stage.setTitle("Link Songs to Album Images");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(linkSongsToAlbumImages!=null) {
                    linkSongsToAlbumImages.setShowCompletedNotification(true);
                }
                stage.hide();
            }
        });
}
    public void displayWindow(){
    mainBox=  new VBox();
        linkFiles= new Button("Link Files");
        selectFiles= new Button("Select Files");
        selectFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage= new Stage();
                DirectoryChooser directoryChooser= new DirectoryChooser();
                file=directoryChooser.showDialog(stage);
                directoryLabel.setText("Location To Link: "+file.getAbsolutePath());

            }
        });
        linkFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               linkSongsToAlbumImages= new LinkSongsToAlbumImages(updateLabel, progressBar);
                Settings settings=window.getSettings();
                linkSongsToAlbumImages.setShowNotifications(settings.isShowProcessNotifications());
                linkSongsToAlbumImages.setShowCompletedNotification(settings.isShowNotifcationOnProcessComplete());
               ProgressWindow progressWindow= new ProgressWindow(linkSongsToAlbumImages);
                progressBox.getChildren().clear();
                progressBox.getChildren().add(progressWindow.getWindow());
                stage.hide();// refresh stage so added ui  nodes  show
                stage.show();
                linkSongsToAlbumImages.linkFiles(file);
            }
        });
        HBox buttons= new HBox(selectFiles, linkFiles);
                mainBox.getChildren().add(directoryLabel);
                mainBox.getChildren().add(buttons);
                mainBox.getChildren().add(finished);
                mainBox.getChildren().add(progressBox);
                mainBox.setMinSize(300,300);
        mainBox.setMaxSize(300,300);
        mainBox.setPrefSize(300,300);
        stage.setScene(new Scene(mainBox));
                stage.show();

    }
}
