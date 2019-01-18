package pl.przemek.service;

import pl.przemek.model.Discovery;
import pl.przemek.model.Vote;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaVoteRepository;

import javax.inject.Inject;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryService {

    private JpaDiscoveryRepository discRepo;
    private JpaVoteRepository<Vote> voteRepo;
    private JpaCommentRepository commentRepo;
    private Logger logger;

    @Inject
    public DiscoveryService(Logger logger,  JpaDiscoveryRepository discRepo, JpaVoteRepository<Vote> voteRepo,JpaCommentRepository commentRepo){
        this.logger=logger;
        this.discRepo=discRepo;
        this.voteRepo=voteRepo;
        this.commentRepo=commentRepo;
    }
    public DiscoveryService(){
    }
    public void addDiscovery(Discovery discovery){
        if(discovery!=null) {
            discovery.setDownVote(0);
            discovery.setUpVote(0);
            discovery.setTimestamp(new Timestamp(new Date().getTime()));
            discRepo.add(discovery);
        }else {
            logger.log(Level.WARNING, "[DiscoveryService] addDiscovery() discovery " + discovery);
        }
    }

    public List<Discovery> getWithLimit(int begin, int quantity){
        return discRepo.getWithLimit(begin,quantity);
    }

    public List<Discovery> getWithLimitOrderByDate(int begin, int quantity){
        return discRepo.getWithLimitOrderByDate(begin,quantity);
    }

    List<Discovery> getByName(String name){
        if(name!=null && !"".equals(name))
            return discRepo.getByName(name);
        else {
            logger.log(Level.SEVERE,"[DiscoveryService] getByName() name="+name);
            return Collections.emptyList();
        }
    }
    public Optional<Discovery> getById(long id){
        Discovery discovery=discRepo.get(Discovery.class,id);
        if(discovery==null){
            logger.log(Level.SEVERE,"[DiscoveryService] getById() Discovery wasn't found");
        }
        return Optional.ofNullable(discovery);
    }

    public List<Discovery> getAll(String order){
        List<Discovery> discoveries;

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
        return discoveries;
    }

    public void removeDiscoveryById(long id){
        Discovery discovery=discRepo.get(Discovery.class,id);
        if(discovery==null){
            logger.log(Level.SEVERE,"[DiscoveryService] removeDiscoveryById() Discovery wasn't found id="+id);
        }
        voteRepo.removeByVotedElementId(id);
        commentRepo.removeByDiscoveryId(id);
        discRepo.remove(discovery);

    }

    public void updateDiscovery(Discovery discovery){
        if(discovery==null){
            logger.log(Level.SEVERE,"[DiscoveryService] updateDiscovery() Discovery is null");
        }else {
            discRepo.update(discovery);
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
        return discRepo.getQuantityOfDiscoveries();
        }
    }
