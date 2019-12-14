package sample.AudioProcessors;
import sample.Library.AudioInformation;
import java.util.ArrayList;
import java.util.List;
public class RemoveDuplicateSongs {
    public ArrayList<AudioInformation> removeDuplicateSongs(List<AudioInformation> currentSongs , List<AudioInformation> newSongs){
        ArrayList<AudioInformation> singleSongs= new ArrayList<>();
        int size=newSongs.size();
        for(int count=0; count<size; count++){
            AudioInformation song= newSongs.get(count);
            boolean isDuplicate=isDuplicateSong(song, currentSongs);
            System.out.println("Song is dupicate " +isDuplicate);
            if(isDuplicate==false){
                singleSongs.add(song);
            }
            }
            return singleSongs;
    }
        private  boolean isDuplicateSong(AudioInformation song, List<AudioInformation> currentSongs){
        int size=currentSongs.size();
            for(int count=0; count<size; count++){
                if(currentSongs.get(count).getAudioFilePath().equals(song.getAudioFilePath())){
                    System.out.println(currentSongs.get(count).getAudioFilePath());
                    System.out.println(song.getAudioFilePath());
                    return true;
                }
            }
            return false;
            }
}
