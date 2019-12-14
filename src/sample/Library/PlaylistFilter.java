package sample.Library;
import org.jaudiotagger.tag.FieldKey;
import sample.SearchAudio.SearchTerms;
import java.util.ArrayList;
import java.util.List;
public class PlaylistFilter {
    private List<FieldKey> fieldKeys= new ArrayList<>();// the J audio tagger feild keys to search for the text in.
    private  List<SearchTerms> searchTerms= new ArrayList<>(); // the search terms
    // fieldKeys.get(0) corrosponds to searchTerms.get(0) and searchText.get(0);
    // contains = contains the corrosdponding string ingoring case  text  anywhere in the matching  j audiotagger field,
    // does not contain= does not contain the corrosponding string of text ingoring case  anywherein the jaudo tagger field or
    // equals= matches corrosponding string of  text exactly in the corrosponding jaudiotagger field .
    private  List<String> searchText= new ArrayList<>(); // the text to search for for.
    private boolean anyTermsMatch;// whether any of the search terms must match or all of them.
    public PlaylistFilter() { // creates a new play list filter for a smart play list  that will add all songs from the library to playlist
    }
    public PlaylistFilter(List<FieldKey> fieldKeys, List<SearchTerms> searchTerms, List<String> searchText, boolean anyTermsMatch) { // creates a new play list filter
        // with fieldKeys, search text , and search terms for a smart playlist .
        this.fieldKeys = fieldKeys;
        this.searchTerms = searchTerms;
        this.searchText = searchText;
        this.anyTermsMatch=anyTermsMatch;
    }
    public PlaylistFilter(FieldKey fieldKey,SearchTerms searchTerm, String searchText) {
        this.fieldKeys.add(fieldKey);
        this.searchTerms.add(searchTerm);
        this.searchText.add(searchText);
    }
    public List<FieldKey> getFieldKeys() {
        return fieldKeys;
    }
    public void setFieldKeys(List<FieldKey> fieldKeys) {
        this.fieldKeys = fieldKeys;
    }
    public List<SearchTerms> getSearchTerms() {
        return searchTerms;
    }
    public void setSearchTerms(List<SearchTerms> searchTerms) {
        this.searchTerms = searchTerms;
    }
    public List<String> getSearchText() {
        return searchText;
    }
    public void setSearchText(List<String> searchText) {
        this.searchText = searchText;
    }
    public boolean isAnyTermsMatch() {
        return anyTermsMatch;
    }
    public void setAnyTermsMatch(boolean anyTermsMatch) {
        this.anyTermsMatch = anyTermsMatch;
    }
}
