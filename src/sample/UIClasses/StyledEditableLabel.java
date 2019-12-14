package sample.UIClasses;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.jaudiotagger.tag.FieldKey;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.Library.AudioInformation;

import java.util.ArrayList;
import java.util.List;


public class StyledEditableLabel extends Label {
   private  String styleSheetPath;
    private String styleName;
    private TextField textField= new TextField();
    private FieldKey keyToSet;
    private List<AudioInformation>songs = new ArrayList<>();
   private  Label   graphicLabel= new Label();
    private String text;
   private  boolean editable;


    public StyledEditableLabel(String text, String styleSheetPath, String styleName,  boolean editable) {
        this.styleSheetPath = styleSheetPath;
        this.styleName = styleName;
        this.editable = editable;
        this.text=text;
    }

    public StyledEditableLabel(String text, String styleSheetPath, String styleName, FieldKey keyToSet, List<AudioInformation> songs, boolean editable) {
        this.text=text;
        this.songs=songs;
        this.keyToSet=keyToSet;
        this.styleName=styleName;
        this.styleSheetPath=styleSheetPath;
        this.editable=editable;
        setUp();


    }

    public StyledEditableLabel(String styleSheetPath, String styleName, FieldKey keyToSet, List<AudioInformation> songs, boolean editable) {
        this.styleSheetPath = styleSheetPath;
        this.styleName = styleName;
        this.keyToSet = keyToSet;
        this.songs = songs;
        this.editable=editable;

        setUp();
    }

    private void setUp(){

        getStyleClass().clear();
        getStyleClass().add(styleName);
        getStylesheets().add(styleSheetPath);
        graphicLabel.setText(text);
        setGraphic(graphicLabel);
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && event.getButton()== MouseButton.PRIMARY && editable==true) {
                    textField.setText(text);

                    setGraphic(textField);
                    textField.selectAll();
                    textField.requestFocus();
                }

            }});


        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text=textField.getText();
                graphicLabel.setText(text);
                setGraphic(graphicLabel);
                setTextToSongs(text);





            }
        });


        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    setText(textField.getText());
                    setGraphic(graphicLabel);
                }
            }
        });

    }



    public String getStyleSheetPath() {
        return styleSheetPath;
    }

    public void setStyleSheetPath(String styleSheetPath) {
        this.styleSheetPath = styleSheetPath;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

  public Label getLabel(){

        return this;
    }



    private void setTextToSongs(String text){
        int size=songs.size();
        ExtractAudioInformation extractAudioInformation= new ExtractAudioInformation();

        for(int count=0; count<size; count++){
            extractAudioInformation.setInformation(songs.get(count), keyToSet, text);



        }


    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}


