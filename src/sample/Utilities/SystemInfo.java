package sample.Utilities;

public class SystemInfo {
    static  private   String systemOs;
     static private  String systemOsVersion;
    static private String fileSeperator;
    static String userHomePath;


    public SystemInfo() {
        fileSeperator=System.getProperty("file.separator");
        systemOs =System.getProperty("os.name");
        systemOsVersion =System.getProperty("os.version");
       userHomePath =System.getProperty("user.home");


    }

    public static String getSystemOs() {
        return systemOs;
    }

    public  static String getSystemOsVersion() {
        return systemOsVersion;
    }

    public static String getFileSeperator() {
        return fileSeperator;
    }

    public  static String getUserHomePath() {
        return userHomePath;
    }
}
