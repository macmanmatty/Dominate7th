package sample.AudioProcessors;

import sample.Library.AudioInformation;
import sample.Utilities.AudioFileUtilities;
import sample.Windows.FileProgressBar;
import sample.Windows.Notifications;
import sample.Windows.UpdateLabel;
import tray.notification.NotificationType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AudioInformationProcessor implements AudioProcess {

  private   AudioInformationAction action;// the action to preform on the audio files
   private  ArrayList<String> errorMessages= new ArrayList<>();// error messages generated in the processing of the files
   private Thread utilityThread;// the thread to run the process on
    private Thread startThread;


   private CountDownLatch countDownLatch; // thread count down latch.
    private UpdateLabel updateLabel= new UpdateLabel(); // label for  displaying the progress ina progress window.
    private FileProgressBar progressBar= new FileProgressBar(); // progress bar to display the progress in the progress window.
   private int numberOfFilesToProcess; // number of files to process
   private int numberOfFilesProcessed; // number files that have been processed
   private String processName; // the name of the process being executed.
    private List<AudioInformation> songs;
    private OnFinishAction onFinishAction;
    private boolean  showCompletedNotification;
    private boolean showNotifications;
    private Notifications notification = new Notifications();

    public AudioInformationProcessor(AudioInformationAction action) {
        this.action = action;
        processName=action.getActionName();
    }

    public AudioInformationProcessor(AudioInformationAction action, CountDownLatch countDownLatch, UpdateLabel updateLabel, FileProgressBar progressBar) {
        this.action = action;
        processName=action.getActionName();

        this.countDownLatch = countDownLatch;
        this.updateLabel = updateLabel;
        this.progressBar = progressBar;
    }

    public AudioInformationProcessor(AudioInformationAction action , UpdateLabel updateLabel, FileProgressBar progressBar) {
       this.action=action;
        this.updateLabel = updateLabel;
        this.progressBar = progressBar;
    }

    public AudioInformationProcessor(AudioInformationAction action, CountDownLatch countDownLatch) {
        this.action = action;
        processName=action.getActionName();

        this.countDownLatch = countDownLatch;
    }

    public void manipulateAudioInformation(List<AudioInformation> songs) {
        this.songs=songs;
        System.out.println("Songs to Manuiplate "+songs.size());

        Runnable runnable= new Runnable() {
            @Override
            public void run() {


                countDownLatch = new CountDownLatch(1 + action.getNumberOfThreads()); // create count down latch
                action.setCountDownLatch(countDownLatch); // set latch for  the audio process  so its thread(s) if any will set  the proper count down.
                updateLabel.setText(numberOfFilesProcessed + " of " + numberOfFilesToProcess + "Files Processed");

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {


                        numberOfFilesToProcess = songs.size();
                        boolean done = false;
                        while (!(utilityThread.isInterrupted()) && done == false) {
                            for (int count = 0; count < numberOfFilesToProcess; count++) {
                                manipulateAudioInformationFunction(songs.get(count)); // manuiplate the audio files
                            }
                            done = true;

                        }

                        countDownLatch.countDown(); // thread is finished notify countDownLatch


                    }

                };

                utilityThread = new Thread(runnable);
                utilityThread.start();

                try {
                    countDownLatch.await();// wait till execution is done


                } catch (InterruptedException e) {
                    errorMessages.add(e.getMessage());
                    utilityThread.interrupt();
                    e.printStackTrace();
                    updateLabel.setText("Error Thread Interrupted");

                }
                //updateLabel.setText("operation Competed with " + errorMessages.size() + " errors"); // update the label



                progressBar.setProgress(1);
                if(onFinishAction!=null) {
                    onFinishAction.act();
                }
                errorMessages.addAll(action.getErrorMessages());
                updateLabel.setText("operation Competed with " + errorMessages.size() + " errors"); // update the label

                if (showCompletedNotification == true) {
                    if (errorMessages.size() == 0) {
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);

                    } else {
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);

                    }

                }







            }


        };

        startThread= new Thread(runnable);
        startThread.start();



    }

    public void manipulateAudioInformationFunction(AudioInformation information){
        action.action(information); // perform the action
                    numberOfFilesProcessed++;
                    progressBar.setFilesProcessed(numberOfFilesProcessed);// update progress bar.
                updateLabel.setText(numberOfFilesProcessed + " of "+numberOfFilesToProcess + "Audio Files Processed");
                if(showNotifications==true){

                    notification.showNotification(action);

                }

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

    public int getTotalThreads(){
        return 1+action.getNumberOfThreads();
    }
}
