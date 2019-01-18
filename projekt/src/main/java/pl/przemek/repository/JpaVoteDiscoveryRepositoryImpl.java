package pl.przemek.repository;

import pl.przemek.model.Vote;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaVoteDiscoveryRepositoryImpl extends JpaRepository<Vote> implements JpaVoteRepository<Vote> {


	@RolesAllowed({"admin","user"})
	@Override
	public List<Vote> getVoteByUserIdVotedElementId(long userId, long discoveryId) {
		TypedQuery<Vote> query = em.createQuery("SELECT p FROM Vote p WHERE p.user.id=:userid AND p.discovery.id=:discoveryid", Vote.class);
		query.setParameter("userid", userId);
		query.setParameter("discoveryid", discoveryId);
		return query.getResultList();
	}


	@RolesAllowed({"admin","user"})
	@Override
	public void removeByVotedElementId(long discoveryId) {
		Query query= em.createNativeQuery("DELETE FROM Vote WHERE discovery_id=:discoveryid");
		query.setParameter("discoveryid",discoveryId);
		query.executeUpdate();
	}

}
