package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.przemek.model.Comment;
import pl.przemek.model.User;
import pl.przemek.model.VoteComment;
import pl.przemek.model.VoteType;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.repository.JpaVoteRepository;
import pl.przemek.repository.inMemoryRepository.JpaCommentRepositoryInMemoryImpl;
import pl.przemek.repository.inMemoryRepository.JpaUserRepositoryInMemoryImpl;
import pl.przemek.repository.inMemoryRepository.JpaVoteCommentRepositoryInMemoryImpl;

import java.util.Collections;
import java.util.logging.Logger;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class VoteCommentServiceTest {

    private JpaUserRepository userRepo;
    private JpaCommentRepository commentRepo;
    private JpaVoteRepository<VoteComment> voteCommentRepo;
    private VoteCommentService voteCommentService;
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        userRepo=mock(JpaUserRepository.class);
        commentRepo=mock(JpaCommentRepository.class);
        voteCommentRepo=mock(JpaVoteRepository.class);
        logger=mock(Logger.class);
        VoteCommentService voteCommentService=spy(new VoteCommentService(logger,userRepo,commentRepo,voteCommentRepo));
    }

    @Test
    public void shouldReturnVoteWithAssignVoteTypeUserAndComment() throws Exception {
        createVoteCommentServiceObjectWithInMemoryLayer();
        long userId=1234;
        User user=new User();
        user.setId(userId);
        userRepo.add(user);
        Comment comment=new Comment();
        long commentId=4321;
        comment.setId(commentId);
        commentRepo.add(comment);

        voteCommentService.getCommentAndUserFromDataBase(commentId,userId);
        VoteComment voteComment=voteCommentService.createVote(VoteType.VOTE_UP);

        assertEquals(user, voteComment.getUser());
        assertEquals(comment,voteComment.getComment());
        assertEquals(VoteType.VOTE_UP,voteComment.getVoteType());
        assertNotSame(VoteType.VOTE_DOWN, voteComment.getVoteType());
    }
    private void createVoteCommentServiceObjectWithInMemoryLayer(){
        userRepo=new JpaUserRepositoryInMemoryImpl();
        voteCommentRepo=new JpaVoteCommentRepositoryInMemoryImpl();
        commentRepo=new JpaCommentRepositoryInMemoryImpl();
        voteCommentService=new VoteCommentService(logger,userRepo,commentRepo,voteCommentRepo);
    }

    public Object[] elements(){
        return $(new Object[]{1234,5678, VoteType.VOTE_UP},new Object[]{4321,8765, VoteType.VOTE_DOWN});
    }
    @Test
    @Parameters(method = "elements")
    public void shouldAddNewVoteAndUpdateCommentWhenOldVoteIsNotExist(long userId,long commentId,VoteType voteType) throws Exception {
        createVoteCommentServiceObjectWithInMemoryLayer();
        User user=new User();
        user.setId(userId);
        userRepo.add(user);
        Comment comment=new Comment();
        comment.setId(commentId);
        commentRepo.add(comment);

        voteCommentService.getCommentAndUserFromDataBase(commentId,userId);
        assertEquals(Collections.emptyList(),voteCommentRepo.getVoteByUserIdVotedElementId(userId,commentId));

        voteCommentService.updateVote(userId,commentId,voteType);

        assertEquals(voteType,voteCommentRepo.getVoteByUserIdVotedElementId(userId,commentId).get(0).getVoteType());
    }

    @Test
    public void shouldNotUpdateVoteAndCommentWhenOldVoteAndNewVoteAreEqual() throws Exception{
        createVoteCommentServiceObjectWithInMemoryLayer();

        int userId=1;
        int commentId=2;

        User user=new User();
        user.setId(userId);
        userRepo.add(user);

        Comment comment=new Comment();
        comment.setId(commentId);
        commentRepo.add(comment);

        VoteComment existingVote=new VoteComment();
        existingVote.setVoteType(VoteType.VOTE_UP);
        existingVote.setComment(comment);
        existingVote.setUser(user);
        voteCommentRepo.add(existingVote);

        voteCommentService.updateVote(userId,commentId, VoteType.VOTE_UP);

        assertSame(existingVote, voteCommentRepo.getVoteByUserIdVotedElementId(userId, commentId).get(0));
    }

    @Test
    public void shouldUpdateVoteAndCommentWhenOldVoteAndNewVoteAreNotEqual() throws Exception{
        createVoteCommentServiceObjectWithInMemoryLayer();

        int userId=1;
        int commentId=2;

        User user=new User();
        user.setId(userId);
        userRepo.add(user);

        Comment comment=new Comment();
        comment.setId(commentId);
        commentRepo.add(comment);

        VoteComment existingVote=new VoteComment();
        existingVote.setVoteType(VoteType.VOTE_DOWN);
        existingVote.setComment(comment);
        existingVote.setUser(user);
        voteCommentRepo.add(existingVote);

        voteCommentService.updateVote(userId,commentId, VoteType.VOTE_UP);

        VoteComment updatedVote=voteCommentRepo.getVoteByUserIdVotedElementId(userId, commentId).get(0);

        assertNotEquals(existingVote, updatedVote);
        assertEquals(updatedVote.getVoteType(),VoteType.VOTE_UP);
    }

    @Test
    public void shouldReturnCommentWithValueOfVoteUpPlusOneWhenNewVoteIsVoteUp(){
        voteCommentService=new VoteCommentService(logger,userRepo,commentRepo,voteCommentRepo);
        int valueOfVoteUp=0;
        Comment comment=new Comment();
        comment.setUpVote(valueOfVoteUp);

        //Comment comment1=voteCommentService.addCommentVote(comment, VoteType.VOTE_UP);

        //assertEquals(valueOfVoteUp+1,comment1.getUpVote());

    }
    @Test
    public void shouldReturnCommentWithValueOfVoteDownPlusOneWhenNewVoteIsVoteDown() throws Exception{
        voteCommentService=new VoteCommentService(logger,userRepo,commentRepo,voteCommentRepo);
        int valueOfVoteDown=0;
        Comment comment=new Comment();
        comment.setDownVote(valueOfVoteDown);

        //Comment comment1=voteCommentService.addCommentVote(comment, VoteType.VOTE_DOWN);

        //assertEquals(valueOfVoteDown+1,comment1.getDownVote());

    }
    @Test
    public void shouldReturnCommentWithValueOfVoteUpMinusOneWhenVoteTypeIsUp() throws Exception {
        voteCommentService=new VoteCommentService(logger,userRepo,commentRepo,voteCommentRepo);
        int valueOfVoteUp=1;
        Comment comment=new Comment();
        comment.setUpVote(valueOfVoteUp);

        //Comment comment1=voteCommentService.removeCommentVote(comment, VoteType.VOTE_UP);

        //assertEquals(valueOfVoteUp-1,comment1.getUpVote());

    }
    @Test
    public void shouldReturnCommentWithValueOfVoteDownMinusOneWhenVoteTypeIDown() throws Exception {
        voteCommentService=new VoteCommentService(logger,userRepo,commentRepo,voteCommentRepo);
        int valueOfVoteDown=1;
        Comment comment=new Comment();
        comment.setDownVote(valueOfVoteDown);
        //Comment comment1=voteCommentService.removeCommentVote(comment, VoteType.VOTE_DOWN);
        //assertEquals(valueOfVoteDown-1,comment1.getDownVote());

    }
}