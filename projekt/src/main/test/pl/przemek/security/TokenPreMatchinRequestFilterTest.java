package pl.przemek.security;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;

import java.io.IOException;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class TokenPreMatchinRequestFilterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    @InjectMocks
    private TokenPreMatchingRequestFilter requestFilter;
    @Mock
    private TokenStore tokenStore;

    private ContainerRequestContext requestContext;

    @Before
    public void setUp(){
        requestContext=mock(ContainerRequestContext.class);
    }

    public Object[] role(){
        return $(new Object[]{"admin",true},new Object[]{"user",true});
    }

    @Test
    @Parameters(method = "role")
    public void shouldAddTokenAsRequestHeaderWhenUserRoleIsUserOrAdmin(String roleName,boolean bolean) throws IOException {

        SecurityContext securityContext=mock(SecurityContext.class);
        MultivaluedMap<String,String> multivaluedMap=mock(MultivaluedMap.class);
        String token="token";

        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.isUserInRole(roleName)).thenReturn(bolean);
        when(requestContext.getHeaders()).thenReturn(multivaluedMap);
        when(tokenStore.getToken()).thenReturn(token);

        requestFilter.filter(requestContext);
        verify(tokenStore,times(1)).getToken();
        verify(multivaluedMap,times(1)).add(anyString(),anyString());
    }

    public Object token(){
        return $("token","","000000","TOKEN");
    }

    @Test
    @Parameters(method = "token")
    public void shouldSaveAsRequestHeaderTokenFromTokenStore(String token) throws IOException {
        SecurityContext securityContext=mock(SecurityContext.class);
        MultivaluedMap<String,String> multivaluedMap=mock(MultivaluedMap.class);

        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.isUserInRole(anyString())).thenReturn(true);
        when(requestContext.getHeaders()).thenReturn(multivaluedMap);
        when(tokenStore.getToken()).thenReturn(token);

        requestFilter.filter(requestContext);
        verify(multivaluedMap,times(1)).add("Authorization",token);
        verify(multivaluedMap,never()).add(anyString(), AdditionalMatchers.not(eq(token)));
    }

    @Test
    public void shouldDoNothingWhenLoggedUserIsNotAdminOrUser() throws IOException {
        SecurityContext securityContext=mock(SecurityContext.class);

        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.isUserInRole("user")).thenReturn(false);
        when(securityContext.isUserInRole("admin")).thenReturn(false);

        requestFilter.filter(requestContext);
        verify(tokenStore,never()).getToken();
        verify(requestContext,never()).getHeaders();

    }

    @Test
    @Parameters(method = "token")
    public void shouldAddHeaderWithTokenAsValue(String token) throws IOException {
        MultivaluedMap<String,String> multivaluedMap=new MultivaluedHashMap<>();
        multivaluedMap.add("header","header");
        SecurityContext securityContext=mock(SecurityContext.class);

        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.isUserInRole("user")).thenReturn(true);
        when(requestContext.getHeaders()).thenReturn(multivaluedMap);
        when(tokenStore.getToken()).thenReturn(token);

        requestFilter.filter(requestContext);

        assertEquals(2,multivaluedMap.size());
        assertEquals(token,multivaluedMap.getFirst("Authorization"));

    }
}
