package sample.SearchAudio;

import org.jaudiotagger.tag.FieldKey;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.Library.AudioInformation;

import java.util.List;
import java.util.Random;

public class Shuffle {
    private Random random= new Random();
    private ExtractAudioInformation extractAudioInformation= new ExtractAudioInformation();
    private AudioInformation previousSong;



    public AudioInformation getShuffleSong(List<FieldKey> shuffleKeys, List<AudioInformation> songs){


        AudioInformation  song=null;
        int counter=0;
        if(previousSong!=null) {
            while (song == null) {
                int randomNumber = random.nextInt(songs.size() - 1);
                AudioInformation newSong = songs.get(randomNumber);
                int size = shuffleKeys.size();
                boolean notTheSame=true;
                for (int count = 0; count < size; count++) {
                    String text = extractAudioInformation.getInformation(newSong, shuffleKeys.get(count));
                    String text2 = extractAudioInformation.getInformation(previousSong, shuffleKeys.get(count));
                    if(text.equals(text2)){
                        notTheSame=false;
                        break;

                    }


                }
                if(notTheSame==true){
                    song=songs.get(randomNumber);
                }


                if (counter == songs.size() - 3) {

                    randomNumber = random.nextInt(songs.size() - 1);
                    song = songs.get(randomNumber);
                    break;

                }


            }
        }
        else{

           int randomNumber = random.nextInt(songs.size() - 1);
            song = songs.get(randomNumber);

        }

        previousSong=song;
        return song;


    }

    public AudioInformation getPreviousSong() {
        return previousSong;
    }

    public void setPreviousSong(AudioInformation previousSong) {
        this.previousSong = previousSong;
    }


    public AudioInformation getRandomSong(List<AudioInformation> songs) {
        Random random=new Random();
        System.out.println("getting Songs");
        int randomNumber=	random.nextInt(songs.size()-1);


        return  songs.get(randomNumber);
    }
}
