package pl.przemek.repository;


import pl.przemek.model.EventPosition;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class JpaEventPositionRepositoryImpl extends JpaRepository<EventPosition> implements JpaEventPositionRepository {


}
