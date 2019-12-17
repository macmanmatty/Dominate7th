package sample.Library;
import org.jaudiotagger.audio.AudioFile;
import sample.Library.CueSheets.CueSheet;
import sample.Utilities.AudioFileUtilities;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.AudioProcessors.FindAudioFiles;
import sample.AudioProcessors.RemoveDuplicateSongs;

import java.util.ArrayList;
import java.util.List;
public class MusicLibrary implements AudioInformationAddable, AudioInformationRemovable {
    private List<Playlist> regularPlaylists = new ArrayList<Playlist>();
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




}
