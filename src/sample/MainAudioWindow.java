package sample;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.stage.WindowEvent;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import sample.InputDevices.DeviceInputThread;
import sample.PlaylistPanes.CDPlaylistPane;
import sample.Utilities.AudioFileUtilities;
import sample.Library.*;
import sample.MusicPlayers.MusicPlayer;
import sample.MusicPlayers.PlayMode;
import sample.MusicPlayers.PlayerState;
import sample.PlaylistPanes.PlaylistTab;
import sample.PlaylistPanes.PlaylistPane;
import sample.PlaylistPanes.TablePlaylistPane;
import sample.Utilities.FormatTime;
import sample.Utilities.SystemInfo;
import sample.Windows.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainAudioWindow  implements  PlayerWindow{
   protected MusicPlayer musicPlayer;// the music player class
   protected boolean audioFileChanged;
  protected   Stage stage; // the main stage
  protected String audioFileExtension;
  protected Button play; // buttons for playing pausing stopping ECT.
  protected Button pause;
  protected Button stop;
  protected Button fastForward;
  protected Button rewind;
  protected Button mute;
  protected Button skipSong;
  protected Button rewindSongToStart;
    protected Button songMode;
    protected Button nextImage;
    protected Button previousImage;
  protected boolean newSongPlay;
  protected PlayMode playMode=PlayMode.REPEAT_PLAYLIST; // the current  playmode
  protected PlayMode[] playModes=PlayMode.values();
  protected int playModeNumber=3; // playmode numbers
    protected SystemInfo systemInfo;
    protected VBox mainBox= new VBox();
    protected Notifications notifications = new Notifications();
    protected int currentPlaylistNumber;
    protected AudioFileUtilities audioFileUtilities;
   protected  TrackSlider trackSlider; // the seek bar that moves with songs time
  protected MusicLibrary library = new MusicLibrary() ; // the music library
  protected LibraryLoader loader; // the loader class for library to seralize and deseralize it from json
  protected Settings settings= new Settings();
  protected TabPane tabPane= new TabPane();// the tabe pane  for all of the playlists;
  protected AudioInformation currentSong;// the current song
  protected PlaylistPane currentPlaylistPane;// current playlist pane  showing song information
  protected ImageView albumImage= new ImageView();// the image view for album image;
    protected List<Image> albumArtworkImages=  new ArrayList<>();
    protected ArrayList<PlaylistPane> panes= new ArrayList<>(); // all of the playlist panes
    protected TextFlow currentTrackLabel; // labes for the current track / song information
    protected TextFlow currentArtistLabel;
    protected TextFlow currentAlbumLabel;
    protected SimpleStringProperty currentArtist = new SimpleStringProperty("");
    protected SimpleStringProperty currentTrackTitle = new SimpleStringProperty("");
    protected SimpleStringProperty currentAlbum= new SimpleStringProperty("");
    protected Slider volumeControl; // volume control slider
    protected Slider panControl;// pan conttrol slider
    protected Label volumeLabel; // labes to show pand and volume
    protected Label panLabel;
    protected float volume=0; // the volume number in DB's  Max= 6DB  Min= -80DB
    protected float pan=0;  // left right speaaker pan number  -1 = 100% left 0= center 1= 100% right
    protected int startTime; // the start time for song playback in trackTimerSeconds
    protected boolean muted; // boolean for muting
    protected int currentImageNumber; // current album image number being displayed
    protected int numberOfImages; // number fo alums images  for the current song
    protected String artist="";
  protected TopMenuBar menuBar; // the top menu bar  with drop down menus
   protected File audioFile; // the current  audio file
    protected int msCounter;// counter for parts of a second for the track timer for playimng songs
    protected AnimationTimer songTimer= songTimer=new AnimationTimer() { // the track timer to display lasped time for the current playing song
        @Override
        public void handle(long now) {
            // timer runs at about 60 fps  so 60 calls of the method equals about 1 second
            if(msCounter !=60) { // one second has not passed return
                msCounter++;
                return;
            }
            else{ // one second has passed up date timer
                msCounter =0;
                // update  the time and corrosponding label
                trackTimerSeconds++;
                String currentTimeString= FormatTime.formatTimeWithColons(trackTimerSeconds);
                trackTimeString.set(currentTimeString + "/ "+totalTimeString);
                trackSlider.setValue(trackTimerSeconds);
            }
        }
    };
   private  int trackLength; // the current track /song length in trackTimerSeconds
   private  int trackTimerSeconds; // trackTimerSeconds for the timer
    private int cueTrackStart;// when the tracks starts used for songs with cue sheets
   private  String totalTimeString="";
    private SimpleStringProperty trackTimeString= new SimpleStringProperty("0 / 0");
    protected boolean notifyOnPlayStart;
    protected DeviceInputThread deviceInputThread;
    protected int startOffset;// startOffset seeking play
    MainScene mainScene;
    private boolean findFile;

    public MainAudioWindow(Stage stage, MusicLibrary library) {
        this.stage = stage;
        stage.initStyle(StageStyle.DECORATED);
        loader= new LibraryLoader( SystemInfo.getUserHomePath(),SystemInfo.getFileSeperator());
        this.library=library;
        this.systemInfo = systemInfo;
        audioFileUtilities=new AudioFileUtilities();
        // save library changes on closing
        stage.setOnCloseRequest(event -> {
            closeApp();
        });
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(settings.isShowMiniPlayerOnMainWindowClose()){
                    new MiniPlayerWindow(getAudioWindow()).displayWindow();
                }
            }
        });

        findFile=getLibrary().getSettings().isAskToFindMissingFilesForSongs();


    }
    public void closeApp(){
        if(musicPlayer!=null ){
            PlayerState state=musicPlayer.getPlayerState();
            if(state== PlayerState.PLAYING){ // stop music playback
                musicPlayer.stop();
                musicPlayer.close();
            }
            deviceInputThread.stop();//  stop input device polling
        }
        if(settings.isSaveLibraryOnExit()==true){
            loader.saveLibrary(library);// save library
        }
        Platform.exit(); // exit platform and close app
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
        tabPane.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> old,
                                        Tab oldTab, Tab newTab) {
                        if (newTab != null) {
                            PlaylistPane pane = ((PlaylistTab) newTab).getPlaylistPane();
                            setCurrentPlaylistPane(pane);
                        }
                    }
                });
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
        fastForward= new Button();
        image = new Image("/fastforward.png");
        imageView = new ImageView(image);
        fastForward.setGraphic(imageView);
        fastForward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            fastForward();
            }
        });
        rewind= new Button();
        image = new Image("/rewind.png");
        imageView = new ImageView(image);
        rewind.setGraphic(imageView);
        rewind.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rewind();
            }
        });
        nextImage=new Button();
        image = new Image("/imageNext.png");
        imageView = new ImageView(image);
        nextImage.setGraphic(imageView);
        nextImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(numberOfImages>0) {
                    currentImageNumber++;
                    if (currentImageNumber > numberOfImages-1) {
                        currentImageNumber = 0;
                    }
                        setCurrentImage();
                }
                else{
                    setCurrentImage("/noAlbumCover.jpg");
                }
            }
        });
        previousImage=new Button();
        image = new Image("/imagePrevious.png");
        imageView = new ImageView(image);
        previousImage.setGraphic(imageView);
        previousImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(numberOfImages>0) {
                    currentImageNumber--;
                    if (currentImageNumber < 0) {
                        currentImageNumber = numberOfImages - 1;
                    }
                        setCurrentImage();
                }
                else{
                    setCurrentImage("/noAlbumCover.jpg");
                }
            }
        });
        // makes the volume control slider that simply sets the volume for the players and updates the volume label when changed
        volumeControl= new Slider(-80,6,.1f);
        volumeControl.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volume=newValue.floatValue();
                if(musicPlayer!=null){
                    musicPlayer.setVolume(volume);
                }
                volumeLabel.setText(volume+" DB");
            }
        });
        volumeLabel=new Label(volume+"DB");
        // makes the pan control slider that simply sets the speaker left right pan  for the players and updates the pan label when changed
        panControl= new Slider(-1,1,.01f);
        panControl.setValue(0);
        panControl.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pan=newValue.floatValue();
                setVolume(pan);
                if(musicPlayer!=null){
                    musicPlayer.setPan(newValue.floatValue());
                }
                setPanLabel();
            }
        });
        panLabel= new Label();
        setPanLabel();
        // makes the track slider
        trackSlider= new TrackSlider(this);
                songMode = new Button();
        image = new Image("/repeatPlaylist.png");
        imageView = new ImageView(image);
        songMode.setGraphic(imageView);
        songMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changePlayMode();
            }
        });
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
    public void setCurrentImage(){
            albumImage.setImage(albumArtworkImages.get(currentImageNumber));
    }
    public void changePlayMode(){
        playModeNumber++;
        Image image= null;
        ImageView imageView;
        // changes the play mode for the music player and updates the button Image to show the mode
        if(playModeNumber>2){
            playModeNumber=0;
        }
        switch (playModeNumber){
            case  0:{
                playMode=playModes[0];
                musicPlayer.setPlayMode(playMode);
                image = new Image("/stopSong.png");
                imageView = new ImageView(image);
                songMode.setGraphic(imageView);
                break;
            }
            case 1:{
                playMode=playModes[1];
                musicPlayer.setPlayMode(playMode);
                image = new Image("/repeatSong.png");
                imageView = new ImageView(image);
                songMode.setGraphic(imageView);
                break;
            }
            case 2:{
                playMode=playModes[2];
                musicPlayer.setPlayMode(playMode);
                image = new Image("/repeatPlaylist.png");
                imageView = new ImageView(image);
                songMode.setGraphic(imageView);
                break;
            }
        }
    }
    private void setPanLabel() {
        if(pan<0) {
            panLabel.setText(Math.abs(pan) + "Left");
        }
       else  if(pan>0){
            panLabel.setText(Math.abs(pan) + "Right");
        }
        else{
            panLabel.setText("Center");
        }
    }
    public  void loadPlaylistPanes(){ // loads the music library
       List<Playlist> playlists=library.getAllPlayLists();
        int size=playlists.size();
        for(int count=0; count<size; count++){
            Playlist playlist=playlists.get(count);
            String name=playlist.getName();
            if(name==null|| name.isEmpty()){
                name="Playlist "+count;
                playlist.setName(name);
            }
            playlist.setTabNumber(count);
            PlaylistPane pane= new TablePlaylistPane(playlist,this);
                    pane= new TablePlaylistPane(playlist, this);
                   pane.makePane();
            Tab tab= new PlaylistTab(name, playlist, pane, this);
            tab.setContent(pane.getPlaylistWindow());
            tabPane.getTabs().add(tab);
            panes.add(pane);
        }
        currentPlaylistNumber=library.getPlaylistNumber();
        if(panes.size()>0) {
            setCurrentPlaylistPane(panes.get(0));
        }
        this.settings=library.getSettings();
        notifyOnPlayStart=settings.isShowNotificationsOnSongChange();
    }
    public Playlist newPlaylist(){// creates a  new regular  table play list and tab for it in the tab pane
        Playlist playlist=library.newPlayList();
        playlist.setName("PlayList"+panes.size());
        playlist.setTabNumber(tabPane.getTabs().size()-1);
        TablePlaylistPane pane= new TablePlaylistPane(playlist, this);
        pane.makePane();
        Tab tab= new PlaylistTab(playlist.getName() , playlist, pane, this);
        panes.add(pane);
        tab.setContent(pane.getPlaylistWindow());
        tabPane.getTabs().add(tab);
        return playlist;
    }
    public void refreshStage(){
        stage.hide();
        stage.show();
    }
    public void  updateTab(Playlist playlist){
        Tab tab= playlist.getPlaylistTab();
        panes.set(playlist.getTabNumber(),playlist.getPane());
        tab.setContent(playlist.getPane().getPlaylistWindow());
       refreshStage();
    }
    public void addPlaylist(Playlist playlist){ // adds a play list to the  music library  and  new tab  on the tab pane for it
        if(playlist instanceof SmartPlaylist){
            library.getSmartPlaylists().add((SmartPlaylist) playlist);
        }
        else if(playlist.isCDPlaylist()==false){
            library.getRegularPlaylists().add(playlist);
        }
        playlist.setTabNumber(tabPane.getTabs().size()-1);
        TablePlaylistPane pane;
        if(playlist.isCDPlaylist()){
            pane=new CDPlaylistPane(playlist, this);
        }
        else {
            pane = new TablePlaylistPane(playlist, this);
        }
        pane.makePane();
        panes.add(pane);
        Tab tab= new PlaylistTab(playlist.getName() , playlist, pane, this);
        tab.setContent(pane.getPlaylistWindow());
        tabPane.getTabs().add(tab);
        stage.hide();
        stage.show();
    }
    public void deletePlaylist(Playlist playlist){ // // removes  a play list from the  music library  and  it's corrosponding  tab  on the tab pane for it
        if(playlist instanceof SmartPlaylist){
            library.getSmartPlaylists().remove(playlist);
        }
        else {
            library.getRegularPlaylists().remove(playlist);
        }
        tabPane.getTabs().remove(playlist.getPlaylistTab());
        }
   public  void displayWindow() {// ui code for showing the window with the playlist pane  and buttons for playing music and slider for seeking music
       makeButtons();
        loadPlaylistPanes();
       musicPlayer = new MusicPlayer(this);
       menuBar = new TopMenuBar(this);
       HBox header = new HBox();
       VBox controls = new VBox();
       HBox volumeBox = new HBox();
       Label volume = new Label("Volume");
       volumeBox.getChildren().add(volume);
       volumeBox.getChildren().add(volumeControl);
       volumeBox.getChildren().add(mute);
       HBox panBox = new HBox();
       Label pan = new Label("Left Right Speaker Pan");
       panBox.getChildren().add(pan);
       panBox.getChildren().add(panControl);
       controls.getChildren().add(volumeBox);
       controls.getChildren().add(volumeLabel);
       controls.getChildren().add(panBox);
       controls.getChildren().add(panLabel);
       header.getChildren().add(controls);
       header.setSpacing(5);
       Text text = new Text("Track: ");
       text.getStyleClass().add("titleText");
       Text text2 = new Text("Artist: ");
       text2.getStyleClass().add("titleText");
       Text text3 = new Text("Album: ");
       text3.getStyleClass().add("titleText");
       Text text4 = new Text("");
       Text text5 = new Text("");
       Text text6 = new Text("");
       currentTrackLabel = new TextFlow(text, text4);
       currentArtistLabel = new TextFlow(text2, text5);
       currentAlbumLabel = new TextFlow(text3, text6);
       VBox menuBar = new VBox(this.menuBar.getMenuBar());
       VBox playing = new VBox();
       Label playingL = new Label(" Currently Playing");
       playingL.getStyleClass().add("currentlyPlaying");
       playing.getChildren().add(playingL);
       playing.getChildren().add(currentArtistLabel);
       playing.getChildren().add(currentAlbumLabel);
       playing.getChildren().add(currentTrackLabel);
       VBox albumnImageBox = new VBox();
       albumImage.setFitHeight(75);
       albumImage.setFitWidth(75);
       albumnImageBox.getChildren().add(albumImage);
       HBox imageSelectButtons = new HBox();
       imageSelectButtons.getChildren().add(previousImage);
       imageSelectButtons.getChildren().add(nextImage);
       albumnImageBox.getChildren().add(imageSelectButtons);
       HBox buttons = new HBox();
       buttons.getChildren().add(playing);
       buttons.getChildren().add(albumnImageBox);
       buttons.getChildren().add(stop);
       buttons.getChildren().add(pause);
       buttons.getChildren().add(play);
       buttons.getChildren().add(rewindSongToStart);
       buttons.getChildren().add(skipSong);
       buttons.getChildren().add(rewind);
       buttons.getChildren().add(fastForward);
       Label trackTimeLabel = new Label();
       trackTimeLabel.textProperty().bind(trackTimeString);
       buttons.getChildren().add(songMode);
       mainBox.getStylesheets().add("css/mainAudioWindow.css");
       mainBox.getChildren().add(menuBar);
       mainBox.getChildren().add(header);
       mainBox.getChildren().add(tabPane);
       mainBox.setSpacing(5);
       if (currentPlaylistPane != null) {
           setCurrentPlaylistPane(currentPlaylistPane);
           mainBox.getChildren().add(currentPlaylistPane.getPlaylistWindow());
       }
       HBox trackTime = new HBox(trackSlider, trackTimeLabel);
       VBox trackSliderBox = new VBox(trackTime);
       trackTime.setSpacing(15);
       mainBox.getChildren().add(trackSliderBox);
       mainBox.getChildren().add(buttons);
       mainScene= new MainScene(this, mainBox);
       stage.setScene(mainScene);
       stage.show();
       libraryFolderCheck();
       deviceInputThread = new DeviceInputThread(this);
       deviceInputThread.start();
   }
    public  void reloadPlaylistTabs(){// ui code for showing the window with the playlist pane  and buttons for playing music and slider for seeking music
        tabPane.getTabs().clear();
        panes.clear();
        List<Playlist> playlists=library.getAllPlayLists();
        int size=playlists.size();
        for(int count=0; count<size; count++) {
            Playlist playlist = playlists.get(count);
            String name = playlist.getName();
            if (name == null || name.isEmpty()) {
                name = "Playlist " + count;
                playlist.setName(name);
            }
            tabPane.getTabs().add(playlist.getPlaylistTab());
            panes.add(playlist.getPane());
        }
        List<Playlist> devicePlaylists=deviceInputThread.getPlayLists();
        size=devicePlaylists.size();
        for(int count=0; count<size; count++){
            Playlist playlist =devicePlaylists.get(count);
            String name = playlist.getName();
            if (name == null || name.isEmpty()) {
                name = "Playlist " + count;
                playlist.setName(name);
            }
            tabPane.getTabs().add(playlist.getPlaylistTab());
            panes.add(playlist.getPane());        }
        }
    @Override
    public int getTime() {
        return trackTimerSeconds;
    }
    public void libraryFolderCheck(){
       String libraryPath=library.getSettings().getLibraryPath();
       if(libraryPath==null || libraryPath.isEmpty()){
           new OptionPane().showSetLibraryFolderPane(this);
       }
       else {
           File libraryFolder = new File(libraryPath);
           if (libraryFolder.exists() == false) {
               new OptionPane().showSetLibraryFolderPane(this);
           }
       }
   }
   public void increaseVolume(){
        volumeControl.setValue(volumeControl.getValue()+.5f);
   }
    public void decreaseVolume(){
        volumeControl.setValue(volumeControl.getValue()-.5f);
    }
    public void panLeft(){
        panControl.setValue(panControl.getValue()-.1f);
    }
    public void panRight(){
        panControl.setValue(panControl.getValue()+.1f);
    }
    public void panCenter() {
        panControl.setValue(0);
    }
    public void play(){ // play a  song method
newSongPlay=false;
        if(audioFile!=null && audioFile.exists()) {
                PlayerState player=musicPlayer.getPlayerState();
                switch (player){
                    case PAUSED:
                        if(audioFileChanged==false) {// if file hasn't changed use same player
                            musicPlayer.resume();
                            songTimer.start();
                        }
                        else{
                            startTime=0;
                            startPlay(); // create a new player in music player class
                        }
                        case PLAYING:
                            if(audioFileChanged==true  ) {
                                startTime = 0;
                                trackTimerSeconds = 0;
                                musicPlayer.stop(); // stop current song
                                musicPlayer.close(); // close current player
                                startPlay(); // create new player in music player class
                            }
                        break;
                    case STOPPED:
                        songTimer.stop();
                        break;
                    case FINISHED:
                       startPlay(); // start play again
                        break;
                    case NOTSTARTED:
                        startPlay();
                        break;
                }
        }
   }
   public void startPlay(){
        if(audioFile!=null) {
            audioFileChanged = false;
            musicPlayer.setPlayerState(PlayerState.NOTSTARTED);
            if (notifyOnPlayStart == true) {
                notifications.showSongStartNotification(currentSong);
            }
            currentImageNumber = 0;
            setCurrentAlbum(currentSong.getAlbum());
            setCurrentArtist(currentSong.getArtist());
            setCurrentTrackTitle(currentSong.getTitle());
            getCurrentImages(currentSong);
            trackLength = (int) currentSong.getTrackLengthNumber();
            cueTrackStart = currentSong.getCueStart();
            //musicPlayer.setStartOffset(trackStart);
            totalTimeString = FormatTime.formatTimeWithColons(trackLength);
            musicPlayer.setStart(cueTrackStart);
            musicPlayer.setEnd(trackLength);
            musicPlayer.setVolume(volume);
            musicPlayer.setStartOffset(startOffset);
            trackSlider.setMin(0);
            trackSlider.setMax(trackLength);
            double sliderWidth = trackLength * 3;
            if (sliderWidth > mainBox.getWidth() - 60) {
                sliderWidth = mainBox.getWidth() - 60;
            }
            trackSlider.setMinWidth(sliderWidth);
            trackSlider.setPrefWidth(sliderWidth);
            trackSlider.setMaxWidth(sliderWidth);
            trackSlider.setValue(startOffset);
            musicPlayer.play(audioFile);
            newSongPlay = true;
            trackTimerSeconds = startOffset;
   }
   }
   public  void playNext(){
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                trackSlider.setValue(0);
                startOffset=0;
                songTimer.stop();
                startTime=0;
                trackTimerSeconds =0;
                getAudioFileToPlay();

                musicPlayer.stop();
                musicPlayer.close();
                startPlay();
            }
        };
       Platform.runLater(runnable);
   }

   private void getAudioFileToPlay(){
       currentSong= currentPlaylistPane.getNextSong();

       audioFile=currentSong.getPhysicalFile();
       System.out.println(audioFile + " "+findFile);
       boolean removeSongsWithMissingFiles=library.getSettings().isRemoveSongsWithMissingFilesOnDiscovery();
       if(audioFile==null && findFile==true && removeSongsWithMissingFiles==false ){
                   new OptionPane().showLocateFilePane(currentSong);

       }


       else {
           if(removeSongsWithMissingFiles){
               library.getAllSongs().remove(currentSong);
               currentPlaylistPane.getPlaylist().removeSong(currentSong);
           }
           while (audioFile==null){
               currentSong= currentPlaylistPane.getNextSong();
               audioFile=currentSong.getPhysicalFile();
               if(audioFile!=null){
                   break;
               }

               if(removeSongsWithMissingFiles){
                   library.getAllSongs().remove(currentSong);
                   currentPlaylistPane.getPlaylist().removeSong(currentSong);
               }

           }



       }






   }



    public void seekPlay(int time){// starts playing of the current song at specified  time
        startTime=time;
        audioFileChanged=false;
        trackSlider.setValue(startTime); // set  the time on track slider
        trackTimerSeconds =startTime;
        if(musicPlayer!=null && musicPlayer.getPlayerState()== PlayerState.PLAYING){ // set volume, pan, and time and play song
           musicPlayer.seekPlay(time+cueTrackStart);
            newSongPlay=true;
        }
    }
    public void stopPlay(){
        musicPlayer.stop();
        trackSlider.valueProperty().setValue(0);
        startTime=0;
        startOffset=0;
        if(songTimer!=null) {
            songTimer.stop();
        }
   }
    public void pause(){
      musicPlayer.pause();
        if(songTimer!=null) {
            songTimer.stop();
        }
    }
    public void fastForward(){
        int start=trackSlider.valueProperty().intValue()+10;
        if(start<trackLength) {
            seekPlay(start);
        }
        else{
            playNext();
        }
        try {
            Thread.currentThread().sleep(500);// prevents user from clciking the fast foward button too fast and causig issues
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void rewind(){
        int start=trackSlider.valueProperty().intValue()-10;
        if(start>0) {
            seekPlay(start);
        }
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setAudioFile(File file) {
        startOffset=0;
            this.audioFile =file;
        audioFileChanged=true;
        if(audioFile!=null) {
            this.audioFileExtension = audioFileUtilities.getExtensionOfFile(audioFile);
        }
        else if(library.getSettings().isAskToFindMissingFilesForSongs()){

            new OptionPane().showLocateFilePane(currentSong);

        }
        else if(library.getSettings().isRemoveSongsWithMissingFilesOnDiscovery()){
            library.getAllSongs().remove(currentSong);
            currentPlaylistPane.getPlaylist().getAllSongs().remove(currentSong);
            currentPlaylistPane.updatePane();

        }



    }
    public void setCurrentSong(AudioInformation currentSong){
        if(this.currentSong!=null) {
            this.currentSong.clearAudioFile();
        }
        this.currentSong = currentSong;
      this.trackLength = (int) currentSong.getTrackLengthNumber();
        if(musicPlayer.getPlayerState()!= PlayerState.PLAYING) {
            trackSlider.setMax(trackLength);
            trackSlider.setMinWidth(trackLength*2);
            setCurrentAlbum(currentSong.getAlbum());
            setCurrentArtist(currentSong.getArtist());
            setCurrentTrackTitle(currentSong.getTitle());
            getCurrentImages(currentSong);
            setAudioFile(currentSong.getPhysicalFile());
        }
    }
    public void getCurrentImages(AudioInformation currentSong){
        albumArtworkImages.clear();
        Tag tag=null;
        if(audioFile!=null && currentSong.isTaggable()) {
            AudioFile audioFile=currentSong.getAudioFile();
            if(audioFile!=null) {
                tag = audioFile.getTag();
            }
        }
        if(tag!=null) {
            List<Artwork> albumArtwork = tag.getArtworkList();
            int size = albumArtwork.size();
            for (int count = 0; count < size; count++) {
                byte[] b = albumArtwork.get(count).getBinaryData();
                if (b != null) {
                    ByteArrayInputStream stream = new ByteArrayInputStream(b);
                    try {
                        BufferedImage image = ImageIO.read(stream);
                        if (image != null) {
                            Image fxImage = SwingFXUtils.toFXImage(image, null);
                            albumArtworkImages.add(fxImage);
                        }
                    } catch (IllegalArgumentException | IOException e) { // if cant read image set image to no image;
                    }
                }
            }
            List<String> albumArtworkPaths = currentSong.getAlbumArtPaths();
            int numberOfFileImages = albumArtworkPaths.size();
            numberOfImages = numberOfImages + numberOfFileImages;
            for (int count = 0; count < numberOfFileImages; count++) {
                Image fxImage = new Image("file:" + albumArtworkPaths.get(count));
                if (fxImage != null) {
                    albumArtworkImages.add(fxImage);
                }
            }
            numberOfImages = albumArtworkImages.size();
            if (numberOfImages > 0) {
                setCurrentImage();
            } else {
                setCurrentImage("");// set image to no image are there are no images
            }
        }
        else{
            setCurrentImage("");//  no valid tag so set image to no image are there are no images
        }
    }
    public PlaylistPane getCurrentPlaylistPane(){
        return currentPlaylistPane;
    }
    public void saveLibrary(){ //  saves the music library using the library saving class  that saves it in json called from new thread  as it may
        // take awhile to save depending on the number of playlists and songs.
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                loader.saveLibrary(library);
            }
        };
        Thread thread= new Thread(runnable);
        thread.start();
    }



    public Settings getSettings() {
        return settings;
    }
    public SimpleStringProperty getCurrentArtist() {
        return currentArtist;
    }
    public void setCurrentArtist(String currentArtist) {
        this.currentArtist.set( currentArtist);
        currentArtistLabel.getChildren().remove(1);
        currentArtistLabel.getChildren().add(new Text(currentArtist));
    }
    public SimpleStringProperty getCurrentTrackTitle() {
        return currentTrackTitle;
    }
    public void setCurrentTrackTitle(String currentTrackTitle) {
        this.currentTrackTitle.set(currentTrackTitle);
        currentTrackLabel.getChildren().remove(1); // remove the old track
        currentTrackLabel.getChildren().add(new Text(currentTrackTitle));
    }
    public SimpleStringProperty getCurrentAlbum() {
        return currentAlbum;
    }
    public void setCurrentAlbum(String currentAlbum) {
        this.currentAlbum.set(currentAlbum);
        currentAlbumLabel.getChildren().remove(1);
        currentAlbumLabel.getChildren().add(new Text(currentAlbum));
    }
    public MusicLibrary getLibrary() {
        return library;
    }
    public float getVolume() {
        return volume;
    }
    public void setVolume(float volume) {
        this.volume = volume;
    }
    public PlayMode getPlayMode() {
        return playMode;
    }
    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }
    public float getPan() {
        return pan;
    }
    public void setPan(float pan) {
        this.pan = pan;
    }
    public void setCurrentPlaylistPane(PlaylistPane pane){
        this.currentPlaylistPane =pane;
        if(menuBar!=null) {
            menuBar.updateMenusToPlaylistPane(pane);
        }
        }
    public void repeatSong() {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                startTime=0;
                startOffset=0;
                songTimer.stop();
                songTimer.start();
                trackSlider.setValue(0);
                musicPlayer.stop();
                startPlay();
            }
        };
        Platform.runLater(runnable);
    }
    public void stopSong() {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                startTime=0;
                songTimer.stop();
                trackSlider.setValue(0);
                musicPlayer.stop();
            }
        };
        Platform.runLater(runnable);
    }
    public int getStartOffset() {
        return startOffset;
    }
    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }
    public boolean isAudioFileChanged() {
        return audioFileChanged;
    }
    public MainAudioWindow getAudioWindow(){
        return this;
    }
    public Stage getStage() {
        return stage;
    }
    public VBox getWindow() {
        return mainBox;
    }
    public boolean isMuted() {
        return muted;
    }
    public PlayerState getPlayerState(){
        return musicPlayer.getPlayerState();
    }
    public boolean isNotifyOnPlayStart() {
        return notifyOnPlayStart;
    }
    public void setNotifyOnPlayStart(boolean notifyOnPlayStart) {
        this.notifyOnPlayStart = notifyOnPlayStart;
    }
    public Notifications getNotifications() {
        return notifications;
    }
     public void removePane(int number){
        panes.remove(number);
     }
    public AnimationTimer getSongTimer() {
        return songTimer;
    }
    public SystemInfo getSystemInfo() {
        return systemInfo;
    }
    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }
    public TrackSlider getTrackSlider() {
        return trackSlider;
    }
    public Slider getVolumeControl() {
        return volumeControl;
    }
    public Slider getPanControl() {
        return panControl;
    }
    public ImageView getAlbumImage() {
        return albumImage;
    }
    public TextFlow getCurrentTrackLabel() {
        return currentTrackLabel;
    }
    public TextFlow getCurrentArtistLabel() {
        return currentArtistLabel;
    }
    public TextFlow getCurrentAlbumLabel() {
        return currentAlbumLabel;
    }
    public SimpleStringProperty getTrackTimeString() {
        return trackTimeString;
    }
    public Label getVolumeLabel() {
        return volumeLabel;
    }
    public int getTrackTimerSeconds() {
        return trackTimerSeconds;
    }

    public MainScene getMainScene() {
        return mainScene;
    }

    public boolean isFindFile() {
        return findFile;
    }

    public void setFindFile(boolean findFile) {
        this.findFile = findFile;
    }
}
