package pl.przemek.repository;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.przemek.model.Vote;
@Stateless
public class VoteRepository {
	@PersistenceContext
	private EntityManager em;

	public void add(Vote vote) {
	    em.persist(vote);
	}
	 
	public void remove(Vote vote) {
	    em.remove(vote);
	}
	public Vote update(Vote vote) {
		em.merge(vote);
		return vote;
	}
	public Vote get(Long id) {
	    Vote vote = em.find(Vote.class, id);
	    return vote;
	}
	 
	public List<Vote> getAll() {
	    return null;
	}
	public Vote getVoteByUserIdDiscoveryId(Long UserId,Long DiscoveryId) {
		TypedQuery<Vote> getQuery = em.createQuery("SELECT p FROM Vote p WHERE p.user.id=:userid AND p.discovery.id=:discoveryid", Vote.class);
	    getQuery.setParameter("userid", UserId);
	    getQuery.setParameter("discoveryid", DiscoveryId);
	    Vote chosenvote=null;
	    
	    try{
	    	 chosenvote=getQuery.getSingleResult();
	    }
	    	catch (NoResultException nre){
	    	}
	    
	 
	    	return chosenvote;
	    }

}
