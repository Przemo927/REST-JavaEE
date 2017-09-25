package pl.przemek.service;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.DiscoveryRepository;
import pl.przemek.repository.UserRepository;
import pl.przemek.repository.VoteRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DiscoveryService {

    @Inject
    private DiscoveryRepository discrepo;
    @Inject
    private VoteRepository voterepo;

    List<Discovery> discoveries;

    public Discovery addDiscovery(Discovery discovery){
        discovery.setDownVote(0);
        discovery.setUpVote(0);
        discovery.setTimestamp(new Timestamp(new Date().getTime()));
        discrepo.add(discovery);
        return discovery;
        }

    public Discovery getByName(String name){
        return discrepo.getByName(name);

    }
    public Discovery getById(long id){
        return discrepo.get(id);
    }

    public List<Discovery> getAll(String order){
        List<Discovery> discoveries = null;
        if(order.equals("popular")){
            discoveries=discrepo.getAll(new Comparator<Discovery>() {
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
                }});
            return discoveries;
        }

        else if(order.equals("time")){
            discoveries=discrepo.getAll(new Comparator<Discovery>() {
                @Override
                public int compare(Discovery d1, Discovery d2) {
                    return d1.getTimestamp().compareTo(d2.getTimestamp());
                }});
        }
        return discoveries;
    }


    public void removeDiscoveryByName(String name) {
        Discovery discovery=discrepo.getByName(name);
        voterepo.removeByDiscoveryId(discovery.getId());
        discrepo.remove(discovery);
    }
    public void removeDiscoveryById(long id){
        Discovery discovery=discrepo.get(id);
        voterepo.removeByDiscoveryId(id);
        discrepo.remove(discovery);
    }

    public void updateDiscovery(Discovery discovery){
        discrepo.update(discovery);
    }
    }