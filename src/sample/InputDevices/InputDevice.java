package sample.InputDevices;

import sample.Library.AudioInformation;
import sample.Library.DevicePlaylist;
import sample.Utilities.SystemInfo;

import java.io.File;
import java.nio.file.FileStore;
import java.util.List;
import java.util.regex.Pattern;

public abstract class InputDevice {
    protected String deviceName;
    protected String path;
    protected DevicePlaylist playlist;
    protected FileStore fileStore;
    protected File mainDirectory;
    String playlistPath;
    SystemInfo systemInfo;


    public InputDevice(SystemInfo systemInfo, String path, String deviceName, FileStore fileStore) {
        this.deviceName = deviceName;
        this.systemInfo=systemInfo;
        this.path = path;
        this.fileStore = fileStore;
        this.path=getPath(fileStore.toString());
        this.mainDirectory =new File(path);
       playlistPath=path+"/music/playlist.json";

    }

    DevicePlaylist getPlaylist(){
        
        return  playlist;
    }
     abstract void makePlaylist();
     abstract  void addSongs(List<AudioInformation> tracks);
    abstract  void addDevice();
    abstract void onRemoved();


    String getPath(){
        
        return  path;
        
    }
    void setPath(String path){
        this.path=path;
        
        
    }
            
    
    String getDeviceName(){
        return deviceName;
        
    }

    private String getPath(String toString) {
        String[] path=toString.split(Pattern.quote("("));
        String fullPath=path[0].substring(0,path[0].length()-1);
        return fullPath;


    }

    public FileStore getFileStore() {
        return fileStore;
    }
}
