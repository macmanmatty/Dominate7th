package sample.AudioProcessors.CueSheets;

public enum CueSheetFieldKey {
    TITLE("TILE"), REM_TITLE("REM TITLE"), REM_DATE("REM DATE"), REM_GENRE("REM GENRE"), PERFORMER("PERFORMER"), REM_PERFORMER("REM PERFORMER"), REM_DISCID("REM DISCID"), REM_DISC_NO("REM DISCNO"), REM_DISC_TOTAL("REM DISCTOTAL");



    String text;

    CueSheetFieldKey(String text) {
        this.text = text;
    }

    public String getCueSheetText() {
        return text;
    }


}
