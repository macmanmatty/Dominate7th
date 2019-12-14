package sample.AudioProcessors.Encoders;
import org.bytedeco.javacv.*;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.AudioProcessors.CueSheets.CueSheetReader;
import sample.Library.AudioInformation;
import sample.Library.CueSheets.CueSheet;
import sample.Library.MusicLibrary;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Map;
public class FFMpegEncoder extends Encoder  {
    private int audioCodec;// ffmpeg audio codes
    private  String outputPath;// whewre to save encoded files to
   private  int bitrate; // encoding bitrate
  private   int channels; // number of audio channles to enode with
   private  double audioQuailty;// ffmpeg audio quailty
   private String extension;
   private boolean completed;
   private String fileName;
    private int currentErrors;
    private CueSheetReader reader;
    private String fileSeperator;
    
   private Map<String, String> options;// ffmpeg  encoding options
    public FFMpegEncoder( String fileSeperator, MusicLibrary library,String outputPath, String extension, int audioCodec, int bitrate, int channels, double audioQuailty, Map<String, String> options) {
        super(library);
        this.outputPath = outputPath;
        this.audioCodec = audioCodec;
        this.bitrate = bitrate;
        this.channels = channels;
        this.audioQuailty = audioQuailty;
        this.options=options;
        this.extension=extension;
        this.fileSeperator=fileSeperator;
        reader= new CueSheetReader();
    }
    public File encode(File audioFile) {
        currentErrors=0;
        completed=false;
        this.fileName=audioFile.getName();
        String fullOutputPath=outputPath+fileSeperator+fileName+"."+extension; // create the output path
                    File newFile= new File(fullOutputPath);
                    try {
                        newFile.createNewFile(); // make  the new file
                    }
                    catch(FileAlreadyExistsException e){// if files exists rename it
                        int counter=1;
                        boolean fileExists=true;
                        while(fileExists==true){
                           fullOutputPath=outputPath+fileSeperator+audioFile.getName()+counter+"."+extension;
                           File file= new File(fullOutputPath);
                           fileExists=file.exists();
                        }
                        try {
                            newFile.createNewFile();
                        }
                        catch(IOException ex){
                            errorMessages.add(ex.getMessage());
                            ex.printStackTrace();
                            currentErrors++;
                            return null;
                        }
                    }
                    catch (IOException e) {
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                        return null;
                    }
                    catch(UnsupportedOperationException e){
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                        return null;
                    }
                    FFmpegFrameGrabber grabber =null;
                    if(useFileInputStream ==true) {

                        try {
                            grabber= new FFmpegFrameGrabber(new FileInputStream(audioFile));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            return null;



                        }

                    }else{
                        grabber=new FFmpegFrameGrabber(audioFile);

                    }

                    try {
                        grabber.start();
                        FFmpegFrameRecorder frameRecorder =null;
                        if(useFileOutputStream==true) {
                            frameRecorder = new FFmpegFrameRecorder(new FileOutputStream(fullOutputPath), 0, 0, grabber.getAudioChannels());

                        }

                        else {

                            frameRecorder = new FFmpegFrameRecorder(fullOutputPath, 0, 0, grabber.getAudioChannels());


                        }

                        frameRecorder.setAudioCodec(audioCodec);
                        frameRecorder.setAudioBitrate(bitrate);
                        frameRecorder.setAudioChannels(channels);
                        frameRecorder.setAudioQuality(audioQuailty);
                        frameRecorder.setAudioOptions(options);
                        frameRecorder.setFormat(extension);
                        frameRecorder.setFrameRate(grabber.getFrameRate());
                        frameRecorder.start();
                        Frame frame = grabber.grabSamples();
                        while (frame!=null) {
                         frameRecorder.recordSamples(frame.samples);
                            frame =  grabber.grabSamples();
                        }
                        grabber.stop();
                        frameRecorder.stop();
                    } catch (FrameRecorder.Exception e) {
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                        return null;
                    }
                        catch (FrameGrabber.Exception e) {
                        errorMessages.add(e.getMessage());
                            currentErrors++;
                        e.printStackTrace();
                    return null;
                    }
                    catch(UnsupportedOperationException e){
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                        return null;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
        songsEncoded++;
                completed=true;
                
                return newFile;
                }
    public void splitAndEncode( File cueSheetFile) {
        currentErrors = 0;
        completed = false;
        CueSheet cueSheet = null;
        try {
            cueSheet = reader.readCueSheet(cueSheetFile);
        } catch (CueSheetExeception cueSheetExeception) {// file is corrupt or has  no information to encode so return
            cueSheetExeception.printStackTrace();
            errorMessages.add(cueSheetExeception.getMessage());
            return;
        }
        if(cueSheet!=null) {
            File audioFile = new File(cueSheet.getAudioFilePath());
            List<AudioInformation> songs = cueSheet.getTracks();
            tracksToEncode = songs.size();
            updateLabel.setText(tracksEncoded+" out of "+tracksToEncode+" tracks encoded");
            if (audioFile.exists()) {
                FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(audioFile);
                try {
                    frameGrabber.start();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                    errorMessages.add(e.getMessage());
                    return;
                }
                double lengthInSeconds=frameGrabber.getLengthInTime()/1000000;
                double framesPerSecond = frameGrabber.getLengthInAudioFrames() / (lengthInSeconds);// get frames  persecond
                System.out.println("length Frames "+frameGrabber.getLengthInAudioFrames() +"Legnth in Time "+ lengthInSeconds);
                int framesGrabbed=0;
                for (int count = 0; count < tracksToEncode; count++) {
                    AudioInformation song = songs.get(count);
                    this.fileName = audioFile.getName() + " Track " + count + " " + song.getTitle();
                    String fullOutputPath = outputPath + fileSeperator + count + song.getTitle() + "." + extension; // create the output path
                    File newFile = new File(fullOutputPath);
                    try {
                        newFile.createNewFile(); // make  the new file
                    } catch (FileAlreadyExistsException e) {// if files exists rename it
                        int counter = 1;
                        boolean fileExists = true;
                        while (fileExists == true) {
                            fullOutputPath = outputPath + fileSeperator + count + song.getTitle() + counter + "." + extension;
                            File file = new File(fullOutputPath);
                            fileExists = file.exists();
                        }
                        try {
                            newFile.createNewFile();
                        } catch (IOException ex) {
                            errorMessages.add(ex.getMessage());
                            ex.printStackTrace();
                            currentErrors++;
                            return;
                        }
                    } catch (IOException e) {
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                        return;
                    } catch (UnsupportedOperationException e) {
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                        return;
                    }
                    double startFrame = ((song.getCueStart()) * (framesPerSecond));// calculate start frame for ffmpeg.
                    double endFrame = ((song.getTrackLengthNumber()+song.getCueStart()) * framesPerSecond);
                    System.out.println("FPS "+framesPerSecond+" Start Frame "+startFrame+" End Frame "+endFrame);
                    try {
                        FFmpegFrameRecorder frameRecorder = new FFmpegFrameRecorder(fullOutputPath, 0, 0, frameGrabber.getAudioChannels());
                        frameRecorder.setAudioCodec(audioCodec);
                        frameRecorder.setAudioBitrate(bitrate);
                        frameRecorder.setAudioChannels(channels);
                        frameRecorder.setAudioQuality(audioQuailty);
                        frameRecorder.setAudioOptions(options);
                        frameRecorder.setFormat(extension);
                        frameRecorder.setFrameRate(frameGrabber.getFrameRate());
                        frameRecorder.start();
                        Frame frame = frameGrabber.grabFrame();
                        while (frame != null && framesGrabbed < endFrame) {
                            frameRecorder.recordSamples(frame.samples);
                            frame = frameGrabber.grabSamples();
                            framesGrabbed++;
                        }
                        frameRecorder.stop();
                            setTagsFromCue(audioFile, cueSheet, count, newFile);
                    } catch (FrameRecorder.Exception e) {
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                    } catch (FrameGrabber.Exception e) {
                        errorMessages.add(e.getMessage());
                        currentErrors++;
                        e.printStackTrace();
                    } catch (UnsupportedOperationException e) {
                        errorMessages.add(e.getMessage());
                        e.printStackTrace();
                        currentErrors++;
                    }
                    tracksEncoded++;
                    updateLabel.setText(tracksEncoded+" out of "+tracksToEncode+" tracks encoded");
                    completed = true;
                }
            }
        }
        songsEncoded++;
    }
    @Override
    public void setBitrate(int bitrate) {
        this.bitrate=bitrate;
    }
    @Override
    public String getActionName() {
        return "encoded  to "+extension;
    }
    @Override
    public void stopAction() {
    }
    public int getCurrentErrors(){
        return currentErrors;
    }
    public  boolean completed(){
        return completed;
    }
    @Override
    public String getFileName() {
        return fileName;
    }
}
