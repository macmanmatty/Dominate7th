package sample.Utilities;

import sample.Library.AudioInformation;

public class CopyAudioInformation {

  static  public void copyAudioInformation(AudioInformation information2 , AudioInformation information1) {
        information1.setAudioFilePath( information2.getAudioFilePath());
        information1.setTrackLength( information2.getTrackLength());
        information1.setBitrate( information2.getBitRate());
        information1.setArtist( information2.getArtist());
        information1.setAlbum( information2.getAlbum());
        information1.setBpm( information2.getBpm());
        information1.setCatalogNo( information2.getCatalogNo());
        information1.setComment( information2.getComment());
        information1.setComposer( information2.getComposer());
        information1.setConductor( information2.getConductor());
        information1.setCopyright( information2.getCopyright());
        information1.setCountry( information2.getCountry());
        information1.setDiscNo( information2.getDiscNo());
        information1.setDiscTotal( information2.getDiscTotal());
        information1.setEncoder( information2.getEncoder());
        information1.setEngineer( information2.getEngineer());
        information1.setEnsemble( information2.getEnsemble());
        information1.setGenre( information2.getGenre());
        information1.setGroup( information2.getGroup());
        information1.setInstruemnt( information2.getInstruemnt());
        information1.setLangauge(information2.getLaugauge());
        information1.setLyricist( information2.getLyricist());
        information1.setLyrics( information2.getLyrics());
        information1.setMedia( information2.getMedia());
        information1.setProducer( information2.getProducer());
        information1.setOriginalArtist( information2.getOriginalArtist());
        information1.setRanking( information2.getRanking());
        information1.setRating( information2.getRating());
        information1.setRecordLabel( information2.getRecordLabel());
        information1.setTempo( information2.getTempo());
        information1.setTitle( information2.getTitle());
        information1.setTrack( String.valueOf(information2.getTrack()));
        information1.setTrackTotal( information2.getTrackTotal());
        information1.setYear( information2.getYear());
        information1.setTrackLengthNumber( information2.getTrackLengthNumber());
        information1.setPlays( information2.getPlays());
        information1.setAudioFormat( information2.getAudioFormat());
        information1.setAlbumArtPaths( information2.getAlbumArtPaths());
    }
}
