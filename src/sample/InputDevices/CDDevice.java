package sample.InputDevices;
import sample.AudioProcessors.GetAudioFiles;
import sample.Library.AudioInformation;
import sample.Library.DevicePlaylist;
import sample.Utilities.SystemInfo;

import java.nio.file.FileStore;
import java.util.List;

public class CDDevice extends InputDevice { // gets  any available cds and creates a playlist pane for them


    public CDDevice( SystemInfo info, String name, String path, FileStore fileStore) {
        super(info, name, path, fileStore);
    }

    @Override
    public void makePlaylist() {
       playlist= new DevicePlaylist();
        playlist.setCDPlaylist(true);
        playlist.setName(deviceName);

         GetAudioFiles getAudioFiles= new GetAudioFiles(systemInfo, playlist);
        getAudioFiles.setAddSongsToCurrentPlayList(true);
        getAudioFiles.setAddSongsToLibrary(false);
        getAudioFiles.setSortByBitRate(false);
        getAudioFiles.setMoveSongsToLibrary(false);
        getAudioFiles.setCopyFilesThenMove(false);
        getAudioFiles.setSortByFileType(false);
        getAudioFiles.setGetMissingTags(true);
        getAudioFiles.setOnCD(true);
        getAudioFiles.loadFiles(path, false);


    }

    @Override
    void addSongs(List<AudioInformation> tracks) {

    }


    @Override
    public void addDevice() {

    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public void onRemoved() {

    }


}
