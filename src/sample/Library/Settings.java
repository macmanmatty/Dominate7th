package sample.Library;

import org.jaudiotagger.tag.FieldKey;
import sample.AudioProcessors.AudioCodec;
import sample.AudioProcessors.Encoders.ChannelMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Settings {

   public transient  final  List<FieldKey> usedFieldKeys;

  private  boolean sortLibraryByFileType;
  private  boolean  sortLibraryByBitRate;
   private  String libraryPath;
    private boolean moveFilesToLibraryFolder;
    private boolean scanLibraryFoldreForNewSongs;
    private boolean getMissingTagsOnAddingSongsToLibrary;
    private boolean  showNotificationsOnSongChange;
    private boolean  showNotifcationOnProcessComplete;
    private boolean showProcessNotifications;
    private boolean saveLibraryOnEdits;
    private boolean saveLibraryOnExit;
    private boolean copyFilesToLibraryFolder;
    private boolean addAllAddedSongsToLibrary;
    private AudioCodec defaultImportCodec=AudioCodec.AV_CODEC_ID_MP3;
    private int defaultImportBitRate=320000;
    private ChannelMode defaultImportChannelMode=ChannelMode.STEREO;
    private HashMap<String, String> defaultImportFFmpegOptions= new HashMap<>();
    private boolean autoImportCds;
    private String graceNoteUserId;
    private boolean showMiniPlayerOnMainWindowClose;









    private String fpCalcPath;






    public Settings() {
        saveLibraryOnExit=true;



        List <FieldKey> usedFieldKeys= new ArrayList<>();
          usedFieldKeys.add(FieldKey.ARTIST);
          usedFieldKeys.add(FieldKey.ALBUM);
         usedFieldKeys.add(FieldKey.BPM);
        usedFieldKeys.add(FieldKey.CATALOG_NO);
         usedFieldKeys.add(FieldKey.COMMENT);
         usedFieldKeys.add(FieldKey.COMPOSER);
         usedFieldKeys.add(FieldKey.COMMENT);
         usedFieldKeys.add(FieldKey.COPYRIGHT);
         usedFieldKeys.add(FieldKey.COUNTRY);
         usedFieldKeys.add(FieldKey.CUSTOM1); // user selected  and named custom 1
         usedFieldKeys.add(FieldKey.CUSTOM2); // BitRate
        usedFieldKeys.add(FieldKey.CUSTOM3); // audio format
        usedFieldKeys.add(FieldKey.CUSTOM4); // file path
          usedFieldKeys.add(FieldKey.DISC_NO);
         usedFieldKeys.add(FieldKey.DISC_TOTAL);
         usedFieldKeys.add(FieldKey.ENCODER);
          usedFieldKeys.add(FieldKey.ENGINEER);
         usedFieldKeys.add(FieldKey.ENSEMBLE);
         usedFieldKeys.add(FieldKey.GENRE);
         usedFieldKeys.add(FieldKey.GROUP);
          usedFieldKeys.add(FieldKey.INSTRUMENT);
         usedFieldKeys.add(FieldKey.LANGUAGE);
         usedFieldKeys.add(FieldKey.LYRICIST);
       usedFieldKeys.add(FieldKey.LYRICS);
         usedFieldKeys.add(FieldKey.MEDIA);
          usedFieldKeys.add(FieldKey.PRODUCER);
          usedFieldKeys.add(FieldKey.ORIGINAL_ARTIST);
         usedFieldKeys.add(FieldKey.RANKING);
        usedFieldKeys.add(FieldKey.RATING);
        usedFieldKeys.add(FieldKey.RECORD_LABEL);
        usedFieldKeys.add(FieldKey.TEMPO);
         usedFieldKeys.add(FieldKey.TITLE);
        usedFieldKeys.add(FieldKey.TRACK);
         usedFieldKeys.add(FieldKey.TRACK_TOTAL);
          usedFieldKeys.add(FieldKey.YEAR);
        this.usedFieldKeys= Collections.unmodifiableList(usedFieldKeys);










    }

    public List<FieldKey> getUsedFieldKeys() {
        return usedFieldKeys;
    }




    public boolean isSortLibraryByFileType() {
        return sortLibraryByFileType;
    }

    public void setSortLibraryByFileType(boolean sortLibraryByFileType) {
        this.sortLibraryByFileType = sortLibraryByFileType;
    }

    public boolean isSortLibraryByBitRate() {
        return sortLibraryByBitRate;
    }

    public void setSortLibraryByBitRate(boolean sortLibraryByBitRate) {
        this.sortLibraryByBitRate = sortLibraryByBitRate;
    }

    public String getLibraryPath() {
        return libraryPath;
    }

    public void setLibraryPath(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    public boolean isMoveFilesToLibraryFolder() {
        return moveFilesToLibraryFolder;
    }

    public void setMoveFilesToLibraryFolder(boolean moveFilesToLibraryFolder) {
        this.moveFilesToLibraryFolder = moveFilesToLibraryFolder;
    }



    public boolean isScanLibraryFoldreForNewSongs() {
        return scanLibraryFoldreForNewSongs;
    }

    public void setScanLibraryFoldreForNewSongs(boolean scanLibraryFoldreForNewSongs) {
        this.scanLibraryFoldreForNewSongs = scanLibraryFoldreForNewSongs;
    }


    public boolean isGetMissingTagsOnAddingSongsToLibrary() {
        return getMissingTagsOnAddingSongsToLibrary;
    }

    public void setGetMissingTagsOnAddingSongsToLibrary(boolean getMissingTagsOnAddingSongsToLibrary) {
        this.getMissingTagsOnAddingSongsToLibrary = getMissingTagsOnAddingSongsToLibrary;
    }

    public boolean isShowNotificationsOnSongChange() {
        return showNotificationsOnSongChange;
    }

    public void setShowNotificationsOnSongChange(boolean showNotificationsOnSongChange) {
        this.showNotificationsOnSongChange = showNotificationsOnSongChange;
    }

    public boolean isShowNotifcationOnProcessComplete() {
        return showNotifcationOnProcessComplete;
    }

    public void setShowNotifcationOnProcessComplete(boolean showNotifcationOnProcessComplete) {
        this.showNotifcationOnProcessComplete = showNotifcationOnProcessComplete;
    }

    public boolean isShowProcessNotifications() {
        return showProcessNotifications;
    }

    public void setShowProcessNotifications(boolean showProcessNotifications) {
        this.showProcessNotifications = showProcessNotifications;
    }

    public boolean isSaveLibraryOnEdits() {
        return saveLibraryOnEdits;
    }

    public void setSaveLibraryOnEdits(boolean saveLibraryOnEdits) {
        this.saveLibraryOnEdits = saveLibraryOnEdits;
    }

    public boolean isSaveLibraryOnExit() {
        return saveLibraryOnExit;
    }

    public void setSaveLibraryOnExit(boolean saveLibraryOnExit) {
        this.saveLibraryOnExit = saveLibraryOnExit;
    }

    public String getFpCalcPath() {
        return fpCalcPath;
    }

    public void setFpCalcPath(String fpCalcPath) {
        this.fpCalcPath = fpCalcPath;
    }

    public boolean isCopyFilesToLibraryFolder() {
        return copyFilesToLibraryFolder;
    }

    public void setCopyFilesToLibraryFolder(boolean copyFilesToLibraryFolder) {
        this.copyFilesToLibraryFolder = copyFilesToLibraryFolder;
    }

    public boolean isAddAllAddedSongsToLibrary() {
        return addAllAddedSongsToLibrary;
    }

    public void setAddAllAddedSongsToLibrary(boolean addAllAddedSongsToLibrary) {
        this.addAllAddedSongsToLibrary = addAllAddedSongsToLibrary;
    }

    public AudioCodec getDefaultImportCodec() {
        return defaultImportCodec;
    }

    public void setDefaultImportCodec(AudioCodec defaultImportCodec) {
        this.defaultImportCodec = defaultImportCodec;
    }

    public int getDefaultImportBitRate() {
        return defaultImportBitRate;
    }

    public void setDefaultImportBitRate(int defaultImportBitRate) {
        this.defaultImportBitRate = defaultImportBitRate;
    }

    public ChannelMode getDefaultImportChannelMode() {
        return defaultImportChannelMode;
    }

    public void setDefaultImportChannelMode(ChannelMode defaultImportChannelMode) {
        this.defaultImportChannelMode = defaultImportChannelMode;
    }

    public HashMap<String, String> getDefaultImportFFmpegOptions() {
        return defaultImportFFmpegOptions;
    }

    public void setDefaultImportFFmpegOptions(HashMap<String, String> defaultImportFFmpegOptions) {
        this.defaultImportFFmpegOptions = defaultImportFFmpegOptions;
    }

    public boolean isAutoImportCds() {
        return autoImportCds;
    }

    public void setAutoImportCds(boolean autoImportCds) {
        this.autoImportCds = autoImportCds;
    }

    public String getGraceNoteUserId() {
        return graceNoteUserId;
    }

    public void setGraceNoteUserId(String graceNoteUserId) {
        this.graceNoteUserId = graceNoteUserId;
    }

    public boolean isShowMiniPlayerOnMainWindowClose() {
        return showMiniPlayerOnMainWindowClose;
    }

    public void setShowMiniPlayerOnMainWindowClose(boolean showMiniPlayerOnMainWindowClose) {
        this.showMiniPlayerOnMainWindowClose = showMiniPlayerOnMainWindowClose;
    }
}
