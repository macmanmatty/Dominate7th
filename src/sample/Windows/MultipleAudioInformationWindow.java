package sample.Windows;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;
import sample.Library.AudioInformation;
import sample.PlaylistPanes.PlaylistPane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
public class MultipleAudioInformationWindow {
    private Stage stage;
   private  List<AudioInformation> songs;
   private  Button setFields;
   private Button addAlbumArt;
   private Button deleteImages;
   private CheckBox embedAlbumArt;
   private List<VBox> albumImageBoxes= new ArrayList<>();
   private List<Artwork> songsArtwork= new ArrayList<>();
   private List<String> albumArtPaths= new ArrayList<>();
   private Button close;
    private transient   TextField artist;
    private transient   TextField album;
    private transient   TextField catalogNo;
    private transient   TextField composer;
    private transient   TextField conductor;
    private transient   TextField copyright;
    private transient   TextField country;
    private transient   TextField discNo;
    private transient   TextField discTotal;
    private transient   TextField encoder;
    private transient   TextField engineer;
    private transient   TextField ensemble;
    private transient   TextField genre;
    private transient   TextField group;
    private transient   TextField instruemnt;
    private transient   TextField laugauge;
    private transient   TextField lyricist;
    private transient   TextField media;
    private transient   TextField producer;
    private transient   TextField originalArtist;
    private transient   TextField ranking;
    private transient   TextField rating;
    private transient   TextField recordLabel;
    private transient   TextField trackTotal;
    private transient   TextField year;
    private  TextField title;
    private Label  filePath;
    private VBox artBox;
    private FileChooser.ExtensionFilter filter;
    private ScrollPane mainPane;
    private GridPane albumImageGridPane = new GridPane();
    private int gridPaneRows;
    private int gridPaneColumns;
    private PlaylistPane pane;

