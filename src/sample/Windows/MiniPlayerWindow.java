package sample.Windows;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import sample.Library.*;
import sample.MainAudioWindow;
import sample.MusicPlayers.MusicPlayer;
import sample.MusicPlayers.PlayMode;
import sample.MusicPlayers.PlayerState;
import sample.PlayerScene;
import sample.PlayerWindow;
import sample.PlaylistPanes.PlaylistPane;
import sample.Utilities.AudioFileUtilities;
import sample.Utilities.FormatTime;
import sample.Utilities.SystemInfo;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class MiniPlayerWindow implements PlayerWindow {
    Stage stage= new Stage();
        protected MusicPlayer musicPlayer;// the music player class
        protected Button play; // buttons for playing pausing stopping ECT.
        protected Button pause;
        protected Button stop;
        protected Button mute;
        protected Button skipSong;
        protected Button rewindSongToStart;
        protected PlayMode playMode=PlayMode.REPEAT_PLAYLIST; // the current  playmode
        protected SystemInfo systemInfo;
        protected VBox mainBox= new VBox();
        protected Notifications notifications = new Notifications();
        protected MusicLibrary library;
        protected Settings settings;
        protected AudioInformation currentSong;// the current song
        protected PlaylistPane currentPlayListPane;// current playlist pane  showing song information
        protected ImageView albumImage= new ImageView();// the image view for album image;
        protected TextFlow currentTrackLabel; // labes for the current track / song information
        protected TextFlow currentArtistLabel;
        protected TextFlow currentAlbumLabel;
        protected Slider volumeControl; // volume control slider
        protected Label volumeLabel; // labes to show pand and volume
        protected float volume=0; // the volume number in DB's  Max= 6DB  Min= -80DB
        protected int startTime; // the start time for song playback in trackTimerSeconds
        protected boolean muted; // boolean for muting
        protected String artist="";
        protected File audioFile; // the current  audio file
        protected int msCounter;// counter for parts of a second for the track timer for playimng songs
        private  int trackLength; // the current track /song length in trackTimerSeconds
        private  int trackTimerSeconds; // trackTimerSeconds for the timer
        private  String totalTimeString="";
        private SimpleStringProperty trackTimeString= new SimpleStringProperty("0 / 0");
        protected MainAudioWindow mainAudioWindow;
        protected Slider trackSlider;
        public MiniPlayerWindow(MainAudioWindow mainAudioWindow) {
            stage.initStyle(StageStyle.DECORATED);
            this.mainAudioWindow=mainAudioWindow;
            this.musicPlayer= mainAudioWindow.getMusicPlayer();
            this.library= mainAudioWindow.getLibrary();
            this.currentPlayListPane= mainAudioWindow.getCurrentPlaylistPane();
            this.systemInfo= mainAudioWindow.getSystemInfo();
            this.playMode= mainAudioWindow.getPlayMode();
            stage.setOnCloseRequest(event -> {
            });
        }
        public void mute(){
            muted=true;
            if(musicPlayer!=null){
                musicPlayer.mute();
                Image image = new Image("/mute.png");
                ImageView imageView = new ImageView(image);
                mute.setGraphic(imageView);
            }
        }
        public void unMute(){
            muted=false;
            if(musicPlayer!=null) {
                musicPlayer.unMute();
                Image image = new Image("/unmute.png");
                ImageView imageView = new ImageView(image);
                mute.setGraphic(imageView);
            }
        }
        public void makeButtons(){
            mute= new Button();
            mute.setPrefSize(20,20);
            mute.setMinSize(20,20);
            mute.setMaxSize(20,20);
            //  sets the current playlist pane when a tab is changed
            // make the buttons
            Image image = new Image("/unmute.png");
            ImageView imageView = new ImageView(image);
            mute.setGraphic(imageView);
            mute.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(muted==true){
                        unMute();
                    }
                    else{
                        mute();
                    }
                }
            });
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            stop= new Button();
            image = new Image("/stop.png");
            imageView = new ImageView(image);
            stop.setGraphic(imageView);
            stop.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    trackTimerSeconds =0;
                    startTime=0;
                    stopPlay();
                }
            });
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            rewindSongToStart= new Button();
            image = new Image("/seekBack.png");
            imageView = new ImageView(image);
            rewindSongToStart.setGraphic(imageView);
            rewindSongToStart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    seekPlay(0);
                }
            });
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            skipSong= new Button();
            image = new Image("/skip.png");
            imageView = new ImageView(image);
            skipSong.setGraphic(imageView);
            skipSong.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    skipSong();
                }
            });
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            pause= new Button();
            image = new Image("/pause.png");
            imageView = new ImageView(image);
            pause.setGraphic(imageView);
            pause.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pause();
                }
            });
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            play= new Button();
            image = new Image("/play.png");
            imageView = new ImageView(image);
            play.setGraphic(imageView);
            play.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    play();
                }
            });
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            // makes the volume control slider that simply sets the volume for the players and updates the volume label when changed
            volumeControl= new Slider(-80,6,.1f);
          volumeControl.valueProperty().bindBidirectional(mainAudioWindow.getVolumeControl().valueProperty());
            volumeLabel=new Label(volume+"DB");
            volumeLabel.textProperty().bind(mainAudioWindow.getVolumeLabel().textProperty());
            // makes the pan control slider that simply sets the speaker left right pan  for the players and updates the pan label when changed
            albumImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Stage stage= new Stage();
                    ImageView view= new ImageView(albumImage.getImage());
                    Pane pane= new Pane(view);
                    Scene scene= new Scene(pane);
                    stage.setScene(scene);
                    stage.show();
                }
            });
        }
        private void skipSong() {
            musicPlayer.stop();
            musicPlayer.close();
            playNext();
        }
        public void setCurrentImage(String path){
            try{
                if(path!=null && path.isEmpty()==false) {
                    Image image = new Image("file:"+path);
                    albumImage.setImage(image);
                }
                else{
                    Image image=  new Image("/noAlbumCover.jpg");
                    albumImage.setImage(image);
                }
            }
            catch (IllegalArgumentException e){
            }
        }
        public  void displayWindow() {// ui code for showing the window with the playlist pane  and buttons for playing music and slider for seeking music
            makeButtons();
            HBox header = new HBox();
            VBox controls = new VBox();
            HBox volumeBox = new HBox();
            Label volume = new Label("Volume");
            volumeBox.getChildren().add(volume);
            volumeBox.getChildren().add(volumeControl);
            volumeBox.getChildren().add(mute);
            controls.getChildren().add(volumeBox);
            controls.getChildren().add(volumeLabel);
            header.getChildren().add(controls);
            header.setSpacing(5);
            Text text = new Text("Track: ");
            text.getStyleClass().add("titleText");
            Text text2 = new Text("Artist: ");
            text2.getStyleClass().add("titleText");
            Text text3 = new Text("Album: ");
            text3.getStyleClass().add("titleText");
            Text trackText = new Text("");
            Text artistText = new Text("");
            Text albumText = new Text("");
            currentTrackLabel = new TextFlow(text, trackText);
            currentArtistLabel = new TextFlow(text2, artistText);
            currentAlbumLabel = new TextFlow(text3, albumText);
            trackSlider= new TrackSlider(mainAudioWindow);
            VBox playing = new VBox();
            Label playingL = new Label(" Currently Playing");
            playingL.getStyleClass().add("currentlyPlaying");
            playing.getChildren().add(playingL);
            artistText.textProperty().bind(mainAudioWindow.getCurrentArtist());
            albumText.textProperty().bind(mainAudioWindow.getCurrentAlbum());
            trackText.textProperty().bind(mainAudioWindow.getCurrentTrackTitle());
            TrackSlider mainTrackSlider=mainAudioWindow.getTrackSlider();

            // bind values to main window ,track slider
            trackSlider.valueProperty().bindBidirectional(mainTrackSlider.valueProperty());
            trackSlider.maxProperty().bind(mainTrackSlider.maxProperty());
            trackSlider.minorTickCountProperty().bind(mainTrackSlider.minorTickCountProperty());
            trackSlider.blockIncrementProperty().bind(mainTrackSlider.blockIncrementProperty());
            trackSlider.majorTickUnitProperty().bind(mainTrackSlider.majorTickUnitProperty());
            playing.getChildren().add(currentArtistLabel);
            playing.getChildren().add(currentAlbumLabel);
            playing.getChildren().add(currentTrackLabel);
            VBox albumnImageBox = new VBox();
            albumImage.setFitHeight(50);
            albumImage.setFitWidth(50);
            albumnImageBox.getChildren().add(albumImage);
            albumImage.imageProperty().bind(mainAudioWindow.getAlbumImage().imageProperty());
            HBox imageSelectButtons = new HBox();
            albumnImageBox.getChildren().add(imageSelectButtons);
            HBox buttons = new HBox();
            
            buttons.getChildren().add(stop);
            buttons.getChildren().add(pause);
            buttons.getChildren().add(play);
            buttons.getChildren().add(rewindSongToStart);
            buttons.getChildren().add(skipSong);
            Label trackTimeLabel = new Label();
            HBox trackTimeBox=new HBox(trackTimeLabel, trackSlider);
            trackTimeLabel.textProperty().bind(mainAudioWindow.getTrackTimeString());
            mainBox.getStylesheets().add("css/mainAudioWindow.css");
            mainBox.getChildren().add(header);
            mainBox.setSpacing(5);
            mainBox.getChildren().add(trackTimeBox);
            mainBox.getChildren().add(buttons);
            HBox songInfo= new HBox(playing, albumnImageBox);
            mainBox.getChildren().add(songInfo);
            
            stage.setScene(new PlayerScene(this, mainBox));
            stage.show();
        }
        public void increaseVolume(){
            volume= (float) (volumeControl.getValue()+.5f);
            volumeControl.setValue(volume);
           mainAudioWindow.setVolume(volume);
        }
        public void decreaseVolume(){
            volume= (float) (volumeControl.getValue()-.5f);
            volumeControl.setValue(volume);
            mainAudioWindow.setVolume(volume);
        }
        public void panLeft(){
            Slider panControl=mainAudioWindow.getPanControl();
            panControl.setValue(panControl.getValue()-.1f);
        }
        public void panRight(){
            Slider panControl=mainAudioWindow.getPanControl();
            panControl.setValue(panControl.getValue()+.1f);
        }
        public void panCenter() {
            Slider panControl=mainAudioWindow.getPanControl();
            panControl.setValue(0);
        }
        public void play(){ // play a  song method
          mainAudioWindow.play();
        }
      
        public  void playNext(){
          mainAudioWindow.playNext();
        }
        public void seekPlay(int time){// starts playing of the current song at spefcied time
           mainAudioWindow.seekPlay(time);
        }

    @Override
    public void reloadPlaylistTabs() {

    }

    public void stopPlay(){
           mainAudioWindow.stopPlay();
        }
        public void pause(){
           mainAudioWindow.pause();
        }
        public void fastForward(){
           mainAudioWindow.fastForward();
        }
        public void rewind(){
          mainAudioWindow.rewind();
        }
        public void setAudioFile(File file) {
           mainAudioWindow.setAudioFile(file);
        }
        public MusicLibrary getLibrary() {
            return library;
        }
        public void repeatSong() {
           mainAudioWindow.repeatSong();
        }
        public void stopSong() {
           mainAudioWindow.stopSong();
        }
        public boolean isMuted() {
            return muted;
        }
        public PlayerState getPlayerState(){
            return musicPlayer.getPlayerState();
        }
        public AnimationTimer getSongTimer() {
            return mainAudioWindow.getSongTimer();
        }
        public SystemInfo getSystemInfo() {
            return systemInfo;
        }
}
