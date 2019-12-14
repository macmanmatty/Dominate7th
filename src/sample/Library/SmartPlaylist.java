package sample.Library;

import sample.SearchAudio.Sorter;

import java.util.List;

public class SmartPlaylist extends Playlist {

   private PlaylistFilter filter;






    public SmartPlaylist() { // used for serilzation
    }


    public SmartPlaylist(int number, PlaylistFilter filter) {
        super(number);
        this.filter = filter;
    }


    public SmartPlaylist(String name, PlaylistFilter filter) { // creates new smart playlist with a given search  filter.

        this.filter=filter;

        this.name=name;

    }

    public void createPlayList(List<AudioInformation> songs){

       setName(name);
        Sorter sorter = new Sorter();

        if(filter.getSearchText().size()<=0) {
            setAllSongs(songs);

        }
        else {

            List<AudioInformation> songs2 = sorter.search(filter.getSearchTerms(), filter.getSearchText(), filter.getFieldKeys(), songs, filter.isAnyTermsMatch());

            setAllSongs(songs2);


        }





        }









    public void updatePlayList(List<AudioInformation> songs){

        Sorter sorter = new Sorter();


        if(filter.getSearchText().size()>0) {
            List<AudioInformation> songs2 = sorter.search(filter.getSearchTerms(), filter.getSearchText(), filter.getFieldKeys(), songs, filter.isAnyTermsMatch());

            setAllSongs(songs2);
            System.out.println("smart play list siongs " +songs2.size());

        }

        else {

            setAllSongs(songs);
            System.out.println("smart play list siongs " +allSongs.size());


        }












    }


    public PlaylistFilter getFilter() {
        return filter;
    }

    public void setFilter(PlaylistFilter filter) {
        this.filter = filter;
    }



}
