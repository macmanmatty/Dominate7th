package sample.Windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.animations.*;
import tray.models.CustomStage;
import tray.notification.NotificationType;

import java.io.IOException;
import java.net.URL;

public class TrayNotification {


    @FXML
    private Label title;
    @FXML
    private Label mainTitle;
            @FXML
        private Label artist;
            @FXML
        private Label album;
            @FXML
        private Label lblClose;
            @FXML
        private ImageView imageIcon;
            @FXML
        private Rectangle rectangleColor;
        @FXML
        private AnchorPane rootNode;
        private CustomStage stage;
        private NotificationType notificationType;
        private AnimationType animationType;
        private EventHandler<ActionEvent> onDismissedCallBack;
        private EventHandler<ActionEvent> onShownCallback;
        private TrayAnimation animator;
        private AnimationProvider animationProvider;

        public TrayNotification(String title, String artist,  String album,   Image img) {
            this.initTrayNotification(title,  artist, album, img);
            this.setImage(img);
        }


    public TrayNotification(String title, String text, NotificationType type) {
        this.initTrayNotification(title,  text, type);
    }

    public TrayNotification(String title, NotificationType type) {
        this.initTrayNotification(title,   type);
    }

     private void initTrayNotification(String title, String artist,  String album, Image image) {
        try {


            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/TrayNotification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();


            this.initStage();
            this.initAnimations();
            setPlayingTray(title,artist,album,image, NotificationType.INFORMATION);

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private void initTrayNotification(String title, String text, NotificationType type) {
        try {


            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/TrayNotification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();


            this.initStage();
            this.initAnimations();
            setPlayingTray(title,text, type);

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private void initTrayNotification(String title,  NotificationType type) {
        try {


            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/TrayNotification.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.load();


            this.initStage();
            this.initAnimations();
            setPlayingTray(title, type);

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private void initAnimations() {
            this.animationProvider = new AnimationProvider(new TrayAnimation[]{new FadeAnimation(this.stage), new SlideAnimation(this.stage), new PopupAnimation(this.stage)});
            this.setAnimationType(AnimationType.SLIDE);
        }

        private void initStage() {
            this.stage = new CustomStage(this.rootNode, StageStyle.UNDECORATED);
            this.stage.setScene(new Scene(this.rootNode));
            this.stage.setAlwaysOnTop(true);
            this.stage.setLocation(this.stage.getBottomRight());
            this.lblClose.setOnMouseClicked((e) -> {
                this.dismiss();
            });
        }

        public void setNotificationType(NotificationType nType) {
            this.notificationType = nType;
            URL imageLocation = null;
            String paintHex = null;
            switch(nType) {
                case INFORMATION:
                    imageLocation = this.getClass().getResource("/tray/resources/info.png");
                    paintHex = "#2C54AB";
                    break;
                case NOTICE:
                    imageLocation = this.getClass().getResource("/tray/resources/notice.png");
                    paintHex = "#8D9695";
                    break;
                case SUCCESS:
                    imageLocation = this.getClass().getResource("/tray/resources/success.png");
                    paintHex = "#009961";
                    break;
                case WARNING:
                    imageLocation = this.getClass().getResource("/tray/resources/warning.png");
                    paintHex = "#E23E0A";
                    break;
                case ERROR:
                    imageLocation = this.getClass().getResource("/tray/resources/error.png");
                    paintHex = "#CC0033";
                    break;
                case CUSTOM:
                    return;
            }

            this.setRectangleFill(Paint.valueOf(paintHex));
            this.setImage(new Image(imageLocation.toString()));
            this.setTrayIcon(this.imageIcon.getImage());
        }

        public NotificationType getNotificationType() {
            return this.notificationType;
        }

        public void setPlayingTray(String title, String artist, String album, Image image, NotificationType type) {
            this.mainTitle.setText("Now Playing...");
            this.setTitle( "Title: "+title);
            this.setArtist("Artist: "+artist);
            this.setAlbum("Album: "+album);
            this.setImage(image);
            this.setNotificationType(type);
            int length=title.length();
            if(album.length()>length){
                length=album.length();
            }
            if(artist.length()>length){
                length=artist.length();
            }


            rootNode.setMinWidth((length*2)+126);
            rootNode.setPrefWidth((length*2)+126);

        }



    public void setPlayingTray(String title, String message, NotificationType type) {
        this.setMainTitle(title);
        this.setArtist(message);
        this.setNotificationType(type);
    }


    public void setPlayingTray(String title,  NotificationType type) {
        this.setMainTitle(title);
        this.setNotificationType(type);
        rootNode.setMinWidth((title.length()*2)+126);
        rootNode.setPrefWidth((title.length()*2)+126);

    }
        public boolean isTrayShowing() {
            return this.animator.isShowing();
        }

        public void showAndDismiss(Duration dismissDelay) {
            if (this.isTrayShowing()) {
                this.dismiss();
            } else {
                this.stage.show();
                this.onShown();
                this.animator.playSequential(dismissDelay);
            }

            this.onDismissed();
        }

        public void showAndWait() {
            if (!this.isTrayShowing()) {
                this.stage.show();
                this.animator.playShowAnimation();
                this.onShown();
            }

        }

        public void dismiss() {
            if (this.isTrayShowing()) {
                this.animator.playDismissAnimation();
                this.onDismissed();
            }

        }

        private void onShown() {
            if (this.onShownCallback != null) {
                this.onShownCallback.handle(new ActionEvent());
            }

        }

        private void onDismissed() {
            if (this.onDismissedCallBack != null) {
                this.onDismissedCallBack.handle(new ActionEvent());
            }

        }

        public void setOnDismiss(EventHandler<ActionEvent> event) {
            this.onDismissedCallBack = event;
        }

        public void setOnShown(EventHandler<ActionEvent> event) {
            this.onShownCallback = event;
        }

        public void setTrayIcon(Image img) {
            this.stage.getIcons().clear();
            this.stage.getIcons().add(img);
        }

        public Image getTrayIcon() {
            return (Image)this.stage.getIcons().get(0);
        }

        public void setTitle(String txt) {
            this.title.setText(txt);
        }

        public String getTitle() {
            return this.title.getText();
        }

    public void setArtist(String txt) {
        this.artist.setText(txt);
    }

    public String getArtist() {
        return this.artist.getText();
    }

    public void setAlbum(String txt) {
        this.album.setText(txt);
    }

    public String getAlbum() {
        return this.album.getText();
    }


    public void setImage(Image img) {
        this.imageIcon.setImage(img);
        this.setTrayIcon(img);
        }

        public Image getImage() {
            return this.imageIcon.getImage();
        }

        public void setRectangleFill(Paint value) {
            this.rectangleColor.setFill(value);
        }

        public Paint getRectangleFill() {
            return this.rectangleColor.getFill();
        }

        public void setAnimationType(AnimationType type) {
            this.animator = this.animationProvider.findFirstWhere((a) -> {
                return a.getAnimationType() == type;
            });
            this.animationType = type;
        }

        public AnimationType getAnimationType() {
            return this.animationType;
        }


        public void setMainTitle(String text){
            this.mainTitle.setText(text);
        }

        public String getMainTitle(){

            return mainTitle.getText();

        }


}
