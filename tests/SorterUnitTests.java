import org.jaudiotagger.tag.FieldKey;
import org.junit.Test;
import sample.AcoustID.MusicBrianzJson.Artist;
import sample.AudioProcessors.AudioCompareType;
import sample.Library.AudioInformation;
import sample.SearchAudio.SearchTerms;
import sample.SearchAudio.Sorter;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;


public class SorterUnitTests {


    @Test


    public void comparator(){


        Sorter sorter= new Sorter();

        sorter.addComparetor(AudioCompareType.ARTIST);
        sorter.addComparetor(AudioCompareType.ALBUM);
        assertEquals(sorter.getComparators().size(), 2);

        sorter.sort(new ArrayList<>());

        assertEquals(sorter.getComparatorChain().size(), 2);


    }










    @Test
    public void chainTest(){


        List<AudioInformation> audioInformations= new ArrayList<>();
        AudioInformation information= new AudioInformation(false);
        information.setAlbum("live");
        information.setArtist("willie");


        audioInformations.add(information);
        information= new AudioInformation(false);
        information.setAlbum("hags");
        information.setArtist("haggard");
        audioInformations.add(information);

        information= new AudioInformation(false);
        information.setAlbum("circle");
        information.setArtist("willie");
        audioInformations.add(information);


        information= new AudioInformation(false);
        information.setAlbum("z");
        information.setArtist("john");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("aa");
        information.setArtist("willie");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("out");
        information.setArtist("willie");
        audioInformations.add(information);





        information= new AudioInformation(false);
        information.setAlbum("99");
        information.setArtist("willie");
        audioInformations.add(information);
        Sorter sorter= new Sorter();
        sorter.addComparetor(AudioCompareType.ARTIST);
        sorter.addComparetor(AudioCompareType.ALBUM);

        sorter.sort(audioInformations);
        assertEquals(audioInformations.size(), 7);



    }



    @Test
    public void serachTest(){

        List<AudioInformation> audioInformations= new ArrayList<>();
        AudioInformation information= new AudioInformation(false);
        information.setAlbum("Live");
        information.setArtist("Willie Nelson");


        audioInformations.add(information);
        information= new AudioInformation(false);
        information.setAlbum("H. A. G.");
        information.setArtist(" Merle Haggard");
        audioInformations.add(information);

        information= new AudioInformation(false);
        information.setAlbum("Country Songs Willie Style ");
        information.setArtist("Willie Nelson");
        audioInformations.add(information);


        information= new AudioInformation(false);
        information.setAlbum("z");
        information.setArtist("John Prine");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("A Road That Bends");
        information.setArtist("Willie Nelson");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("Me & Paul ");
        information.setArtist("Willie Nelson ");
        audioInformations.add(information);





        information= new AudioInformation(false);
        information.setAlbum("Full Nelson ");
        information.setArtist("Willie Nelson ");
        audioInformations.add(information);

        Sorter sorter= new Sorter();


       List<AudioInformation> searchSongs= sorter.search(FieldKey.ARTIST, "willie",  audioInformations );

       assertEquals(searchSongs.size(), 5);


    }



    @Test
    public void albumKeyTest(){

        List<AudioInformation> audioInformations= new ArrayList<>();
        AudioInformation information= new AudioInformation(false);
        information.setAlbum("Live");
        information.setArtist("Willie Nelson");


        audioInformations.add(information);
        information= new AudioInformation(false);
        information.setAlbum("H. A. G.");
        information.setArtist(" Merle Haggard");
        audioInformations.add(information);

        information= new AudioInformation(false);
        information.setAlbum("Country Songs Willie Style ");
        information.setArtist("Willie Nelson");
        audioInformations.add(information);


        information= new AudioInformation(false);
        information.setAlbum("z");
        information.setArtist("John Prine");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("A Road That Bends");
        information.setArtist("Willie Nelson");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("Me & Paul ");
        information.setArtist("Willie Nelson ");
        audioInformations.add(information);





        information= new AudioInformation(false);
        information.setAlbum("Full Nelson ");
        information.setArtist("Willie Nelson ");
        audioInformations.add(information);

        Sorter sorter= new Sorter();


        List<String> searchAlbums= sorter.getMatchingKeys(audioInformations, FieldKey.ALBUM);


        assertEquals(searchAlbums.size(), audioInformations.size());
        assertEquals(searchAlbums.get(0), audioInformations.get(0).getAlbum());


    }

