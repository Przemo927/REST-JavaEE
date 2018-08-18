package pl.przemek.repository;

import pl.przemek.model.Discovery;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

@Stateless
public class JpaDiscoveryRepositoryImpl extends JpaRepository<Discovery> implements JpaDiscoveryRepository {

	@RolesAllowed({"admin","user"})
	@Override
	public List<Discovery> getWithLimit(int begin, int quantity) {
		TypedQuery<Discovery> getAllQueryWithLimit=em.createNamedQuery("Discovery.findAllWithLimit",Discovery.class);
		getAllQueryWithLimit.setParameter("begin",begin);
		getAllQueryWithLimit.setParameter("quantity",quantity);
		return getAllQueryWithLimit.getResultList();
	}

	@RolesAllowed({"admin","user"})
	@Override
	public List<Discovery> getAllInOneQuery() {
		TypedQuery<Discovery> getAllInOneQuery= em.createQuery("SELECT p FROM Discovery p LEFT JOIN FETCH p.user",Discovery.class);
		return getAllInOneQuery.getResultList();
	}

	@RolesAllowed({"admin","user"})
    @Override
	public List<Discovery> getAll(Comparator<Discovery> comparator) {
		TypedQuery<Discovery> getAllQuery = em.createNamedQuery("Discovery.findAll", Discovery.class);
	    List<Discovery> discovery = getAllQuery.getResultList();
	    if(comparator != null && !discovery.isEmpty()) {
            discovery.sort(comparator);
        }
        return discovery;

	}

	@RolesAllowed({"admin","user"})
    @Override
	public List<Discovery> getByName(String name) throws NullPointerException{
		Discovery discovery=null;
		TypedQuery<Discovery> disc = em.createNamedQuery("Discovery.search", Discovery.class);
		disc.setParameter("name", name);
		return disc.getResultList();
	
	}
	@PermitAll
	public boolean checkPresenceDiscveryByUrl(String url){
		Query query=em.createNativeQuery("SELECT 1 FROM Discovery WHERE url=:url");
		query.setParameter("url",url);
		List list=query.getResultList();
		return list.size() == 0;
	}
	@RolesAllowed({"admin","user"})
	@Override
	public BigInteger getQuantityOfDiscoveries() {
		Query query=em.createNativeQuery("SELECT count(*) FROM discovery");
		return (BigInteger)query.getSingleResult();
	}

}
