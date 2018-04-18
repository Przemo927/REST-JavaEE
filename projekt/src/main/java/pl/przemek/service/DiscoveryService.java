package pl.przemek.service;

import pl.przemek.model.Discovery;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaVoteRepository;

import javax.inject.Inject;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DiscoveryService {

    private JpaDiscoveryRepository discRepo;
    private JpaVoteRepository voteRepo;

    @Inject
    public DiscoveryService(JpaDiscoveryRepository discRepo, JpaVoteRepository voteRepo){
        this.discRepo=discRepo;
        this.voteRepo=voteRepo;
    }
    public DiscoveryService(){
        this.discRepo=null;
        this.voteRepo=null;
    }
    public void addDiscovery(Discovery discovery){
        if(discovery!=null) {
            discovery.setDownVote(0);
            discovery.setUpVote(0);
            discovery.setTimestamp(new Timestamp(new Date().getTime()));
            discRepo.add(discovery);
        }
    }

    public List<Discovery> getWithLimit(int begin, int quantity){
        return discRepo.getWithLimit(begin,quantity);
    }

    public List<Discovery> getByName(String name){
        if(name!=null && !"".equals(name))
            return discRepo.getByName(name);
        return null;
    }
    public Discovery getById(long id){
        return discRepo.get(Discovery.class,id);
    }

    public List<Discovery> getAll(String order){
        List<Discovery> discoveries = null;
        try {
            if (order.equals("popular")) {
                discoveries = discRepo.getAll(new PopularComparator());
            } else if (order.equals("time")) {
                discoveries = discRepo.getAll(new TimeComparator());
            } else {
                discoveries = discRepo.getAll("Discovery.findAll",Discovery.class);
            }
        }catch(NullPointerException e){
            discoveries = discRepo.getAll("Discovery.findAll",Discovery.class);
        }
        return discoveries;
    }

    public List<Discovery> getAllInOneQuery(){
        return discRepo.getAllInOneQuery();
    }

    public void removeDiscoveryById(long id){
        Discovery discovery=discRepo.get(Discovery.class,id);
        voteRepo.removeByDiscoveryId(id);
        discRepo.remove(discovery);
    }

    public void updateDiscovery(Discovery discovery){
        discRepo.update(discovery);
    }

    public static class PopularComparator implements Comparator<Discovery>{

        @Override
        public int compare(Discovery d1, Discovery d2) {
            int d1Vote = d1.getUpVote() - d1.getDownVote();
            int d2Vote = d2.getUpVote() - d2.getDownVote();
            if(d1Vote < d2Vote) {
                return 1;
            } else if(d1Vote > d2Vote) {
                return -1;
            }
            return 0;
        }}
        public static class TimeComparator implements Comparator<Discovery>{

            @Override
            public int compare(Discovery d1, Discovery d2) {
                return d1.getTimestamp().compareTo(d2.getTimestamp());
            }
        }

        public BigInteger getQuantityOfDiscoveries(){
            return discRepo.getQuantityOfDiscoveries();
        }
    }
