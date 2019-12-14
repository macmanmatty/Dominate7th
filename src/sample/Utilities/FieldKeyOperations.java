package sample.Utilities;

import org.jaudiotagger.tag.FieldKey;

import java.util.Comparator;

public class FieldKeyOperations {
    static String custom1Name="Custom 1";
    static String custom2Name="Custom 2";
    static Comparator<String> numberStringComparartor= new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            try{

                Integer int1=Integer.valueOf(o1);
                Integer int2=Integer.valueOf(o2);
                if(int1>int2){
                    return 1;
                }
                if(int1<int2){

                    return -1;

                }
                return 0;




            }
            catch(NumberFormatException e){


            }

            return -1;

        }
    };

    static Comparator<String> timeDurationComparartor= new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            try{

                Long int1=FormatTime.unformatTimeWithColons(o1);
                Long int2=FormatTime.unformatTimeWithColons(o2);

                if(int1>int2){
                    return 1;
                }
                if(int1<int2){

                    return -1;

                }
                return 0;




            }
            catch(NumberFormatException e){


            }

            return -1;

        }
    };

    public static  String getFieldKeyGetter(FieldKey key){ // turns a field key enum in to displayable string text


        switch (key){


            case ALBUM:
                return "Album";

            case ARTIST:
                return "Artist";

            case BPM:
                return "Bpm";

            case COMMENT:
                return "Comment";

            case COMPOSER:
                return "Composer";


            case CONDUCTOR:
                return "Conductor";


            case COPYRIGHT:
                return "Copyright";
            case COUNTRY:
                return "Country";

            case CUSTOM1:
                return "CueStartString";
            case CUSTOM2:
                return "BitRate";
            case CUSTOM3:
                return "AudioFormat";
            case CUSTOM4:
                return "AudioFilePath";

            case CATALOG_NO:
                return  "CatalogNumber";

            case DISC_NO:
                return "Disc";
            case DISC_TOTAL:
                return "DiscTotal";

            case ENCODER:
                return "Encoder";
            case ENGINEER:
                return "Engineer";
            case ENSEMBLE:
                return "Ensemble";

            case GENRE:
                return "Genre";
            case GROUP:
                return "Group";


            case INSTRUMENT:
                return "Instrument";

            case LANGUAGE:
                return "Language";
            case LYRICIST:
                return "Lyricist";
            case LYRICS:
                return "Lyrics";
            case MEDIA:
                return "Media";


            case PRODUCER:
                return "Producer";

            case ORIGINAL_ARTIST:
                return "OriginalArtist";

            case QUALITY:
                return "Quailty";

            case RANKING:
                return "Ranking";

            case RATING:
                return "Rating";

            case RECORD_LABEL:
                return"RecordLabel";



            case TEMPO:
                return "Tempo";

            case TITLE:
                return "Title";


            case TRACK:
                return "Track";

            case TRACK_TOTAL:
                return "TrackTotal";

            case YEAR:
                return "Year";
        }



        return "";

    }


    public static  String getFieldKeyDisplayName(FieldKey key){ // turns a field key enum in to displayable string text

        switch (key){


            case ALBUM:
                return "Album";

            case ARTIST:
                return "Artist";

            case BPM:
                return "Bpm";

            case COMMENT:
                return "Comment";

            case COMPOSER:
                return "Composer";


            case CONDUCTOR:
                return "Conductor";


            case COPYRIGHT:
                return "Copyright";
            case COUNTRY:
                return "Country";

            case CUSTOM1:
                return "Cue Start";
            case CUSTOM2:
                return "Bit Rate";
            case CUSTOM3:
                return "Audio Format";
            case CUSTOM4:
                return "File Path";

            case CATALOG_NO:
                return  "Catalog #";

            case DISC_NO:
                return "Disc # ";
            case DISC_TOTAL:
                return "# Of Dics";

            case ENCODER:
                return "Encoder";
            case ENGINEER:
                return "Engineer";
            case ENSEMBLE:
                return "Ensemble";

            case GENRE:
                return "Genre";
            case GROUP:
                return "Group";


            case INSTRUMENT:
                return "Instrument";

            case LANGUAGE:
                return "Language";
            case LYRICIST:
                return "Lyricist";
            case LYRICS:
                return "Lyrics";
            case MEDIA:
                return "Media";


            case PRODUCER:
                return "Producer";

            case ORIGINAL_ARTIST:
                return "Original Artist";

            case QUALITY:
                return "Quailty";

            case RANKING:
                return "Ranking";

            case RATING:
                return "Rating";

            case RECORD_LABEL:
                return"Record Label";



            case TEMPO:
                return "Tempo";

            case TITLE:
                return "Title";


            case TRACK:
                return "Track #";

            case TRACK_TOTAL:
                return "# Of Tracks";

            case YEAR:
                return "Year";

            default:

               return  "";

        }




    }



    public static boolean  getEditable(FieldKey key){ // turns a field key enum in to displayable string text



        switch (key){


            case ALBUM:
                return true;

            case ARTIST:
                return true;

            case BPM:
                return true;

            case COMMENT:
                return true;

            case COMPOSER:
                return true;


            case CONDUCTOR:
                return true;

            case CATALOG_NO:
                return  true;


            case COPYRIGHT:
                return true;
            case COUNTRY:
                return true;

            case CUSTOM1:
                return true;
            case CUSTOM2:
                return false;
            case CUSTOM3:
                return false;
            case CUSTOM4:
                return false;





            case DISC_NO:
                return true;
            case DISC_TOTAL:
                return true;

            case ENCODER:
                return true;
            case ENGINEER:
                return true;
            case ENSEMBLE:
                return true;

            case GENRE:
                return true;
            case GROUP:
                return true;


            case INSTRUMENT:
                return true;

            case LANGUAGE:
                return true;
            case LYRICIST:
                return true;
            case LYRICS:
                return true;
            case MEDIA:
                return true;


            case PRODUCER:
                return true;

            case QUALITY:
                return true;

            case RANKING:
                return true;

            case RATING:
                return true;

            case RECORD_LABEL:
                return true;



            case TEMPO:
                return true;

            case TITLE:
                return true;


            case TRACK:
                return true;

            case TRACK_TOTAL:
                return true;

            case YEAR:
                return true;
        }



        return false;

    }

    public static Comparator<String> getNumberStringComparartor() {
        return numberStringComparartor;
    }

    public static Comparator<String> getTimeDurationComparartor() {
        return timeDurationComparartor;
    }
}
