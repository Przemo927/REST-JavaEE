package pl.przemek.repository;

import java.util.Comparator;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.Null;

import pl.przemek.model.Discovery;

@Stateless
public class JpaDiscoveryRepositoryImpl implements JpaDiscoveryRepository {
	
	
	@PersistenceContext
	private EntityManager em;

	@RolesAllowed({"admin","user"})
    @Override
	public void add(Discovery discovery) {
	    em.persist(discovery);
	}

	@RolesAllowed("admin")
    @Override
	public void remove(Discovery discovery) {
	    em.remove(em.merge(discovery));
	}

	@RolesAllowed({"admin","user"})
    @Override
	public Discovery update(Discovery discovery) {
		Discovery updateDiscovery=em.merge(discovery);
		return updateDiscovery;
	}

	@RolesAllowed({"admin","user"})
	@Override
	public Discovery get(long id) {
		Discovery discovery = em.find(Discovery.class, id);
		return discovery;
	}

	@RolesAllowed({"admin","user"})
    @Override
	public List<Discovery> getAll() {
		 List<Discovery> discovery = getAll(null);
	    return discovery;
	}

	@RolesAllowed({"admin","user"})
    @Override
	public List<Discovery> getAll(Comparator<Discovery> comparator) {
		TypedQuery<Discovery> getAllQuery = em.createNamedQuery("Discovery.findAll", Discovery.class);
	    List<Discovery> discovery = getAllQuery.getResultList();
	    if(comparator != null && discovery != null) {
            discovery.sort(comparator);
        }
        return discovery;
		
	}

	@RolesAllowed({"admin","user"})
    @Override
	public Discovery getByName(String name){
		Discovery discovery=null;
		TypedQuery<Discovery> disc = em.createNamedQuery("Discovery.search", Discovery.class);
		disc.setParameter("name", name);
		List<Discovery> discoveryResultList=disc.getResultList();
		try{
			discovery=discoveryResultList.get(0);
		} catch(NullPointerException e){

		}
		return discovery;
	
	}
	@PermitAll
	public boolean checkPresenceDiscveryByUrl(String url){
		Query query=em.createNativeQuery("SELECT 1 FROM Discovery WHERE url=:url");
		query.setParameter("url",url);
		List list=query.getResultList();
		if(list.size()!=0){
			return false;
		}
		return true;
	}

}
