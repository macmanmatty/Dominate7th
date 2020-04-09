package sample.InputDevices;

import javafx.application.Platform;
import org.apache.commons.lang3.StringUtils;
import sample.Library.Playlist;
import sample.MainAudioWindow;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class DeviceInputThread {// looped  thread that calls a check method on various  system device getters.  it does this until intruppted
    private  Thread checkThread;
    private MainAudioWindow window;
    private int counter;

    List<InputDevice> inputDevices = Collections.synchronizedList(new ArrayList<>());



    public DeviceInputThread(MainAudioWindow window) {
        this.window = window;


    }



    public void start() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                while (!checkThread.isInterrupted()) {
                   deviceCheck();


                }

            }
        };

        checkThread = new Thread(runnable);
        checkThread.start();

    }

    private void deviceCheck() {
        for (FileStore store : FileSystems.getDefault().getFileStores()) {
            String type = store.type();
            String path=getPath(store.toString());
            if (isCD(type) &&  !inPaths(path)) {

                InputDevice device= new CDDevice( window.getSystemInfo(),path, new File(path).getName(), store);
                device.addDevice();
                device.makePlaylist();
                Playlist playList=device.getPlaylist();
                addPlaylistToWindow(playList);
                inputDevices.add(device);



            }
        }

        int size= inputDevices.size();
        for(int count=0; count<size; count++){
            InputDevice inputDevice=inputDevices.get(count);
            String path= inputDevice.getPath();
            boolean exists=new File(path).exists();
            if(exists==false){
                inputDevice.onRemoved();

                    removePlaylistFromWindow(inputDevice.getPlaylist());
                    inputDevices.remove(inputDevice);
            }

        }

        if(counter%30==0){
            window.getMainScene().setPressed(false);
        }
        counter++;





    }

    private void addPlaylistToWindow(Playlist playlist) {

        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                window.addPlaylist(playlist);
            }
        };
        Platform.runLater(runnable);
    }


    private void removePlaylistFromWindow(Playlist playlist) {

        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                window.deletePlaylist(playlist);
            }
        };
        Platform.runLater(runnable);
    }
    private boolean isCD(String type){


        if(StringUtils.containsIgnoreCase(type, "cddafs") || StringUtils.containsIgnoreCase(type, "cdfs")){

            return true;
        }


        return false;

    }

    public List<Playlist> getPlayLists(){
        List<Playlist> playlists= new ArrayList<>();
        int size=inputDevices.size();
        for(int count=0; count<size; count++){
            playlists.add(inputDevices.get(count).getPlaylist());
        }

        return  playlists;

    }


    private String getPath(String toString) {
        String[] path=toString.split(Pattern.quote("("));
        String fullPath=path[0].substring(0,path[0].length()-1);
        return fullPath;


    }

    private boolean inPaths(String path) {
        int size= inputDevices.size();
        for (int count=0; count<size; count++){


            if(path.equalsIgnoreCase(inputDevices.get(count).getPath())){

                return  true;
            }
            else{

            }

        }
        return false;


    }

    public Thread getCheckThread() {
        return checkThread;
    }

    public void setCheckThread(Thread checkThread) {
        this.checkThread = checkThread;
    }

    public List<InputDevice> getInputDevices() {
        return inputDevices;
    }

    public void setInputDevices(List<InputDevice> inputDevices) {
        this.inputDevices = inputDevices;
    }
    public void stop(){

        checkThread.interrupt();
    }
}
