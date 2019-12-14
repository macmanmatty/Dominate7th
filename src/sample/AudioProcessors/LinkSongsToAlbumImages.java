package sample.AudioProcessors;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;
import sample.Utilities.AudioFileUtilities;
import sample.Library.AudioInformation;
import sample.Windows.FileProgressBar;
import sample.Windows.Notifications;
import sample.Windows.ProgressWindow;
import sample.Windows.UpdateLabel;
import tray.notification.NotificationType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class LinkSongsToAlbumImages implements AudioProcess { // links all images in a folder to the corrosponding songs in a  folder
    private ArrayList<File> directories = new ArrayList<>();
   private ArrayList<String> errorMessages = new ArrayList<String>();
   private   volatile AudioFileUtilities utilities = new AudioFileUtilities();
    private  volatile UpdateLabel updateLabel= new UpdateLabel();
    private volatile FileProgressBar fileProgressBar= new FileProgressBar();
    private  volatile  Thread utilityThread;
    private volatile  Thread startThread;
    private  volatile  int numberOfFilesToProcess;
    private  volatile  int filesProcessed;
    private CountDownLatch countDownLatch= new CountDownLatch(1);
    private boolean showProgressWindow;
    private Notifications notification= new Notifications();
    private boolean  showNotifications;
    private boolean showCompletedNotification;
    public LinkSongsToAlbumImages(boolean showProgressWindow) {
        this.showProgressWindow=showProgressWindow;
    }
    public LinkSongsToAlbumImages(UpdateLabel updateLabel, FileProgressBar fileProgressBar, CountDownLatch countDownLatch, boolean showProgressWindow) {
        this.updateLabel = updateLabel;
        this.fileProgressBar = fileProgressBar;
        this.countDownLatch = countDownLatch;
        this.showProgressWindow = showProgressWindow;
    }
    public LinkSongsToAlbumImages(UpdateLabel updateLabel, FileProgressBar fileProgressBar) {
        this.updateLabel = updateLabel;
        this.fileProgressBar = fileProgressBar;
    }
    public LinkSongsToAlbumImages(UpdateLabel updateLabel, FileProgressBar fileProgressBar, boolean showProgressWindow) {
        this.updateLabel = updateLabel;
        this.fileProgressBar = fileProgressBar;
        this.showProgressWindow = showProgressWindow;
    }
    public void linkSongsToAlbumArt(String path) {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                if (showProgressWindow == true) {
                    ProgressWindow progressWindow = new ProgressWindow(getProcess()); // create progress window
                    progressWindow.displayWindow();
                }
                updateLabel.setText(numberOfFilesToProcess+ " of "+numberOfFilesToProcess +"Files Processed");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        directories = utilities.findDirectories(path); // find all directories in a given path
                        int size = directories.size();
                        boolean done=false;
                        while(!(utilityThread.isInterrupted())&& done==false) {
                            for (int count = 0; count < size; count++) {
                                linkFiles(directories.get(count)); // links the images to songs in the directory
                            }
                            done=true;
                        }
                        countDownLatch.countDown();
                    }
                };
                utilityThread = new Thread(runnable);
                utilityThread.start();
                updateLabel.setText("operation Competed with " + errorMessages.size() + " errors");
                if(showCompletedNotification==true){
                    if(errorMessages.size()==0) {
                        notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);
                    }
                    else{
                        notification.showAudioProcessNotification("Linking Songs Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);
                    }
                }
                if (errorMessages.size() > 0) {
                }
                fileProgressBar.setProgress(1);
            }
        };
        startThread=new Thread(runnable);
        utilityThread.start();
    }
    public void linkSongsToAlbumArt(List<AudioInformation> songs) {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                if (showProgressWindow == true) {
                    ProgressWindow progressWindow = new ProgressWindow(getProcess()); // create progress window
                    progressWindow.displayWindow();
                }
                updateLabel.setText(numberOfFilesToProcess + " of " + numberOfFilesToProcess + "Files Processed");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        int size = songs.size();
                        for (int count = 0; count < size; count++) {
                            AudioInformation song = songs.get(count);
                            File file = song.getAudioFile().getFile();
                            File directory = file.getParentFile();
                            linkFiles(directory, song);
                            updateLabel.setText(numberOfFilesToProcess + " of " + numberOfFilesToProcess + "Files Processed");
                        }
                        countDownLatch.countDown();
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    errorMessages.add(e.getMessage());
                    thread.interrupt();
                }
                updateLabel.setText("operation Competed with " + errorMessages.size() + " errors");
                if (errorMessages.size() > 0) {
                }                fileProgressBar.setProgress(1);
            }
        };
        startThread= new Thread(runnable);
        startThread.start();
    }
    public void linkFiles(File directory) {
        File[] allFiles = directory.listFiles();
        int size = allFiles.length;
        numberOfFilesToProcess = numberOfFilesToProcess + size;
        fileProgressBar.setFilesToProcess(numberOfFilesToProcess);
        ArrayList<File> images = new ArrayList<>(); // list of images
        ArrayList<File> audioFiles = new ArrayList<>(); // list of songs
        for (int count = 0; count < size; count++) {
            System.out.println("linking files....");
            File fileInFolder = allFiles[count];
            if (utilities.isAudioFile(fileInFolder)) { // find audio files
                audioFiles.add(fileInFolder);
            } else if (utilities.isImageFile(fileInFolder)) { // find images
                images.add(fileInFolder);
            }
        }
        size = audioFiles.size();
        try {
            int size2 = images.size();
            for (int count = 0; count < size; count++) {
                AudioFile audioFile = AudioFileIO.read(audioFiles.get(count)); // get audio file
                if(audioFile!=null) {
                    for (int count2 = 0; count2 < size2; count2++) {
                        Tag tag=audioFile.getTag();
                        Artwork artwork=new StandardArtwork();
                        artwork.setImageUrl(images.get(count).getAbsolutePath());// add image path to art work
                        tag.getArtworkList().add(artwork);
                    }
                }
            }
        } catch (CannotReadException e) {
           errorMessages.add(e.getMessage());
        } catch (IOException e) {
            errorMessages.add(e.getMessage());
        } catch (TagException e) {
            errorMessages.add(e.getMessage());
        } catch (ReadOnlyFileException e) {
            errorMessages.add(e.getMessage());
        } catch (InvalidAudioFrameException e) {
            errorMessages.add(e.getMessage());
        }
    }
    public void linkFiles(File directory, AudioInformation song) {
        File[] allFiles = directory.listFiles();
        int size = allFiles.length;
        ArrayList<File> images = new ArrayList<>(); // list of images
        for (int count = 0; count < size; count++) {
            System.out.println("linking files....");
            File fileInFolder = allFiles[count];
           if (utilities.isImageFile(fileInFolder)) { // find images
                images.add(fileInFolder);
            }
        }
            int size2 = images.size();
        AudioFile audioFile=song.getAudioFile();
                    for (int count2 = 0; count2 < size2; count2++) {
                        Artwork artwork= new StandardArtwork();
                        artwork.setImageUrl(images.get(count2).getAbsolutePath());
                   Tag tag= audioFile.getTag();
                   if(tag!=null){
                       audioFile.getTag().getArtworkList().add(artwork);
                   }
                    }
        try {
            AudioFileIO.write(audioFile);
        } catch (CannotWriteException e) {
            e.printStackTrace();
        }
        song.clearAudioFile();
                        filesProcessed++;
                }
    @Override
    public void stopProcess() {
        if(utilityThread !=null){
            utilityThread.interrupt();
            utilityThread =null;
        }
    }
    public UpdateLabel getUpdateLabel() {
        return updateLabel;
    }
    @Override
    public FileProgressBar getProgressBar() {
        return fileProgressBar;
    }
    @Override
    public String getProcessName() {
        return "Link Files To Audio Images";
    }
    public void setUpdateLabel(UpdateLabel updateLabel) {
        this.updateLabel = updateLabel;
    }
    public AudioProcess getProcess(){
        return this;
    }
    public boolean isShowCompletedNotification() {
        return showCompletedNotification;
    }
    public void setShowCompletedNotification(boolean showCompletedNotification) {
        this.showCompletedNotification = showCompletedNotification;
    }
    public boolean isShowNotifications() {
        return showNotifications;
    }
    public void setShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
    }
}
