package pl.przemek.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.przemek.model.Discovery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JpaDiscoveryRepositoryImplTest {

    @Mock
    private EntityManager em;
    @InjectMocks
    private JpaDiscoveryRepositoryImpl jpaDiscoveryRepository;
    @Test
    public void shouldNotSortListOfDiscoveriesWhenThisListIsEmpty() throws Exception {
        List<Discovery> discoveryList=spy(new ArrayList<>());
        TypedQuery<Discovery> queryList=mock(TypedQuery.class);
        when(em.createNamedQuery(anyString(), eq(Discovery.class))).thenReturn(queryList);
        when(queryList.getResultList()).thenReturn(discoveryList);

        jpaDiscoveryRepository.getAll(new JpaDiscoveryRepositoryImplTest.SimpleComparator());

        verify(discoveryList,never()).sort(isA(Comparator.class));

    }

    @Test
    public void shouldNotSortListOfDiscoveriesWhenTheComparatorIsNull() throws Exception {
        List<Discovery> discoveryList=spy(new ArrayList<>());
        discoveryList.add(new Discovery());
        TypedQuery<Discovery> queryList=mock(TypedQuery.class);
        when(em.createNamedQuery(anyString(), eq(Discovery.class))).thenReturn(queryList);
        when(queryList.getResultList()).thenReturn(discoveryList);
        jpaDiscoveryRepository.getAll(null);
        verify(discoveryList,never()).sort(isA(Comparator.class));

    }

    @Test
    public void shouldNotSortListOfDiscoveriesWhenThisListIsEmptyAndTheComparatorIsNull() throws Exception {
        List<Discovery> discoveryList=spy(new ArrayList<>());
        TypedQuery<Discovery> queryList=mock(TypedQuery.class);
        when(em.createNamedQuery(anyString(), eq(Discovery.class))).thenReturn(queryList);
        when(queryList.getResultList()).thenReturn(discoveryList);
        jpaDiscoveryRepository.getAll(null);
        verify(discoveryList,never()).sort(isA(Comparator.class));

    }

    @Test
    public void shouldSortListOfDiscoveriesWhenThisListIsNotEmptyAndComparatorIsDefined() throws Exception {
        List<Discovery> discoveryList=spy(new ArrayList<>());
        discoveryList.add(new Discovery());
        TypedQuery<Discovery> queryList=mock(TypedQuery.class);
        when(em.createNamedQuery(anyString(), eq(Discovery.class))).thenReturn(queryList);
        when(queryList.getResultList()).thenReturn(discoveryList);
        jpaDiscoveryRepository.getAll(new JpaDiscoveryRepositoryImplTest.SimpleComparator());
        verify(discoveryList).sort(isA(Comparator.class));

    }
    @Test
    public void shouldReturnTrueWhenListOfDiscoveriesIsEmpty() throws Exception {
        List<Discovery> discoveryList=new ArrayList<>();
        String url="url";
        Query query=mock(Query.class);

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(discoveryList);

        assertTrue(jpaDiscoveryRepository.checkPresenceDiscveryByUrl(url));

    }@Test
    public void shouldReturnFalseWhenListOfDiscoveriesIsNotEmpty() throws Exception {
        List<Discovery> discoveryList=new ArrayList<>();
        discoveryList.add(new Discovery());
        String url="url";
        Query query=mock(Query.class);

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(discoveryList);

        assertFalse(jpaDiscoveryRepository.checkPresenceDiscveryByUrl(url));

    }

    private static class SimpleComparator implements Comparator<Discovery>{


        @Override
        public int compare(Discovery o1, Discovery o2) {
            return 0;
        }
    }

}