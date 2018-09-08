package pl.przemek.Message;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class EmailMessageTemplateTest {


    public Object[] baseUrlAndUserName() {
        return $(new String[] {"http://wwww.baseurl.pl","username"},
                new String[]{"http://wwww.localhost:8080","user1234"});
    }
    @Parameters(method = "baseUrlAndUserName")
    @Test
    public void shouldReturnEmailMessageWithUername(String baseUrl,String username) throws Exception {
        String message="Click link below to continue registration<br><br><a href="+
                baseUrl+"/register/"+username+">Continue</a>";
        assertEquals(message,EmailMessageTemplate.getPreparedMessage(baseUrl,username));
    }

}