package sample.AudioProcessors;

import sample.Library.AudioInformation;
import sample.Utilities.AudioFileUtilities;
import sample.Windows.FileProgressBar;
import sample.Windows.Notifications;
import sample.Windows.UpdateLabel;
import tray.notification.NotificationType;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FileProcessor implements AudioProcess {

  private   FileAction action;// the action to preform on the audio files
   private  ArrayList<String> errorMessages= new ArrayList<>();// error messages generated in the processing of the files
   private AudioFileUtilities utilities;
   private Thread utilityThread;// the thread to run the process on
    private Thread startThread;
    private OnFinishAction onFinishAction;
    private boolean showNotifications;
    private boolean showCompletedNotification;

   private CountDownLatch externalCountDownLatch; // thread count down latch.
    private UpdateLabel updateLabel= new UpdateLabel(); // label for  displaying the progress ina progress window.
    private FileProgressBar progressBar= new FileProgressBar(); // progress bar to display the progress in the progress window.
   private int numberOfFilesToProcess; // number of files to process
   private int numberOfFilesProcessed; // number files that have been processed
   private Character exclude= new Character('?'); // folders or files that start with this char will be ingored by the processor
   private String processName; // the name of the process being executed.
    private Notifications notification= new Notifications();
    private List<Object> results= new ArrayList<>();
    private List<AudioInformation> tracks= new ArrayList<>();
    private String fileSeperator;


    public FileProcessor( String fileSeperator,FileAction action) {
        this.action = action;
        processName=action.getActionName();
        utilities= new AudioFileUtilities(); // audio utilities class
        this.fileSeperator=fileSeperator;
    }

    public FileProcessor(FileAction action, CountDownLatch externalCountDownLatch, UpdateLabel updateLabel, FileProgressBar progressBar) {
        this.action = action;
        processName=action.getActionName();

        this.externalCountDownLatch = externalCountDownLatch;
        this.updateLabel = updateLabel;
        this.progressBar = progressBar;
    }

    public FileProcessor(FileAction action , UpdateLabel updateLabel, FileProgressBar progressBar) {
       this.action=action;
        this.updateLabel = updateLabel;
        this.progressBar = progressBar;
    }

    public FileProcessor(FileAction action, CountDownLatch externalCountDownLatch) {
        this.action = action;
        processName=action.getActionName();

        this.externalCountDownLatch = externalCountDownLatch;
    }

    public void manipulateFiles(String path) { // recusivly finds all files in a given directory and manipulates them  with  the given audio file action.


        action.setUpdateLabel(updateLabel);
        action.setProgressBar(progressBar);
        Runnable runnable= new Runnable() {
            @Override
            public void run() {



                   CountDownLatch countDownLatch = new CountDownLatch(1 + action.getNumberOfThreads()); // create count down latch

                action.setCountDownLatch(countDownLatch); // set latch for  the audio process  so its thread(s) if any will set  the proper count down.
                updateLabel.setText(numberOfFilesProcessed+ " of "+numberOfFilesToProcess +"Files Processed");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        boolean done=false;
                        while(!(utilityThread.isInterrupted()) && done==false) {
                            done = manipulateAudioFilesFunction(path); // manuiplate the audio files
                        }
                        tracks=action.getTracks();

                        countDownLatch.countDown(); // thread is finished notify countDownLatch


                    }

                };

                utilityThread = new Thread(runnable);
                utilityThread.start();

                try {
                    countDownLatch.await();// wait till execution is done


                }

                    catch (InterruptedException e) {
                        errorMessages.add(e.getMessage());
                        utilityThread.interrupt();
                        e.printStackTrace();
                        updateLabel.setText("Error Thread Interrupted");
                    }



                    progressBar.setProgress(1);

                    if(onFinishAction!=null) {
                        onFinishAction.act();
                    }
                errorMessages.addAll(action.getErrorMessages());
                updateLabel.setText("operation Competed with " + errorMessages.size() + " errors"); // update the label

                if(showCompletedNotification==true){
                    if(errorMessages.size()==0) {
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);
                    }
                    else{
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);
                    }
                }
                if(externalCountDownLatch!=null){
                    externalCountDownLatch.countDown();
                }

            }


        };

        startThread= new Thread(runnable);
        startThread.start();



    }

    public boolean manipulateAudioFilesFunction( String path){ //  recursively performes  a supplied  action on all audio files in the directory


        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((FileFilter) null); // get all files in folder

        int size;
        if(listOfFiles==null) {
            size=0;
        }
        else {
            size = listOfFiles.length;
        }

        numberOfFilesToProcess = numberOfFilesToProcess + size;
        progressBar.setFilesToProcess(numberOfFilesToProcess);

        for (int count = 0; count < size; count++) {
            File file = listOfFiles[count];

            if (!(file.getName().charAt(0) == exclude)) {
                if (file.isFile()) {

                            action.action(file); // preforme the action


                    numberOfFilesProcessed++;
                    progressBar.setFilesProcessed(numberOfFilesProcessed);// update progress bar.
                    if(showNotifications==true){

                        notification.showNotification(action);

                    }

                } else if (file.isDirectory()) { // look for  more files y calling function again


                    manipulateAudioFilesFunction(listOfFiles[count].getPath());
                    numberOfFilesProcessed++;
                    progressBar.setFilesProcessed(numberOfFilesProcessed);// update progress bar.

                }
                updateLabel.setText(numberOfFilesProcessed + " of "+numberOfFilesToProcess + "Audio Files Processed");

            }


        }



        return true;
    }


    public void manipulateFiles(List<File> files) { // recusivly finds all files in a given directory and manipulates them  with  the given audio file action.

        numberOfFilesToProcess=files.size();
        action.setUpdateLabel(updateLabel);
        action.setProgressBar(progressBar);
        Runnable runnable= new Runnable() {
            @Override
            public void run() {



                  CountDownLatch  countDownLatch = new CountDownLatch(1 + action.getNumberOfThreads()); // create count down latch


                action.setCountDownLatch(countDownLatch); // set latch for  the audio process  so its thread(s) if any will set  the proper count down.
                updateLabel.setText(numberOfFilesProcessed+ " of "+numberOfFilesToProcess +"Files Processed");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        boolean done=false;
                        int size=files.size();
                        out:
                        for(int count=0; count<size; count++){
                            if(!utilityThread.isInterrupted()) {
                                action.action(files.get(count));
                                numberOfFilesProcessed++;
                                progressBar.setFilesProcessed(numberOfFilesProcessed);// update progress bar.
                                updateLabel.setText(numberOfFilesProcessed+ " of "+numberOfFilesToProcess +"Files Processed");

                                if(showNotifications==true){

                                    notification.showNotification(action);

                                }
                            }
                            else{

                                break out;

                            }
                        }

                        tracks=action.getTracks();

                        countDownLatch.countDown(); // thread is finished notify countDownLatch


                    }

                };

                utilityThread = new Thread(runnable);
                utilityThread.start();

                try {
                    countDownLatch.await();// wait till execution is done
                    updateLabel.setText("operation Competed with " + errorMessages.size() + " errors"); // update the label

                }

                catch (InterruptedException e) {
                    errorMessages.add(e.getMessage());
                    utilityThread.interrupt();
                    e.printStackTrace();
                    updateLabel.setText("Error Thread Interrupted");
                }

                if (errorMessages.size() > 0) {
                    utilities.saveErrorMessages(errorMessages, "/users/AudioApp/errors");

                }


                progressBar.setProgress(1);
                if(onFinishAction!=null) {
                    onFinishAction.act();
                }



                if(showCompletedNotification==true){
                    if(errorMessages.size()==0) {
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);
                    }
                    else{
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);
                    }
                }

                if(externalCountDownLatch!=null){
                    externalCountDownLatch.countDown();
                }


            }


        };

        startThread= new Thread(runnable);
        startThread.start();



    }


    @Override
    public void stopProcess() {

        if(startThread!=null) {


            if (utilityThread != null) {
                action.stopAction();

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
        return processName;
    }

    public void setUpdateLabel(UpdateLabel updateLabel) {
        this.updateLabel = updateLabel;
    }

    public void setProgressBar(FileProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public AudioProcess getProcess(){
        return this;

    }

    public OnFinishAction getOnFinishAction() {
        return onFinishAction;
    }

    public void setOnFinishAction(OnFinishAction onFinishAction) {
        this.onFinishAction = onFinishAction;
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

    public List<AudioInformation> getTracks() {
        return tracks;
    }

    public void setExternalCountDownLatch(CountDownLatch externalCountDownLatch) {
        this.externalCountDownLatch = externalCountDownLatch;
    }

    public int getTotalThreads(){
        return 1+action.getNumberOfThreads();
    }
}
