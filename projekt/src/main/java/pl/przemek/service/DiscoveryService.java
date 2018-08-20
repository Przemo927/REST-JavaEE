package pl.przemek.service;

import pl.przemek.model.Discovery;
import pl.przemek.model.Vote;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaVoteRepository;

import javax.inject.Inject;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryService {

    private JpaDiscoveryRepository discRepo;
    private JpaVoteRepository<Vote> voteRepo;
    private Logger logger;

    @Inject
    public DiscoveryService(Logger logger,  JpaDiscoveryRepository discRepo, JpaVoteRepository<Vote> voteRepo){
        this.logger=logger;
        this.discRepo=discRepo;
        this.voteRepo=voteRepo;
    }
    public DiscoveryService(){
    }
    public void addDiscovery(Discovery discovery){
        try{
            if(discovery!=null) {
                discovery.setDownVote(0);
                discovery.setUpVote(0);
                discovery.setTimestamp(new Timestamp(new Date().getTime()));
                discRepo.add(discovery);
            }else{
                logger.log(Level.WARNING,"[DiscoveryService] addDiscovery() discovery "+discovery);
            }
        }catch (Exception e){
            logger.log(Level.SEVERE,"[DiscoveryService] addDiscovery() discovery "+discovery,e);
        }
    }

    public List<Discovery> getWithLimit(int begin, int quantity){
        List<Discovery> listOfDiscoveries=null;
        try {
            listOfDiscoveries=discRepo.getWithLimit(begin,quantity);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[DiscoveryService] getWithLimit() begin="+begin+" quantity="+quantity);
            return Collections.emptyList();
        }
        return listOfDiscoveries;
    }

    public List<Discovery> getByName(String name){
        List<Discovery> listOfDiscoveries=null;
        if(name!=null && !"".equals(name))
            listOfDiscoveries=discRepo.getByName(name);
        else {
            logger.log(Level.SEVERE,"[DiscoveryService] getByName() name="+name);
            return Collections.emptyList();
        }
        return listOfDiscoveries;
    }
    public Discovery getById(long id){
        Discovery discovery=null;
        try{
            discovery=discRepo.get(Discovery.class,id);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[DiscoveryService] getById() dscovery id="+id,e);
        }
        return discovery;
    }

    public List<Discovery> getAll(String order){
        List<Discovery> discoveries = null;
        try {
            switch (order) {
                case "popular":
                    discoveries = discRepo.getAll(new PopularComparator());
                    break;
                case "time":
                    discoveries = discRepo.getAll(new TimeComparator());
                    break;
                default:
                    discoveries = discRepo.getAll("Discovery.findAll", Discovery.class);
                    break;
            }
        }catch(NullPointerException e){
            discoveries = discRepo.getAll("Discovery.findAll",Discovery.class);
        }
        return discoveries;
    }

    public List<Discovery> getAllInOneQuery(){
        List<Discovery> listOfDiscoveries=null;
        try {
            listOfDiscoveries=discRepo.getAllInOneQuery();
        }catch (Exception e){
            logger.log(Level.SEVERE,"[DiscoveryService] getAllInOneQuery()",e);
            return Collections.emptyList();
        }
        return listOfDiscoveries;
    }

    public void removeDiscoveryById(long id){
        try {
            Discovery discovery=discRepo.get(Discovery.class,id);
            voteRepo.removeByVotedElementId(id);
            discRepo.remove(discovery);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[DiscoveryService] removeDiscoveryById() discovery id="+id,e);
        }
    }

    public void updateDiscovery(Discovery discovery){
        try {
            discRepo.update(discovery);
        }catch (Exception e){
            logger.log(Level.SEVERE,"[DiscoveryService] updateDiscovery()",e);
        }
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
        BigInteger bigInteger=null;
        try {
            bigInteger=discRepo.getQuantityOfDiscoveries();
        }catch (Exception e){
            logger.log(Level.SEVERE,"[DiscoveryService] getQuantityOfDiscoveries()",e);
            return BigInteger.ZERO;
        }
            return bigInteger;
        }
    }
