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

	private final static String PRESENCE_DISCOVERY_BY_URL="SELECT 1 FROM Discovery WHERE url=:url";
	private final static String QUANTITY_OF_DISCOVERIES="SELECT count(*) FROM discovery";

	@RolesAllowed({"admin","user"})
	@Override
	public List<Discovery> getWithLimit(int begin, int quantity) {
		TypedQuery<Discovery> query=em.createNamedQuery("Discovery.findAllWithLimit",Discovery.class);
		query.setParameter("begin",begin);
		query.setParameter("quantity",quantity);
		return query.getResultList();
	}

	@RolesAllowed({"admin","user"})
	@Override
	public List<Discovery> getWithLimitOrderByDate(int begin, int quantity) {
		TypedQuery<Discovery> query=em.createNamedQuery("Discovery.findAllWithLimitOrderByDate",Discovery.class);
		query.setParameter("begin",begin);
		query.setParameter("quantity",quantity);
		return query.getResultList();
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
		TypedQuery<Discovery> disc = em.createNamedQuery("Discovery.search", Discovery.class);
		disc.setParameter("name", name);
		return disc.getResultList();
	
	}
	@PermitAll
	public boolean checkPresenceDiscveryByUrl(String url){
		Query query=em.createNativeQuery(PRESENCE_DISCOVERY_BY_URL);
		query.setParameter("url",url);
		List list=query.getResultList();
		return list.size() == 0;
	}
	@RolesAllowed({"admin","user"})
	@Override
	public BigInteger getQuantityOfDiscoveries() {
		Query query=em.createNativeQuery(QUANTITY_OF_DISCOVERIES);
		return (BigInteger)query.getSingleResult();
	}

	@RolesAllowed({"admin","user"})
	@Override
	public List<Discovery> getDiscoveryWithComments(long id) {
		TypedQuery<Discovery> query=em.createNamedQuery("Discovery.getDiscoveryWithComments",Discovery.class);
		query.setParameter("discovery_id",id);
		return query.getResultList();
	}

}
