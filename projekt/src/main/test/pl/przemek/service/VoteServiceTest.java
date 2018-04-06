package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.model.Vote;
import pl.przemek.model.VoteType;
import pl.przemek.repository.JpaDiscoveryRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.repository.JpaVoteRepository;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class VoteServiceTest {

    private JpaUserRepository userRepo;
    private JpaDiscoveryRepository discRepo;
    private JpaVoteRepository voteRepo;
    private VoteService voteService;
    @Before
    public void setUp() throws Exception{
        userRepo=mock(JpaUserRepository.class);
        discRepo=mock(JpaDiscoveryRepository.class);
        voteRepo=mock(JpaVoteRepository.class);
        voteService=new VoteService(voteRepo,discRepo,userRepo);
    }

    @Test
    public void shouldReturnVoteWithAssignVoteTypeAndUserAndComment() throws Exception {
        User user=mock(User.class);
        Discovery discovery=mock(Discovery.class);

        when(discRepo.get(anyLong())).thenReturn(discovery);
        when(userRepo.get(anyLong())).thenReturn(user);
        Vote vote=voteService.createVote(1,2, VoteType.VOTE_UP);

        assertEquals(user, vote.getUser());
        assertEquals(discovery,vote.getDiscovery());
        assertEquals(VoteType.VOTE_UP,vote.getVoteType());
    }

    public Object[] discoveryUserAndVote(){
        return $(new Object[]{new Discovery(),null, VoteType.VOTE_DOWN},new Object[]{null,new User(), VoteType.VOTE_UP},new Object[]{null,null, VoteType.VOTE_UP});
    }
    @Test
    @Parameters(method = "discoveryUserAndVote")
    public void shouldReturnNullIfCommentOrUserAreNull(Discovery discovery, User user, VoteType voteType) throws Exception{


        when(userRepo.get(anyLong())).thenReturn(user);
        when(discRepo.get(anyLong())).thenReturn(discovery);
        assertEquals(null,voteService.createVote(1,2, voteType));
    }

    @Test
    public void shouldExecuteAddAndUpdateDiscoveryWhenOldVoteIsNotExist() throws Exception {
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Vote updateVote=mock(Vote.class);

        when(voteRepo.getVoteByUserIdDiscoveryId(anyLong(),anyLong())).thenReturn(null);
        doReturn(updateVote).when(voteService).createVote(anyLong(),anyLong(),isA(VoteType.class));
        doNothing().when(voteService).updateDiscovery(anyLong(),eq(null),isA(Vote.class));
        voteService.updateVote(1,2, VoteType.VOTE_UP);

        verify(voteRepo).add(updateVote);
        verify(voteService).updateDiscovery(2,null,updateVote);

    }

    @Test
    public void shouldNotUpdateVoteAndDiscoveryWhenOldVoteAndNewVoteAreEqual() throws Exception {
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Vote updateVote=new Vote();
        Vote existingVote=new Vote();
        existingVote.setVoteType(VoteType.VOTE_UP);

        when(voteRepo.getVoteByUserIdDiscoveryId(anyLong(),anyLong())).thenReturn(existingVote);
        doReturn(updateVote).when(voteService).createVote(existingVote);
        voteService.updateVote(1,2, VoteType.VOTE_UP);

        verify(voteRepo,never()).update(updateVote);
        assertEquals(VoteType.VOTE_UP,updateVote.getVoteType());
        verify(voteService,never()).updateDiscovery(anyLong(),isA(Vote.class),isA(Vote.class));
    }

    @Test
    public void shouldExecuteMethodUpdateAndUpdateDiscoveryWhenOldVoteAndNewVoteAreNotEqual() throws Exception {
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Vote updateVote=new Vote();
        Vote existingVote=new Vote();
        existingVote.setVoteType(VoteType.VOTE_UP);

        when(voteRepo.getVoteByUserIdDiscoveryId(anyLong(),anyLong())).thenReturn(existingVote);
        doNothing().when(voteService).updateDiscovery(anyLong(),isA(Vote.class),isA(Vote.class));
        doReturn(updateVote).when(voteService).createVote(existingVote);
        voteService.updateVote(1,2, VoteType.VOTE_DOWN);

        verify(voteRepo).update(updateVote);
        assertEquals(VoteType.VOTE_DOWN,updateVote.getVoteType());
        verify(voteService).updateDiscovery(anyLong(),isA(Vote.class),isA(Vote.class));
    }
    @Test
    public void shouldExecuteAddDiscoveryVoteAndUpdateWhenOldVoteIsNotExist() throws Exception {
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Vote updateVote=mock(Vote.class);
        Discovery discovery=mock(Discovery.class);
        Discovery updateDiscovery=mock(Discovery.class);

        when(discRepo.get(anyLong())).thenReturn(discovery);
        doReturn(updateDiscovery).when(voteService).createDicovery(isA(Discovery.class));
        voteService.updateDiscovery(1,null,updateVote);

        verify(voteService).addDiscoveryVote(discovery,updateVote.getVoteType());
        verify(discRepo).update(updateDiscovery);
    }
    @Test
    public void shouldExecuteRemoveDiscoveryVoteAndUpdateWhenOldVoteAndNewVoteIsExist() throws Exception {
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Vote updateVote=mock(Vote.class);
        Vote oldVote=mock(Vote.class);
        Discovery discovery=mock(Discovery.class);
        Discovery updateDiscovery=mock(Discovery.class);

        when(discRepo.get(anyLong())).thenReturn(discovery);
        doReturn(updateDiscovery).when(voteService).createDicovery(isA(Discovery.class));
        voteService.updateDiscovery(1,oldVote,updateVote);

        verify(voteService).removeDiscoveryVote(discovery,updateVote.getVoteType());
        verify(discRepo).update(updateDiscovery);
    }
    @Test
    public void shouldDoNothingWhenNewVoteAndOldVoteIsNull() throws Exception{
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Discovery discovery=mock(Discovery.class);

        when(discRepo.get(anyLong())).thenReturn(discovery);
        voteService.updateDiscovery(1,null,null);

        verify(discRepo).get(1);
        verify(voteService,never()).removeDiscoveryVote(isA(Discovery.class),isA(VoteType.class));
        verify(voteService,never()).addDiscoveryVote(isA(Discovery.class),isA(VoteType.class));
        verify(discRepo,never()).update(isA(Discovery.class));
    }
    @Test
    public void shouldReturnCommentWithValueOfVoteUpPlusOneWhenNewVoteIsVoteUp(){
        int valueOfVoteup=0;
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Discovery discovery=new Discovery();
        discovery.setUpVote(valueOfVoteup);

        doReturn(discovery).when(voteService).createDicovery(isA(Discovery.class));
        Discovery discovery1=voteService.addDiscoveryVote(discovery, VoteType.VOTE_UP);

        assertEquals(valueOfVoteup+1,discovery1.getUpVote());

    }
    @Test
    public void shouldReturnCommentWithValueOfVoteDownPlusOneWhenNewVoteIsVoteDown(){
        int valueOfVoteDown=0;
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Discovery discovery=new Discovery();
        discovery.setDownVote(valueOfVoteDown);

        doReturn(discovery).when(voteService).createDicovery(isA(Discovery.class));
        Discovery discovery1=voteService.addDiscoveryVote(discovery, VoteType.VOTE_DOWN);

        assertEquals(valueOfVoteDown+1,discovery1.getDownVote());

    }
    @Test
    public void shouldReturnDiscoveryWithValueOfVoteUpMinusOneWhenVoteTypeIsVoteUp() throws Exception {
        int valueOfVoteUp=1;
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Discovery discovery=new Discovery();
        discovery.setUpVote(valueOfVoteUp);

        doReturn(discovery).when(voteService).createDicovery(isA(Discovery.class));
        Discovery discovery1=voteService.removeDiscoveryVote(discovery, VoteType.VOTE_UP);

        assertEquals(valueOfVoteUp-1,discovery1.getUpVote());

    }
    @Test
    public void shouldReturnDiscoveryWithValueOfVoteDownMinusOneWhenVoteTypeIsVoteDown() throws Exception {
        int valueOfVoteDown=1;
        voteService=spy(new VoteService(voteRepo,discRepo,userRepo));
        Discovery discovery=new Discovery();
        discovery.setDownVote(valueOfVoteDown);

        doReturn(discovery).when(voteService).createDicovery(isA(Discovery.class));
        Discovery discovery1=voteService.removeDiscoveryVote(discovery, VoteType.VOTE_DOWN);

        assertEquals(valueOfVoteDown-1,discovery1.getDownVote());

    }

}