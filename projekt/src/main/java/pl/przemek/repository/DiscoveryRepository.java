package pl.przemek.repository;

import pl.przemek.model.Discovery;

import javax.annotation.security.RolesAllowed;
import java.util.Comparator;
import java.util.List;

public interface DiscoveryRepository {
    public void add(Discovery discovery);
    public void remove(Discovery discovery);
    public Discovery update(Discovery discovery);
    public List<Discovery> getAll();
    public List<Discovery> getAll(Comparator<Discovery> c);
    public Discovery getByName(String name);
    public Discovery get(long id);
    public boolean checkPresenceDiscveryByUrl(String url);
}
