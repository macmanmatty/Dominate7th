package sample.Utilities;import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import sample.AcoustID.AcoustID;
import sample.AcoustID.ChromaPrint;
import sample.AudioProcessors.AudioCodec;
import sample.Library.AudioInformation;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioFileUtilities  {
    private volatile ArrayList<File> files = new ArrayList<File>();
    private ArrayList<String> fileNames = new ArrayList<String>();
    private Character exclude = new Character('?'); // exclude files or folders  that start with this char
    private  ArrayList<File> directories = new ArrayList<File>();
    private List<String> codecExtensions = AudioCodec.getCodecExtensions(); // list of audio file extensions that FFmpeg Supports
    private  List<String> imageExtensions = new ArrayList<>(); // list of supported images for album artwork
    private String fileSeperator;
    public AudioFileUtilities() {
        imageExtensions.add("jpg");
        imageExtensions.add("png");
        fileSeperator=System.getProperty("file.separator");
    }
    public ArrayList<File> findFiles(String path) {
        files.clear();
       return  findFilesInternal(path);


    }



    public boolean isOnCD(File file){
       Path path= file.toPath();

        try {
           FileStore fileStore= path.getFileSystem().provider().getFileStore(path);
           String type=fileStore.type();
           if(type.equalsIgnoreCase("cddafs") || type.equalsIgnoreCase("cdfs")){
               return true;
           }

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }


        return false;
    }
    private ArrayList<File> findFilesInternal(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((FileFilter) null);
        ArrayList<String> names = new ArrayList<String>();
        int size = listOfFiles.length;
        for (int count = 0; count < size; count++) {
            if (!(listOfFiles[count].getName().charAt(0) == exclude)) {
                if (listOfFiles[count].isFile()) {
                    files.add(listOfFiles[count]);
                    fileNames.add(listOfFiles[count].getName());
                } else if (listOfFiles[count].isDirectory()) {
                    findFiles(listOfFiles[count].getPath());
                }
            }
        }
        return files;
    }

    public ArrayList<File> findFiles(String path, List<String> extensions) {
        files.clear();
       return  findFilesInternal(path, extensions);


    }
    public ArrayList<File> findFilesInternal(String path, List<String> extensions) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((FileFilter) null);
        ArrayList<String> names = new ArrayList<String>();
        int size = listOfFiles.length;
        for (int count = 0; count < size; count++) {
            File file=listOfFiles[count];
            if (!(listOfFiles[count].getName().charAt(0) == exclude)) {
                if (listOfFiles[count].isFile()) {
                    if(matchesExtension(file, extensions)) {
                        files.add(file);
                        fileNames.add(file.getName());
                    }
                    
                } else if (listOfFiles[count].isDirectory()) {
                    findFiles(listOfFiles[count].getPath());
                }
            }
        }
        return files;
    }
    public ArrayList<File> findFiles(List<File> filesToSearch) {
        files.clear();
        int size=filesToSearch.size();
        for(int count=0; count<size; count++){
            File file=filesToSearch.get(count);
            if(file.isFile()){
                this.files.add(file);
                fileNames.add(file.getName());
            }
            else if(file.isDirectory()){
               List<File> files= findFilesInternal(file.getAbsolutePath());
               this.files.addAll(files);
               
                
                
                
            }
            
        }
      
        return this.files;
    }
    public ArrayList<File> findFiles(List<File> filesToSearch, List<String> extensions) {
        files.clear();
        int size=filesToSearch.size();
        for(int count=0; count<size; count++){
            File file=filesToSearch.get(count);
            if(file.isFile() ){
                if(matchesExtension(file, extensions)) {
                    this.files.add(file);
                }
                
            }
            else if(file.isDirectory()){
                List<File> files= findFilesInternal(file.getAbsolutePath(), extensions);
                this.files.addAll(files);
            }
        }
        return this.files;
    }
    public ArrayList<File> findDirectories(String path) { // gets all  directories in a given directiory including subDirectories
            directories.clear();
            return findFilesInternal(path);


    }
    public ArrayList<File> findDirectoriesInternal(String path){ // gets all  directories in a given directiory including subDirectories
        File folder= new File(path);
        File [] listOfFiles=folder.listFiles();
        int size=listOfFiles.length;
        for (int count = 0; count < size; count++) {
            if (listOfFiles[count].isDirectory()) { // if it is a  directory search it by recursivly calling this method.
                directories.add(listOfFiles[count]);
                findDirectories(listOfFiles[count].getPath());
            }
        }
        return directories;
    }
    public ArrayList<String> getSingleFileNames( String path){
        findFiles(path);
        Collections.sort(fileNames);
         int size=fileNames.size();
        String name1=fileNames.get(0);
        ArrayList<String> singleNames= new ArrayList<String>(100);
        singleNames.add(name1);
        for(int count=1; count<size; count++){
            String name2=fileNames.get(count);
            if(!(name1.equals(name2))){
                singleNames.add(name2);
            }
        }
       return singleNames;
    }
    public  String getExtensionOfFile(File file) // returns the extension of given file like .png or .tmx ECT.
    {
        String fileExtension="";
        // Get file Name first
        String fileName=file.getName();
        // If fileName do not contain "." or starts with "." then it is not a valid file
        if(fileName.contains(".") && fileName.lastIndexOf(".")!= 0)
        {
            fileExtension=fileName.substring(fileName.lastIndexOf(".")+1);
        }
        return fileExtension;
    }
    public boolean isImageFile(File file) { // checks to see the file extension matches on the  given audio file extensions
        String extension=getExtensionOfFile(file);
        int size= imageExtensions.size();
        for( int count=0; count<size; count++){
            if(extension.equalsIgnoreCase(imageExtensions.get(count))){
                return true;
            }
        }
        return false;
    }
    public boolean isAudioFile(File file) { // checks to see the file extension matches on the  given audio file extensions
        String extension=getExtensionOfFile(file);
        int size= codecExtensions.size();
        for( int count=0; count<size; count++){
            if(extension.equalsIgnoreCase(codecExtensions.get(count))){
                return true;
            }
        }
        return false;
    }
    public ArrayList<String> getFileNames(String path) {
        findFiles(path);
        return fileNames;
    }
    public boolean isAudioCopy(File file1, File file2){ // gets a chormaprint audio foot print  for  2 audio files and determines if they are the same
        // return true if they are or false if they are not or if either of the two given files is not an audio file or the two files don't have the format
        // track length , bitrate or duration
        if(!(isAudioFile(file1))|| !(isAudioFile(file2))){ // check if the files are  audio files
            return false;
        }
        try {// read audio files
            AudioFile audioFile1=AudioFileIO.read(file1);
            AudioFile audioFile2=AudioFileIO.read(file2);
            AudioHeader header1=audioFile1.getAudioHeader();
            AudioHeader header2=audioFile2.getAudioHeader();
            if(header1.getTrackLength()!=header2.getTrackLength()){ // not the same track length not duplicate
                return false;
            }
            if(!(header1.getEncodingType().equals(header1.getEncodingType()))){ // not the same encoding type not same file
                return false;
            }
            if(!(header1.getFormat().equals(header2.getFormat()))){// not same format not same file
                return false;
            }
            if(header1.getBitRateAsNumber()!=header2.getBitRateAsNumber()){ // note sme bit rate not same file
                return false;
            }
        } catch (CannotReadException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (TagException e) {
            return false;
        } catch (ReadOnlyFileException e) {
            return false;
        } catch (InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        AcoustID acoustID= new AcoustID();
        try {
            ChromaPrint print1=acoustID.getChromaPrint(file1.getAbsolutePath());
            ChromaPrint print2=acoustID.getChromaPrint(file2.getAbsolutePath());
            if((print1.getChromaprint().equals(print2.getChromaprint()))){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public char getExclude() {
        return exclude;
    }
    public void setExclude(char exclude) {
        this.exclude = exclude;
    }
    public boolean isInPath(File file, String libraryPath){
        String path=file.getPath();
        if (StringUtils.contains(path, libraryPath)){
            return true;
        }
        return false;
    }
    public  void saveErrorMessages( ArrayList<String> errorMessages, String path){
        File file= new File(path+"errors.txt");
        try {
            file.createNewFile();
            FileWriter fileWriter= new FileWriter(file);
            int size=errorMessages.size();
            for(int count=0; count<size; count++) {
                fileWriter.write("Error Message #" + count);
                fileWriter.write(System.lineSeparator());
                fileWriter.write(errorMessages.get(count));
                fileWriter.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public File renameDuplicateFile(File fileToMove, File directory) {
        String newName = "";
        boolean duplicateName = true;
        if (duplicateName == true) { // if  the file  with  same  name is already exists keeping counter to till files does not exist.
            int counter = 2;
            while (duplicateName == true) {
                String fileName=fileToMove.getName();
                String name= FilenameUtils.getBaseName(fileName);
                String extension=FilenameUtils.getExtension(fileName);
                newName=name+counter+"."+extension;
                duplicateName = checkFileName(directory.listFiles(), newName);
                counter++;
            }
        }
        fileToMove= reNameFile(fileToMove, newName);
        return fileToMove;
    }
    private boolean checkFileName(File[] files, String name) {
        int size = files.length;
        for (int count = 0; count < size; count++) {
            String name2 = files[count].getName();
            if (name.equals(name2)) {
                return true;
            }
        }
        return false;
    }
   private File  reNameFile(File fileToMove, String newName){
        File renamedFile=  new File(fileToMove.getParentFile().getAbsolutePath()+fileSeperator+newName);
        fileToMove.renameTo(renamedFile);
        return renamedFile;
    }

    public long getTrackLengthUsingFFmpeg(File file) throws FrameGrabber.Exception {
        FFmpegFrameGrabber frameGrabber= new FFmpegFrameGrabber(file);
            frameGrabber.start();
        long trackLength=(frameGrabber.getLengthInTime()/1000000);
            frameGrabber.stop();
        return  trackLength;
    }



    public File copyThenMoveFile(File file, File moveDirectory) throws IOException { // copies  file to new directory and makes the directory if it doesn't allready exist aka copy then move
        if(moveDirectory.exists()==false){
                Files.createDirectories(Paths.get(moveDirectory.getAbsolutePath()));
        }
        return copyThenMoveFile(file, moveDirectory.getAbsolutePath());
    }
    
    
    public boolean matchesExtension(File file, List<String> extensions){
        
        String fileExtension=getExtensionOfFile(file);
        int size=extensions.size();
        for(int count=0; count<size; count++){
            if(fileExtension.equalsIgnoreCase(extensions.get(count))){
                return  true;
            }
            
        }
        
        
        
        return false;
        
    }

        public  void copyFileToFile(File in, File out) throws IOException{

            FileChannel inChannel = new
                    FileInputStream(in).getChannel();
            FileChannel outChannel = new
                    FileOutputStream(out).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(),
                        outChannel);
            }
            catch (IOException e) {
                throw e;
            }
            finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        }



    public File copyThenMoveFile(File file, String directoryPath) throws IOException { // copies  file to new directory aka copy then move
        String newFilePath=directoryPath+fileSeperator+file.getName();
        System.out.println(newFilePath);
        File newFile= new File(newFilePath);
        FileUtils.copyFile(file, newFile);


        return  newFile;
    }

    public List<String> getCodecExtensions() {
        return codecExtensions;
    }
}
