package pl.przemek.Utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesFileUtils {

    private String directory;
    private static Properties properties;

    private static Logger logger=Logger.getLogger(PropertiesFileUtils.class.getName());

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
        createObjectProperties();
        String value=null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream resourceStream = loader.getResourceAsStream(directory)
        ){
            properties.load(resourceStream);
            value=(String)properties.get(key);

        } catch (IOException e) {
            logger.log(Level.SEVERE,"PropertiesFileUtils getValue()",e);
        }
        return value;
    }
    public static Properties getProperties(String directory){
        createObjectProperties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream resourceStream = loader.getResourceAsStream(directory)
        ){
            properties.load(resourceStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"PropertiesFileUtils getProperties()",e);
        }
        return properties;
    }

    private static void createObjectProperties(){
        if(properties==null)
            properties=new Properties();
    }

}
