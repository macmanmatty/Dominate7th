package sample.Windows;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CenteredLabel extends HBox {
    Label label = new Label();

    public CenteredLabel(String text) {
        label= new Label(text);
        getChildren().add(label);
        getStylesheets().add("/css/centeredLabel.css");

    }

    public CenteredLabel() {
        getChildren().add(label);
        getStylesheets().add("/css/centeredLabel.css");

    }

    public void setText(String text){
        label.setText(text);

    }

    public  void setGraphic(Node icon){
        label.setGraphic(icon);

    }
}
