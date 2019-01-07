package pl.przemek.config;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CDIconfig {

    private static final String NAME_LOG_FILE="ProjektLog.log";
    private static FileHandler fh;
    private static Logger logger;
    private final static String LOG_PATH=System.getProperty("user.dir")+ File.separator+"log"+File.separator+NAME_LOG_FILE;

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        try {
            if(logger==null){
                logger = Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
            }
            if(fh==null){
                fh = new FileHandler(LOG_PATH);
                fh.setLevel(Level.ALL);
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
            }
        } catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE,"Logger exception",e);
        }
        return logger;
    }
}
