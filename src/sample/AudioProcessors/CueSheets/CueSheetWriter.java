package sample.AudioProcessors.CueSheets;

import sample.Library.AudioInformation;
import sample.Library.CueSheets.CueSheet;
import sample.Utilities.FormatTime;

import java.io.*;
import java.util.List;

public class CueSheetWriter {



   public void  write(CueSheet cueSheet,File file) throws CueSheetExeception {
       try {
           PrintWriter writer= new PrintWriter(file, "UTF-8");
           String fileName=cueSheet.getFileName();
           if(fileName!=null && !(fileName.isEmpty())) {
               writer.write("FILE" + " \"" + fileName + "\"");
           }
           String artist=cueSheet.getArtist();
           if(artist!=null && !(artist.isEmpty())) {
               writer.write("PERFORMER" + " \"" + artist + "\"");
           }
           String albumName=cueSheet.getAlbumName();
          if(albumName!=null && !albumName.isEmpty()) {
               writer.write("TITLE" + " \"" + albumName + "\"");

           }

          String genre=cueSheet.getGenre();
           if(genre!=null && !genre.isEmpty()) {
               writer.write("GENRE" + " \"" + genre + "\"");

           }

           List<AudioInformation> tracks=cueSheet.getTracks();
           int size=tracks.size();
           for(int count=0; count<size; count++){
               writer.write("TRACK" + count);

               writeTrack(tracks.get(count), writer);




           }
           writer.close();


       } catch (FileNotFoundException | UnsupportedEncodingException e) {
           e.printStackTrace();
           throw new CueSheetExeception(" Errore writing cue Sheet",  e);
       }



   }

    private void writeTrack(AudioInformation information, PrintWriter writer) {
       String title=information.getTitle();
       if(title!=null&& !title.isEmpty()) {
           writer.write("TITLE" + " \"" +title + "\"");

       }
        String artist=information.getTitle();

      if(artist!=null&& !artist.isEmpty()) {
            writer.write("ARTIST" + " \"" +artist+ "\"");

        }
        String composer=information.getComposer();

        if(artist!=null&& !artist.isEmpty()) {
            writer.write("SONGWRITER" + " \"" +composer+ "\"");

        }
        long trackIndex=information.getStartFrame();
        String trackIndexString= FormatTime.formatTrackIndex(trackIndex);
        writer.write("INDEX 01 "+trackIndexString);





    }




}
