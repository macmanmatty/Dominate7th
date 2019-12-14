package sample.Windows;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

public class FileProgressBar {
  private   int filesToProcess=1;
  private   int filesProcessed=1;
  private ProgressBar progressBar = new ProgressBar();


    public FileProgressBar(int filesToProcess) {
        this.filesToProcess = filesToProcess;
    }

    public void updateProgressBar(double progress){



        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);

            }
        };
        Platform.runLater(runnable);


    }

    public int getFilesToProcess() {
        return filesToProcess;
    }

    public FileProgressBar() {
    }

    public void setFilesToProcess(int filesToProcess) {
        this.filesToProcess = filesToProcess;

    }

    public int getFilesProcessed() {
        return filesProcessed;
    }

    public void setFilesProcessed(int filesProcessed) {
        this.filesProcessed = filesProcessed;

    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgress(int progress) {
        Runnable runnable= new Runnable() {
            @Override
            public void run() {


                progressBar.setProgress(progress);
            }
        };
        Platform.runLater(runnable);


    }
}
