package sample.AudioProcessors;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.Library.CueSheets.CueSheet;
import sample.Utilities.AudioFileUtilities;
import sample.Library.AudioInformation;
import sample.Windows.FileProgressBar;
import sample.Windows.Notifications;
import sample.Windows.ProgressWindow;
import sample.Windows.UpdateLabel;
import tray.notification.NotificationType;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class GetAudioInformationFromFiles implements AudioProcess {
    private ArrayList<String> errorMessages= new ArrayList<>();
    private AudioFileUtilities utilities;
    private Thread utilityThread;
    private Thread startThread;
    private CountDownLatch countDownLatchUtilityThread;
    private CountDownLatch countDownLatchStartThread;
    private UpdateLabel updateLabel= new UpdateLabel();
    private FileProgressBar progressBar= new FileProgressBar();
    private boolean showProgressWindows;
    private int numberOfFilesToProcess;
    private int numberOfFilesProcessed;
    private Character exclude= new Character('?');
    private String processName;
    private List<AudioInformation> informationList= new ArrayList<>();
    private Notifications notification= new Notifications();
    private boolean  showNotifications;
    private boolean showCompletedNotification;
    private boolean onCD;
    private String fileSeperator;
    private ExtractAudioInformation extractAudioInformation = new ExtractAudioInformation();
    public GetAudioInformationFromFiles(boolean showProgressWindows) {

        this(new UpdateLabel(), new FileProgressBar(), showProgressWindows, false);

    }
    public GetAudioInformationFromFiles( UpdateLabel updateLabel, FileProgressBar progressBar, boolean showProgressWindows, boolean onCD) {
        this.updateLabel = updateLabel;
        this.progressBar = progressBar;
        this.fileSeperator=fileSeperator;
        this.showProgressWindows = showProgressWindows;
        this.onCD=onCD;
        utilities= new AudioFileUtilities();
    }
    public List<AudioInformation> getAudioInformation(String path) { // returns  a list of audio information
        countDownLatchStartThread = new CountDownLatch(1);
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                processName = "Extract Audio Information";
                if (showProgressWindows == true) {
                    ProgressWindow progressWindow = new ProgressWindow(getAudioProcessor());
                    progressWindow.displayWindow();
                }
                 countDownLatchUtilityThread = new CountDownLatch(1);
                if(updateLabel!=null) {
                    updateLabel.setText(numberOfFilesToProcess + " of " + numberOfFilesToProcess + "Files Processed");
                }

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        boolean done=false;
                        while(!(utilityThread.isInterrupted())&& done==false) {
                            getAudioInformationFunction(path);
                            done=true;
                        }
                        countDownLatchUtilityThread.countDown();
                    }
                };
                utilityThread = new Thread(runnable);
                utilityThread.start();
                try {
                    countDownLatchUtilityThread.await();
                } catch (InterruptedException e) {
                    errorMessages.add(e.getMessage());
                    updateLabel.setText("Thread Iterrupted");
                    utilityThread.interrupt();
                }
                if(updateLabel!=null) {
                    updateLabel.setText("operation Competed with " + errorMessages.size() + " errors");
                }

                if(showCompletedNotification==true){
                    if(errorMessages.size()==0) {
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);
                    }
                    else{
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);
                    }
                }
                countDownLatchStartThread.countDown();
            }
        };
        startThread= new Thread(runnable);
        startThread.start();
        try {
            countDownLatchStartThread.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return informationList;
    }
    private AudioProcess getAudioProcessor() {
        return this;
    }
    public List<AudioInformation> getAudioInformationFunction(String path){ // returns  a list of audio information
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((FileFilter) null);// get all files  in folder
        if(listOfFiles!=null){
        int size = listOfFiles.length;
        numberOfFilesToProcess = numberOfFilesToProcess + size;
        if(progressBar!=null) {
            progressBar.setFilesToProcess(numberOfFilesToProcess);
        }

        for (int count = 0; count < size; count++) {
            File file = listOfFiles[count];
            boolean read=false;
            if (!(file.getName().charAt(0) == exclude)) { // if   starts with exclude charcter  ignore
                if (file.isFile()) {
                    boolean isAudioFile = utilities.isAudioFile(file); // check if file is audio file
                    if (isAudioFile == true) {
                        AudioInformation information=extractAudioInformation.extractAudioInformationFromFile(file);
                        if(onCD==true) {
                            information.setWriteFieldsToFile(false);
                        }

                            informationList.add(information);

                    }
                    else if(utilities.getExtensionOfFile(file).equalsIgnoreCase("cue")){
                        try {
                            CueSheet cueSheet=extractAudioInformation.getCueSheet(file);
                            List<AudioInformation> tracks=cueSheet.getTracks();
                            informationList.addAll(tracks);
                        } catch (CueSheetExeception cueSheetExeception) {
                            cueSheetExeception.printStackTrace();
                            errorMessages.add(cueSheetExeception.getMessage());
                        }
                    }
                    numberOfFilesProcessed++;
                    if(progressBar!=null) {
                        progressBar.setFilesProcessed(numberOfFilesProcessed);
                    }
                } else if (file.isDirectory()) {
                    numberOfFilesProcessed++;
                    if(progressBar!=null) {

                        progressBar.setFilesProcessed(numberOfFilesProcessed);
                    }

                    getAudioInformationFunction(listOfFiles[count].getPath());
                }
                if(updateLabel!=null) {
                    updateLabel.setText(numberOfFilesToProcess + " of " + numberOfFilesToProcess + "Files Processed");
                }

            }
        }
        }
        return informationList;
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
    public CountDownLatch getCountDownLatchUtilityThread() {
        return countDownLatchUtilityThread;
    }
    public void setCountDownLatchUtilityThread(CountDownLatch countDownLatchUtilityThread) {
        this.countDownLatchUtilityThread = countDownLatchUtilityThread;
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

    public boolean isOnCD() {
        return onCD;
    }

    public void setOnCD(boolean onCD) {
        this.onCD = onCD;
    }
}
