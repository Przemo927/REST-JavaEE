package pl.przemek.repository.inMemoryRepository;

import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.model.Vote;
import pl.przemek.model.VoteType;
import pl.przemek.repository.JpaVoteRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JpaVoteDicoveryRepositoryInMemoryImpl implements JpaVoteRepository<Vote> {

    private List<Vote> listOfVotes;

    public JpaVoteDicoveryRepositoryInMemoryImpl(){
        listOfVotes=new ArrayList<>();
        populateVoteList();
    }
    @Override
    public void add(Vote vote) {
        listOfVotes.add(vote);
    }

    @Override
    public Vote update(Vote vote) {
        for(int i=0;i<listOfVotes.size();i++){
            if(vote.getId()==listOfVotes.get(i).getId()){
                listOfVotes.set(i,vote);
            }
        }
        return vote;
    }

    @Override
    public void remove(Vote vote) {
        listOfVotes.remove(vote);
    }

    @Override
    public Vote get(Class<Vote> clazz, long id) {
        for (Vote vote : listOfVotes) {
            if (vote.getId() == id)
                return vote;
        }
        return null;
    }

    @Override
    public List<Vote> getAll(String nameOfQuery, Class<Vote> clazz) {
        return listOfVotes;
    }

    @Override
    public List<Vote> getVoteByUserIdVotedElementId(long userId, long discoveryId) {
        List<Vote> temporaryList=new ArrayList<>();
        for (Vote vote : listOfVotes) {
            if (vote.getDiscovery().getId() == discoveryId && vote.getUser().getId() == userId)
                temporaryList.add(vote);
        }
        return temporaryList;
    }

    @Override
    public void removeByVotedElementId(long discoveryId) {
        for(int i=0;i<listOfVotes.size();i++) {
            Vote vote = listOfVotes.get(i);
            if(vote.getDiscovery().getId()==discoveryId)
                listOfVotes.remove(vote);
        }
    }
    private void populateVoteList(){
        for(int i=0;i<10;i++){
            Vote vote=new Vote();
            if(i%2==0)
                vote.setVoteType(VoteType.VOTE_UP);
            else
                vote.setVoteType(VoteType.VOTE_DOWN);
            vote.setDate(new Timestamp(new Date().getTime()));
            vote.setDiscovery(new Discovery());
            vote.setUser(new User());
            vote.setId(i);
            listOfVotes.add(vote);
        }
    }
}
