package sample.Windows;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class UpdateLabel  {

    Label label;



    public UpdateLabel(String text) {
        label= new Label(text);

    }

    public UpdateLabel() {
        label= new Label();

    }


    public UpdateLabel(Label label) {
        this.label = label;
    }


    public Label getLabel() {
        return label;
    }




    public void setText(String text){

        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                label.setText(text);
            }
        };
        Platform.runLater(runnable);
    }



}
