package sample.Windows;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import sample.AudioProcessors.SaveAlbumArtFromWeb;
import sample.Library.AudioInformation;
import sample.Utilities.CopyAudioInformation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class IdSelectionMenu  extends ContextMenu{

   private  ToggleGroup selection= new ToggleGroup();// the result selection group
    private List<List<AudioInformation>> results= new ArrayList<>();// Acoustid / music brainz results
   private  SongIDTable songIDTable;
    private int songIndex;
    private HashMap<Toggle,AudioInformation > resultSelection= new HashMap<>();// hash map of toggle menus linked to the results
    private boolean getAlbumArt;
    private boolean embedAlbumArt;
    public IdSelectionMenu(  List<List<AudioInformation>> results, SongIDTable songIDTable, boolean getAlbumArt, boolean embedAlbumArt) {
        this.results = results;
        this.getAlbumArt=getAlbumArt;
        this.embedAlbumArt=embedAlbumArt;
        this.songIDTable = songIDTable;
        hideOnEscapeProperty().setValue(true);
    }
    public void createMenu(int number){// makes the menu for the  selected song based on the song in the audio selection menu number.
       setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               System.out.println(resultSelection);
               AudioInformation information=resultSelection.get(selection.getSelectedToggle());
               CopyAudioInformation.copyAudioInformation(information,songIDTable.getDisplayList().get(number));// copy the new  information over to the old information instance
               if(getAlbumArt==true){
                   SaveAlbumArtFromWeb saveAlbumArtFromWeb= new SaveAlbumArtFromWeb( embedAlbumArt);
                   saveAlbumArtFromWeb.getAlbumArtFromWeb(information.getMusicBrainzId(), information);
               }
               songIDTable.updateTable();


           }
       });
        int size=results.get(number).size();
        for(int count=0; count<size; count++){
           AudioInformation information=results.get(number).get(count);
            RadioMenuItem result= new RadioMenuItem("Title: "+information.getTitle()+"  Artist: "+information.getArtist()+"  Album: "+information.getAlbum()+"  Year: "+information.getYear());
            result.setToggleGroup(selection);
         getItems().add(result);
         resultSelection.put(result, information);
        }

    }

}
