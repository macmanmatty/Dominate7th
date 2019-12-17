package sample.Windows;import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.*;
import sample.AudioProcessors.*;
import sample.AudioProcessors.Encoders.ChannelMode;
import sample.AudioProcessors.Encoders.FFMpegEncoder;
import sample.Library.Settings;
import sample.Utilities.NumberField;
import sample.Utilities.AudioFileUtilities;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;public class FFMpegEnocderWindow {
    private  Button selectFiles;
    private  Button selectOutput;
    private Button encodeButton;
    private ChoiceBox<AudioCodec> audioFormatsChoice;
   private  List<AudioCodec> audioFormats= Arrays.asList(AudioCodec.values());
   private  Stage stage=  new Stage();
    private MainAudioWindow window;
    private File outputLocation;
    private Label outputPath;
   private  Button  addSettings;
   private ArrayList<TextField> optionKeys= new ArrayList<>();
    private ArrayList<TextField> optionValues= new ArrayList<>();
    private VBox optionsBox;
    private CheckBox manuallyEnterBitRate;
    private CheckBox splitCuedTracks;
    private HBox bitRateNumberBox;
    private AudioFileUtilities audioFileUtilities;
    private NumberField bitRateField;
    private VBox fileBox;
    HBox bitrateBox;
    FileListView<File> fileListView;
    private UpdateLabel updateLabel= new UpdateLabel();
    private FileProgressBar fileProgressBar= new FileProgressBar();
    private FileProcessor audioFileProcessor;
    private  VBox mainBox;
    private VBox progressWindow= new VBox();    private ChoiceBox<Integer> quality= new ChoiceBox<>();
    private ChoiceBox<Integer> bitRates= new ChoiceBox<>();
    private ChoiceBox<ChannelMode> modes= new ChoiceBox<>();
    private ArrayList<Integer> bitRateNumbers= new ArrayList<>();
    private ArrayList <Integer> ffMpegQualities =  new ArrayList<>();
    private  ChannelMode [] mpegModes= ChannelMode.values();
    private List<String> fileExtensions= new ArrayList<>();
    private Label bitRateLabel;
    private String fileSeperator;
    private Label error= new Label();
    public FFMpegEnocderWindow(  MainAudioWindow window) {
        stage.setTitle("FFmpeg Encoder");
        this.fileSeperator=window.getSystemInfo().getFileSeperator();
        this.window=window;
        audioFileUtilities=new AudioFileUtilities();
        fileExtensions=audioFileUtilities.getCodecExtensions();
        fileExtensions.add("cue");
        audioFileUtilities=new AudioFileUtilities();
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
                if (audioFileProcessor != null){
                    audioFileProcessor.setShowCompletedNotification(true);
            }
                stage.hide();

            }
        });
    }
        public void displayWindow(){
       fileListView = new FileListView<>( fileExtensions);
      quality.setItems(FXCollections.observableList(ffMpegQualities));
        modes.setItems(FXCollections.observableList(Arrays.asList(mpegModes)));
        audioFormatsChoice= new ChoiceBox<>();
        audioFormatsChoice.setItems(FXCollections.observableList(audioFormats));
        bitRates.setItems(FXCollections.observableList(bitRateNumbers));
        bitRates.getSelectionModel().select(5);
        manuallyEnterBitRate= new CheckBox("Manually Enter BitRate");
        splitCuedTracks= new CheckBox("Split tracks with a .cue file on encoding them?");
        manuallyEnterBitRate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed (ObservableValue< ? extends Boolean > observable, Boolean oldValue, Boolean newValue){
                if(manuallyEnterBitRate.isSelected()==true){
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

            Label bitRateLabel= new Label("Enter Conversion Bit Rate ");
           this.bitRateLabel= new Label("Enter Conversion Bit Rate ");


            Label audioFormatLabel= new Label("Select Audio Format ");

            bitRateNumberBox= new HBox(bitRateLabel, bitRateField);
        outputPath= new Label("Output Path:");
        selectFiles=new Button("Add Files");
        addSettings= new Button("Add FFMpeg Enocde Option");
        addSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addOptionFields();
            }
        });
        selectFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                 File file = directoryChooser.showDialog(stage);
                List<File> allFiles=audioFileUtilities.findFiles(file.getAbsolutePath() );

                fileListView.getItems().addAll( allFiles);



            }
        });        selectOutput= new Button("Select Output Location");
        selectOutput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                outputLocation = directoryChooser.showDialog(stage);
                outputPath.setText("Output Path: "+outputLocation.getAbsolutePath());
            }
        });        encodeButton= new Button("Encode Files");
        encodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              encode();
            }
        });



       mainBox= new VBox();
       mainBox.setSpacing(7);
       mainBox.getStylesheets().add("css/ffmpegEncoderWindow.css");
            CenteredLabel label= new CenteredLabel("Audio Encoder");
            label.getStyleClass().add("large");
            mainBox.getChildren().add(label);

            HBox ffmpegQuailtyBox= new HBox();
        Label quailtyLabel= new Label("Select FFmpeg Encoder Quailty:");
        ffmpegQuailtyBox.getChildren().add(quailtyLabel);
        ffmpegQuailtyBox.getChildren().add(quality);
        ffmpegQuailtyBox.setSpacing(7);
        quality.getSelectionModel().select(10);
        bitRateLabel= new Label("Select Bitrate:");
        mainBox.getChildren().add(ffmpegQuailtyBox);
        HBox mpegModexBox= new HBox();
        Label mpegMopdeLabel = new Label("Select Mpeg Mode:");
        mpegModexBox.getChildren().add(mpegMopdeLabel);
        mpegModexBox.getChildren().add(modes);
        mpegModexBox.setSpacing(7);
        modes.getSelectionModel().select(1);
        mainBox.getChildren().add(mpegModexBox);
        bitrateBox= new HBox();
        bitrateBox.getChildren().add(bitRateLabel);
        bitrateBox.getChildren().add(bitRates);
        bitrateBox.setSpacing(7);
        bitRates.getSelectionModel().select(0);
        mainBox.getChildren().add(manuallyEnterBitRate);
        mainBox.getChildren().add(bitrateBox);
        HBox selectAudioFormat= new HBox(audioFormatLabel, audioFormatsChoice);
        selectAudioFormat.setSpacing(7);
        mainBox.getChildren().add(selectAudioFormat);
        audioFormatsChoice.getSelectionModel().select(1);
        mainBox.getChildren().add(splitCuedTracks);
        mainBox.getChildren().add(selectFiles);
        mainBox.getChildren().add(selectOutput);
        mainBox.getChildren().add(outputPath);
        mainBox.getChildren().add(encodeButton);
        mainBox.getChildren().add(optionsBox);
        mainBox.getChildren().add(addSettings);
        mainBox.getChildren().add(error);
        mainBox.getChildren().add(progressWindow);
        mainBox.setSpacing(10);
        Label files= new Label("Files To Encode");
        fileListView.setMinSize(150,600);
        fileBox=new VBox(files, fileListView);
        HBox hBox=new HBox(mainBox, fileBox);
        hBox.setSpacing(5);




        Scene scene= new Scene(hBox);
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
    public FFMpegEnocderWindow getEncoderWindow(){
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


    private void encode(){


        error.setText("");
        HashMap <String, String> options=getOptionKeys();
        AudioCodec codec=audioFormatsChoice.getSelectionModel().getSelectedItem();
        int bitrate=320000;
        if (manuallyEnterBitRate.isSelected()==false){
            bitrate = bitRates.getSelectionModel().getSelectedItem();


        }
        else{
            try {
                bitrate = bitRateField.getIntValue();
            }
            catch (NumberFormatException e){
               error.setText("Bit Rate Must be a Number!");

                return;
            }
        }
        if(bitrate==0){
            error.setText("Bit Rate Cannot be Zero!");
            return;

        }

        if(outputLocation==null) {
            error.setText("Please Select an Output Location!");
            return;
        }


        FFMpegEncoder encoder= new FFMpegEncoder(fileSeperator,window.getLibrary(), outputLocation.getPath(),codec.getExtension(), codec.getCodecNumber(),bitrate,modes.getSelectionModel().getSelectedItem().getChannels(), quality.getSelectionModel().getSelectedItem(), options);
        encoder.setSplitAndEnocdeCueFiles(splitCuedTracks.isSelected());
        audioFileUtilities= new AudioFileUtilities();
        audioFileProcessor= new FileProcessor(encoder, updateLabel, fileProgressBar );
        Settings settings=window.getSettings();
        audioFileProcessor.setShowNotifications(settings.isShowProcessNotifications());
        audioFileProcessor.setShowCompletedNotification(settings.isShowNotifcationOnProcessComplete());
        ProgressWindow window= new ProgressWindow(audioFileProcessor);
        progressWindow.getChildren().clear();
        progressWindow.getChildren().add(window.getWindow());
        stage.hide();// refresh stage so added ui  nodes  show
        stage.show();
        audioFileProcessor.manipulateFiles( fileListView.getItems());
    }
}
