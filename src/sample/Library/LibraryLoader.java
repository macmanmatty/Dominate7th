package sample.Library;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LibraryLoader {//the loader class for library to seralize and deseralize it from json
    private Gson gson;
    private String fileSeperator;
    private String userHomePath;
    private String libraryPath;
    private String backUpLibraryPath;
    public LibraryLoader( String userHomePath, String fileSeperator) {
        this.fileSeperator=fileSeperator;
        this.userHomePath=userHomePath;
        this.libraryPath=userHomePath+fileSeperator+"Dominate7th"+fileSeperator+"library.json";
        this.backUpLibraryPath=userHomePath+fileSeperator+"Dominate7th"+fileSeperator+".backUp"+fileSeperator+"library.json";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(SimpleStringProperty.class, new SimpleStringAdapter());
        builder.registerTypeAdapter(SimpleIntegerProperty.class, new SimpleIntegerAdapter());
        gson=builder.create();
    }
    public MusicLibrary loadLibrary() throws IOException, JsonParseException {
        try {
            BufferedReader input = new BufferedReader(new FileReader(libraryPath));
           //convert the json string back to object
            try {
                MusicLibrary library = gson.fromJson(input, MusicLibrary.class);
                if (library == null) {
                    input = new BufferedReader(new FileReader(libraryPath));
                    library= gson.fromJson(input, MusicLibrary.class);
                    saveBackUpLibrary(library);
                    return library;
                }
                return library;
            } catch (JsonParseException e) {
                MusicLibrary library=null;
                input = new BufferedReader(new FileReader(backUpLibraryPath));
                try {
                     library = gson.fromJson(input, MusicLibrary.class);
                }
                catch (JsonParseException jpe) {
                    throw new JsonParseException(" Back Up Library  at  "+backUpLibraryPath+ "could not be parsed");
                }
                saveLibrary(library);
                return library;
            }
        } catch (FileNotFoundException e) {
            BufferedReader input = null;

                input = new BufferedReader(new FileReader(backUpLibraryPath));
                MusicLibrary library = gson.fromJson(input, MusicLibrary.class);
                saveLibrary(library);
                return library;

        }
    }

    public void saveLibrary(MusicLibrary library) {
        try {
            File file = new File(userHomePath+fileSeperator+"dominate7th");
            if(!file.exists()) {
                file.mkdirs();
            }


            file = new File(libraryPath);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            gson.toJson(library, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBackUpLibrary(MusicLibrary library) {
        try {
            File file = new File(userHomePath+fileSeperator+"dominate7th"+fileSeperator+".backUp");
            if(!file.exists()) {
                Files.createDirectories(Paths.get(file.getAbsolutePath()));
            }


            file = new File(backUpLibraryPath);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            gson.toJson(library, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public DevicePlaylist loadDevicePlaylist(String path) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(path));

        //convert the json string back to object
                DevicePlaylist library = gson.fromJson(input, DevicePlaylist.class);

                return library;
            } catch (JsonParseException e) {

            } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
    public void saveDevicePlaylist(DevicePlaylist library, String path) {
        try {
            File file = new File(path);
            file.mkdirs();
            file = new File(""+fileSeperator+"Users"+fileSeperator+"AudioApp"+fileSeperator+"library.json");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            gson.toJson(library, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
