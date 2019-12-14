package sample.SearchAudio;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.tag.FieldKey;
import sample.AudioProcessors.AudioCompareType;
import sample.AudioProcessors.ExtractAudioInformation;
import sample.Library.AudioInformation;
import sample.SearchAudio.SearchTerms;
import java.util.*;
public class Sorter {
    ExtractAudioInformation getInformtion= new ExtractAudioInformation();
    ComparatorChain comparatorChain= new ComparatorChain();
    private HashSet<Comparator> comparators= new HashSet<>();
    private Comparator<AudioInformation> artistComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
           String artist1= information1.getArtist();
           String artist2=information2.getArtist();
            return artist1.compareToIgnoreCase(artist2);
        }
    };
   private  Comparator<AudioInformation> titleComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String artist1= information1.getTitle();
            String artist2=information2.getTitle();
            return artist1.compareToIgnoreCase(artist2);
        }
    };
    private  Comparator<AudioInformation> conductorComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String artist1= information1.getConductor();
            String artist2=information2.getConductor();
            return artist1.compareToIgnoreCase(artist2);
        }
    };
  private   Comparator<AudioInformation> albumComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getAlbum();
            String s2=information2.getAlbum();
            return s1.compareToIgnoreCase(s2);
        }
    };
   private  Comparator<AudioInformation> bitRateComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getBitRate();
            String s2=information2.getBitRate();
            return s1.compareToIgnoreCase(s2);
        }
    };
   private  Comparator<AudioInformation> genereComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getGenre();
            String s2=information2.getGenre();
            return s1.compareToIgnoreCase(s2);
        }
    };
  private   Comparator<AudioInformation> lengthComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getTrackLength();
            String s2=information2.getTrackLength();
            return s1.compareToIgnoreCase(s2);
        }
    };
  private   Comparator<AudioInformation> composerComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getComposer();
            String s2=information2.getComposer();
            return s1.compareToIgnoreCase(s2);
        }
    };
   private  Comparator<AudioInformation> yearComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getYear();
            String s2=information2.getYear();
            return s1.compareToIgnoreCase(s2);
        }
    };
  private   Comparator<AudioInformation> trackComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getTrack();
            String s2=information2.getTrack();
            return s1.compareToIgnoreCase(s2);
        }
    };
   private Comparator<AudioInformation> discComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getDiscNo();
            String s2=information2.getDiscNo();
            return s1.compareToIgnoreCase(s2);
        }
    };
   private  Comparator<AudioInformation> engineerComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getEngineer();
            String s2=information2.getEngineer();
            return s1.compareToIgnoreCase(s2);
        }
    };
  private   Comparator<AudioInformation> producerComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getProducer();
            String s2=information2.getProducer();
            return s1.compareToIgnoreCase(s2);
        }
    };
   private  Comparator<AudioInformation> lyricistComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getLyricist();
            String s2=information2.getLyricist();
            return s1.compareToIgnoreCase(s2);
        }
    };
   private Comparator<AudioInformation> encoderComparator= new Comparator<AudioInformation>() {
        @Override
        public int compare(AudioInformation information1, AudioInformation information2) {
            String s1= information1.getEncoder();
            String s2=information2.getEncoder();
            return s1.compareToIgnoreCase(s2);
        }
    };
    
    
    
    public void sort(List<AudioInformation> information){// sorts a list of audio information using the  apache comparetor chain
        int size=comparators.size();
        ArrayList<Comparator> comparetorsArray= new ArrayList<>();
        comparetorsArray.addAll(comparators);
        for(int count=0; count<size; count++){
            comparatorChain.addComparator(comparetorsArray.get(count));
        }
        Collections.sort(information, comparatorChain);
    }
    
    
    public void addComparetor(AudioCompareType type){
        
        
        
        switch (type){
            case ARTIST:
                comparators.add(artistComparator);
                break;
            case TRACK:
                comparators.add(trackComparator);
                break;
            case ALBUM:
                comparators.add(albumComparator);
                break;
            case TITLE:
                comparators.add(titleComparator);
                break;
            case YEAR:
                comparators.add(yearComparator);
                break;
            case ENCODER:
                comparators.add(encoderComparator);
                break;
            case BIT_RATE:
                comparators.add(bitRateComparator);
                break;
            case LENGTH:
                comparators.add(lengthComparator);
                break;
            case COMPOSER:
                comparators.add(composerComparator);
                break;
            case CONDUCTOR:
                comparators.add(conductorComparator);
                break;
            case PRODUCER:
                comparators.add(producerComparator);
                break;
            case ENGINEER:
                comparators.add(engineerComparator);
                break;
            case DISC:
                comparators.add(discComparator);
                break;
            case LYRICIST:
                comparators.add(lyricistComparator);
                break;
            case GENERE:
                comparators.add(genereComparator);
                break;
        }
    }
    public void removeComparetor(AudioCompareType type){
        switch (type){
            case ARTIST:
                comparators.remove(artistComparator);
                break;
            case TRACK:
                comparators.remove(trackComparator);
                break;
            case ALBUM:
                comparators.remove(albumComparator);
                break;
            case TITLE:
                comparators.remove(titleComparator);
                break;
            case YEAR:
                comparators.remove(yearComparator);
                break;
            case ENCODER:
                comparators.remove(encoderComparator);
                break;
            case BIT_RATE:
                comparators.remove(bitRateComparator);
                break;
            case LENGTH:
                comparators.remove(lengthComparator);
                break;
            case COMPOSER:
                comparators.remove(composerComparator);
                break;
            case CONDUCTOR:
                comparators.remove(conductorComparator);
                break;
            case PRODUCER:
                comparators.remove(producerComparator);
                break;
            case ENGINEER:
                comparators.remove(engineerComparator);
                break;
            case DISC:
                comparators.remove(discComparator);
                break;
            case LYRICIST:
                comparators.remove(lyricistComparator);
                break;
            case GENERE:
                comparators.remove(genereComparator);
                break;
        }
    }
    public  void addMultipleComparetors(ArrayList<AudioCompareType> compares){
        int size=compares.size();
        for(int count=0; count<size; count++){
            addComparetor(compares.get(count));
        }
    }
    public  void removeMultipleComparetors(ArrayList<AudioCompareType> compares){
        int size=compares.size();
        for(int count=0; count<size; count++){
            removeComparetor(compares.get(count));
        }
    }
    public List<AudioInformation>  search(String text, List<AudioInformation> songs, List<FieldKey> fieldKeys,  boolean and ){
        List<String> searchTerms=parseTextForQuotes(text);
        ArrayList<AudioInformation> searchedSongs = new ArrayList<>();
        if(and==false) {
            int size2 = searchTerms.size();
            int size = songs.size();
            for (int count = 0; count < size; count++) {
                AudioInformation song = songs.get(count);
                ArrayList<String> information = getInformtion.getInformation(fieldKeys, songs.get(count));
                for (int count2 = 0; count2 < size2; count2++) {
                    text = searchTerms.get(count2);
                    boolean hasText = hasText(information, text);
                    if (hasText == true) {
                        searchedSongs.add(song);
                    }
                }
            }
        }
        else{
            int size = songs.size();
            for (int count = 0; count < size; count++) {
                AudioInformation song = songs.get(count);
                ArrayList<String> information = getInformtion.getInformation(fieldKeys, songs.get(count));
                    boolean hasText = hasText(information,searchTerms);
                    if (hasText == true) {
                        searchedSongs.add(song);
                    }
                }
            }
        return searchedSongs;
    }
    public List<AudioInformation>  search(String text, List<AudioInformation> songs, List<FieldKey> fieldKeys){
        ArrayList<AudioInformation> searchedSongs = new ArrayList<>();
            int size = songs.size();
            for (int count = 0; count < size; count++) {
                AudioInformation song = songs.get(count);
                ArrayList<String> information = getInformtion.getInformation(fieldKeys, songs.get(count));
                    boolean hasText = hasText(information, text);
                    if (hasText == true) {
                        searchedSongs.add(song);
                    }
                }
            return searchedSongs;
        }
    private List<String> parseTextForQuotes(String text) {
        ArrayList<String> searchTerms= new ArrayList<>();
        String [] strings=text.split("\\s+");
        searchTerms.addAll(Arrays.asList(strings));
        return searchTerms;
    }
    public ArrayList<AudioInformation>  search(List<SearchTerms> terms, List<String> text, List<FieldKey> fieldKeys, List<AudioInformation> songs, boolean anyTermsMath) {
        if(anyTermsMath==true){
            return searchOr(terms, text, fieldKeys, songs);
        }
        else{
            return searchAnd(terms, text, fieldKeys, songs);
        }
    }
        public ArrayList<AudioInformation>  searchAnd(List<SearchTerms> terms, List<String> text, List<FieldKey> fieldKeys, List<AudioInformation> songs){
        ArrayList<AudioInformation> searchedSongs = new ArrayList<>();
        int size=songs.size();
        for(int count=0; count<size; count++){
            AudioInformation song=songs.get(count);
            int size2=text.size();
            boolean canAdd=true;
            for(int count2=0; count2<size2; count2++){
                String searchText=text.get(count2);
                SearchTerms searchTerm=terms.get(count2);
                FieldKey searchKey=fieldKeys.get(count2);
                if(searchText==null || searchText.isEmpty() ){
                    continue;
                }
              canAdd=fullSongMatch(song, searchText, searchTerm, searchKey);
                if(canAdd==false){
                    break;
                }
            }
            if(canAdd==true) {
                searchedSongs.add(song);
            }
        }
        return searchedSongs;
    }
    public ArrayList<AudioInformation>  searchOr(List<SearchTerms> terms, List<String> text, List<FieldKey> fieldKeys, List<AudioInformation> songs){
        ArrayList<AudioInformation> searchedSongs = new ArrayList<>();
        int size=songs.size();
        for(int count=0; count<size; count++){
            AudioInformation song=songs.get(count);
            int size2=text.size();
            for(int count2=0; count2<size2; count2++){
                String searchText=text.get(count2);
                SearchTerms searchTerm=terms.get(count2);
                FieldKey searchKey=fieldKeys.get(count2);
                if(searchText==null || searchText.isEmpty()  || searchTerm==null){ // if no search term or search text  continue nothing to search for
                    continue;
                }
                boolean canAdd=fullSongMatch(song, searchText, searchTerm, searchKey); // see if song matches
                if(canAdd==true){
                    searchedSongs.add(song);
                    break;
                }
            }
        }
        return searchedSongs;
    }
    private boolean fullSongMatch(AudioInformation song, String searchText, SearchTerms searchTerm, FieldKey searchKey) {
        if(searchKey!=null) {
            if(searchTerm!=null) {
                switch (searchTerm) {
                    case Contains:
                        String information = getInformtion.getInformation(song, searchKey);
                        return StringUtils.containsIgnoreCase(information, searchText);
                    case Does_Not_Contain:
                        information = getInformtion.getInformation(song, searchKey);
                        return !(StringUtils.containsIgnoreCase(information, searchText));
                    case Equals:
                        information = getInformtion.getInformation(song, searchKey);
                        return information.equals(searchTerm);
                }
            }
            else{
                return true;
            }
        }
        else{
            if(searchTerm!=null) {
                switch (searchTerm) {
                    case Contains:
                        ArrayList<String> information = getInformtion.getAllInformation(song);
                        return hasText(information, searchText);
                    case Does_Not_Contain:
                        information = getInformtion.getAllInformation(song);
                        return !(hasText(information, searchText));
                    case Equals:
                        information = getInformtion.getAllInformation(song);
                        return stringInList(searchText, information);
                }
            }
            else{
                return true;
            }
        }
        return false;
    }
    public List<AudioInformation>  search(FieldKey key, String text, List<AudioInformation> songs ){
        ArrayList<AudioInformation> searchedSongs = new ArrayList<>();
        int size=songs.size();
        for(int count=0; count<size; count++){
            AudioInformation song=songs.get(count);
           String information=getInformtion.getInformation(song, key);
            boolean hasText=hasText(information, text);
            if(hasText==true){
                searchedSongs.add(song);
            }
        }
        return searchedSongs;
    }
    private boolean hasText(ArrayList<String> information, String searchText ){// checks if any strings in list of string contains a given string returns true if ANY of the strings in the list  contain the given string
        int size= information.size();
        for(int count=0; count<size; count++) {
            boolean hasText=  StringUtils.containsIgnoreCase(information.get(count), searchText);
            if(hasText==true){
                return true;
            }
        }
        return false;
    }
    private boolean hasText(List<String> information, List<String> searchText ){ // check if   of a list of strings containes ALL
        // strings in another list returns true if ALL of the strings in the list  contain all of  the given strings in the other list
        List searchTextCopy= new ArrayList(); // copy list for removal of matched text
        searchTextCopy.addAll(searchText);
        int size= information.size();
        for(int count=0; count<size; count++) {
            int size2=searchText.size();
            Iterator<String > searchTextIt=searchTextCopy.iterator(); // get irator to remove search text if found
            while(searchTextIt.hasNext()){
                boolean hasText = StringUtils.containsIgnoreCase(information.get(count), searchTextIt.next());
                if(hasText==true)  {
                    searchTextIt.remove(); // we matched text  so remove it no need match again
                    if(searchTextCopy.size()==0){ // we have matched all information so  return true song matches
                        return true;
                    }
                }
            }
        }
        if(searchTextCopy.size()==0){ // we have matched all information so  return true song matches
            return true;
        }
            return false;
    }
    private boolean hasText(String information, String searchText ){ // checks of string contains another string in any part of it retusn true if the other string does
     return    StringUtils.containsIgnoreCase(information, searchText);
    }
    public List<String> getMatchingKeys(List<AudioInformation> allSongs, FieldKey key) {
        ArrayList<String> names= new ArrayList<>();
        int size=allSongs.size();
        for(int count=0; count<size; count++) {
            String name=getInformtion.getInformation(allSongs.get(count), key);
            boolean inList=stringInList(name, names);
            if(inList==false && !(name.isEmpty())){
                names.add(name);
            }
            }
            return names;
        }
    public boolean stringInList(String name, List<String> names) { // checks if a string exists in list of strings returns true if does and false if does not
        int size=names.size();
        for(int count=0; count<size; count++) {
            if(name.equalsIgnoreCase(names.get(count))){
                return true;
        }
        }
        return false;
    }

      public void addNewStrings(List<String> list , List<String> newStrings){
        int size=newStrings.size();
        for(int count=0; count<size; count++){
            boolean inList=stringInList( newStrings.get(count), list);
            if(inList==false){
                list.add(newStrings.get(count));
                System.out.println("String added!");
                continue;


            }

            System.out.println("String not Added!");

        }

    }
    
    
    
    public List<String> getMatchingKeys(List<AudioInformation> allSongs, FieldKey key, String item1, FieldKey key2) {//returns a list of all individual fieldkeys where  the text  matches   the first field key
        ArrayList<String> names= new ArrayList<>();
    List<AudioInformation> songs= search(key,  item1, allSongs);
        int size=songs.size();
        for(int count=0; count<size; count++) {
                String name=getInformtion.getInformation(songs.get(count), key2);
            boolean inList=stringInList(name, names);
            if(inList==false && !(name.isEmpty())){ //no need for empty strings or if the string is in the list allready don't add it.
                names.add(name);
            }
            }
        return names;
    }
    public ComparatorChain getComparatorChain() {
        return comparatorChain;
    }
    public HashSet<Comparator> getComparators() {
        return comparators;
    }
    ;
}
