package sample.Windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Library.AudioInformation;

public class LyricsWindow {


    public void showLyrics(AudioInformation information){
        Stage lyricsWindow =new Stage();
        lyricsWindow.setTitle("Lyrics");
        Pane pane= new Pane();
        String lyrics= information.getLyrics();
        Label lyricsLabel;
        if(lyrics!=null && !(lyrics.isEmpty())) {
            lyricsLabel = new Label("Lyrics: " + lyrics);
        }
        else  {
            lyricsLabel= new Label(" This Song Has No Entered Lyrics");
        }
        pane.getChildren().add(lyricsLabel);
        Button button= new Button("Close");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lyricsWindow.close();
            }
        });
        pane.getChildren().add(button);
        lyricsWindow.setScene(new Scene(pane));
        lyricsWindow.show();


    }
}
