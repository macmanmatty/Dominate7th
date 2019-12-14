package sample.AudioProcessors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import sample.AcoustID.AcoustIDJson.Result;
import sample.AcoustID.MusicBrianzJson.MusicBrainzResult;

import java.util.ArrayList;
import java.util.List;

public class SaveMusicBrainzToAudioFile {
private List<String> errors= new ArrayList<>();


    public void   setResultToAudioFile(AudioFile audioFile, MusicBrainzResult result){
        Tag tag=audioFile.getTag();
        if(tag==null|| result==null){
            return;

        }
        try {
            tag.setField(FieldKey.TITLE, result.getTitle());
            tag.setField(FieldKey.ARTIST, result.getArtistCredit().get(0).getName());
            tag.setField(FieldKey.MUSICBRAINZ_WORK, result.getId());

            AudioFileIO.write(audioFile);



        } catch (FieldDataInvalidException | CannotWriteException e) {
            errors.add(e.getMessage());

        }


    }

    public void   setResultToAudioFile(AudioFile audioFile, Result acousticIdResult){
        Tag tag=audioFile.getTag();
        try {
            tag.setField(FieldKey.TITLE, acousticIdResult.getRecordings().get(0).getTitle());
            tag.setField(FieldKey.ARTIST, acousticIdResult.getRecordings().get(0).getArtists().get(0).getName());
            tag.setField(FieldKey.ACOUSTID_ID, acousticIdResult.getId());

            AudioFileIO.write(audioFile);



        } catch (FieldDataInvalidException | CannotWriteException e) {
            errors.add(e.getMessage());

        }


    }

    public List<String> getErrors() {
        return errors;
    }
}
