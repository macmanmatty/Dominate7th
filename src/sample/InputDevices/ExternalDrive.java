package sample.InputDevices;

import sample.AudioProcessors.*;
import sample.Library.*;
import sample.Utilities.SystemInfo;

import java.io.File;
import java.nio.file.FileStore;
import java.util.List;

public class ExternalDrive extends InputDevice implements AudioInformationAddable {


    public ExternalDrive(SystemInfo info, String name, String path, FileStore fileStore) {
        super( info , name, path, fileStore);

    }

    @Override
    public void addSongs(List<AudioInformation> tracks) {
       AudioInformationProcessor processor= new AudioInformationProcessor((new AudioFileSorter(systemInfo.getFileSeperator(),path, false, false, true, true, true, false)));
       processor.manipulateAudioInformation(tracks);
       playlist.addSongs(tracks);




    }


    @Override
    public void makePlaylist() {


    }



    @Override
    public void addDevice() {
        LibraryLoader loader= new LibraryLoader("", "");
        File file= new File("/Music/library.json");

        if(file.exists()==false){ // create library if it does  not exist


            loader.saveDevicePlaylist(playlist, playlistPath); // make the library file


        }

        else {


            playlist=loader.loadDevicePlaylist(playlistPath); // load library
            PlaylistSettings settings=playlist.getPlaylistSettings();


        }


    }


    @Override
    public void onRemoved() {

    }


}
