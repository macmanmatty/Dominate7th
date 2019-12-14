package sample.AudioProcessors;

import sample.Windows.FileProgressBar;
import sample.Windows.UpdateLabel;

public interface AudioProcess {


    void stopProcess();
    UpdateLabel getUpdateLabel();
    FileProgressBar getProgressBar();
    String getProcessName();





}
