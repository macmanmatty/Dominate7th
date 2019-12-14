package sample.Windows;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import sample.AudioProcessors.*;
import sample.Library.AudioInformation;
import tray.animations.AnimationType;
import tray.notification.NotificationType;

import java.io.RandomAccessFile;


public   class Notifications {

    ExtractAudioInformation extractAudioInformation = new ExtractAudioInformation();

    public void showSongStartNotification(AudioInformation information) {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {


                String info = information.getTitle() + " " + information.getArtist() + " " + information.getAlbum();
                Image image = extractAudioInformation.getFirstImage(information);


                sample.Windows.TrayNotification trayNotification = new TrayNotification(information.getTitle(), information.getArtist(), information.getAlbum(), image);
                trayNotification.setAnimationType(AnimationType.SLIDE);
                if (image != null) {
                    trayNotification.setImage(image);

                }
                trayNotification.showAndDismiss(new Duration(4000));

            }
        }
        ;
        Platform.runLater(runnable);
    }


    public void showAudioProcessNotification(String tile, String text, NotificationType type, long duration) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
        sample.Windows.TrayNotification trayNotification = new TrayNotification(tile, text, type);
        trayNotification.setAnimationType(AnimationType.SLIDE);



                trayNotification.showAndDismiss(new Duration(duration));

            }
        };
        Platform.runLater(runnable);


    }

    public void showAudioProcessNotification(String tile, String text, NotificationType type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                sample.Windows.TrayNotification trayNotification = new TrayNotification(tile, text, type);
        trayNotification.setAnimationType(AnimationType.SLIDE);


                trayNotification.showAndDismiss(new Duration(2000));

            }
        };
        Platform.runLater(runnable);


    }


    public void showAudioProcessNotification(String tite, NotificationType type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
        sample.Windows.TrayNotification trayNotification = new TrayNotification(tite, type);
        trayNotification.setAnimationType(AnimationType.SLIDE);




                trayNotification.showAndDismiss(new Duration(2000));

            }
        };
        Platform.runLater(runnable);


    }

   public void  showNotification(Action action) {
        if (action.completed() == true) {
            if (action.getCurrentErrors() == 0) {
               showAudioProcessNotification(action.getFileName()+"  Succussfully "+ action.getActionName(), NotificationType.SUCCESS);
            } else {
               showAudioProcessNotification(action.getFileName()+" "+ action.getActionName()+" with " + action.getCurrentErrors() + "errors", NotificationType.WARNING);

            }

        } else {
            showAudioProcessNotification(action.getFileName()+" Not " +action.getActionName()+"  with " + action.getCurrentErrors() + "errors", NotificationType.ERROR);


        }
    }


    public void  showNotification(AudioInformationAction action) {
        if (action.completed() == true) {
            if (action.getCurrentErrors() == 0) {
                showAudioProcessNotification("File Succussfully processed", NotificationType.SUCCESS);
            } else {
                showAudioProcessNotification("File Processed with " + action.getCurrentErrors() + "errors", NotificationType.WARNING);

            }

        } else {
            showAudioProcessNotification("File Not Processed with " + action.getCurrentErrors() + "errors", NotificationType.ERROR);


        }
    }
}






