package pl.przemek.repository;

import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.przemek.model.Discovery;

@Stateless
public class DiscoveryRepositoryImpl implements DiscoveryRepository {
	
	
	@PersistenceContext
	private EntityManager em;
	
	
	public void add(Discovery discovery) {
	    em.persist(discovery);
	}
	
	
	public void remove(Discovery discovery) {
	    em.remove(discovery);
	}
	
	
	public Discovery update(Discovery discovery) {
		Discovery updateDiscovery=em.merge(discovery);
		return updateDiscovery;
	}
	
	
	public Discovery get(Long id) {
	    Discovery discovery = em.find(Discovery.class, id);
	    return discovery;
	}
	 
	public List<Discovery> getAll() {
		 List<Discovery> discovery = getAll(null);
	    return discovery;
	}


	public List<Discovery> getAll(Comparator<Discovery> comparator) {
		TypedQuery<Discovery> getAllQuery = em.createNamedQuery("Discovery.findAll", Discovery.class);
	    List<Discovery> discovery = getAllQuery.getResultList();
	    if(comparator != null && discovery != null) {
            discovery.sort(comparator);
        }
        return discovery;
		
	}
	public List<Discovery> getByName(String name){
		TypedQuery<Discovery> disc = em.createNamedQuery("Discovery.search", Discovery.class);
		disc.setParameter("name", name);
		List<Discovery> discovery=disc.getResultList();
		return discovery;
	
	}

	}
