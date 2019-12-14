package sample.AudioProcessors;


import org.jaudiotagger.audio.AudioFile;
import sample.Library.AudioInformation;
import sample.Windows.FileProgressBar;
import sample.Windows.UpdateLabel;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface FileAction extends Action{


        public abstract  void action(File file); // the action to be preformed
        public abstract List<String> getErrorMessages(); //  gets list of errors   the operation my generate
        public String getActionName(); //gets the action name
        public int getNumberOfThreads();
        public void setCountDownLatch(CountDownLatch latch);
        public void stopAction();
        public void setProgressBar(FileProgressBar progressBar);
        public void setUpdateLabel(UpdateLabel label);
        List<AudioInformation> getTracks();













}