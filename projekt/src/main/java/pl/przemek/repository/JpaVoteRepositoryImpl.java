package pl.przemek.repository;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.przemek.model.Role;
import pl.przemek.model.Vote;
@Stateless
public class JpaVoteRepositoryImpl implements JpaVoteRepository {
	@PersistenceContext
	private EntityManager em;

	@RolesAllowed({"admin","user"})
	public void add(Vote vote) {
	    em.persist(vote);
	}

	@RolesAllowed("admin")
	public void remove(Vote vote) {
	    em.remove(em.merge(vote));
	}

	@RolesAllowed({"admin","user"})
	public Vote update(Vote vote) {
		em.merge(vote);
		return vote;
	}

	@RolesAllowed({"admin","user"})
	public Vote get(Long id) {
	    Vote vote = em.find(Vote.class, id);
	    return vote;
	}

	@RolesAllowed({"admin","user"})
	public List<Vote> getAll() {
	    return null;
	}

	@RolesAllowed({"admin","user"})
	public Vote getVoteByUserIdDiscoveryId(Long UserId,Long DiscoveryId) {
		TypedQuery<Vote> query = em.createQuery("SELECT p FROM Vote p WHERE p.user.id=:userid AND p.discovery.id=:discoveryid", Vote.class);
		query.setParameter("userid", UserId);
		query.setParameter("discoveryid", DiscoveryId);
		Vote chosenvote = null;
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
		votesToRemove.forEach(x-> remove(x));

	}

}
