package sample.Utilities;

import com.google.gson.Gson;
import com.sun.deploy.net.HttpResponse;
import org.bytedeco.javacv.FrameGrabber;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sample.Windows.OptionPane;
import sun.net.httpserver.DefaultHttpServerProvider;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.sql.Connection;

public class HttpReader {
    // takes a url and returns a Java object instance of specified class  containing the json data
    public <T> T getJSONFromUrl(String urlString, Class<? extends T> clazz) throws  IOException {
        URL url = null;
        try {
            url = new URL(urlString);
            System.out.println(url);
          InputStreamReader reader= new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(reader); // the read  data from the url stream
            Gson gson = new Gson();

            T tObject = gson.fromJson(br, clazz);// parse json data with google  GSON
            br.close();
            reader.close();
            return tObject; // return object of type T passe into method.
        }
        catch (UnknownHostException e){

             new OptionPane().showOptionPane("Can't Find Host Check Your Internet Connection", "Ok");
             e.printStackTrace();
            Thread.currentThread().interrupt(); // can't connect so  quit process

        }
        catch (ConnectException  e){

            new OptionPane().showOptionPane("Can't Find Host Check Your Internet Connection", "Ok");
            e.printStackTrace();
            Thread.currentThread().interrupt(); // can't connect so  quit process
        }
        catch (FileNotFoundException e){
            // the sever  may have no data  for teh given url  or the url is invalid either way no data  so do nothing and return null;

            e.printStackTrace();

        }
        return null;

    }



    public BufferedImage saveImageFromUrl(String imageUrl) throws IOException { // reads a image url to a buffered image
        try {
            URL url = new URL(imageUrl);
            System.out.println(url);

            InputStream stream = url.openStream();
            BufferedImage image = ImageIO.read(stream);
            stream.close();
            return image;
        }
         catch (UnknownHostException e){

            new OptionPane().showOptionPane("Can't Find Host Check Your Internet Connection", "Ok");
            e.printStackTrace();
            Thread.currentThread().interrupt(); // can't connect so  quit process

        }
        catch (ConnectException  e){

            new OptionPane().showOptionPane("Can't Find Host Check Your Internet Connection", "Ok");
            e.printStackTrace();
            Thread.currentThread().interrupt(); // can't connect so  quit process
        }
        catch (FileNotFoundException e){
            // the sever  may have no data  for teh given url  or the url is invalid either way no data  so do nothing and return null;

            e.printStackTrace();

        }
        return null;

    }

    public <T> T getXMLFromUrl(String urlString, Class<? extends T> clazz) throws  IOException {
        URL url = null;
        try {
            url = new URL(urlString);
            System.out.println(url);
             // the read  data from the url stream
            DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            T tObject = (T) builder.parse(url.openStream());// parse json data with google  GSON
            return tObject; // return object of type T passe into method.
        }
        catch (UnknownHostException e){

            new OptionPane().showOptionPane("Can't Find Host Check Your Internet Connection", "Ok");
            e.printStackTrace();
            Thread.currentThread().interrupt(); // can't connect so  quit process

        }
        catch (ConnectException  e){

            new OptionPane().showOptionPane("Can't Find Host Check Your Internet Connection", "Ok");
            e.printStackTrace();
            Thread.currentThread().interrupt(); // can't connect so  quit process
        }
        catch (FileNotFoundException e){
            // the sever  may have no data  for teh given url  or the url is invalid either way no data  so do nothing and return null;

            e.printStackTrace();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;

    }







}
