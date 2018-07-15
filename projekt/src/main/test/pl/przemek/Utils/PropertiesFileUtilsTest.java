package pl.przemek.Utils;

import org.junit.Test;

import javax.validation.constraints.AssertTrue;

import static junit.framework.TestCase.assertTrue;


public class PropertiesFileUtilsTest {

    private String directoryOne="ValidationMessages.properties";
    private String directoryTwo="InformationEmail.properties";

    @Test
    public void getValue() throws Exception {
        assertTrue("testtest".equals(PropertiesFileUtils.getValue(directoryOne,"pl.przemek.polish.signs")));
        assertTrue("smtp.gmail.com".equals(PropertiesFileUtils.getValue(directoryOne,"mail.smtp.host")));
        assertTrue("javax.net.ssl.SSLSocketFactory".equals(PropertiesFileUtils.getValue(directoryTwo,"mail.smtp.SocketFactory.class")));
    }

}