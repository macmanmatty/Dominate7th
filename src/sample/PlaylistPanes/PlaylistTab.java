package sample.PlaylistPanes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import sample.MainAudioWindow;
import sample.Library.Playlist;
import sample.Library.SmartPlaylist;
import sample.Windows.PlaylistCreatorWindow;
public class PlaylistTab extends Tab{
    Playlist playlist;
    PlaylistPane pane;
    ContextMenu menu;
    MainAudioWindow window;
    Label playlistNameLabel;



    public PlaylistTab(String text, Playlist playList, PlaylistPane pane, MainAudioWindow window) {
        this.playlist=playList;
        this.pane=pane;
  menu= new ContextMenu();
        MenuItem item= new MenuItem("Delete PLaylist");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.deletePlaylist(playlist);
            }
        });
        MenuItem item3= new MenuItem("New  Playlist");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new PlaylistCreatorWindow(window).displayWindow();
            }
        });

        MenuItem editPlayList= new MenuItem("Edit  Playlist");
        editPlayList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new PlaylistCreatorWindow(window, playList).displayWindow();
            }
        });
        menu.getItems().add(item);
        menu.getItems().add(item3);
       playlistNameLabel =new Label(text);
        ImageView imageView = new ImageView();
        HBox tabBox= new HBox();
        HBox editBox= new HBox();
        if(playList instanceof SmartPlaylist){
            imageView.setImage( new Image("/smartPlaylist.png"));
            setStyle(" -fx-background-color: #4ff7ac;");
            menu.getItems().add(editPlayList);

        }
       else  if(playList.isCDPlaylist()){
            imageView.setImage(new Image("/cdPlaylist.png"));


        }

       else{
            menu.getItems().add(editPlayList);


        }
        this.playlist=playList;
        playList.setPlaylistTab(this);
        this.window=window;

        final TextField textField = new TextField();
        tabBox.getChildren().add(imageView);
        tabBox.getChildren().add(playlistNameLabel);
        imageView =new ImageView(imageView.getImage());

        editBox.getChildren().add(imageView);
        editBox.getChildren().add(textField);
       setGraphic(tabBox);

        tabBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && event.getButton()==MouseButton.PRIMARY) {
                    textField.setText(playlistNameLabel.getText());
                    setGraphic(editBox);
                    textField.selectAll();
                    textField.requestFocus();
                }
                else if(event.getButton() == MouseButton.SECONDARY) {
                    menu.show(getTab(), event.getScreenX(), event.getScreenY());
                }
        }});
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text=textField.getText();
                playlistNameLabel.setText(text);
                playList.setName(text);
                setGraphic(tabBox);
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    playlistNameLabel.setText(textField.getText());
                    setGraphic(tabBox);
                }
            }
        });
        setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                window.removePane(playlist.getTabNumber());
            }
        });
    }
    public Node getTab(){
        return getTabPane();
    }
    public Playlist getPlaylist() {
        return playlist;
    }
    public PlaylistPane getPlaylistPane() {
        return pane;
    }

    public  void setTitle(String title){

        playlistNameLabel.setText(title);
    }
}
