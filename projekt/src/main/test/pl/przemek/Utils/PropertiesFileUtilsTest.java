package pl.przemek.Utils;

import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.constraints.AssertTrue;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class PropertiesFileUtilsTest {

    private String directoryOne="ValidationMessages.properties";
    private String directoryTwo="InformationEmail.properties";

    @Test
    public void shouldReturnValueFromLoadedPropertiesFile() throws Exception {
        assertEquals("przemek", PropertiesFileUtils.getValue(directoryOne, "pl.przemek.polish.signs"));
        assertEquals("smtp.gmail.com", PropertiesFileUtils.getValue(directoryOne, "mail.smtp.host"));
        assertEquals("javax.net.ssl.SSLSocketFactory", PropertiesFileUtils.getValue(directoryTwo, "mail.smtp.SocketFactory.class"));
    }

}