    @Test
    public void serachAndTest(){

        List<AudioInformation> audioInformations= new ArrayList<>();
        AudioInformation information= new AudioInformation(false);
        information.setAlbum("Live");
        information.setArtist("Willie Nelson");
        information.setTitle("On The Road Again ");
        information.setComposer("Willie Nelson");


        audioInformations.add(information);
        information= new AudioInformation(false);
        information.setAlbum("H. A. G. ");
        information.setArtist("Merle Haggard");
        information.setTitle("Farmers Daughter");

        audioInformations.add(information);

        information= new AudioInformation(false);
        information.setAlbum("Half Nelson ");
        information.setArtist("Willie Nelson");
        information.setTitle("Pancho and Lefty (With Merle Haggard");

        audioInformations.add(information);


        information= new AudioInformation(false);
        information.setAlbum("Bruised Orange");
        information.setArtist("John Prine");
        information.setTitle("Fish & Whistle");

        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("Good Bye");
        information.setArtist("J. T. Van Zandt");
        information.setTitle("If I Needed You");

        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("Live With Friends");
        information.setArtist("Willie Nelson & Arlo Guthire");
        information.setTitle("City Of New Orleans" );

        audioInformations.add(information);





        information= new AudioInformation(false);
        information.setAlbum("Songs I Wrote");
        information.setArtist("Paul OverStreet");
        information.setTitle("I Think She Only Likes Me For My Willie" );

        audioInformations.add(information);

        Sorter sorter= new Sorter();
        List<String> searchText = new ArrayList<>();
        searchText.add("haggard");
        searchText.add("willie");

        List<FieldKey> fieldKeys= new ArrayList<>();
        fieldKeys.add(FieldKey.TITLE);
        fieldKeys.add(FieldKey.ARTIST);

        List<SearchTerms> searchTerms1= new ArrayList<>();
        searchTerms1.add(SearchTerms.Contains);
        searchTerms1.add(SearchTerms.Contains);






        List<AudioInformation> searchSongs= sorter.search(searchTerms1, searchText, fieldKeys,  audioInformations, false);

        assertEquals(searchSongs.size(), 1);
        assertEquals(searchSongs.get(0).getTitle(), "Pancho and Lefty (With Merle Haggard");


    }


    @Test
    public void getArtists(){

        List<AudioInformation> audioInformations= new ArrayList<>();
        AudioInformation information= new AudioInformation(false);
        information.setAlbum("live");
        information.setArtist("willie");


        audioInformations.add(information);
        information= new AudioInformation(false);
        information.setAlbum("hags");
        information.setArtist("haggard");
        audioInformations.add(information);

        information= new AudioInformation(false);
        information.setAlbum("circle");
        information.setArtist("willie");
        audioInformations.add(information);


        information= new AudioInformation(false);
        information.setAlbum("z");
        information.setArtist("john");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("aa");
        information.setArtist("willie");
        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("out");
        information.setArtist("willie");
        audioInformations.add(information);





        information= new AudioInformation(false);
        information.setAlbum("99");
        information.setArtist("willie");
        audioInformations.add(information);
        Sorter sorter= new Sorter();
        sorter.addComparetor(AudioCompareType.ARTIST);
        sorter.addComparetor(AudioCompareType.ALBUM);

     List<String>  artists=sorter.getMatchingKeys(audioInformations, FieldKey.ARTIST);

        assertEquals(artists.size(), 3);




    }

    @Test
    public void serachOrTest(){

        List<AudioInformation> audioInformations= new ArrayList<>();
        AudioInformation information= new AudioInformation(false);
        information.setAlbum("Live");
        information.setArtist("Willie Nelson");
        information.setTitle("On The Road Again ");
        information.setComposer("Willie Nelson");


        audioInformations.add(information);
        information= new AudioInformation(false);
        information.setAlbum("H. A. G. ");
        information.setArtist("Merle Haggard");
        information.setTitle("Farmers Daughter");

        audioInformations.add(information);

        information= new AudioInformation(false);
        information.setAlbum("Half Nelson ");
        information.setArtist("Willie Nelson");
        information.setTitle("Pancho and Lefty (With Merle Haggard");

        audioInformations.add(information);


        information= new AudioInformation(false);
        information.setAlbum("Bruised Orange");
        information.setArtist("John Prine");
        information.setTitle("Fish & Whistle");

        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("Good Bye");
        information.setArtist("J. T. Van Zandt");
        information.setTitle("If I Needed You");

        audioInformations.add(information);



        information= new AudioInformation(false);
        information.setAlbum("Live With Friends");
        information.setArtist("willie Nelson & Arlo Guthire");
        information.setTitle("City Of New Orleans" );

        audioInformations.add(information);





        information= new AudioInformation(false);
        information.setAlbum("Songs I Wrote");
        information.setArtist("Paul OverStreet");
        information.setTitle("I Think She Only Likes Me For My willie" );

        audioInformations.add(information);

        Sorter sorter= new Sorter();
        List<String> searchText = new ArrayList<>();
        searchText.add("haggard");
        searchText.add("willie");

        List<FieldKey> fieldKeys= new ArrayList<>();
        fieldKeys.add(FieldKey.TITLE);
        fieldKeys.add(FieldKey.ARTIST);

        List<SearchTerms> searchTerms1= new ArrayList<>();
        searchTerms1.add(SearchTerms.Contains);
        searchTerms1.add(SearchTerms.Contains);






        List<AudioInformation> searchSongs= sorter.search(searchTerms1, searchText, fieldKeys,  audioInformations, true );


        for (int count=0; count<searchSongs.size(); count++){

            System.out.println(searchSongs.get(count).getTitle());

        }


        assertEquals(searchSongs.size(), 3);


    }

}
