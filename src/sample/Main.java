package sample;

import com.google.gson.JsonParseException;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import sample.Library.LibraryLoader;
import sample.Library.MusicLibrary;
import sample.Library.Settings;
import sample.Library.SmartPlaylist;
import sample.Utilities.SystemInfo;
import sample.Windows.OptionPane;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    MusicLibrary library;
    SystemInfo info =new SystemInfo();

    @Override
    public void init() throws Exception {

        super.init();
        library= new MusicLibrary();

        String userHomePath=info.getUserHomePath();
        String fileSeperator=info.getFileSeperator();
        LibraryLoader loader= new LibraryLoader(userHomePath, fileSeperator);
        File file= new File(userHomePath+fileSeperator+"dominate7th"+fileSeperator+"library.json");

        if(file.exists()==false){ // create library if it does  not exist

            loader.saveLibrary(library); // make the library file
            SmartPlaylist playlist= library.newSmartPlayList();
            playlist.setName("All Songs");


        }

        else {




            try {
                library = loader.loadLibrary(); // load library
                Settings settings = library.getSettings();
                if (settings.isScanLibraryFoldreForNewSongs()) {
                    library.checkForNewSongsinLibraryFolder();
                }
                library.updateSmartPlaylists();
            }
            catch (JsonParseException | IOException e){


                new OptionPane().showOptionPane("Library Could Not Be Loaded at "+userHomePath+fileSeperator+"dominate7th"+fileSeperator+"library.json", "OK");

            }




        }






    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        MainAudioWindow window=null;

        window= new MainAudioWindow(primaryStage, library, info);
        window.displayWindow();



    }


    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, AudioWindowPreLoader.class, args);


    }
}
