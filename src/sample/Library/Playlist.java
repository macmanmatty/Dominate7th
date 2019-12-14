package sample.Library;
import javafx.application.Platform;
import org.jaudiotagger.tag.FieldKey;
import sample.AudioProcessors.RemoveDuplicateSongs;
import sample.PlaylistPanes.PlaylistTab;
import sample.PlaylistPanes.PlaylistPane;
import sample.MusicPlayers.ShuffleMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Playlist implements AudioInformationAddable, AudioInformationRemovable {
    protected List<AudioInformation> allSongs = new ArrayList<AudioInformation>(); // the songs to display
    protected List<FieldKey> showFields= new ArrayList<>(); // the j audio tagger keys to show in the playlist pane
    protected HashMap<FieldKey, Double>  tableCoumnWidths= new HashMap<>(); // width of each column in the playlist pane
    protected int listPlace; // the list place in the library's array of playlists
    protected int tabNumber; // the list in the playlist tabs list
    protected String name; // playlist name
    protected  transient PlaylistPane playListPane; // the javafx pane displaying  playlist
    protected PlaylistSettings playlistSettings = new PlaylistSettings(); // the play list settings
    protected ShuffleMode shuffleMode;
    protected transient PlaylistTab playlistTab;
    protected List<FieldKey> sortOrder= new ArrayList<>();
    protected List<FieldKey> shuffleKeys= new ArrayList<>();
    protected PlaylistPaneKind playListPaneKind=PlaylistPaneKind.TABLE;
    protected  boolean isCDPlaylist;
    protected boolean checkForDuplicates;
    protected boolean main;
    public Playlist() {


    }
    public Playlist(List<AudioInformation> allSongs) {
        this.allSongs = allSongs;
    } // creates a new playlist with a set of song information to display
    public Playlist(int number) {// creates a new playlist empty  with a  name that is  playlist + number
        name="playlist "+number;
    }
    public Playlist(List<AudioInformation> allSongs, String name) { // creates a new playlist with a list of  song information to display and a specified name
        this.allSongs = allSongs;
        this.name = name;
    }
    public Playlist(String name) {
        this.name = name;
    }
    public  void addSongs(List<AudioInformation> songs){ // adds songs to the playlist and updatyes the pane if it exists;
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(checkForDuplicates==true) {
                    List<AudioInformation> nonDuplicateSongs = new RemoveDuplicateSongs().removeDuplicateSongs(allSongs, songs);
                    allSongs.addAll(nonDuplicateSongs);
                    System.out.println(nonDuplicateSongs.size() +" of "+allSongs.size()+" added to library");
                }else{

                    allSongs.addAll(songs);
                }
        if(playListPane!=null) {
            playListPane.updatePane();

        }

            }
        };
        Platform.runLater(runnable);

    }
    public void setAllSongs(List<AudioInformation> allSongs) { // sets all of the songs  to a given list of songs  and updates the pnae if not null
        this.allSongs=allSongs;
        Runnable runnable=new Runnable() {
            @Override
            public void run() {

                    if(playListPane!=null) {
                        playListPane.updatePane();
                        playListPane.setSongs(allSongs);

                    }

            }
        };
        Platform.runLater(runnable);
    }
    public  void removeSongs(List<AudioInformation> information){// removes  songs to the playlist and updatyes the pane if it exists;
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                allSongs.removeAll(information);


                    if(playListPane!=null) {
                        playListPane.updatePane();

                    }

            }
        };
        Platform.runLater(runnable);
    }
    public  void removeSong(AudioInformation information){// removes  songs to the playlist and updatyes the pane if it exists;
      List<AudioInformation> songs= new ArrayList<>();
      songs.add(information);
      removeSongs(songs);
    }

    public  void addSong(AudioInformation information){// removes  songs to the playlist and updatyes the pane if it exists;
        List<AudioInformation> songs= new ArrayList<>();
        songs.add(information);
        addSongs(songs);
    }
    public List<AudioInformation> getAllSongs() {
        return allSongs;
    }
    public int getListPlace() {
        return listPlace;
    }
    public void setListPlace(int listPlace) {
        this.listPlace = listPlace;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { // set the playlist name and updates the name on the  tab
        this.name = name;

        Runnable  runnable = new Runnable() {

            @Override
            public void run() {
                if(playlistTab!=null){
                    System.out.println("updating tab");
                    playlistTab.setTitle(name);
                }

            }
        };
        Platform.runLater(runnable);
    }
    public List<FieldKey> getShowFields() {
        return showFields;
    }
    public void setShowFields(List<FieldKey> showFields) {
        this.showFields = showFields;
    }
    public void setColumnWidth(FieldKey key, double  width){
        tableCoumnWidths.put(key , width);
    }
    public double getColumnWidth(FieldKey key){// gets the column width for a given field key column in the playlist pane  if no column  width exists
        //returns 250 as the default value;
       Double width=tableCoumnWidths.get(key);
        if(width!=null){
            return  width;
        }
        return 250;
    }
    public void updatePlayListPane(){ // updates the pane linked to the current playlist uses platform.run later
        // as this may not be called from the edt thread.

        Runnable runnable= new Runnable() {
            @Override
            public void run() {


                if (playListPane != null) {
                    playListPane.updatePane();
                }
            }
        };
        Platform.runLater(runnable);


    }
    public HashMap<FieldKey, Double> getTableCoumnWidths() {
        return tableCoumnWidths;
    }
    public void setTableCoumnWidths(HashMap<FieldKey, Double> tableCoumnWidths) {
        this.tableCoumnWidths = tableCoumnWidths;
    }
    public int getTabNumber() {
        return tabNumber;
    }
    public void setTabNumber(int tabNumber) {
        this.tabNumber = tabNumber;
    }
    public PlaylistPane getPane() {
        return playListPane;
    }
    public void setPane(PlaylistPane pane) {
        this.playListPane = pane;
    }
    public PlaylistSettings getPlaylistSettings() {
        return playlistSettings;
    }
    public void setPlaylistSettings(PlaylistSettings playlistSettings) {
        this.playlistSettings = playlistSettings;
    }
    public ShuffleMode getShuffleMode() {
        return shuffleMode;
    }
    public void setShuffleMode(ShuffleMode shuffleMode) {
        this.shuffleMode = shuffleMode;
    }
    public PlaylistPane getPlayListPane() {
        return playListPane;
    }
    public void setPlayListPane(PlaylistPane playListPane) {
        this.playListPane = playListPane;
    }
    public PlaylistPaneKind getPlayListPaneKind() {
        return playListPaneKind;
    }
    public void setPlayListPaneKind(PlaylistPaneKind playListPaneKind) {
        this.playListPaneKind = playListPaneKind;
    }
    public PlaylistTab getPlaylistTab() {
        return playlistTab;
    }
    public void setPlaylistTab(PlaylistTab playlistTab) {
        this.playlistTab = playlistTab;
    }
    public List<FieldKey> getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(List<FieldKey> sortOrder) {
        this.sortOrder = sortOrder;
    }
    public List<FieldKey> getShuffleKeys() {
        return shuffleKeys;
    }
    public void setShuffleKeys(List<FieldKey> shuffleKeys) {
        this.shuffleKeys = shuffleKeys;
    }
    @Override
    public String toString() {
        return name;
    }

    public boolean isCDPlaylist() {
        return isCDPlaylist;
    }

    public void setCDPlaylist(boolean CDPlaylist) {
        isCDPlaylist = CDPlaylist;
    }

    public boolean isCheckForDuplicates() {
        return checkForDuplicates;
    }

    public void setCheckForDuplicates(boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
