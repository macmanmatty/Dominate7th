package sample.AudioProcessors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.tag.images.Artwork;
import sample.Library.AudioInformation;
import sample.Utilities.AudioFileUtilities;
import sample.Windows.*;
import tray.notification.NotificationType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SaveAlbumArt implements AudioProcess {// class for saving imbedded album  art to a file on the users disk


    private AudioFileAction action;
    private ArrayList<String> errorMessages= new ArrayList<>();
    private Thread utilityThread;
    private Thread startThread;
    private CountDownLatch countDownLatch;
    private UpdateLabel updateLabel= new UpdateLabel();
    private FileProgressBar progressBar= new FileProgressBar();
    private boolean showProgressWindows;

    private Character exclude= new Character('?');
    private String processName;
    private Notifications notification= new Notifications();
    private boolean  showNotifications;
    private boolean showCompletedNotification;
    private String fileSeperator;
    


    public SaveAlbumArt(String fileSeperator, boolean showProgressWindows) {
        this.showProgressWindows = showProgressWindows;
        this.fileSeperator=fileSeperator;
    }

    public void saveAlbumArtToFile(AudioFile audioFile, String outputPath, String albumName){


        List<Artwork> artworkList = audioFile.getTag().getArtworkList();

        int size = artworkList.size();

        boolean done=false;
        while(!(utilityThread.isInterrupted() && done==false)) {

            for (int count = 0; count < size; count++) {
                Artwork artwork = artworkList.get(count);
                byte[] imageBytes = artwork.getBinaryData();
                File file = new File(outputPath + fileSeperator + albumName + count);
                file.mkdir();


                try {
                    FileOutputStream fileOut = new FileOutputStream(file);
                    fileOut.write(imageBytes);
                } catch (FileNotFoundException e) {
                    new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
                } catch (IOException e) {
                    new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
                }


            }

            done=true;
        }


        if(showCompletedNotification==true){
            if(errorMessages.size()==0) {
                notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);

            }
            else{
                notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);

            }

        }



    }



    public  List<String> saveAlbumArtToFile(AudioInformation song,  String outputPath, String albumName){


        AudioFile audioFile=song.getAudioFile();

        List<String> pathNames= new ArrayList<>();



        List<Artwork> artworkList = audioFile.getTag().getArtworkList();

        int size = artworkList.size();

        boolean done=false;

            for (int count = 0; count < size; count++) {
                Artwork artwork = artworkList.get(count);
                byte[] imageBytes = artwork.getBinaryData();
                String imagePath=outputPath + fileSeperator + albumName + count;

                File file = new File(imagePath);
                pathNames.add(imagePath);
                file.mkdir();


                try {
                    FileOutputStream fileOut = new FileOutputStream(file);
                    fileOut.write(imageBytes);
                } catch (FileNotFoundException e) {
                    new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
                } catch (IOException e) {
                    new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
                }


            }

            done=true;



        if(showCompletedNotification==true){
            if(errorMessages.size()==0) {
                notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);

            }
            else{
                notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);

            }

        }




        return  pathNames;

    }



    public void saveAlbumArtToFile(List<AudioInformation> songs, String outputPath) {// saves the embedded art in the tags of audio file to a jpg image file



        Runnable runnable= new Runnable() {
            @Override
            public void run() {


                processName = "Save Album Art ";
                if (showProgressWindows == true) {
                    ProgressWindow progressWindow = new ProgressWindow(getAudioProcessor());
                    progressWindow.displayWindow();
                }
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {


                        int size2 = songs.size();


                        boolean done=false;

                        while(!(utilityThread.isInterrupted()) && done==false) {
                            for (int count2 = 0; count2 < size2; count2++) {
                                List<Artwork> artworkList = songs.get(count2).getAudioFile().getTag().getArtworkList();
                                String albumName = songs.get(count2).getAlbum();


                                int size = artworkList.size();
                                System.out.println("Artowk saving..  " + artworkList.size());

                                for (int count = 0; count < size; count++) {
                                    Artwork artwork = artworkList.get(count);
                                    byte[] imageBytes = artwork.getBinaryData();
                                    File file = new File(outputPath + fileSeperator + albumName + count + ".jpg");
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        new OptionPane().showOptionPane("Cannot Create File " + e.getMessage(), "OK");
                                    }


                                    try {
                                        FileOutputStream fileOut = new FileOutputStream(file);
                                        fileOut.write(imageBytes);
                                    } catch (FileNotFoundException e) {
                                        new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
                                    } catch (IOException e) {
                                        new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
                                    }


                                }


                            }
                            done=true;

                        }


                        countDownLatch.countDown();


                    }

                };

                utilityThread = new Thread(runnable);
                utilityThread.start();
                try {
                    countDownLatch.await();// wait till execution is done
                } catch (InterruptedException e) {
                    errorMessages.add(e.getMessage());
                    utilityThread.interrupt();
                }

                updateLabel.setText("operation Competed with " + errorMessages.size() + " errors"); // update the label

                if(showCompletedNotification==true){
                    if(errorMessages.size()==0) {
                        notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);

                    }
                    else{
                        notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);

                    }

                }



            }
        };
        startThread= new Thread(runnable);
        startThread.start();




    }

    private AudioProcess getAudioProcessor() {

        return this;

    }





    @Override
    public void stopProcess() {

        if(startThread!=null) {


            if (utilityThread != null) {
                utilityThread.interrupt();
                utilityThread = null;


            }
            startThread.interrupt();
            startThread=null;



        }

    }

    @Override
    public UpdateLabel getUpdateLabel() {
        return updateLabel;
    }

    @Override
    public FileProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public String getProcessName() {
        return "Get Audio Files";
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public boolean isShowNotifications() {
        return showNotifications;
    }

    public void setShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
    }

    public boolean isShowCompletedNotification() {
        return showCompletedNotification;
    }

    public void setShowCompletedNotification(boolean showCompletedNotification) {
        this.showCompletedNotification = showCompletedNotification;
    }
}