    public MultipleAudioInformationWindow(PlaylistPane pane) {
        stage= new Stage();
        stage.setTitle("Edit Information");
        this.pane=pane;
      filter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
    }
   public void  displayInformation(List<AudioInformation> songs){
        this.songs =songs;
       VBox mainBox= new VBox();
       mainBox.getStylesheets().add("css/multipleAudioInformationWindow.css");
       artBox=new VBox();
       setFields= new Button("Set Tags" );
       setFields.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               setFields();
           }
       });
       close= new Button("Close" );
       close.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               stage.close();
           }
       });
       addAlbumArt = new Button("Add Album Art");
       addAlbumArt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               Stage stage= new Stage();
               FileChooser fileChooser= new FileChooser();
               fileChooser.getExtensionFilters().add(filter);
               List<File> files=fileChooser.showOpenMultipleDialog(stage);
               displayArtAndAddToSongs(files);

           }
       });

       deleteImages = new Button("Delete Selected  Artworks");
      deleteImages.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               deleteAlbumImages();

           }
       });
      embedAlbumArt=new CheckBox("Embed Album Images Into  Audio File?");


       Label artistLabel= new Label("Artist :");
       
       Label albumLabel= new Label("Album: ");
       
       Label catalogNoLabel=new Label("Catalog #: ");
       Label composerLabel= new Label("Composer: ");
       Label conductorLabel= new Label(" Conductor" );
       
       Label copyrightLabel=new Label("Copyright: ");
       Label countryLabel= new Label("Country: ");
       Label discNoLabel= new Label("Disc #: ");
       Label discTotalLabel= new Label("# of Discs: ");
       Label encoderLabel= new Label("Encoder: ");
       Label engineerLabel= new Label("Engineer: ");
       Label ensembleLabel= new Label("Ensemble: ");
       Label genereLabel= new Label("Genere: ");
       Label groupLabel= new Label("Group: ");
       Label languageLabel= new Label("Language: ");
       Label instruemntLabel= new Label("Instrument: ");
       Label lyicistLabel=new Label("Lyricist: ");
       Label producerLabel= new Label("Producer: ");
       Label originalArtistLabel= new Label("Original Artist: ");
       Label rankingLabel= new Label("Ranking: ");
       Label ratingLabel= new Label("Rating: ");
       Label recordLabelLabel= new Label("Record Label: " );
       Label trackTotalLabel= new Label("# of Tracks: ");
       Label yearLabel=  new Label("Year: ");
       
       
       
        artist=new TextField();
        album=new TextField();
        catalogNo=new TextField();
        composer=new TextField();
        conductor=new TextField();
        copyright=new TextField();
        country=new TextField();
        discNo=new TextField();
        discTotal=new TextField();
        encoder=new TextField();
        engineer=new TextField();
        ensemble=new TextField();
        genre=new TextField();
        group=new TextField();
        instruemnt=new TextField();
        laugauge=new TextField();
        lyricist=new TextField();
        media=new TextField();
        producer=new TextField();
        originalArtist=new TextField();
        ranking=new TextField();
        rating=new TextField();
        recordLabel=new TextField();
        trackTotal=new TextField();
        year=new TextField();

       HBox artistBox= new HBox();
       artistBox.getChildren().add(artistLabel);
       artistBox.getChildren().add(artist);
       HBox albumBox= new HBox();
       albumBox.getChildren().add(albumLabel);
       albumBox.getChildren().add(album);
       HBox encoderBox= new HBox();
       encoderBox.getChildren().add(encoderLabel);
       encoderBox.getChildren().add(encoder);
       HBox trackTotalBox= new HBox();
       trackTotalBox.getChildren().add(trackTotalLabel);
       trackTotalBox.getChildren().add(trackTotal);
       HBox discNoBox= new HBox();
       discNoBox.getChildren().add(discNoLabel);
       discNoBox.getChildren().add(discNo);
       HBox discTotalBox= new HBox();
       discTotalBox.getChildren().add(discTotalLabel);
       discTotalBox.getChildren().add(discTotal);
       HBox composerBox= new HBox();
       composerBox.getChildren().add(composerLabel);
       composerBox.getChildren().add(composer);
       HBox conductorBox= new HBox();
       conductorBox.getChildren().add(conductorLabel);
       conductorBox.getChildren().add(conductor);
       HBox lyricistBox= new HBox();
       lyricistBox.getChildren().add(lyicistLabel);
       lyricistBox.getChildren().add(lyricist);
       HBox instrumentBox= new HBox();
       instrumentBox.getChildren().add(instruemntLabel);
       instrumentBox.getChildren().add(instruemnt);
       HBox producerBox= new HBox();
       producerBox.getChildren().add(producerLabel);
       producerBox.getChildren().add(producer);
       HBox engineerBox= new HBox();
       engineerBox.getChildren().add(engineerLabel);
       engineerBox.getChildren().add(engineer);
       HBox yearBox= new HBox();
       yearBox.getChildren().add(yearLabel);
       yearBox.getChildren().add(year);
       HBox genreBox= new HBox();
       genreBox.getChildren().add(genereLabel);
       genreBox.getChildren().add(genre);
       HBox groupBox= new HBox();
       groupBox.getChildren().add(groupLabel);
       groupBox.getChildren().add(group);
       HBox languageBox= new HBox();
       languageBox.getChildren().add(languageLabel);
       languageBox.getChildren().add(laugauge);
       HBox originalArtistBox= new HBox();
       originalArtistBox.getChildren().add(originalArtistLabel);
       originalArtistBox.getChildren().add(originalArtist);
       HBox rankingBox= new HBox();
       rankingBox.getChildren().add(rankingLabel);
       rankingBox.getChildren().add(ranking);
      HBox ratingBox= new HBox();
       ratingBox.getChildren().add(ratingLabel);
       ratingBox.getChildren().add(rating);
       HBox recordLabelBox= new HBox();
       recordLabelBox.getChildren().add(recordLabelLabel);
       recordLabelBox.getChildren().add(recordLabel);
       HBox ensembleBox= new HBox();
       ensembleBox.getChildren().add(ensembleLabel);
       ensembleBox.getChildren().add(ensemble);
       HBox catalogNoBox= new HBox();
       catalogNoBox.getChildren().add(catalogNoLabel);
       catalogNoBox.getChildren().add(catalogNo);
       HBox copyrightBox= new HBox();
       copyrightBox.getChildren().add(copyrightLabel);
       copyrightBox.getChildren().add(copyright);
       HBox countryBox= new HBox();
       countryBox.getChildren().add(countryLabel);
       countryBox.getChildren().add(country);

           Label mainTitle = new Label("Edit File Information");
      if(songs.size()<2){
           mainTitle = new Label("Edit Track Information");

       }
       mainTitle.getStylesheets().add("css/albumPlaylistPane.css");
       mainTitle.getStyleClass().add("audioFileInformationTitle");
       mainBox.getChildren().add(mainTitle);

       if(songs.size()==1){ // only one song so add title box and file path label to allow title editing
           Label titleLabel= new Label("Track Title: ");
           title=new TextField();
           title.setText(songs.get(0).getTitle());
           HBox titleBox= new HBox(titleLabel, title);
          


            mainBox.getChildren().add(new AudioFileInformationWindow().getAudioFileInformation(songs.get(0)));
           mainBox.getChildren().add(titleBox);

       }
      
       mainBox.getChildren().add(artistBox);
       mainBox.getChildren().add(albumBox);
       mainBox.getChildren().add(trackTotalBox);
       mainBox.getChildren().add(discNoBox);
       mainBox.getChildren().add(discTotalBox);
       mainBox.getChildren().add(composerBox);
       mainBox.getChildren().add(encoderBox);
       mainBox.getChildren().add(conductorBox);
       mainBox.getChildren().add(lyricistBox);
       mainBox.getChildren().add(instrumentBox);
       mainBox.getChildren().add(producerBox);
       mainBox.getChildren().add(engineerBox);
       mainBox.getChildren().add(yearBox);
       mainBox.getChildren().add(genreBox);
       mainBox.getChildren().add(groupBox);
       mainBox.getChildren().add(languageBox);
       mainBox.getChildren().add(originalArtistBox);
       mainBox.getChildren().add(rankingBox);
       mainBox.getChildren().add(ratingBox);
       mainBox.getChildren().add(recordLabelBox);
       mainBox.getChildren().add(ensembleBox);
       mainBox.getChildren().add(catalogNoBox);
       mainBox.getChildren().add(copyrightBox);
       mainBox.getChildren().add(countryBox);

       mainBox.getChildren().add(albumImageGridPane);
       mainBox.getChildren().add(embedAlbumArt);
       HBox buttons= new HBox();
       buttons.getChildren().add(setFields);
       buttons.getChildren().add(addAlbumArt);
       buttons.getChildren().add(deleteImages);
       buttons.getChildren().add(close);
       Label art= new Label("Album Art");
       artBox.getChildren().add(art);
       mainBox.getChildren().add(artBox);
       mainBox.getChildren().add(buttons);
       
            setInformationToTextFields();
       mainPane = new ScrollPane(mainBox);

       stage.setScene(new Scene(mainPane));
       stage.show();
   }


    private void setInformationToTextFields()  {
       int numberOfSongs= songs.size();
       String textYear=songs.get(0).getYear();
       
       boolean setYear=true;
       for(int count=0; count<numberOfSongs; count++){
           String newYear= songs.get(count).getYear();
             if((!newYear.equals(textYear))){
                 
                 setYear=false;
                 break;
                 
                 
                 
             }
       }
       
       if(setYear==true){
           year.setText(textYear);
       }
     String textArtist=songs.get(0).getArtist();
       boolean setArtist=true;
       for(int count=0; count<numberOfSongs; count++){
           String newArtist= songs.get(count).getArtist();
           if((!newArtist.equals(textArtist))){
               setArtist=false;
               break;

           }
       }
       if(setArtist==true){
           artist.setText(textArtist);
       }

       String textAlbum=songs.get(0).getAlbum();
       boolean setAlbum=true;
       for(int count=0; count<numberOfSongs; count++){
           String newAlbum= songs.get(count).getAlbum();
           if((!newAlbum.equals(textAlbum))){
               setAlbum=false;
               break;

           }
       }
       if(setAlbum==true){
           album.setText(textAlbum);
       }

       String textEncoder=songs.get(0).getEncoder();
       boolean setEncoder=true;
       for(int count=0; count<numberOfSongs; count++){
           String newEncoder= songs.get(count).getEncoder();
           if((!newEncoder.equals(textEncoder))){
               setEncoder=false;
               break;

           }
       }
       if(setEncoder==true){
           encoder.setText(textEncoder);
       }
       String textRanking=songs.get(0).getRanking();
       boolean setRanking=true;
       for(int count=0; count<numberOfSongs; count++){
           String newRanking= songs.get(count).getRanking();
           if((!newRanking.equals(textRanking))){
               setRanking=false;
               break;

           }
       }
       if(setRanking==true){
           ranking.setText(textRanking);
       }

       String textRating=songs.get(0).getRating();
       boolean setRating=true;
       for(int count=0; count<numberOfSongs; count++){
           String newRating= songs.get(count).getRating();
           if((!newRating.equals(textRating))){
               setRating=false;
               break;

           }
       }
       if(setRating==true){
           rating.setText(textRating);
       }
       String textDiscNo=songs.get(0).getDiscNo();
       boolean setDiscNo=true;
       for(int count=0; count<numberOfSongs; count++){
           String newDiscNo= songs.get(count).getDiscNo();
           if((!newDiscNo.equals(textDiscNo))){
               setDiscNo=false;
               break;

           }
       }
       if(setDiscNo==true){
           discNo.setText(textDiscNo);
       }
       String textProducer=songs.get(0).getProducer();
       boolean setProducer=true;
       for(int count=0; count<numberOfSongs; count++){
           String newProducer= songs.get(count).getProducer();
           if((!newProducer.equals(textProducer))){
               setProducer=false;
               break;

           }
       }
       if(setProducer==true){
           producer.setText(textProducer);
       }

       String textMedia=songs.get(0).getMedia();
       boolean setMedia=true;
       for(int count=0; count<numberOfSongs; count++){
           String newMedia= songs.get(count).getMedia();
           if((!newMedia.equals(textMedia))){
               setMedia=false;
               break;

           }
       }
       if(setMedia==true){
           media.setText(textMedia);
       }

       String textOriginalArtist=songs.get(0).getOriginalArtist();
       boolean setOriginalArtist=true;
       for(int count=0; count<numberOfSongs; count++){
           String newOriginalArtist= songs.get(count).getOriginalArtist();
           if((!newOriginalArtist.equals(textOriginalArtist))){
               setOriginalArtist=false;
               break;

           }
       }
       if(setOriginalArtist==true){
           originalArtist.setText(textOriginalArtist);
       }
       String textLyricist=songs.get(0).getLyricist();
       boolean setLyricist=true;
       for(int count=0; count<numberOfSongs; count++){
           String newLyricist= songs.get(count).getLyricist();
           if((!newLyricist.equals(textLyricist))){
               setLyricist=false;
               break;

           }
       }
       if(setLyricist==true){
           lyricist.setText(textLyricist);
       }

       String textLaugage=songs.get(0).getLaugauge();
       boolean setLaugage=true;
       for(int count=0; count<numberOfSongs; count++){
           String newLaugage= songs.get(count).getLaugauge();
           if((!newLaugage.equals(textLaugage))){
               setLaugage=false;
               break;

           }
       }
       if(setLaugage==true){
           laugauge.setText(textLaugage);
       }

       String textGroup=songs.get(0).getGroup();
       boolean setGroup=true;
       for(int count=0; count<numberOfSongs; count++){
           String newGroup= songs.get(count).getGroup();
           if((!newGroup.equals(textGroup))){
               setGroup=false;
               break;

           }
       }
       if(setGroup==true){
           group.setText(textGroup);
       }
       String textGenere=songs.get(0).getGenre();
       boolean setGenere=true;
       for(int count=0; count<numberOfSongs; count++){
           String newGenere= songs.get(count).getGenre();
           if((!newGenere.equals(textGenere))){
               setGenere=false;
               break;

           }
       }
       if(setGenere==true){
           genre.setText(textGenere);
       }

       String textEnsamble=songs.get(0).getEnsemble();
       boolean setEnsamble=true;
       for(int count=0; count<numberOfSongs; count++){
           String newEnsamble= songs.get(count).getEnsemble();
           if((!newEnsamble.equals(textEnsamble))){
               setEnsamble=false;
               break;

           }
       }
       if(setEnsamble==true){
           ensemble.setText(textEnsamble);
       }
       String textCounrty=songs.get(0).getCountry();
       boolean setCounrty=true;
       for(int count=0; count<numberOfSongs; count++){
           String newCounrty= songs.get(count).getCountry();
           if((!newCounrty.equals(textCounrty))){
               setCounrty=false;
               break;

           }
       }
       if(setCounrty==true){
           country.setText(textCounrty);
       }

       String textComposer=songs.get(0).getComposer();
       boolean setComposer=true;
       for(int count=0; count<numberOfSongs; count++){
           String newComposer= songs.get(count).getComposer();
           if((!newComposer.equals(textComposer))){
               setComposer=false;
               break;

           }
       }
       if(setComposer==true){
           composer.setText(textComposer);
       }
       String textConductor=songs.get(0).getConductor();
       boolean setConductor=true;
       for(int count=0; count<numberOfSongs; count++){
           String newConductor= songs.get(count).getConductor();
           if((!newConductor.equals(textConductor))){
               setConductor=false;
               break;

           }
       }
       if(setConductor==true){
           conductor.setText(textConductor);
       }
        List<Artwork> song1AlbumArt=songs.get(0).getAudioFile().getTag().getArtworkList(); // get first songs  artwork list
       int song1ArtworkSize=song1AlbumArt.size(); // artwork list size for song one
       boolean [] artworkToAdd= new boolean[song1ArtworkSize]; // booleans array for which artwork images are the same for all songs
       for(int count=0; count<song1ArtworkSize; count++){
           artworkToAdd[count]=true; // make artwork adding boolean true
       }
        for(int count=0; count<numberOfSongs; count++){
            List<Artwork> currentSongArt=songs.get(count).getAudioFile().getTag().getArtworkList(); // get current songs artwork
            int numberOfImages=currentSongArt.size();
            if(numberOfImages==0){// if song has no images no need to check further all songs can't have shared images
                for(int count3=0; count3<song1ArtworkSize; count3++){
                    artworkToAdd[count3]=false; // make artwork adding false as there are no shared images;
                }
                break;
            }
            if(numberOfImages>song1ArtworkSize){ // if size of artwork is greater is greater use smaller size
                numberOfImages=song1ArtworkSize;
            }
            for(int count2=0; count2<numberOfImages; count2++){
               boolean sameArtwork= compareArtwork(song1AlbumArt.get(count2), currentSongArt.get(count2)); // compare artwork images
                if(sameArtwork==false){
                    artworkToAdd[count2]=false;

                }

            }
        }
        for(int count=0; count<song1ArtworkSize; count++) {
            if (artworkToAdd[count] == true) {
                try {
                    Artwork artwork = song1AlbumArt.get(count);
                    songsArtwork.add(artwork);
                    CheckBox checkBox = new CheckBox("Delete Image");
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(artwork.getBinaryData());
                    BufferedImage image = null;
                    image = ImageIO.read(inputStream);
                    if(image!=null) {
                        Image fxImage = SwingFXUtils.toFXImage(image, null);
                        ImageView fxImageView = new ImageView(fxImage);
                        fxImageView.setFitHeight(150);
                        fxImageView.setFitWidth(150);
                        VBox imageBox = new VBox(fxImageView, checkBox);
                        addImageBoxToGridPane(imageBox);
                        albumImageBoxes.add(imageBox);
                    }
                } catch (IOException e) {
                    new OptionPane().showOptionPane("Error Reading Image Data " + e.getMessage(), "OK");
                }

            }
        }
            List<String> artworkPathsSong0=songs.get(0).getAlbumArtPaths(); // get first songs  artwork  file paths list
            int artworkPathSizeSong0=artworkPathsSong0.size(); // artwork list size for song one
            boolean [] artworkPathsToAdd= new boolean[artworkPathSizeSong0]; // booleans array for which artwork image paths are the same for all songs
            for(int count=0; count<artworkPathSizeSong0; count++){
                artworkPathsToAdd[count]=true; // make artwork adding boolean true
            }
            for(int count=0; count<numberOfSongs; count++){
                List<String> currentSongArtworkPaths=songs.get(count).getAlbumArtPaths(); // get current songs artwork
                int numberOfArtworkImages=currentSongArtworkPaths.size();
                if(numberOfArtworkImages==0){// if song has no images no need to check further all songs can't have shared images
                    for(int count3=0; count3<artworkPathSizeSong0; count3++){
                        artworkPathsToAdd[count3]=false; // make artwork adding false as there are no shared images;
                    }
                    break;
                }
                if(numberOfArtworkImages>artworkPathSizeSong0){ // if size of artwork is greater is greater use smaller size
                    numberOfArtworkImages=artworkPathSizeSong0;
                }
                for(int count2=0; count2<numberOfArtworkImages; count2++){
                    if(!(currentSongArtworkPaths.get(count2).equals(artworkPathsSong0.get(count2)))){
                        artworkPathsToAdd[count2]=false;

                    }

                }
            }
            for(int count=0; count<artworkPathSizeSong0; count++){
                if(artworkPathsToAdd[count]==true){
                    try {
                        String imagePath=artworkPathsSong0.get(count);

                        albumArtPaths.add(imagePath);
                        CheckBox checkBox= new CheckBox("Delete Image");
                        Image fxImage= new Image("file:"+imagePath);
                        ImageView fxImageView= new ImageView(fxImage);
                        fxImageView.setFitHeight(150);
                        fxImageView.setFitWidth(150);
                        VBox imageBox= new VBox(fxImageView, checkBox);
                        addImageBoxToGridPane(imageBox);
                        albumImageBoxes.add(imageBox);
                        Artwork artwork= new StandardArtwork();
                        artwork.setImageUrl(imagePath);
                        songsArtwork.add(artwork);

                    } catch (IllegalArgumentException e) {
                        new OptionPane() .showOptionPane("Error Reading Image Data "+e.getMessage(), "OK");
                    }

                }
        }


   }

   private void addImageBoxToGridPane(VBox imageBox){
        albumImageGridPane.add(imageBox, gridPaneRows, gridPaneColumns);
        gridPaneRows++;
        if(gridPaneRows==6){
            gridPaneRows=0;
            gridPaneColumns++;
        }
   }

   private boolean compareArtwork(Artwork artwork1, Artwork artwork2){
        byte []  imageBytes= artwork1.getBinaryData();
        byte[] imageBytes2= artwork2.getBinaryData();
        if(imageBytes==null || imageBytes2==null){
            return false;
        }
        int length=imageBytes.length;
        int length2=imageBytes2.length;
        if(length!=length2){
            return false;
        }
        for(int count=0; count<length; count++){
            if(imageBytes[count]!=imageBytes2[count]){
                return false;
            }

        }

        return true;

   }
           
           
           
           
   private void setFields(){
        int size= songs.size();
        if(size==1){
            if(title.getText().isEmpty()==false) {
                songs.get(0).setTitle(title.getText());
            }
        }
        for(int count=0; count<size; count++){
            AudioInformation song= songs.get(count);
            if(year.getText().isEmpty()==false){
            song.setYear(year.getText());
            }
            if(ranking.getText().isEmpty()==false) {
                song.setRating(ranking.getText());
            }
            if(producer.getText().isEmpty()==false) {
                song.setProducer(producer.getText());
            }
            if(originalArtist.getText().isEmpty()==false) {
                song.setOriginalArtist(originalArtist.getText());
            }
            if(media.getText().isEmpty()==false){
            song.setMedia(media.getText());
            }
            if(lyricist.getText().isEmpty()==false) {
                song.setLyricist(lyricist.getText());
            }
            if(laugauge.getText().isEmpty()==false) {
                song.setLangauge(laugauge.getText());
            }
            if(group.getText().isEmpty()==false) {
                song.setGroup(group.getText());
            }
            if(genre.getText().isEmpty()==false) {
                song.setGenre(genre.getText());
            }
            if(ensemble.getText().isEmpty()==false) {
                song.setEnsemble(ensemble.getText());
            }
            if(encoder.getText().isEmpty()==false) {
                song.setEncoder(encoder.getText());
            }
            if(engineer.getText().isEmpty()==false){
            song.setEngineer(engineer.getText());
            }
            if(discNo.getText().isEmpty()==false) {
                song.setDiscNo(discNo.getText());
            }
            if(country.getText().isEmpty()==false) {
                song.setCountry(country.getText());
            }
            if(copyright.getText().isEmpty()==false) {
                song.setCopyright(copyright.getText());
            }
            if(conductor.getText().isEmpty()==false) {
                song.setConductor(conductor.getText());
            }
            if(composer.getText().isEmpty()==false) {
                song.setComposer(composer.getText());
            }
            if(album.getText().isEmpty()==false) {
                song.setAlbum(album.getText());
            }
            if(artist.getText().isEmpty()==false) {
                         song.setArtist(artist.getText());
                     }

            song.clearAudioFile(); // remove audio file from memory  as it takes  up alot of space
        }
       pane.updatePane();

   }

   private void displayArtAndAddToSongs(List<File> files){
        List<Artwork> songsArtwork= new ArrayList<>();
        int size=files.size();
        for(int count=0; count<size; count++) {
            String path=files.get(count).getAbsolutePath();
            Artwork artwork= new StandardArtwork();
            try {
                FileInputStream input= new FileInputStream(path);
                artwork.setBinaryData(IOUtils.toByteArray(path));
                artwork.setImageUrl(path);
                songsArtwork.add(artwork);

                Image fxImage = new Image("file:" + path);
            ImageView fxImageView = new ImageView(fxImage);
                fxImageView.setFitHeight(150);
                fxImageView.setFitWidth(150);
            CheckBox checkBox = new CheckBox("Delete Image");
            VBox imageBox = new VBox(fxImageView, checkBox);
            addImageBoxToGridPane(imageBox);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(embedAlbumArt.isSelected()){
            addAlbumArtAsEmbeddedImage(songsArtwork);
        }
        else{
        addArtToSongsAsUrl(songsArtwork);
        }
   }

    public void addArtToSongsAsUrl(List<Artwork> songsArtwork){

            int size=songs.size();
            int size2=songsArtwork.size();

            if(songsArtwork.size()>0) {
                for (int count = 0; count < size; count++) {
                    AudioInformation song = songs.get(count);
                    for (int count2 = 0; count2 < size2; count2++) {
                        try {
                            Tag tag=song.getAudioFile().getTagOrCreateAndSetDefault();
                            if(tag!=null){
                            song.getAudioFile().getTag().setField(songsArtwork.get(count2));}
                        } catch (FieldDataInvalidException e) {
                            e.printStackTrace();
                        }
                        song.getAlbumArtPaths().add(songsArtwork.get(count2).getImageUrl());

                    }
                    song.clearAudioFile(); // remove audio file from memory  as it takes  up alot of space

                }
            }

   }
    private void addAlbumArtAsEmbeddedImage(List<Artwork> songsArtwork)  {// adds given image files to the artwork tag  of an audio file


        int artSize=songsArtwork.size();
        System.out.println("Artwork "+ songsArtwork.size());
        if(artSize>0){
            int songSize=songs.size();
            for(int count=0; count<songSize; count++){
                AudioInformation song=songs.get(count);
                AudioFile audioFile=song.getAudioFile();
                System.out.println("AudioFile "+ audioFile);

                for(int count2=0; count2<artSize; count2++) {
                    try {
                        if(songsArtwork.get(count2).getBinaryData()!=null) {
                            Tag tag=audioFile.getTagOrCreateAndSetDefault();
                            if(tag!=null) {
                                tag.addField(songsArtwork.get(count2));
                            }
                        }
                    } catch (FieldDataInvalidException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    AudioFileIO.write(audioFile);
                    System.out.println("Artwork in file  "+ songs.get(count).getAudioFile().getTag().getArtworkList().size());
                } catch (CannotWriteException e) {
                    new OptionPane().showOptionPane("Cannot Write to Audio File "+e.getMessage(), "OK");
                }
                song.clearAudioFile();// remove audio file from memory  as it takes  up alot of space
            }

        }

    }
    private void deleteAlbumImages(){
        int numberOfSongs=songs.size();
        int numberOfArtwork=songsArtwork.size();

        for(int count=0; count<numberOfSongs; count++){
            AudioInformation song=songs.get(count);

                for(int count2=0; count2<numberOfArtwork; count2++) {
                    CheckBox box= (CheckBox) albumImageBoxes.get(count2).getChildren().get(1);// each image vbox has an image and delete check box delete box is is the second
                    //node  and if checked image is to be deleted
                    if(box.isSelected()){
                    AudioFile audioFile=song.getAudioFile();
                    audioFile.getTag().deleteArtworkField();// clear all the artwork
                    song.getAlbumArtPaths().remove(songsArtwork.get(count2).getImageUrl());// remove the selected album paths

                    try {
                        AudioFileIO.write(audioFile);// write the data to the file.
                    } catch (CannotWriteException e) {
                        new OptionPane().showOptionPane("Error Writing to Audio File "+e.getMessage(), "OK");
                    }

                }

                }
            song.clearAudioFile(); // remove audio file from memory  as it takes  up alot of space
        }

        int count=0;
        System.out.println("Songs Artwoerk "+songsArtwork.size());
        for (ListIterator<Artwork> it = this.songsArtwork.listIterator(); it.hasNext(); ) {
            CheckBox box= (CheckBox) albumImageBoxes.get(count).getChildren().get(1);// each image vbox has an image and delete check box delete box is is the second
            it.next();
            if(box.isSelected()) {
                it.remove();
            }
            count++;
        }

        for (ListIterator<VBox> it = this.albumImageBoxes.listIterator(); it.hasNext(); ) {
            VBox vbox=  it.next(); // get next image box
            CheckBox box= (CheckBox )vbox.getChildren().get(1);// each image vbox has an image and delete check box delete box is is the second
            if(box.isSelected()) {
                it.remove();
                        albumImageGridPane.getChildren().remove(vbox);


            }

        }
        addAlbumArtAsEmbeddedImage(songsArtwork);// re-add the remaining artwork to the songs

    }



}
