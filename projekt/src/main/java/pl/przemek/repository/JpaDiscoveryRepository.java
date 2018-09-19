package pl.przemek.repository;

import pl.przemek.model.Discovery;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

public interface JpaDiscoveryRepository {
    public void add(Discovery discovery);
    public void remove(Discovery discovery);
    public Discovery update(Discovery discovery);
    public List<Discovery> getAll(String nameOfQuery,Class<Discovery> clazz);
    public List<Discovery> getAllInOneQuery();
    public List<Discovery> getAll(Comparator<Discovery> c);
    public List<Discovery> getWithLimit(int begin, int quantity);
    public List<Discovery> getWithLimitOrderByDate(int begin, int quantity);
    public List<Discovery> getByName(String name);
    public Discovery get(Class<Discovery> clazz,long id);
    public boolean checkPresenceDiscveryByUrl(String url);
    public BigInteger getQuantityOfDiscoveries();
}
