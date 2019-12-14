package sample.Windows;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;
import sample.Library.AudioInformation;
import java.io.File;
public class AudioFileInformationWindow {
    public void displayFileInfo(AudioInformation information){
        Stage stage= new Stage();
        stage.setTitle("Audio File Information");
        stage.setScene(new Scene(getAudioFileInformation(information)));
        stage.show();

    }
    public VBox getAudioFileInformation(AudioInformation information){
        VBox informationBox= new VBox();
        informationBox.getStylesheets().add("css/fileInfoWindow.css");
        Label title= new Label("Audio File Information");
        title.getStyleClass().add("title");

        Label filePath=new Label( "File Path: "+information.getAudioFilePath());
        AudioFile audioFile=information.getAudioFile();
        AudioHeader header=audioFile.getAudioHeader();
        Tag tag=audioFile.getTag();
        File file=audioFile.getFile();
        long length=file.length();
        double fileSize=length/(1024*1024);
            Text text= new Text("File: Size: ");
            text.getStyleClass().add("bold");
            Text text2= new Text(fileSize+"Mb");
        TextFlow fileSizeLabel= new TextFlow(text, text2);
        Text text3= new Text("File Duration: ");
        text3.getStyleClass().add("bold");
        Text text4= new Text(header.getTrackLength()+"");
       TextFlow duration= new TextFlow(text3, text4);
        Text text5= new Text("Sample Rate: ");
        text5.getStyleClass().add("bold");
        Text text6= new Text(header.getSampleRate());
        TextFlow sampleLabel=new TextFlow (text5, text6);
        Text text7= new Text("Enocding Type: ");
        text7.getStyleClass().add("bold");
        Text text8= new Text(header.getEncodingType());
        TextFlow encodingType= new TextFlow(text7, text8);
        Text text9= new Text("Audio Format: ");
        text9.getStyleClass().add("bold");
        Text text10= new Text(header.getFormat());
        TextFlow formatLabel= new TextFlow(text9, text10);
        Text text11= new Text("Channels: ");
        text11.getStyleClass().add("bold");
        Text text12= new Text(header.getChannels());
        TextFlow channlesLabel= new TextFlow(text11, text12);
        Text text13= new Text("Bit Rate: ");
        text13.getStyleClass().add("bold");
        Text text14= new Text(header.getBitRate() +"Kb/s");
       TextFlow bitRateLabel= new TextFlow(text13, text14);
        informationBox.getChildren().add(title);
        informationBox.getChildren().add(filePath);
        informationBox.getChildren().add(fileSizeLabel);
        informationBox.getChildren().add(duration);
        informationBox.getChildren().add(bitRateLabel);
        informationBox.getChildren().add(sampleLabel);
        informationBox.getChildren().add(encodingType);
        if(tag!=null){
            Text text15= new Text("Tag Information: ");
            text15.getStyleClass().add("bold");
            Text text16= new Text(tag.toString());
            TextFlow tagLabel=new TextFlow(text15, text16);
            informationBox.getChildren().add(tagLabel);
        }
        informationBox.getChildren().add(formatLabel);
        informationBox.getChildren().add(channlesLabel);
        return informationBox;
    }
}
