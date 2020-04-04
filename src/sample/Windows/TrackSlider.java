package sample.Windows;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import sample.MainAudioWindow;


public class TrackSlider extends Slider {

private MainAudioWindow window;

    public TrackSlider(MainAudioWindow window) {
        this.window=window;



        this.window = window;
        this.setMajorTickUnit(1);
        this.setMinorTickCount(10);
        this.setSnapToTicks(true);
        this.setBlockIncrement(1);
        this.setSnapToTicks(true);
        this.setShowTickMarks(true);

        // when the mouse is released seekPlay the current song at the given slider time
       setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                int time=valueProperty().intValue();
                window.seekPlay(time);
                window.setStartOffset(time);


            }

        });
        // also play  the song when mouse is clicked





    }



}
