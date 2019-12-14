package sample.Windows;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jaudiotagger.tag.FieldKey;
import sample.*;
import sample.Library.AudioInformation;
import sample.Library.Playlist;
import sample.Library.PlaylistFilter;
import sample.Library.SmartPlaylist;
import sample.SearchAudio.SearchTerms;
import sample.SearchAudio.Sorter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class PlaylistCreatorWindow {
    private MainAudioWindow window;
   private  Stage stage= new Stage();
   private  List<FieldKey> allFieldKeys;
   private List<SearchTerms> allSearchTerms;
   private  TextField playListName;
   private  Button makePlayList;
   private  Button addSearchBox;
   private VBox mainSearchBox;
   private ArrayList<TextField> searchFields= new ArrayList<>();
   private  ArrayList<ChoiceBox<FieldKey>> searchKeyBoxes = new ArrayList<>();
   private ArrayList<ChoiceBox<SearchTerms>> searchTermBoxs = new ArrayList<>();
   private List<String> creators= new ArrayList<>();
   private ChoiceBox<String> creatorChoices;
   private ChoiceBox<FieldKey> playListCreationKeys;
   private List<FieldKey> choiceKeys= new ArrayList<>();
   private ChoiceBox<String> keyChoiceValuesBox;
   private CheckBox emptyPlaylist;
   private Playlist playlist;
   private ChoiceBox<String> searchTerms= new ChoiceBox<>();
   private ChoiceBox<String> playlistKind= new ChoiceBox<>();
   private boolean searchOr;
   private boolean smartPlaylist;

    public PlaylistCreatorWindow(MainAudioWindow window) {
        this.window = window;
        allFieldKeys =window.getLibrary().getSettings().getUsedFieldKeys();
        allSearchTerms= Arrays.asList(SearchTerms.values());
    }
    public PlaylistCreatorWindow(MainAudioWindow window, Playlist playlist  ) {
        this.window = window;
        this.playlist = playlist;
        allFieldKeys =window.getLibrary().getSettings().getUsedFieldKeys();
        allSearchTerms= Arrays.asList(SearchTerms.values());
    }
    public void displayWindow(){
        Label label = new Label();
        if(playlist==null) {
            label.setText("Create Playlist");
            stage.setTitle("Playlist Creator");

        }
        else{
            label.setText("Update Playlist");
            stage.setTitle("Edit PlayList");

        }
        label.getStyleClass().add("veryLarge");
        HBox labelBox= new HBox(label);
        labelBox.setAlignment(Pos.CENTER);

        List<String> searchTermsList= new ArrayList<>();
        searchTermsList.add("Or");
        searchTermsList.add("And");
        this.searchTerms.setItems(FXCollections.observableList(searchTermsList));
        this.searchTerms.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(searchTerms.getSelectionModel().getSelectedIndex()==0){
                    searchOr=true;
                }
                else{
                    searchOr=false;

                }

            }
        });
        this.searchTerms.getSelectionModel().select(1);

        List<String> playListKind= new ArrayList<>();
        playListKind.add("Smart");
        playListKind.add("Regular");
        this.playlistKind.setItems(FXCollections.observableList(playListKind));
        this.playlistKind.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(playlistKind.getSelectionModel().getSelectedIndex()==0){
                    smartPlaylist=true;
                }
                else{
                    smartPlaylist=false;

                }

            }
        });
        this.playlistKind.getSelectionModel().select(1);

        emptyPlaylist= new CheckBox("Make An Empty PlayList");
        creatorChoices= new ChoiceBox<>();
        creators.add("None");
        creators.add("All Songs");
        creators.add("From  Selected Key And Value");
        creatorChoices.setItems(FXCollections.observableList(creators));
        keyChoiceValuesBox = new ChoiceBox<>();
        Sorter sorter= new Sorter();
        List<String> artists=sorter.getMatchingKeys(window.getLibrary().getAllSongs(), FieldKey.ARTIST);
       this.keyChoiceValuesBox.setItems(FXCollections.observableArrayList(artists));
        addSearchBox= new Button("Add Search Box");
        addSearchBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               mainSearchBox.getChildren().add(newSearchBox());
               stage.hide();
               stage.show();
            }
        });
        playListCreationKeys= new ChoiceBox<>();
        choiceKeys.add(FieldKey.ARTIST);
        choiceKeys.add(FieldKey.GENRE);
        choiceKeys.add(FieldKey.COMPOSER);
        choiceKeys.add(FieldKey.GROUP);
        choiceKeys.add(FieldKey.YEAR);
        playListCreationKeys.setItems(FXCollections.observableList(choiceKeys));
        playListCreationKeys.valueProperty().addListener(new ChangeListener<FieldKey>() {
            @Override
            public void changed(ObservableValue<? extends FieldKey> observable, FieldKey oldValue, FieldKey newValue) {
                updateKeyChoicesValueBox(newValue);
            }
        });

        makePlayList= new Button("Create PlayList");
        if(playlist!=null){
            makePlayList.setText("Update Playlist");
        }
        makePlayList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(smartPlaylist==true && playlist==null ){
                    newSmartPlaylist();
                }
                else if(smartPlaylist==false && playlist==null ){
                    newPlaylist();
                }
                else if(playlist!=null && playlist instanceof SmartPlaylist){
                    updateSmartPlaylist();
                }
                else if(playlist!=null){
                    updatePlaylist();
                }
                stage.hide();
            }
        });
        playListName= new TextField();
        Label listName= new Label("Playlist Name");
        listName.getStyleClass().add("infoTitle");

        HBox playlistNames= new HBox(listName, playListName);
        if(playlist==null){
            playlistNames.getChildren().add(playlistKind);
        }
        playlistNames.setSpacing(5);
        HBox buttons= new HBox(addSearchBox, makePlayList);

        CenteredLabel creatPlaylistFromPlaylistCreator= new CenteredLabel("Create Playlist From Playlist Creator");
        creatPlaylistFromPlaylistCreator.getStyleClass().add("large");

        CenteredLabel createPlaylistFromPlaylistFilter= new CenteredLabel("Create Playlist From Playlist Filter");
        createPlaylistFromPlaylistFilter.getStyleClass().add("large");

      VBox  mainBox= new VBox();
        mainBox.getStylesheets().add("/css/playlistCreatorWindow.css");

        mainBox.setSpacing(20);
        Label keyLabel= new Label("Select a Field Key ");
        Label valueLabel= new Label("Select The Value To Search For");
        Label playListCreator= new Label("Select A Playlist Creator");
        HBox creatorLabelBox= new HBox(playListCreator, keyLabel, valueLabel );
        creatorLabelBox.setSpacing(38);
        HBox creatorBox= new HBox(this.creatorChoices, this.playListCreationKeys, this.keyChoiceValuesBox);
        creatorBox.setSpacing(7);
        mainBox.getChildren().add(labelBox);
        mainBox.getChildren().add(playlistNames);

        mainBox.getChildren().add(emptyPlaylist);

        Label searchLabel=new Label("Search Critera Operator ");
        HBox searchBox= new HBox(searchLabel, searchTerms);
        searchBox.setSpacing(5);

        mainBox.getChildren().add(creatPlaylistFromPlaylistCreator);
        mainBox.getChildren().add(creatorLabelBox);
        mainBox.getChildren().add(creatorBox);

        mainBox.getChildren().add(createPlaylistFromPlaylistFilter);

        mainBox.getChildren().add(searchBox);
        mainSearchBox = new VBox();
        mainSearchBox.setSpacing(22);
        if(playlist!=null) {
            playListName.setText(playlist.getName());
            if (playlist instanceof SmartPlaylist) {
                PlaylistFilter filter = ((SmartPlaylist) playlist).getFilter();
                int size = filter.getSearchText().size();
                for (int count = 0; count < size; count++) {
                    mainSearchBox.getChildren().add(newSearchBox(filter.getSearchText().get(count), filter.getFieldKeys().get(count), filter.getSearchTerms().get(count)));
                }
            }
        }
        Label label1=new Label("Search Critera");

        Label key= new Label("Search Key");
        key.getStyleClass().add("searchHeader1");

        Label operator= new Label("Search Operator");
        operator.getStyleClass().add("searchHeader2");

        Label text= new Label("Search Text");
        HBox searchBoxLabels= new HBox( label1, key,operator, text);
        text.getStyleClass().add("searchHeader3");

        searchBoxLabels.getStyleClass().add("searchHeader");


        mainSearchBox.getChildren().add(searchBoxLabels);
        mainSearchBox.getChildren().add(newSearchBox());
        Label search= new Label("Search Criteria");
        search.getStyleClass().add("large");
        HBox searchLabelBox= new HBox(search);
        searchLabelBox.setAlignment(Pos.CENTER);



        mainBox.getChildren().add(searchLabelBox);
        mainBox.getChildren().add(mainSearchBox);
        mainBox.getChildren().add(buttons);
        stage.setScene(new Scene(mainBox));
        stage.show();
    }
    private void updateKeyChoicesValueBox(FieldKey key) {
        Sorter sorter= new Sorter();
        List<String> values=sorter.getMatchingKeys(window.getLibrary().getAllSongs(), key); // find all matching values for the given jaudiotagger field key.
        this.keyChoiceValuesBox.setItems(FXCollections.observableArrayList(values)); // update the selection box;
    }
    public HBox newSearchBox(){
        HBox searchBox= new HBox();
        ChoiceBox<FieldKey> keys= new ChoiceBox<>();
        keys.setItems(FXCollections.observableList(allFieldKeys));
        searchKeyBoxes.add(keys);
        ChoiceBox<SearchTerms> searchTerms= new ChoiceBox<>();
        searchTerms.setItems(FXCollections.observableList(allSearchTerms));
        searchTermBoxs.add(searchTerms);
        TextField searchField= new TextField();
        searchFields.add(searchField);
        Label label=  new Label("Searh Query #"+searchKeyBoxes.size());
        Button remove = new Button();
      Image   image = new Image("/remove.png");
        ImageView imageView = new ImageView(image);
        remove.setGraphic(imageView);
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainSearchBox.getChildren().remove(searchBox);
                searchKeyBoxes.remove(keys);
                searchTermBoxs.remove(searchTerms);
                searchFields.remove(searchField);
            }
        });
        searchBox.setSpacing(15);
        searchBox.getChildren().add(label);
        searchBox.getChildren().add(keys);
        searchBox.getChildren().add(searchTerms);
        searchBox.getChildren().add(searchField);
        searchBox.getChildren().add(remove);
        return  searchBox;
    }
    public HBox newSearchBox(String text, FieldKey key, SearchTerms term){ // creates new search box with field key and search term and string text to search for entered
        HBox searchBox= new HBox();
        ChoiceBox<FieldKey> keys= new ChoiceBox<>();
        keys.setItems(FXCollections.observableList(allFieldKeys));
        keys.getSelectionModel().select(key);
        searchKeyBoxes.add(keys);
        ChoiceBox<SearchTerms> searchTerms= new ChoiceBox<>();
        searchTerms.setItems(FXCollections.observableList(allSearchTerms));
        searchTerms.getSelectionModel().select(term);
        searchTermBoxs.add(searchTerms);
        TextField searchField= new TextField();
        searchField.setText(text);
        searchFields.add(searchField);
        Label label=  new Label("Searh Query #"+searchKeyBoxes.size());
        Button remove = new Button();
        Image   image = new Image("/remove.png");
        ImageView imageView = new ImageView(image);
        remove.setGraphic(imageView);
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainSearchBox.getChildren().remove(searchBox);
                searchKeyBoxes.remove(keys);
                searchTermBoxs.remove(searchTerms);
                searchFields.remove(searchField);
            }
        });
        searchBox.setSpacing(15);
        searchBox.getChildren().add(label);
        searchBox.getChildren().add(keys);
        searchBox.getChildren().add(searchTerms);
        searchBox.getChildren().add(searchField);
        searchBox.getChildren().add(remove);
        return  searchBox;
    }
    List<SearchTerms> getSearchTerms(){
        List<SearchTerms> searchTerms= new ArrayList<>();
        int size=searchTermBoxs.size();
        for(int count=0; count<size; count++){
            searchTerms.add(searchTermBoxs.get(count).getSelectionModel().getSelectedItem());
        }
        return searchTerms;
    }
    List<String> getSearchText(){
        List<String> searchTerms= new ArrayList<>();
        int size=searchTermBoxs.size();
        for(int count=0; count<size; count++){
            searchTerms.add(searchFields.get(count).getText());
        }
        return searchTerms;
    }
    List<FieldKey> getSearchKeys(){
        List<FieldKey> searchTerms= new ArrayList<>();
        int size=searchTermBoxs.size();
        for(int count=0; count<size; count++){
            searchTerms.add(searchKeyBoxes.get(count).getSelectionModel().getSelectedItem());
        }
        return searchTerms;
    }
    public void newSmartPlaylist(){
        if(creatorChoices.getSelectionModel().getSelectedItem()==null ||creatorChoices.getSelectionModel().getSelectedItem().equalsIgnoreCase("None")){
            SmartPlaylist playlist=new SmartPlaylist(  playListName.getText(), new PlaylistFilter(getSearchKeys(), getSearchTerms(), getSearchText(),searchOr ));
            playlist.createPlayList(window.getLibrary().getAllSongs());
            window.addPlaylist(playlist);
        }
        else{
            SmartPlaylist playlist=new SmartPlaylist( playListName.getText(),getFilter(creatorChoices.getSelectionModel().getSelectedItem()));
            playlist.createPlayList(window.getLibrary().getAllSongs());
            window.addPlaylist(playlist);
        }
        stage.hide();
        return;
    }
    private PlaylistFilter getFilter(String selectedItem) {
        FieldKey key=playListCreationKeys.getSelectionModel().getSelectedItem();
        String text=keyChoiceValuesBox.getSelectionModel().getSelectedItem();
      if(selectedItem.equalsIgnoreCase("From  Selected Key And Value") && key!=null && text!=null && !(text.isEmpty()) ){
            return new PlaylistFilter(key, SearchTerms.Equals, text);
        }
        else{ // return all songs playlist filter
            return new PlaylistFilter();
      }
    }
    public void newPlaylist(){
        Sorter sorter= new Sorter();
        if(emptyPlaylist.isSelected()){
            Playlist playlist= new Playlist(playListName.getText());
            window.addPlaylist(playlist);
        }
        else if(creatorChoices.getSelectionModel().getSelectedItem()==null ||creatorChoices.getSelectionModel().getSelectedItem().equalsIgnoreCase("None")){
            List<AudioInformation> songs = sorter.search(getSearchTerms(), getSearchText(), getSearchKeys(), window.getLibrary().getAllSongs(), searchOr);
            Playlist playlist=new Playlist( songs, playListName.getText());
            window.addPlaylist(playlist);
        }
        else{
            PlaylistFilter filter= getFilter(creatorChoices.getSelectionModel().getSelectedItem());
            List<AudioInformation> songs = sorter.search(filter.getSearchTerms(), filter.getSearchText(), filter.getFieldKeys(), window.getLibrary().getAllSongs(), filter.isAnyTermsMatch());
            Playlist playlist=new Playlist( songs, playListName.getText());
            window.addPlaylist(playlist);
        }
        stage.hide();
        return;
}
    public void updatePlaylist(){
        if(!(playListName.getText().isEmpty())){
            playlist.setName(playListName.getText());
        }
        Sorter sorter= new Sorter();
        if(creatorChoices.getSelectionModel().getSelectedItem()==null ||creatorChoices.getSelectionModel().getSelectedItem().equalsIgnoreCase("None")){
            if(playlist!=null) {
                List<AudioInformation> songs = sorter.search(getSearchTerms(), getSearchText(), getSearchKeys(), window.getLibrary().getAllSongs(), searchOr);
                playlist.addSongs(songs);
            }
        }
        else{
            if(playlist!=null) {
                PlaylistFilter filter = getFilter(creatorChoices.getSelectionModel().getSelectedItem());
                List<AudioInformation> songs = sorter.search(filter.getSearchTerms(), filter.getSearchText(), filter.getFieldKeys(), window.getLibrary().getAllSongs(), filter.isAnyTermsMatch());
                playlist.addSongs(songs);
                playlist.getPane().updatePane();
            }
        }
        stage.hide();
        return;
    }
    public void updateSmartPlaylist(){
        if(!(playListName.getText().isEmpty())){
            playlist.setName(playListName.getText());
        }
        if(creatorChoices.getSelectionModel().getSelectedItem()==null ||creatorChoices.getSelectionModel().getSelectedItem().equalsIgnoreCase("None")){
          SmartPlaylist  playlist= (SmartPlaylist) this.playlist;
          playlist.setFilter(new PlaylistFilter(getSearchKeys(), getSearchTerms(), getSearchText(), searchOr));
          window.getLibrary().updateSmartPlaylist(playlist);
        }
        else{
            SmartPlaylist playlist=new SmartPlaylist( playListName.getText(),getFilter(creatorChoices.getSelectionModel().getSelectedItem()));
            playlist.createPlayList(window.getLibrary().getAllSongs());
            playlist.setPane(this.playlist.getPane());
            playlist.updatePlayListPane();
        }
        stage.hide();
        return;
    }
    }
