package pl.przemek.rest;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.tests.utils.impl.PowerMockIgnorePackagesExtractorImpl;
import pl.przemek.Message.MailService;
import pl.przemek.Message.MessageWrapper;
import pl.przemek.model.User;
import pl.przemek.service.UserService;

import static org.junit.Assert.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;

import java.net.URI;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Response.class})
public class RegisterEndPointTest {

    private UserService userService;
    private MailService mailService;
    private User user;
    private HttpServletRequest request;
    private RegisterEndPoint registerEndPoint;
    @Before
    public void setUp() throws Exception {
        userService=mock(UserService.class);
        mailService=mock(MailService.class);
        request=mock(HttpServletRequest.class);
        registerEndPoint=spy(new RegisterEndPoint(userService,mailService,request));
    }

    @Test
    public void shouldSendMessageAsWrappedMessageAndAddUserToSession() throws Exception {
        User user=new User();
        user.setUsername("Name");
        MessageWrapper msg=mock(MessageWrapper.class);
        HttpSession session=mock(HttpSession.class);

        when(registerEndPoint.wrapMessage(anyString(),isA(User.class))).thenReturn(msg);
        when(request.getSession(true)).thenReturn(session);
        registerEndPoint.sendEmailAndSaveUserToRegistration(user);

        verify(mailService).sendMessage(msg);
        verify(session).setAttribute("Name",user);
    }

    @Test
    public void shouldAddUserByUserServiceAndReturnResponse() throws Exception {
        HttpSession session=mock(HttpSession.class);
        PowerMockito.mockStatic(Response.class);
        Response response=mock(Response.class);
        User user=mock(User.class);
        Response.ResponseBuilder responseBuilder=mock(Response.ResponseBuilder.class);

        when(Response.seeOther(isA(URI.class))).thenReturn(responseBuilder);
        when(responseBuilder.build()).thenReturn(response);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(user);

        Response response1=registerEndPoint.addUser(anyString());
        verify(userService).addUser(user);
        assertEquals(response,response1);

    }


}