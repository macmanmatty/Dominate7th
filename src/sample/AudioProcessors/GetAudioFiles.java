package sample.AudioProcessors;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.Library.AudioInformationAddable;
import sample.Library.CueSheets.CueSheet;
import sample.Library.AudioInformation;
import sample.Library.MusicLibrary;
import sample.Library.Playlist;
import sample.Utilities.AudioFileUtilities;
import sample.Utilities.SystemInfo;
import sample.Windows.FileProgressBar;
import sample.Windows.Notifications;
import sample.Windows.ProgressWindow;
import sample.Windows.UpdateLabel;
import tray.notification.NotificationType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class GetAudioFiles  implements AudioProcess {
   private AudioProcess audioProcessor;
   private boolean moveSongsToLibrary;
    private String libraryPath;
    private boolean sortByFileType;
    private boolean sortByBitRate;
    private SimpleStringProperty currentFile= new SimpleStringProperty();
    private Thread thread;
    private Thread startThread;
    private volatile CountDownLatch countDownLatch;
    private UpdateLabel updateLabel= new UpdateLabel();
    private FileProgressBar progressBar=new FileProgressBar();
    private int numberOfFilesToProcess;
    private int filesProcessed;
    private boolean copyDuplicateFiles;
    private boolean getMissingTags;
    private Notifications notification= new Notifications();
    private boolean  showNotifications;
    private boolean showCompletedNotification;
    private boolean copyFilesThenMove;
    private boolean addSongsToCurrentPlayList;
    private boolean addSongsToLibrary;
    private boolean checkForDuplicates=true;
    private AudioFileUtilities utilities= new AudioFileUtilities();
    private ExtractAudioInformation extractAudioInformation= new ExtractAudioInformation();
    private MusicLibrary musicLibrary;
    private List<AudioInformation> foundSongs= new ArrayList<>();
    private List<AudioInformationAddable> audioInformationAddables= new ArrayList<>();
    private SystemInfo systemInfo;
    private boolean onCD;
    private String processName;



    public GetAudioFiles(SystemInfo info, MusicLibrary library, Playlist playlist) {
        this.musicLibrary = library;
        this.systemInfo=info;
        if(library!=null) {
            this.libraryPath = library.getSettings().getLibraryPath();
        }

        this.audioInformationAddables.add(playlist);


    }

    public GetAudioFiles(SystemInfo info, List<AudioInformationAddable> audioInformationAddables) {
        this.audioInformationAddables = audioInformationAddables;
        this.systemInfo=info;

    }

    public GetAudioFiles(SystemInfo info, AudioInformationAddable audioInformationAddables) {
        this.audioInformationAddables.add(audioInformationAddables);
        this.systemInfo=info;

    }


    public List<AudioInformation> loadFiles(String path, boolean displayProgressWindow) {
        if(moveSongsToLibrary==true){
            processName="Find Add and Move SongsTo Library";
        }
        else{
            processName="Find and  Add  Songs";
        }
        boolean inLibrary= StringUtils.contains(libraryPath, path);
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                if(displayProgressWindow==true) {
                    ProgressWindow progressWindow = new ProgressWindow(getAudioProcess());
                    progressWindow.displayWindow();
                }

                countDownLatch = new CountDownLatch(1);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (moveSongsToLibrary == true && inLibrary==false ) {
                            CountDownLatch countDownLatch = new CountDownLatch(1);
                            progressBar.setFilesToProcess(numberOfFilesToProcess);
                           FileProcessor processor = new FileProcessor(new AudioFileSorter(systemInfo.getFileSeperator(),libraryPath, sortByFileType, sortByBitRate, copyDuplicateFiles, getMissingTags, copyFilesThenMove, onCD), countDownLatch, updateLabel, progressBar);
                            audioProcessor = processor;
                            boolean done = false;
                            while (!(thread.isInterrupted()) && done == false) {
                                processor.manipulateFiles(path);// move files to library folder
                                done=true;
                            }
                            try {
                                countDownLatch.await();// wait for results
                            } catch (InterruptedException e) {
                            }
                            List<AudioInformation> audioInformation=processor.getTracks();
                            List<AudioInformation> songs=new ArrayList<>();
                            foundSongs.addAll(songs);

                            songs.addAll(audioInformation);


                        }
                        else {
                            GetAudioInformationFromFiles getInformation = new GetAudioInformationFromFiles(updateLabel, progressBar, false, onCD);
                            audioProcessor = getInformation;
                          List<AudioInformation> audioInformation = getInformation.getAudioInformation(path);// get audio information
                            foundSongs.addAll(audioInformation);

                        }
                        int size=audioInformationAddables.size();
                        for(int count=0; count<size; count++){

                            audioInformationAddables.get(count).addSongs(foundSongs);
                        }
                        if(musicLibrary!=null && addSongsToLibrary==true){
                            musicLibrary.addSongs(foundSongs);
                        }

                        countDownLatch.countDown();
                    }
                };
                thread = new Thread(runnable);
                thread.start();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    thread.interrupt();
                }
                updateLabel.setText("operation Competed");
                progressBar.setProgress(1);
            }
        };
        startThread=new Thread(runnable);
        startThread.start();
        System.out.println("Found Songs "+foundSongs.size());

        return foundSongs;
    }
    public List<AudioInformation> loadFiles(List<File> files, boolean displayProgressWindow) {
        if(moveSongsToLibrary==true){
            processName="Find Add and Move SongsTo Library";
        }

        else{
            processName="Find and  Add  Songs";
        }

        if(displayProgressWindow==true) {
            ProgressWindow progressWindow = new ProgressWindow(getAudioProcess());
            progressWindow.displayWindow();
        }
        AudioFileSorter audioFileSorter= new AudioFileSorter(systemInfo.getFileSeperator(),libraryPath, sortByFileType, sortByBitRate, copyDuplicateFiles, getMissingTags, copyFilesThenMove, onCD);
        FileProcessor processor = new FileProcessor(audioFileSorter,  updateLabel, progressBar);
        GetAudioInformationFromFiles getInformation = new GetAudioInformationFromFiles(false);
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                countDownLatch = new CountDownLatch(1);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        int size=files.size();
                        for(int count=0; count<size; count++) {
                            File file = files.get(count);
                            String path = files.get(count).getAbsolutePath();
                            boolean inLibrary= StringUtils.contains(libraryPath, path);


                            if(file.isDirectory()) {
                                if (moveSongsToLibrary == true && inLibrary==false ) {
                                    CountDownLatch countDownLatch = new CountDownLatch(1);
                                    progressBar.setFilesToProcess(numberOfFilesToProcess);
                                    processor.setExternalCountDownLatch(countDownLatch);
                                    boolean done = false;
                                    while (!(thread.isInterrupted()) && done == false) {
                                        processor.manipulateFiles(path);// move files to library folder
                                        done = true;
                                    }
                                    try {
                                        countDownLatch.await();
                                    } catch (InterruptedException e) {
                                    }
                                   List<AudioInformation> audioInformation = audioFileSorter.getTracks();
                                    List<AudioInformation> songs=new ArrayList<>();
                                    songs.addAll(audioInformation);
                                    foundSongs.addAll(songs);


                                   audioFileSorter.getTracks().clear();
                                } else {
                                    audioProcessor = getInformation;
                                    List<AudioInformation> audioInformation = getInformation.getAudioInformation(path);// get audio information
                                    System.out.println("Songs found " + audioInformation.size());
                                    List<AudioInformation> songs=new ArrayList<>();
                                    songs.addAll(audioInformation);
                                    foundSongs.addAll(songs);


                                }
                            }
                            else if(utilities.getExtensionOfFile(file).equalsIgnoreCase("cue")){
                                try {
                                    if(moveSongsToLibrary==true && inLibrary==false) {
                                        audioFileSorter.action(file);
                                        List<AudioInformation> tracks=audioFileSorter.getTracks();
                                        List<AudioInformation> songs=new ArrayList<>();
                                        songs.addAll(tracks);
                                        foundSongs.addAll(songs);


                                        audioFileSorter.getTracks().clear();
                                    }
                                    else{
                                        CueSheet cueSheet = extractAudioInformation.getCueSheet(file);
                                        List<AudioInformation> songs=cueSheet.getTracks();
                                        foundSongs.addAll(songs);


                                    }
                                    
                                } catch (CueSheetExeception cannotReadCueSheetExeception) {
                                    cannotReadCueSheetExeception.printStackTrace();
                                }
                                audioFileSorter.getTracks().clear();
                            }
                            else{
                                if(utilities.isAudioFile(file)) {
                                    if(moveSongsToLibrary==true && inLibrary==false){
                                        audioFileSorter.action(file);
                                        List<AudioInformation> song=audioFileSorter.getTracks();

                                        foundSongs.addAll(song);


                                        audioFileSorter.getTracks().clear();
                                    }
                                    else {
                                        AudioInformation information = extractAudioInformation.extractAudioInformationFromFile(file);
                                        if(onCD==true){
                                            information.setWriteFieldsToFile(false);
                                        }
                                        foundSongs.add(information);

                                        if(information!=null) {

                                        }

                                    }
                                }
                            }
                        }
                      size=audioInformationAddables.size();

                        for(int count=0; count<size; count++){
                            audioInformationAddables.get(count).addSongs(foundSongs);

                        }
                        if(musicLibrary!=null && addSongsToLibrary==true){
                            musicLibrary.addSongs(foundSongs);
                        }


                        countDownLatch.countDown();
                    }
                };
                thread = new Thread(runnable);
                thread.start();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    thread.interrupt();
                }
                updateLabel.setText("operation Competed");
                if(showCompletedNotification==true){
                        notification.showAudioProcessNotification("operation Competed with " , NotificationType.SUCCESS);
                }


            }
        };
        startThread=new Thread(runnable);
        startThread.start();

        System.out.println("Found Songs "+foundSongs.size());
        return foundSongs;
    }
    public String getCurrentFile() {
        return currentFile.get();
    }
    @Override
    public void stopProcess() {
        if(startThread!=null) {
            startThread.interrupt();
            startThread=null;
        }
        if(thread!=null) {
            if(audioProcessor!=null){
                audioProcessor.stopProcess();// stop current process
            }

            thread.interrupt(); // interrupt current thread to stop it
            thread = null;
        }
    }
    public AudioProcess getAudioProcess(){
        return this;
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
        return processName;
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
    public boolean isSortByFileType() {
        return sortByFileType;
    }
    public void setSortByFileType(boolean sortByFileType) {
        this.sortByFileType = sortByFileType;
    }
    public boolean isSortByBitRate() {
        return sortByBitRate;
    }
    public void setSortByBitRate(boolean sortByBitRate) {
        this.sortByBitRate = sortByBitRate;
    }
    public boolean isCopyDuplicateFiles() {
        return copyDuplicateFiles;
    }
    public void setCopyDuplicateFiles(boolean copyDuplicateFiles) {
        this.copyDuplicateFiles = copyDuplicateFiles;
    }
    public boolean isGetMissingTags() {
        return getMissingTags;
    }
    public void setGetMissingTags(boolean getMissingTags) {
        this.getMissingTags = getMissingTags;
    }
    public boolean isCopyFilesThenMove() {
        return copyFilesThenMove;
    }
    public void setCopyFilesThenMove(boolean copyFilesThenMove) {
        this.copyFilesThenMove = copyFilesThenMove;
    }
    public boolean isAddSongsToCurrentPlayList() {
        return addSongsToCurrentPlayList;
    }
    public void setAddSongsToCurrentPlayList(boolean addSongsToCurrentPlayList) {
        this.addSongsToCurrentPlayList = addSongsToCurrentPlayList;
    }
    public boolean isAddSongsToLibrary() {
        return addSongsToLibrary;
    }
    public void setAddSongsToLibrary(boolean addSongsToLibrary) {
        this.addSongsToLibrary = addSongsToLibrary;
    }
    public boolean isMoveSongsToLibrary() {
        return moveSongsToLibrary;
    }
    public void setMoveSongsToLibrary(boolean moveSongsToLibrary) {
        this.moveSongsToLibrary = moveSongsToLibrary;
    }
    public String getLibraryPath() {
        return libraryPath;
    }
    public void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    public boolean isCheckForDuplicates() {
        return checkForDuplicates;
    }

    public void setCheckForDuplicates(boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    public boolean isOnCD() {
        return onCD;
    }

    public void setOnCD(boolean onCD) {
        this.onCD = onCD;
    }
}
