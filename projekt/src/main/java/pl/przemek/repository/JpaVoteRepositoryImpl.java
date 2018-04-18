package pl.przemek.repository;

import pl.przemek.model.Vote;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaVoteRepositoryImpl extends JpaRepository<Vote> implements JpaVoteRepository {


	@RolesAllowed({"admin","user"})
	public Vote getVoteByUserIdDiscoveryId(Long UserId, Long DiscoveryId) {
		TypedQuery<Vote> query = em.createQuery("SELECT p FROM Vote p WHERE p.user.id=:userid AND p.discovery.id=:discoveryid", Vote.class);
		query.setParameter("userid", UserId);
		query.setParameter("discoveryid", DiscoveryId);
		List<Vote> lisOfVote = query.getResultList();
		if (lisOfVote.isEmpty()) {
			return null;
		}
		return lisOfVote.get(0);
	}


	@RolesAllowed({"admin","user"})
	@Override
	public void removeByDiscoveryId(Long DiscoveryId) {
		TypedQuery<Vote> query= em.createQuery("SELECT p FROM Vote p WHERE p.discovery.id=:discoveryid", Vote.class);
		query.setParameter("discoveryid",DiscoveryId);
		List<Vote> votesToRemove=query.getResultList();
		if(!votesToRemove.isEmpty())
			votesToRemove.forEach(x-> remove(x));
	}

}
