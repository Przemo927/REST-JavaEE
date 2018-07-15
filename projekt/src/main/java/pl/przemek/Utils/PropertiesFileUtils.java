package pl.przemek.Utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileUtils {

    private String directory;
    private static Properties property;

    public PropertiesFileUtils(String directory){
        this.directory=directory;
    }
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public static String getValue(String directory,String key) {
        if(property==null)
            property=new Properties();
        String value=null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream resourceStream = loader.getResourceAsStream(directory)
        ){
            property.load(resourceStream);
            value=(String)property.get(key);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

}
