package sample.Windows;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.AudioProcessors.AudioCodec;
import sample.AudioProcessors.AudioInformationProcessor;
import sample.AudioProcessors.Encoders.ChannelMode;
import sample.AudioProcessors.Encoders.FFMpegEncoder;
import sample.Library.AudioInformation;
import sample.Library.Settings;
import sample.MainAudioWindow;
import sample.Utilities.AudioFileUtilities;
import sample.Utilities.NumberField;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
public class ImportCDWindow {
    private Button encodeButton;
    private ChoiceBox<AudioCodec> audioFormatsChoice;
   private  List<AudioCodec> audioFormats= Arrays.asList(AudioCodec.values());
   private  Stage stage=  new Stage();
    private MainAudioWindow window;
    private String outputPath;
    private Label outputPathLabel;
   private  Button  addSettings;
    private  Button selectOutput;
    private ArrayList<TextField> optionKeys= new ArrayList<>();
    private ArrayList<TextField> optionValues= new ArrayList<>();
    private VBox optionsBox;
    private CheckBox manuallyEnterBitRate;
    private HBox bitRateNumberBox;
    private AudioFileUtilities audioFileUtilities;
    private NumberField bitRateField;
    HBox bitrateBox;
    private UpdateLabel updateLabel= new UpdateLabel();
    private FileProgressBar fileProgressBar= new FileProgressBar();
    private AudioInformationProcessor audioInformationProcessor;
    private  VBox mainBox;
    private VBox progressWindow= new VBox();
    private ChoiceBox<Integer> quality= new ChoiceBox<>();
    private ChoiceBox<Integer> bitRates= new ChoiceBox<>();
    private ChoiceBox<ChannelMode> modes= new ChoiceBox<>();
    private ArrayList<Integer> bitRateNumbers= new ArrayList<>();
    private ArrayList <Integer> ffMpegQualities =  new ArrayList<>();
    private  ChannelMode [] mpegModes= ChannelMode.values();
    private List<String> fileExtensions= new ArrayList<>();
    private Label bitRateLabel;
    private List<AudioInformation> cdTracks;
    private String fileSeperator;
    public ImportCDWindow( MainAudioWindow window, List<AudioInformation> cdTracks) {
        this.cdTracks=cdTracks;
        fileSeperator=window.getSystemInfo().getFileSeperator();
        outputPath =window.getSettings().getLibraryPath();
        stage.setTitle("Import CD");
        this.window=window;
        fileSeperator=window.getSystemInfo().getFileSeperator();
        fileExtensions=audioFileUtilities.getCodecExtensions();
        fileExtensions.add("cue");
        bitRateNumbers.add(128000);
        bitRateNumbers.add(160000);
        bitRateNumbers.add(192000);
        bitRateNumbers.add(256000);
        bitRateNumbers.add(320000);
        bitRateNumbers.add(640000);
        bitRateNumbers.add(960000);

        for(int count=1; count<11; count++){
            ffMpegQualities.add(count);
        }
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (audioInformationProcessor != null){
                    audioInformationProcessor.setShowCompletedNotification(true);
            }
                stage.hide();
            }
        });
    }
        public void displayWindow(){
      quality.setItems(FXCollections.observableList(ffMpegQualities));
        modes.setItems(FXCollections.observableList(Arrays.asList(mpegModes)));
        audioFormatsChoice= new ChoiceBox<>();
        audioFormatsChoice.setItems(FXCollections.observableList(audioFormats));
        bitRates.setItems(FXCollections.observableList(bitRateNumbers));
        bitRates.getSelectionModel().select(5);
        manuallyEnterBitRate= new CheckBox("Manually Enter BitRate");
        manuallyEnterBitRate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed (ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue){
                if(manuallyEnterBitRate.isSelected()==false){
                    bitrateBox.getChildren().add(bitRateNumberBox);
                    bitrateBox.getChildren().remove(bitRates);
                    bitrateBox.getChildren().remove(bitRateLabel);
                }                else{
                    bitrateBox.getChildren().remove(bitRateNumberBox);
                    bitrateBox.getChildren().add(bitRateLabel);
                    bitrateBox.getChildren().add(bitRates);
                }            }
        });       quality.getSelectionModel().select(3);
        modes.getSelectionModel().select(1);
        optionsBox= new VBox();
        bitRateField= new NumberField();
        Label bitrateLabel= new Label("Enter Conversion BitRate");
        bitRateNumberBox= new HBox(bitrateLabel, bitRateField);
        outputPathLabel = new Label("Output Path: "+outputPath);
        addSettings= new Button("Add FFMpeg Enocde Option");
        addSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addOptionFields();
            }
        });
                    encodeButton= new Button("Import CD");
        encodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
             importCD();
            }
        });
            selectOutput= new Button("Select Output Location");
            selectOutput.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Stage stage = new Stage();
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File file = directoryChooser.showDialog(stage);
                    outputPath =file.getAbsolutePath();
                    outputPathLabel.setText("Output Path: "+ outputPath);
                }
            });
            mainBox= new VBox();
       mainBox.setSpacing(7);
       mainBox.getStylesheets().add("css"+fileSeperator+"importCDWindow.css");
       CenteredLabel label= new CenteredLabel("Import CD");
       label.getStyleClass().add("large");
       mainBox.getChildren().add(label);




        HBox ffmpegQuailtyBox= new HBox();
        Label quailtyLabel= new Label("Select FFmpeg Encoder Quailty:");
        ffmpegQuailtyBox.getChildren().add(quailtyLabel);
        ffmpegQuailtyBox.getChildren().add(quality);
        quality.getSelectionModel().select(10);
        bitRateLabel= new Label("Select Bitrate:");
        mainBox.getChildren().add(ffmpegQuailtyBox);
        HBox mpegModexBox= new HBox();
        Label mpegMopdeLabel = new Label("Select Mpeg Mode:");
        mpegModexBox.getChildren().add(mpegMopdeLabel);
        mpegModexBox.getChildren().add(modes);
        modes.getSelectionModel().select(1);
        mainBox.getChildren().add(mpegModexBox);
        bitrateBox= new HBox();
        bitrateBox.getChildren().add(bitRateLabel);
        bitrateBox.getChildren().add(bitRates);
        bitRates.getSelectionModel().select(0);
        mainBox.getChildren().add(manuallyEnterBitRate);
        mainBox.getChildren().add(bitrateBox);
        mainBox.getChildren().add(audioFormatsChoice);
        audioFormatsChoice.getSelectionModel().select(1);
        mainBox.getChildren().add(selectOutput);
        mainBox.getChildren().add(outputPathLabel);
        mainBox.getChildren().add(encodeButton);
        mainBox.getChildren().add(optionsBox);
        mainBox.getChildren().add(addSettings);
        mainBox.getChildren().add(progressWindow);
        Scene scene= new Scene(mainBox);
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.show();
    }
    private HashMap<String, String> getOptionKeys(){
        HashMap <String, String> optionMap= new HashMap<>();
        int size=optionValues.size();
        for(int count=0; count<size; count++){
            String key=optionKeys.get(count).getText();
            String value=optionValues.get(count).getText();
            if(key.isEmpty()==false && value.isEmpty()==false){
                optionMap.put(key, value);
            }
        }        return optionMap;
    }
    public ImportCDWindow getEncoderWindow(){
        return this;
    }
    protected  void addOptionFields(){
        TextField value= new TextField();
        TextField key= new TextField();
        optionKeys.add(key);
        optionValues.add(value);
        Label optionLabel= new Label("FFmpeg Option #"+optionValues.size()+" Key" );
        Label valueLabel= new Label("Value");
        HBox optionHbox= new HBox(optionLabel, key, valueLabel, value);
        optionHbox.setSpacing(12);
        optionsBox.getChildren().add(optionHbox);
        stage.setMinHeight(stage.getHeight()+optionHbox.getHeight()*2);
        stage.setMinWidth(stage.getWidth()+optionHbox.getWidth()*2);
        stage.hide();
        stage.show();
    }
    public void importCD(){
        HashMap <String, String> options=getOptionKeys();
        AudioCodec codec=audioFormatsChoice.getSelectionModel().getSelectedItem();
        int bitrate;
        if (manuallyEnterBitRate.isSelected()==false){
            bitrate = bitRates.getSelectionModel().getSelectedItem();
        }
        else{
            bitrate=bitRateField.getIntValue();
        }
        if(cdTracks.size()>0) {
            String artist =cdTracks.get(0).getArtist();
        if(artist==null || artist.isEmpty()){
            artist="unknown";
        }
        String album=cdTracks.get(0).getAlbum();
        if(artist==null || artist.isEmpty()){
            album="unknown";
        }
       String outputPath=this.outputPath+""+fileSeperator+""+artist+""+fileSeperator+""+album+""+fileSeperator+"";
        try {
            Files.createDirectories(Paths.get(outputPath));
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
        FFMpegEncoder encoder= new FFMpegEncoder(fileSeperator,window.getLibrary(), outputPath,codec.getExtension(), codec.getCodecNumber(),bitrate,modes.getSelectionModel().getSelectedItem().getChannels(), quality.getSelectionModel().getSelectedItem(), options);
        audioFileUtilities= new AudioFileUtilities();
        audioInformationProcessor = new AudioInformationProcessor(encoder, updateLabel, fileProgressBar );
        Settings settings=window.getSettings();
        audioInformationProcessor.setShowNotifications(settings.isShowProcessNotifications());
        audioInformationProcessor.setShowCompletedNotification(settings.isShowNotifcationOnProcessComplete());
        ProgressWindow window= new ProgressWindow(audioInformationProcessor);
        progressWindow.getChildren().clear();
        progressWindow.getChildren().add(window.getWindow());
        stage.hide();//refresh stage so added ui  nodes  show
        stage.show();
        audioInformationProcessor.manipulateAudioInformation( cdTracks);
    }
}
