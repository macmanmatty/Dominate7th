package sample.Library;
import org.jaudiotagger.audio.AudioFile;
import sample.Library.CueSheets.CueSheet;
import sample.Utilities.AudioFileUtilities;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.AudioProcessors.FindAudioFiles;
import sample.AudioProcessors.RemoveDuplicateSongs;
import sample.Utilities.SystemInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MusicLibrary implements AudioInformationAddable, AudioInformationRemovable {
    // the  class that hold all of the information about songs in a music  library
    private List<Playlist> regularPlaylists = new ArrayList<Playlist>(); // regular playlists where s
    private List<SmartPlaylist> smartPlaylists= new ArrayList<>();
    private List<AudioInformation> allSongs= new ArrayList<>();
    private List<CueSheet> cueSheets= new ArrayList<>();
    private List<Album> albums= new ArrayList<>();
     private int playlistNumber;
     private Settings settings=new Settings();
     private boolean checkForDuplicates=true;
    public MusicLibrary() {
    }
    public void setRegularPlaylists(ArrayList<Playlist> regularPlaylists) {
        this.regularPlaylists = regularPlaylists;
    }
    public List<AudioInformation> getAllSongs() {
        return allSongs;
    }
    public void setAllSongs(ArrayList<AudioInformation> allSongs) {
        this.allSongs = allSongs;
    }
    public  void newPlayList(ArrayList<AudioInformation> information){
         regularPlaylists.add(new Playlist(information));
     }
    public  Playlist  newPlayList(){
Playlist playlist=new Playlist(regularPlaylists.size());
        regularPlaylists.add(playlist);
        return playlist;
    }
    public  SmartPlaylist  newSmartPlayList(){
        SmartPlaylist playlist=new SmartPlaylist(regularPlaylists.size(), new PlaylistFilter());
        smartPlaylists.add(playlist);
        return playlist;
    }
    public  void addSongs(List<AudioInformation> songs){
        if(checkForDuplicates==true) {
            List<AudioInformation> nonDuplicateSongs = new RemoveDuplicateSongs().removeDuplicateSongs(allSongs, songs);
            allSongs.addAll(nonDuplicateSongs);
            System.out.println(nonDuplicateSongs.size() +" of "+allSongs.size()+" added to library");
        }else{

            allSongs.addAll(songs);
        }

        updateSmartPlaylists();
    }

    public  void addSong(AudioInformation song){
       List<AudioInformation> songs= new ArrayList<>();
       songs.add(song);
       addSongs(songs);

    }

    public  void removeSong(AudioInformation song){
        List<AudioInformation> songs= new ArrayList<>();
        songs.add(song);
        removeSongs(songs);

    }
   public void removeSongs(List<AudioInformation> songs){
        allSongs.removeAll(songs);
       updateSmartPlaylists();
}
public Playlist getPlaylist(int number){
         return regularPlaylists.get(number);
}
    public int getPlaylistNumber() {
        return playlistNumber;
    }
    public void setPlaylistNumber(int playlistNumber) {
        this.playlistNumber = playlistNumber;
    }
    public List<Playlist> getRegularPlaylists() {
        return regularPlaylists;
    }
    public List<Playlist> getAllPlayLists() {
        List<Playlist> playLists= new ArrayList();
        playLists.addAll(this.regularPlaylists);
        playLists.addAll(this.smartPlaylists);
        return playLists;
    }
    public void setPlayLists(List<Playlist> playLists) {
        this.regularPlaylists = playLists;
    }
    public void setAllSongs(List<AudioInformation> allSongs) {
        this.allSongs = allSongs;
    }


    public Settings getSettings() {
        return settings;
    }
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
    public  void defaultSettings(){
        settings= new Settings();
    }
    public List<SmartPlaylist> getSmartPlaylists() {
        return smartPlaylists;
    }
    public void setSmartPlaylists(List<SmartPlaylist> smartPlaylists) {
        this.smartPlaylists = smartPlaylists;
    }
    public  void updateSmartPlaylists(){
        int size=smartPlaylists.size();
        for(int count=0; count<size; count++){
            smartPlaylists.get(count).updatePlayList(allSongs);
        }
    }
    public  void updateSmartPlaylist(SmartPlaylist playlist){ //updates a smart plalist and it's corrosponding pane  based on all song in the library
            playlist.updatePlayList(allSongs);
               if(playlist.getPane()!=null){
                   playlist.updatePlayListPane();
               }
    }
    public  void refreshSmartPlaylistPanes(){
        int size=smartPlaylists.size();
        for(int count=0; count<size; count++){
            smartPlaylists.get(count).updatePlayListPane();
        }
    }
    public void checkForNewSongsinLibraryFolder(){
        String path=settings.getLibraryPath();
        if(path!=null){
            AudioFileUtilities walker= new AudioFileUtilities();
          List<AudioFile> files= new FindAudioFiles(false).findAudioFiles(path);
           ExtractAudioInformation extract= new ExtractAudioInformation();
          ArrayList<AudioInformation> information= extract.extractAudioInformationFromFile(files);
          allSongs=information;
        }
   }

    public List<CueSheet> getCueSheets() {
        return cueSheets;
    }

    public void addCueSheet(CueSheet cueSheet){

        cueSheets.add(cueSheet);
        addSongs(cueSheet.getTracks());
    }


    public void removeCueSheet(CueSheet cueSheet){
        cueSheets.remove(cueSheet);
        removeSongs(cueSheet.getTracks());


    }







    public boolean isCheckForDuplicates() {
        return checkForDuplicates;
    }

    public void setCheckForDuplicates(boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public  void addAlbum(Album album){
        albums.add(album);
        allSongs.addAll(album.getTracks());
        updateSmartPlaylists();


    }
    public  void removeAlbum(Album album){
        albums.remove(album);
        allSongs.removeAll(album.getTracks());
        updateSmartPlaylists();


    }





// removes songs with a missing audio file from the library

    public void removeNonExistingSongsFromLibrary() {


                Iterator<AudioInformation> informationIterator = allSongs.iterator();
                while (informationIterator.hasNext()) {

                    AudioInformation information = informationIterator.next();

                    File file = new File(information.getAudioFilePath());
                    if (file.exists()) {
                        continue;
                    } else {

                        informationIterator.remove();
                    }


                }




}





// removes songs with a missing audio file from all of the playlists in the library
            public void removeNonExistingSongsFromPlaylists() {




                int size = regularPlaylists.size();
                for (int count = 0; count < size; count++) {
                    List<AudioInformation> allSongs = regularPlaylists.get(count).getAllSongs();
                    Iterator<AudioInformation> informationIterator = allSongs.iterator();
                    while (informationIterator.hasNext()) {

                        AudioInformation information = informationIterator.next();

                        File file = new File(information.getAudioFilePath());
                        if (file.exists()) {
                            continue;
                        } else {

                            informationIterator.remove();
                        }


                    }

                }




    }





}
