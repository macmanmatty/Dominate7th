package sample.Windows;


import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sample.Library.AudioInformation;
import sample.Library.SmartPlaylist;
import sample.Utilities.AudioFileUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListView<T extends File> extends ListView {

   private  AudioFileUtilities audioFileUtilities;
   private  List<String> fileExtensions= new ArrayList<>();



    public FileListView(  List<String> fileExtensions) {
        this.fileExtensions = fileExtensions;
        this.audioFileUtilities=new AudioFileUtilities();
        setUp();
    }



    private void setUp(){



        getStylesheets().add("css/fileListView.css");
        setOnKeyPressed( new EventHandler<KeyEvent>() // new key handler for pressing delete to remove songs from playlist
        {
            @Override
            public void handle( final KeyEvent keyEvent )
            {
                final List<T> selectedItems = getSelectionModel().getSelectedItems();

                if ( selectedItems.size()>0) {

                        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                            getItems().removeAll(selectedItems);

                        }


                }
            }
        } );

       setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if  (db.hasFiles()) {
                    List<File> files=db.getFiles();
                    List<File> allFiles=audioFileUtilities.findFiles(files,fileExtensions );

                   getItems().addAll( allFiles);



                }
                event.consume();
            }
        });

    }

    public List<String> getFileExtensions() {
        return fileExtensions;
    }

    public void setFileExtensions(List<String> fileExtensions) {
        this.fileExtensions = fileExtensions;
    }
}
