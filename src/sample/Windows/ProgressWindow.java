package sample.Windows;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.AudioProcessors.AudioProcess;
public class ProgressWindow {
    private UpdateLabel updateLabel;
    private FileProgressBar progressBar;
   Label processName;
   Stage stage;
   AudioProcess process;
   Button stop;
   String processNameString;
   int xSize;
   int ySize;

    public ProgressWindow( AudioProcess process) {
        processNameString=process.getProcessName();
        this.process=process;
        this.updateLabel=process.getUpdateLabel();
        this.progressBar=process.getProgressBar();
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                stage = new Stage();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() { // terminate process on window close
                    @Override
                    public void handle(WindowEvent event) {
                        process.stopProcess();
                        stage.hide();
                    }
                });
            }
        };
        Platform.runLater(runnable);

    }


    public ProgressWindow( AudioProcess process, int xSize, int ySize) {
        processNameString=process.getProcessName();
        this.process=process;
        this.xSize=xSize;
        this.ySize=ySize;
        this.updateLabel=process.getUpdateLabel();
        this.progressBar=process.getProgressBar();
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                stage = new Stage();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() { // terminate process on window close
                    @Override
                    public void handle(WindowEvent event) {
                        process.stopProcess();
                        stage.hide();
                    }
                });
            }
        };
        Platform.runLater(runnable);


    }
    public void displayWindow(){ // called using platform.runlater so it is  thread safe  basic ui box that displays  a progress  bar
        // and a label that updates with the  progress.
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                VBox mainBox = new VBox();
                processName = new Label(processNameString);
                stop = new Button("Stop Process");
                stop.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        process.stopProcess();
                        updateLabel.setText("Process Stopped");

                        stage.hide();
                    }
                });
                mainBox.getChildren().add(processName);
                mainBox.getChildren().add(progressBar.getProgressBar());
                mainBox.getChildren().add(updateLabel.getLabel());
                mainBox.getChildren().add(stop);
                stage.setScene(new Scene(mainBox));
                stage.show();
            }
        };
        Platform.runLater(runnable);

    }
    public VBox getWindow(){ // creates the vbox and returns it
                VBox mainBox = new VBox();
                processName = new Label(processNameString);
                stop = new Button("Stop Process");
                stop.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        process.stopProcess();
                        updateLabel.setText("Process Stopped");
                        stage.hide();
                    }
                });
                mainBox.getChildren().add(processName);
                mainBox.getChildren().add(progressBar.getProgressBar());
                mainBox.getChildren().add(updateLabel.getLabel());
                mainBox.getChildren().add(stop);

                return mainBox;
    }
}
