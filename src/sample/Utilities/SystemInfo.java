package sample.Utilities;

public class SystemInfo {
   final private   String systemOs;
    final private  String systemOsVersion;
    final private String fileSeperator;
    final String userHomePath;


    public SystemInfo() {
        fileSeperator=System.getProperty("file.separator");
        systemOs =System.getProperty("os.name");
        systemOsVersion =System.getProperty("os.version");
       userHomePath =System.getProperty("user.home");


    }

    public String getSystemOs() {
        return systemOs;
    }

    public String getSystemOsVersion() {
        return systemOsVersion;
    }

    public String getFileSeperator() {
        return fileSeperator;
    }

    public String getUserHomePath() {
        return userHomePath;
    }
}
