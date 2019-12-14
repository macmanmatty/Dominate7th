package sample.AudioProcessors;

import sample.Library.AudioInformation;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface AudioInformationAction extends Action {

    void action(AudioInformation information);
    String getActionName();
    int getNumberOfThreads();
    void setCountDownLatch(CountDownLatch latch);
    public void stopAction();
    public boolean completed();
    int getCurrentErrors();
    List<AudioInformation> getTracks();





}
