package sample.AudioProcessors.CueSheets;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import sample.Library.AudioInformation;
import sample.Library.CueSheets.CueSheet;
import sample.Utilities.AudioFileUtilities;
import sample.Utilities.FormatTime;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CueSheetReader {// class for reading .cue sheets assoicated with audio files

    File cueFile;
    List<String> data= new ArrayList<>();
    String diskArtist="";
    String  diskTitle="";
    String diskGenre="";
    String diskYear="";
    String audioFilePath;
    boolean trackAlbumSet;
    boolean trackDateSet;
    boolean trackArtistSet;
    boolean trackGenreSet;
    String fileSeperator;

    public CueSheetReader() {
        fileSeperator=System.getProperty("file.separator");
    }

    public CueSheet readCueSheet(File file) throws CueSheetExeception {
        this.cueFile=file;
        data= new ArrayList<>();
        diskArtist="";
         diskTitle="";
        diskGenre="";
        diskYear="";
        audioFilePath="";
       trackAlbumSet=false;
        trackDateSet=false;
        trackArtistSet=false;
         trackGenreSet=false;

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(cueFile);
        } catch (FileNotFoundException e) {
            throw new CueSheetExeception("Cue Sheet "+ cueFile.getAbsolutePath()+" not Found", e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

        //read the lines
        while (true) {
            try {

                String line = reader.readLine();
                data.add(line);
                if (line == null) {
                    break;

                }

            } catch (IOException e) {
                throw new CueSheetExeception("Error " +cueFile.getAbsolutePath()+" Reading Cue Sheet", e);
            }


        }
        return  parseData(data);
    }

    public CueSheet parseData(List<String> fileLines ) throws CueSheetExeception {
        CueSheet cueSheet = new CueSheet();
        cueSheet.setCueFilePath(cueFile.getAbsolutePath());
        boolean hasTracks=false;

        for(Iterator<String> it=fileLines.iterator(); it.hasNext();) {
            String line = it.next();
            if(line!=null && !line.isEmpty()) {

                line=line.trim();
                if (StringUtils.startsWithIgnoreCase(line, "track")) { // starting individual track info so break out

                    hasTracks=true;
                break;
            }
                parseLine(line, cueSheet);
                it.remove();
                System.out.println(line);


            }

        }
        if(hasTracks==false){
            throw new CueSheetExeception("Cannot Read"+cueFile.getAbsolutePath()+"  Sheet, No Track Data Found" );



        }
        List<AudioInformation> tracks=getTracks(fileLines);
        cueSheet.setTracks(tracks);

                boolean hasFile=cueSheet.hasAudioFile();
                if(hasFile==false){// throw new cue exeception as there is no audio file to calculate track lengths from
                    throw new CueSheetExeception(" Assiocated Audio File At "+cueSheet.getAudioFilePath()+" Was Not Found Does File Name Match The Cue File Tag Exactly?");

                }


            long totalDuration = 0;
            AudioFile audioFile=cueSheet.getAudioFile();
            if(audioFile!=null) {
                totalDuration = audioFile.getAudioHeader().getTrackLength();
            }
            else {
               File file=cueSheet.getPhysicalFile();
               if(file!=null){

                   try {
                       totalDuration=new AudioFileUtilities().getTrackLengthUsingFFmpeg(file);
                   } catch (FrameGrabber.Exception e) {
                       e.printStackTrace();
                       throw new CueSheetExeception("Cannot Calculate Duration of "+file.getAbsolutePath(), e);// throw new exeception as track length couldn't be calculated by ffmpeg
                   }

               }

            }

                cueSheet.setTotalTime(totalDuration);


            if(totalDuration>0) {
                setTrackDurationForCueTracks(tracks, Math.toIntExact(totalDuration));
            }



        return cueSheet;

    }

   private List<AudioInformation> getTracks(List<String> data) throws CueSheetExeception {
        List<AudioInformation> tracks = new ArrayList<>();
        int size = data.size();
        int numberOfTracks = 0;
        int start=0;
        AudioInformation currentTrack= new AudioInformation(false);

        for (int count = 0; count < size; count++){
            

            String line=data.get(count);
            if(line!=null && !line.isEmpty()) {

                line=line.trim();

                if (StringUtils.startsWithIgnoreCase(line, "TRACK")) {
                //  set the track length
                System.out.println("New TRack!");
               trackInformationCheck(currentTrack);


                    currentTrack = new AudioInformation(false);
                trackArtistSet=false;
                trackDateSet=false;
                trackGenreSet=false;
                tracks.add(currentTrack);

                    numberOfTracks++;
                currentTrack.setTrack(numberOfTracks + "");
                currentTrack.setHasCue(true);


            }


                parseLine(line, currentTrack);
            }

            }
       trackInformationCheck(currentTrack);


       return  tracks;

    }

    private void trackInformationCheck(AudioInformation currentTrack){ // the current individual  track artist album name  year and genre fields  are not set
        // it sets them to what ever the main cue information sheet says.

        if(trackGenreSet==false){
            currentTrack.setGenre(diskGenre);
        }

        if(trackArtistSet==false){
            currentTrack.setArtist(diskArtist);
        }

        if(trackDateSet==false){
            currentTrack.setYear(diskYear);
        }

            currentTrack.setAlbum(diskTitle);
        currentTrack.setAudioFilePath(audioFilePath);

    }

    private void parseLine(String line, AudioInformation information) throws CueSheetExeception {
        String [] data=line.split("\"");// split on quotes to get the data
        String[] parts = line.split("\\s+");// split on  spaces to get data type identifier
        if(parts.length>1) {


            if (StringUtils.startsWithIgnoreCase(parts[0], "index") && StringUtils.startsWithIgnoreCase(parts[1], "01")) {
                if (parts.length > 2) {
                    int gap = Integer.valueOf(parts[1]);
                    int cueStart = getStartTime(parts[2]);
                    int frames=getStartFrames(parts[2]);
                    information.setCueStart(cueStart);
                    information.setStartFrame(frames);
                }


            } else {

                if (data.length > 1) {
                    String dataType = parts[0];

                    String dataInfo = data[1];
                   dataInfo= dataInfo.replace("\"", "");



                    if (StringUtils.startsWithIgnoreCase(dataType, "title")) {
                            information.setTitle(dataInfo);


                    } else if (StringUtils.startsWithIgnoreCase(dataType, "songwriter")) {
                        information.setComposer(dataInfo);


                    } else if (StringUtils.startsWithIgnoreCase(dataType, "performer")) {
                        if (!dataInfo.isEmpty()) {

                            information.setArtist(dataInfo);
                            trackArtistSet = true;
                        }
                    }

                        else if (StringUtils.startsWithIgnoreCase(dataType, "genre")) {
                            if(!dataInfo.isEmpty()) {

                                information.setArtist(dataInfo);
                                trackGenreSet=true;
                            }

                    }

                        else if (StringUtils.startsWithIgnoreCase(dataType, "date")) {
                            if (!dataInfo.isEmpty()) {

                                information.setArtist(dataInfo);
                                trackDateSet = true;
                            }

                        }

                }
            }
        }

    }

    private void parseLine(String line, CueSheet sheet) throws CueSheetExeception {

        String[] data=line.split("\"");
            String[] parts = line.split("\\s+");
            if(parts.length>1) {


                if (parts.length >= 2) {
                    if (StringUtils.startsWithIgnoreCase(parts[0], "REM")) {
                        if (parts.length >2 && data.length > 1) {
                            String dataInfo = data[1].replace("\"", "");

                            if (StringUtils.startsWithIgnoreCase(parts[1], "genre")) {

                                    sheet.setGenre(dataInfo);
                                    diskGenre=dataInfo;


                            } else if (StringUtils.startsWithIgnoreCase(parts[1], "title")) {
                                    sheet.setAlbumName(dataInfo);
                                    diskTitle=dataInfo;



                            }  else if (StringUtils.startsWithIgnoreCase(parts[1], "comment")) {

                                sheet.setComment(dataInfo);



                            }
                            else if (StringUtils.startsWithIgnoreCase(parts[1], "date")) {
                                sheet.setYear(dataInfo);
                                diskYear=dataInfo;
                                


                            }
                        }

                    }


                    String dataType = parts[0];
                    if (data.length > 1) {
                        String dataInfo = data[1].replace("\"", "");

                        if (StringUtils.startsWithIgnoreCase(dataType, "genre")) {

                            sheet.setGenre(dataInfo);
                            diskGenre=dataInfo;


                        } else if (StringUtils.startsWithIgnoreCase(dataType, "title")) {
                            sheet.setAlbumName(dataInfo);
                            diskTitle=dataInfo;


                        } else if (StringUtils.startsWithIgnoreCase(dataType, "file")) {
                            audioFilePath=cueFile.getParent() +fileSeperator + dataInfo;
                            sheet.setAudioFilePath(audioFilePath);
                            System.out.println(audioFilePath);


                        }
                        else if (StringUtils.startsWithIgnoreCase(dataType, "performer")) {
                            sheet.setArtist(dataInfo);
                            diskArtist=dataInfo;



                        }

                    }
                }
            }

    }


    public   int getStartTime(String duration) throws CueSheetExeception {

            String[] parts = duration.split(":");
        if(parts.length>2) {

            int partialSeconds=Integer.valueOf(parts[2])/75;
            int seconds = Integer.valueOf(parts[1]);
        int minutes = Integer.valueOf(parts[0]);


        int time=seconds+(minutes*60+partialSeconds);


        return time;

        }
        else{

            throw new CueSheetExeception("Cannot Read Cue Sheet  No Vaid Start Index For Track");
        }


    }

    public   int getStartFrames(String duration) throws CueSheetExeception {
        String[] parts = duration.split(":");
        int time=0;
        if(parts.length>2) {
            int frames = Integer.valueOf(parts[2]);
            int seconds = Integer.valueOf(parts[1]);
            int minutes = Integer.valueOf(parts[0]);



             time = frames+(seconds * 75) + (minutes * 60 * 75) ;


            return time;
        }
        else{

            throw new CueSheetExeception("Cannot Read Cue Sheet  No Valid Start Index For Track");
        }

    }


    public void setTrackDurationForCueTracks(List<AudioInformation> cueTracks, int totalRunTime){

        int size=cueTracks.size();
        AudioInformation currentTrack= new AudioInformation();
        AudioInformation nextTrack= new AudioInformation();
        int start=0;


        for(int count=0; count<size; count++) {
            currentTrack=cueTracks.get(count);

            if(count<size-1) {// calculate run time for tracks
                nextTrack=cueTracks.get(count+1);
                long trackLength = nextTrack.getCueStart() - currentTrack.getCueStart();
                currentTrack.setTrackLength(FormatTime.formatTimeWithColons((long) trackLength));
                currentTrack.setTrackLengthNumber(trackLength);
            }

            else{// calculate run time for last track
                long trackLength = totalRunTime - currentTrack.getCueStart();
                currentTrack.setTrackLength(FormatTime.formatTimeWithColons((long) trackLength));
                currentTrack.setTrackLengthNumber(trackLength);

            }


        }


    }






}




