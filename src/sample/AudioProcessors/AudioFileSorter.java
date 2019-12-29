package sample.AudioProcessors;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import sample.AudioProcessors.CueSheets.CueSheetExeception;
import sample.AudioProcessors.CueSheets.CueSheetReader;
import sample.Library.AudioInformation;
import sample.Library.CueSheets.CueSheet;
import sample.Utilities.AudioFileUtilities;
import sample.Windows.FileProgressBar;
import sample.Windows.UpdateLabel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class AudioFileSorter implements  FileAction, AudioInformationAction {
    private HashMap<AudioFile, File> files = new HashMap<>();
    private List<String> errorMessages = new ArrayList<>();
    private Character exclude = new Character('?'); // exclude files or folders  that start with this char
    private boolean sortFilesByType;
    private boolean sortByBitRate;
    private String sortFolderPath;
    private boolean renameAndMoveDuplicateFiles;
    private AudioFileUtilities utilities= new AudioFileUtilities();
    private int numberOfThreads=0;
    private int currentErrors;
    private boolean completed;
    private String fileName;
    private boolean copyThenMove;
    private int albumCounter;
    private int artistCounter;
    private List<AudioInformation> songs= Collections.synchronizedList(new ArrayList<>());
    ExtractAudioInformation extractAudioInformation= new ExtractAudioInformation();
    private ArrayList<File> filePaths = new ArrayList<File>();
    private CountDownLatch countDownLatch;
    private boolean getMissingTags;
    private GetAndSaveBestAcoustIDResult getAudioInformationFromWeb= new GetAndSaveBestAcoustIDResult(false, false, false);
    private CueSheetReader cueSheetReader;
    private String fileMovedPath;
    private String fileSeperator;
    private boolean onCD;



    public AudioFileSorter( String fileSeperator,String sortFolderPath, boolean sortFilesByType, boolean sortByBitRate, boolean renameAndMoveDuplicateFiles, boolean getMissingTags, boolean copyThenMove, boolean onCD) {
        this.fileSeperator=fileSeperator;
        this.getMissingTags=getMissingTags;
        this.sortFolderPath=sortFolderPath;
        this.renameAndMoveDuplicateFiles=renameAndMoveDuplicateFiles;
        filePaths.addAll(utilities.findDirectories(sortFolderPath));
        this.sortFilesByType = sortFilesByType;
        this.sortByBitRate = sortByBitRate;
        this.copyThenMove=copyThenMove;
        this.onCD=onCD;
        utilities = new AudioFileUtilities();
        cueSheetReader= new CueSheetReader();

    }
    public  void action(AudioInformation information){

        action(information.getPhysicalFile());
        information.setAudioFilePath(fileMovedPath);


    }




    public void action(File file){
        if(utilities.isAudioFile(file)) {
            try {
                AudioFile audioFile = AudioFileIO.read(file);
                action(audioFile);
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            }
        }
        else if(utilities.getExtensionOfFile(file).equalsIgnoreCase("cue")){
            sortCueFile(file);
        }
    }
    public void action(AudioFile audioFile) { // movies a  list of audio files to the destination directory
        File fileToMove = audioFile.getFile();
        String artistName="";
        String albumName="";
        Tag audioTag = audioFile.getTag();
        if(audioTag==null){
            artistName = "Unknown";
            albumName = "Unknown";
        }
        else {
            artistName = audioTag.getFirst(FieldKey.ARTIST);
            if (artistName == null || artistName.isEmpty()) {
                if(getMissingTags==true){
                    getAudioInformationFromWeb.action(audioFile);
                    artistName=audioTag.getFirst(FieldKey.ARTIST);
                    if(artistName==null || artistName.isEmpty()){
                        artistName = "Unknown";
                    }
                }
                else{
                    artistName="Unknown";
                }
            }
            albumName = audioTag.getFirst(FieldKey.ALBUM);
            if (albumName == null || albumName.isEmpty()) {
                if(getMissingTags==true){
                    getAudioInformationFromWeb.action(audioFile);
                    albumName=audioTag.getFirst(FieldKey.ALBUM);
                    if(albumName==null || albumName.isEmpty()){
                        albumName = "Unknown";
                    }
                }
                else{
                    albumName="Unknown";
                }
            }
        }
        String fileToMoveDestitnationPath = "";
        if (sortFilesByType == true) {  // if sorting files by file type FLAC MP3 ect get file type
            String kind = getKind(audioFile.getAudioHeader(), fileToMove, sortByBitRate);
            fileToMoveDestitnationPath = sortFolderPath + ""+fileSeperator+"" + artistName + ""+fileSeperator+"" + kind + ""+fileSeperator+"" + albumName; // create folder  path  to move file to
        } else {
            fileToMoveDestitnationPath = sortFolderPath + ""+fileSeperator+"" + artistName + ""+fileSeperator+"" + albumName;
        }
        boolean created = new File(fileToMoveDestitnationPath).exists();
        if (created == false) {// if there is no folder create  it
            File directory = new File(fileToMoveDestitnationPath);
            filePaths.add(directory);
            try {
                if(copyThenMove==true){
                    utilities.copyThenMoveFile(fileToMove,directory);
                }
                else {
                    FileUtils.moveToDirectory(fileToMove, directory, true); // move file to new folder
                }
            }
            catch (IOException e) {
                errorMessages.add(e.getMessage());
                e.printStackTrace();
                currentErrors++;
            }
            catch(UnsupportedOperationException e){
                errorMessages.add(e.getMessage());
                e.printStackTrace();
                currentErrors++;
            }
            System.out.println(" folder created!");
        }
        else { // just move file to folder as folder allready exists.
            File directory = new File(fileToMoveDestitnationPath);
                try {
                    if (copyThenMove == true) {
                        utilities.copyThenMoveFile(fileToMove, directory);
                    }
                    else {
                        FileUtils.moveToDirectory(fileToMove, directory, false);
                    }
                } catch (FileExistsException e) {
                    boolean isDuplicate = utilities.isAudioCopy(fileToMove, new File(fileToMoveDestitnationPath));
                    if (renameAndMoveDuplicateFiles == true || isDuplicate == false) { // if rename files is true rename file move it
                        fileToMove = utilities.renameDuplicateFile(fileToMove, directory);
                        try {
                            FileUtils.moveToDirectory(fileToMove, directory, false);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    errorMessages.add(e.getMessage());
                    currentErrors++;
                    e.printStackTrace();
                    return;
                } catch (UnsupportedOperationException e) {
                    errorMessages.add(e.getMessage());
                    currentErrors++;
                    e.printStackTrace();
                    return;
                }
        }
        AudioInformation song=extractAudioInformation.extractInformationUsingJAudioTagger(audioFile);
        song.setAudioFilePath(fileToMoveDestitnationPath+""+fileSeperator+""+fileToMove.getName());
        if(onCD==true){
            song.setWriteFieldsToFile(false);
        }
       songs.add(song);
        if(audioTag!=null) {
            sortAndMoveAlbumArtwork(audioTag.getArtworkList(), fileToMoveDestitnationPath ,song);
        }
        System.out.println("Acted on  Track");
        fileMovedPath=fileToMoveDestitnationPath+""+fileSeperator+""+fileToMove.getName();
        completed=true;
    }
    public void sortCueFile(File cueFile){
        currentErrors=0;
        completed=false;
        this.fileName=cueFile.getName();
        if(utilities.getExtensionOfFile(cueFile).equalsIgnoreCase("cue")) {
            String artistName = "";
            String albumName = "";
            CueSheet cueSheet = null;
            try {
                cueSheet = cueSheetReader.readCueSheet(cueFile);
            } catch (CueSheetExeception cueSheetExeception) {
                cueSheetExeception.printStackTrace();
            }
            System.out.println(cueSheet);
            if (cueSheet != null) {// if cue sheet is null don't bother moving anything
                artistName = cueSheet.getArtist();
                if (artistName == null || artistName.isEmpty()) {
                    artistName = cueSheet.getTracks().get(0).getArtist();
                    if (artistName == null || artistName.isEmpty()) {
                        artistName = "Unknown" + albumCounter;
                        albumCounter++;
                    }
                }
                albumName = cueSheet.getAlbumName();
                if (albumName == null || albumName.isEmpty()) {
                    albumName = "Unknown" + artistCounter;
                    artistCounter++;
                }
                String fileToMoveDestitnationPath = "";
                System.out.println("Path "+cueSheet.getAudioFilePath());
                AudioFile audioFile = null;
                    audioFile = cueSheet.getAudioFile();

                if (audioFile != null) {// if audio file is null again don't move anything
                    if (sortFilesByType == true) {  // if sorting files by file type FLAC MP3 ect get file type
                        String kind = getKind(audioFile.getAudioHeader(), cueFile, sortByBitRate);
                        fileToMoveDestitnationPath = sortFolderPath + ""+fileSeperator+"" + artistName + ""+fileSeperator+"" + kind + ""+fileSeperator+"" + albumName; // create folder  path  to move file to
                    } else {
                        fileToMoveDestitnationPath = sortFolderPath + ""+fileSeperator+"" + artistName + ""+fileSeperator+"" + albumName;
                    }
                    System.out.println("File Exists" +audioFile.getFile().exists());
                    boolean created = pathExists(fileToMoveDestitnationPath);
                    if (created == false) {//if there is no folder create  it
                        File directory = new File(fileToMoveDestitnationPath);
                        filePaths.add(directory);
                        try {
                            if (copyThenMove == true) {
                                utilities.copyThenMoveFile(cueFile, directory);
                                utilities.copyThenMoveFile(audioFile.getFile(), directory);
                                cueSheet.setCueFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ cueFile.getName());
                                cueSheet.setAudioFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ audioFile.getFile().getName());
                            } else {
                                FileUtils.moveToDirectory(cueFile, directory, true); // move file to new folder
                                FileUtils.moveToDirectory(audioFile.getFile(), directory, false);// move file to new folder
                                cueSheet.setCueFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ cueFile.getName());
                                cueSheet.setAudioFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ audioFile.getFile().getName());
                            }
                        } catch (IOException e) {
                            errorMessages.add(e.getMessage());
                            e.printStackTrace();
                            currentErrors++;
                        } catch (UnsupportedOperationException e) {
                            errorMessages.add(e.getMessage());
                            e.printStackTrace();
                            currentErrors++;
                        }
                        System.out.println(" folder created!");
                    } else { // just move file to folder as folder allready exists.
                        String name = cueFile.getName();
                        File directory = new File(fileToMoveDestitnationPath);
                        try {
                            if (copyThenMove == true) {
                                utilities.copyThenMoveFile(cueFile, directory);
                                utilities.copyThenMoveFile(audioFile.getFile(), directory);
                                cueSheet.setCueFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ cueFile.getName());
                                cueSheet.setAudioFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ audioFile.getFile().getName());
                            } else {
                                FileUtils.moveToDirectory(cueFile, directory, false);
                                FileUtils.moveToDirectory(audioFile.getFile(), directory, false);
                                cueSheet.setCueFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ cueFile.getName());
                                cueSheet.setAudioFilePath(directory.getAbsolutePath()+""+fileSeperator+""+ audioFile.getFile().getName());
                            }
                        } catch (FileExistsException e) {
                            boolean isDuplicate = utilities.isAudioCopy(cueFile, new File(fileToMoveDestitnationPath));
                            if (renameAndMoveDuplicateFiles == true || isDuplicate == false) { // if rename files is true rename file move it
                                cueFile = utilities.renameDuplicateFile(cueFile, directory);
                                try {
                                    FileUtils.moveToDirectory(cueFile, directory, false);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            errorMessages.add(e.getMessage());
                            currentErrors++;
                            e.printStackTrace();
                            return;
                        } catch (UnsupportedOperationException e) {
                            errorMessages.add(e.getMessage());
                            currentErrors++;
                            e.printStackTrace();
                            return;
                        }
                    }
                }
                songs.addAll(cueSheet.getTracks());
            }
            completed = true;
        }
    }
    public void sortAndMoveAlbumArtwork(List<Artwork> artworkList, String directoryToMoveTo ,AudioInformation song) { // movies a  list of audio files to the destination directory
        int size=artworkList.size();
        song.getAlbumArtPaths().clear();
        //get artwork urls
        for(int count=0; count<size; count++) {
            Artwork artwork=artworkList.get(count);
            String filePath = artwork.getImageUrl();
            if(filePath!=null && (!(filePath.isEmpty()))) {
                File fileToMove = new File(filePath);
                String name = fileToMove.getName();
                File directory = new File(directoryToMoveTo);
                try {
                    if (copyThenMove == true) {
                        utilities.copyThenMoveFile(fileToMove, directory);
                        String newPath = directoryToMoveTo + ""+fileSeperator+"" + name;
                        artwork.setImageUrl(newPath);
                        song.getAlbumArtPaths().add(newPath);
                    }
                    else {
                        FileUtils.moveToDirectory(fileToMove, directory, false);
                        String newPath = directoryToMoveTo + ""+fileSeperator+"" + name;
                        artwork.setImageUrl(newPath);
                        song.getAlbumArtPaths().add(newPath);
                    }
                }
                catch (FileExistsException e){
                    if(renameAndMoveDuplicateFiles==true) { // if rename files is true rename file move it
                        fileToMove = utilities.renameDuplicateFile(fileToMove, directory);
                        try {
                            FileUtils.moveToDirectory(fileToMove, directory, false);
                            String newPath = directoryToMoveTo + ""+fileSeperator+"" + name;
                            artwork.setImageUrl(newPath);
                            song.getAlbumArtPaths().add(newPath);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                catch (IOException e) {
                    errorMessages.add(e.getMessage());
            currentErrors++;
                    e.printStackTrace();
                } catch (UnsupportedOperationException e) {
                    errorMessages.add(e.getMessage());
            currentErrors++;
                    e.printStackTrace();
                }
            }
        }
    }
    private String getKind(AudioHeader header, File file, boolean sortByBitRate) {
        String extension = utilities.getExtensionOfFile(file);
        if (extension.equals("mp3") && sortByBitRate == true) {
            String sBitRate = header.getBitRate();
            String bitAddition = "";
            if (sBitRate.contains("~")) {
                bitAddition = "vbr";
            } else {
                float bitRate = header.getBitRateAsNumber();
                bitAddition = String.valueOf(bitRate);
            }
            extension = extension + " " + bitAddition;
        }
        return extension;
    }
    private boolean pathExists(String path) {
        int size = filePaths.size();
        for (int count = 0; count < size; count++) {
            String name = filePaths.get(count).getAbsolutePath();
            if (path.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public char getExclude() {
        return exclude;
    }
    public void setExclude(char exclude) {
        this.exclude = exclude;
    }
    public File getFile(AudioFile audioFile) {
        return files.get(audioFile);
    }
    @Override
    public List<String> getErrorMessages() {
        return errorMessages;
    }
    @Override
    public String getActionName() {
        return "Sorting Files";
    }
    @Override
    public int getNumberOfThreads() {
        return numberOfThreads;
    }
    @Override
    public void setCountDownLatch(CountDownLatch latch) {
        this.countDownLatch=countDownLatch;
    }
    @Override
    public void stopAction() {
    }
    public void setSortFilesByType(boolean sortFilesByType) {
        this.sortFilesByType = sortFilesByType;
    }
    public void setSortByBitRate(boolean sortByBitRate) {
        this.sortByBitRate = sortByBitRate;
    }
    public void setSortFolderPath(String sortFolderPath) {
        this.sortFolderPath = sortFolderPath;
    }
    public int getCurrentErrors(){
        return currentErrors;
    }
    public boolean completed() {
        return completed;
    }
    public String getFileName() {
        return  fileName;
    }
    @Override
    public void setProgressBar(FileProgressBar progressBar) {
    }
    @Override
    public void setUpdateLabel(UpdateLabel label) {
    }
    public boolean isSortFilesByType() {
        return sortFilesByType;
    }
    public boolean isSortByBitRate() {
        return sortByBitRate;
    }
    public boolean isCopyThenMove() {
        return copyThenMove;
    }
    public void setCopyThenMove(boolean copyThenMove) {
        this.copyThenMove = copyThenMove;
    }
    public List<AudioInformation> getTracks() {
        return songs;
    }

    public boolean isOnCD() {
        return onCD;
    }

    public void setOnCD(boolean onCD) {
        this.onCD = onCD;
    }
}
