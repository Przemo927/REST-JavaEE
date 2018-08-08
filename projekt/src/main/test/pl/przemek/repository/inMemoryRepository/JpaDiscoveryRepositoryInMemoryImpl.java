package pl.przemek.repository.inMemoryRepository;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.JpaDiscoveryRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

public class JpaDiscoveryRepositoryInMemoryImpl implements JpaDiscoveryRepository {

    private List<Discovery> listOfDiscoveries;

    public JpaDiscoveryRepositoryInMemoryImpl(){
        listOfDiscoveries=new ArrayList<>();
        populateDiscoveryList();
    }
    @Override
    public void add(Discovery discovery) {
        listOfDiscoveries.add(discovery);
    }

    @Override
    public void remove(Discovery discovery) {
        listOfDiscoveries.remove(discovery);
    }

    @Override
    public Discovery update(Discovery discovery) {
        for(int i=0;i<listOfDiscoveries.size();i++){
            if(discovery.getId()==listOfDiscoveries.get(i).getId()){
                listOfDiscoveries.set(i,discovery);
            }
        }
        return discovery;
    }

    @Override
    public List<Discovery> getAll(String nameOfQuery, Class<Discovery> clazz) {
        return listOfDiscoveries;
    }

    @Override
    public List<Discovery> getAllInOneQuery() {
        return null;
    }

    @Override
    public List<Discovery> getAll(Comparator<Discovery> c) {
        List<Discovery> temporaryList=new ArrayList<>(listOfDiscoveries);
        temporaryList.sort(c);
        return temporaryList;
    }

    @Override
    public List<Discovery> getWithLimit(int begin, int quantity) {
        return listOfDiscoveries.subList(begin,quantity);
    }

    @Override
    public List<Discovery> getByName(String name) {
        List<Discovery> temporaryList=new ArrayList<>();
        listOfDiscoveries.forEach((discovery -> {
            if(discovery.getName().equals(name))
                temporaryList.add(discovery);
        }));
        return temporaryList;
    }

    @Override
    public Discovery get(Class<Discovery> clazz, long id) {
        for(int i=0;i<listOfDiscoveries.size();i++){
            Discovery discovery=listOfDiscoveries.get(i);
            if(discovery==null){

            }
            else if(discovery.getId()==id)
                return discovery;
        }
        return null;
    }

    @Override
    public boolean checkPresenceDiscveryByUrl(String url) {
        List<Discovery> temporaryList=new ArrayList<>();
        for(int i=0;i<listOfDiscoveries.size();i++){
            if(listOfDiscoveries.get(i).getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BigInteger getQuantityOfDiscoveries() {
        return BigInteger.valueOf(listOfDiscoveries.size());
    }

    public List<Discovery> getListOfDiscoveries() {
        return listOfDiscoveries;
    }

    public void setListOfDiscoveries(List<Discovery> listOfDiscoveries) {
        this.listOfDiscoveries = listOfDiscoveries;
    }

    private void populateDiscoveryList(){
        for(int i=0;i<10;i++){
            Discovery discovery=new Discovery();
            discovery.setId(i);
            discovery.setDownVote(i+new Random().nextInt(5));
            discovery.setUpVote(i+new Random().nextInt(5));
            discovery.setUser(new User());
            discovery.setTimestamp(new Timestamp(new Date().getTime()));
            discovery.setDescription("description"+i);
            discovery.setName("name"+i);
            discovery.setUrl("https://www."+new Random().nextInt(25)+"a");
            listOfDiscoveries.add(discovery);
        }
    }
}
