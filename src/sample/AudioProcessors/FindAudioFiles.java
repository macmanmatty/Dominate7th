package sample.AudioProcessors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import sample.Utilities.AudioFileUtilities;
import sample.Windows.FileProgressBar;
import sample.Windows.Notifications;
import sample.Windows.ProgressWindow;
import sample.Windows.UpdateLabel;
import tray.notification.NotificationType;

import javax.management.Notification;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FindAudioFiles implements AudioProcess {

    private AudioFileAction action;
    private  ArrayList<String> errorMessages= new ArrayList<>();
    private AudioFileUtilities utilities= new AudioFileUtilities();
    private Thread utilityThread;
    private Thread startThread;

    private CountDownLatch countDownLatch;
    private UpdateLabel updateLabel= new UpdateLabel();
    private FileProgressBar progressBar= new FileProgressBar();
    private boolean showProgressWindows;
    private int numberOfFilesToProcess;
    private int numberOfFilesProcessed;
    private Character exclude= new Character('?');
    private String processName;
    private Notifications notification= new Notifications();
    private boolean showCompletedNotification;


    private List<AudioFile> audioFiles= new ArrayList<>();

    public FindAudioFiles(boolean showProgressWindows) {
        this.showProgressWindows = showProgressWindows;
    }

    public List<AudioFile> findAudioFiles(String path) {

        Runnable runnable= new Runnable() {
            @Override
            public void run() {


                processName = "Find Audio Files";
                if (showProgressWindows == true) {
                    ProgressWindow progressWindow = new ProgressWindow(getAudioProcess());
                    progressWindow.displayWindow();
                }

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {


                        boolean done=true;
                        while(!(utilityThread.isInterrupted()) && done==false)
                        findAudioFilesFunction(path);
                        countDownLatch.countDown();


                    }

                };
                utilityThread = new Thread(runnable);
                utilityThread.start();
                countDownLatch = new CountDownLatch(1);
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    errorMessages.add(e.getMessage());
                    utilityThread.interrupt();
                }

                errorMessages.addAll(action.getErrorMessages());
                updateLabel.setText("operation Competed with " + errorMessages.size() + " errors");
                if(showCompletedNotification==true){
                    if(errorMessages.size()==0) {
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.SUCCESS);

                    }
                    else{
                        notification.showAudioProcessNotification(processName + "operation Competed with " + errorMessages.size() + " errors", NotificationType.ERROR);

                    }

                }
            }
        };
        startThread= new Thread(runnable);
        startThread.start();

        return  audioFiles;


    }
    public List<AudioFile> findAudioFiles(List<File> files) {

        Runnable runnable= new Runnable() {
            @Override
            public void run() {


                processName = "Find Audio Files";
                if (showProgressWindows == true) {
                    ProgressWindow progressWindow = new ProgressWindow(getAudioProcess());
                    progressWindow.displayWindow();
                }

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {



                        int size=files.size();
                        boolean done=false;
                        while(!(utilityThread.isInterrupted())&& done==false) {
                            for (int count = 0; count < size; count++) {
                                String path = files.get(count).getAbsolutePath();
                                done=findAudioFilesFunction(path);

                            }
                            done=true;

                        }

                        countDownLatch.countDown();

                    }

                };
                utilityThread = new Thread(runnable);
                utilityThread.start();
                countDownLatch = new CountDownLatch(1);
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    errorMessages.add(e.getMessage());
                    utilityThread.interrupt();
                }

                updateLabel.setText("operation Competed with " + errorMessages.size() + " errors");


            }
        };
        startThread= new Thread(runnable);
        startThread.start();

        return  audioFiles;


    }


    private boolean findAudioFilesFunction(String path) {





        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((FileFilter) null);


        int size = listOfFiles.length;
        numberOfFilesToProcess = numberOfFilesToProcess + size;
        progressBar.setFilesToProcess(numberOfFilesToProcess);


        for (int count = 0; count < size; count++) {
            File file = listOfFiles[count];

            if (!(file.getName().charAt(0) == exclude)) {
                if (file.isFile()) {
                    boolean isAudioFile = utilities.isAudioFile(file);
                    System.out.println(isAudioFile);
                    if (isAudioFile == true) {
                        try {
                            AudioFile audioFile = AudioFileIO.read(file);
                            audioFiles.add(audioFile);

                        } catch (CannotReadException e) {
                            e.printStackTrace();
                            errorMessages.add(e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                            errorMessages.add(e.getMessage());

                        } catch (TagException e) {
                            e.printStackTrace();
                            errorMessages.add(e.getMessage());

                        } catch (ReadOnlyFileException e) {
                            e.printStackTrace();
                            errorMessages.add(e.getMessage());

                        } catch (InvalidAudioFrameException e) {
                            e.printStackTrace();
                            errorMessages.add(e.getMessage());

                        }
                    }
                    numberOfFilesProcessed++;
                    progressBar.setFilesProcessed(numberOfFilesProcessed);

                } else if (file.isDirectory()) {
                    numberOfFilesProcessed++;
                    progressBar.setFilesProcessed(numberOfFilesProcessed);

                    findAudioFilesFunction(listOfFiles[count].getPath());


                }

            }
            countDownLatch.countDown();

        }





        return true;

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

    public AudioProcess getAudioProcess(){

        return this;

    }

    public boolean isShowCompletedNotification() {
        return showCompletedNotification;
    }

    public void setShowCompletedNotification(boolean showCompletedNotification) {
        this.showCompletedNotification = showCompletedNotification;
    }
}
