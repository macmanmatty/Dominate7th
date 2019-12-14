package sample.Windows;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.Pane;
import sample.AudioProcessors.GetAudioFiles;
import sample.Library.PlaylistSettings;
import sample.Library.Settings;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileDragPane extends Pane {
   private  Label label= new Label("Drop Files Here");
    private List<File> files= new ArrayList<>();

    public FileDragPane() {
    }


    public void setUp(){
        getChildren().add(label);
        getStylesheets().add("css/dragPane.css");
        setOnDragEntered(new EventHandler<DragEvent>() { // drag listener for getting files

            @Override
            public void handle(DragEvent event) {

                Dragboard db = event.getDragboard();
                if  (db.hasFiles()) {
                     files=db.getFiles();


                }
                event.consume();
            }
        });    }


    public List<File> getFiles() {
        return files;
    }


}
