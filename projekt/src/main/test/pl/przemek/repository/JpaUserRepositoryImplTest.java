package pl.przemek.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.przemek.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JpaUserRepositoryImplTest {
    @Mock
    private EntityManager em;
    @InjectMocks
    private JpaUserRepositoryImpl jpaUserRepository;

    @Test
    public void shouldReturnTrueIfListOfUsersWithSpecificNameIsEmpty() throws Exception {
        TypedQuery query=mock(TypedQuery.class);
        List<User> listOfUsers=new ArrayList<>();

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(listOfUsers);
        assertTrue(jpaUserRepository.checkPresenceOfUserByUsername("name"));
    }
    @Test
    public void shouldReturnFalseIfListOfUsersWithSpecificNameIsNotEmpty() throws Exception {
        TypedQuery query=mock(TypedQuery.class);
        List<User> listOfUsers=new ArrayList<>();
        listOfUsers.add(new User());

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(listOfUsers);
        assertFalse(jpaUserRepository.checkPresenceOfUserByUsername("name"));
    }

    @Test
    public void shouldReturnTrueIfListOfUsersWithSpecificEmailIsEmpty() throws Exception {
        TypedQuery query=mock(TypedQuery.class);
        List<User> listOfUsers=new ArrayList<>();

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(listOfUsers);
        assertTrue(jpaUserRepository.checkPresenceOfEmail("email"));
    }
    @Test
    public void shouldReturnFalseIfListOfUsersWithSpecificEmailIsNotEmpty() throws Exception {
        TypedQuery query=mock(TypedQuery.class);
        List<User> listOfUsers=new ArrayList<>();
        listOfUsers.add(new User());

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(listOfUsers);
        assertFalse(jpaUserRepository.checkPresenceOfEmail("email"));
    }

}