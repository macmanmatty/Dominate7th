package sample.AudioProcessors;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface Action {
    public abstract List<String> getErrorMessages(); //  gets list of errors   the operation my generate
    public String getActionName(); //gets the action name
    public int getNumberOfThreads();
    public void setCountDownLatch(CountDownLatch latch);
    public void stopAction();
    public int getCurrentErrors();
    public boolean completed();
    public String getFileName();

}
