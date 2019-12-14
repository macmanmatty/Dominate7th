package sample.AudioProcessors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;
import sample.AcoustID.CoverArtArchive.CoverArtResult;
import sample.Utilities.HttpReader;
import sample.Library.AudioInformation;
import sample.Windows.OptionPane;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveAlbumArtFromWeb {

    private HttpReader httpToJson= new HttpReader();
    private String coverArtArchiveUrl="http://coverartarchive.org/release/";
   private  boolean embedInToAudioFile;
   String fileSeperator;


    public SaveAlbumArtFromWeb( boolean embedInToAudioFile) {
        this.embedInToAudioFile = embedInToAudioFile;
        fileSeperator=System.getProperty("file.separator");
    }

    

    public void getAlbumArtFromWeb(String musicBrainzId, AudioInformation information){
        String resultUrl=coverArtArchiveUrl+musicBrainzId;
        try {
           CoverArtResult coverArtResult= httpToJson.getJSONFromUrl(resultUrl, CoverArtResult.class);
           int size=coverArtResult.getImages().size();
            List<String> imageUrls= new ArrayList<>();
            for(int count=0; count<size; count++) {
               imageUrls.add(coverArtResult.getImages().get(count).getImage());


           }


           List<BufferedImage>  images = getImages(imageUrls);
            if (embedInToAudioFile==true){

                embedImages( images, information);

            }
            else{

                saveImagesToFileAndLinkInAudioFile(images, information);
            }


        }
        catch (IOException e){
            e.printStackTrace();


        }


    }

    private List<BufferedImage> getImages(List<String> imageUrls){

        List<BufferedImage> images= new ArrayList<>();
        int size=imageUrls.size();
        for(int count=0; count<size; count++) {

            String imageUrl=imageUrls.get(count);

            if(imageUrl!=null && !(imageUrl.isEmpty())) {
                try {
                  BufferedImage image=  httpToJson.saveImageFromUrl(imageUrl);
                  images.add(image);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }

        return images;
    }

    public void saveImagesToFileAndLinkInAudioFile(List<BufferedImage> songsArtwork, AudioInformation song){


        int size2=songsArtwork.size();
        AudioFile audioFile=song.getAudioFile();
        int size=songsArtwork.size();
        String outputPath=audioFile.getFile().getParentFile().getAbsolutePath();
        String albumName=song.getAlbum();
        if(albumName==null || albumName.isEmpty()){
            albumName="album";
        }
        List<String>filePaths= new ArrayList<>();
        for(int count=0; count<size; count++) {
            byte[] imageBytes = ((DataBufferByte) songsArtwork.get(count).getRaster().getDataBuffer()).getData();
            String imagePath=outputPath + fileSeperator + albumName + count + ".jpg";
            File file = new File(imagePath);
            try {
                file.createNewFile();
            } catch (IOException e) {
                new OptionPane().showOptionPane("Cannot Create File " + e.getMessage(), "OK");
            }
            try {
                FileOutputStream fileOut = new FileOutputStream(file);
                fileOut.write(imageBytes);
            } catch (FileNotFoundException e) {
                new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
            } catch (IOException e) {
                new OptionPane().showOptionPane("Error Saving Image " + e.getMessage(), "OK");
            }

            filePaths.add(outputPath);

        }

            for(int count2=0; count2<size; count2++) {

                Artwork artwork= new StandardArtwork();
                artwork.setImageUrl(filePaths.get(count2));
                try {
                    song.getAudioFile().getTag().setField(artwork);
                } catch (FieldDataInvalidException e) {
                    e.printStackTrace();
                }
                song.getAlbumArtPaths().add(filePaths.get(count2));


            }
            song.clearAudioFile(); // remove audio file from memory  as it takes  up alot of space
    }

    private void embedImages(List<BufferedImage> songsArtwork, AudioInformation song)  {// adds given image files to the artwork tag  of an audio file

        int artSize=songsArtwork.size();
        System.out.println("Artwork "+ songsArtwork.size());

        if(artSize>0){


                AudioFile audioFile=song.getAudioFile();
                System.out.println("AudioFile "+ audioFile);


                for(int count2=0; count2<artSize; count2++) {
                    try {
                        Artwork artwork= new StandardArtwork();
                        artwork.setBinaryData( ((DataBufferByte) songsArtwork.get(count2).getRaster().getDataBuffer()).getData());
                            audioFile.getTag().addField(artwork);


                    } catch (FieldDataInvalidException  e ) {
                        e.printStackTrace();
                    }

                }

                try {
                    AudioFileIO.write(audioFile);

                } catch (CannotWriteException e) {
                    new OptionPane().showOptionPane("Cannot Write to Audio File "+e.getMessage(), "OK");
                }
                song.clearAudioFile();// remove audio file from memory  as it takes  up alot of space

            }







    }


    public boolean isEmbedInToAudioFile() {
        return embedInToAudioFile;
    }

    public void setEmbedInToAudioFile(boolean embedInToAudioFile) {
        this.embedInToAudioFile = embedInToAudioFile;
    }
}
