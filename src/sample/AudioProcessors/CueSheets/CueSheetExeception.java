package sample.AudioProcessors.CueSheets;

public class CueSheetExeception extends Exception {// thrown if a .cue sheet cannot be correctly parsed.


    public CueSheetExeception(String message) {
        super(message);
    }

    public CueSheetExeception(String message, Throwable cause) {
        super(message, cause);
    }
}
