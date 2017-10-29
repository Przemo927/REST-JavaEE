package pl.przemek.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.przemek.model.*;
import pl.przemek.repository.JpaCommentRepository;
import pl.przemek.repository.JpaUserRepository;
import pl.przemek.repository.JpaVoteCommentRepository;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class VoteCommentServiceTest {

    private JpaUserRepository userRepo;
    private JpaCommentRepository commentRepo;
    private JpaVoteCommentRepository voteCommentRepo;
    private VoteCommentService voteCommentService;

    @Before
    public void setUp() throws Exception {
        userRepo=mock(JpaUserRepository.class);
        commentRepo=mock(JpaCommentRepository.class);
        voteCommentRepo=mock(JpaVoteCommentRepository.class);
    }

    @Test
    public void shouldReturnVoteWithAssignVoteTypeAndUserAndComment() throws Exception {
        voteCommentService=new VoteCommentService(userRepo,commentRepo,voteCommentRepo);
        User user=mock(User.class);
        Comment comment=mock(Comment.class);

        when(userRepo.get(anyLong())).thenReturn(user);
        when(commentRepo.get(anyLong())).thenReturn(comment);
        VoteComment voteComment=voteCommentService.createVote(1,1, VoteType.VOTE_UP);

        assertEquals(user, voteComment.getUser());
        assertEquals(comment,voteComment.getComment());
        assertEquals(VoteType.VOTE_UP,voteComment.getVoteType());
    }

    @Test
    public void shouldAddNewVoteAndUpdateCommentWhenOldVoteIsNotExist(int userId,int commentId,VoteType voteType) throws Exception {
        VoteComment vote=mock(VoteComment.class);
        VoteCommentService voteCommentService=spy(new VoteCommentService(userRepo,commentRepo,voteCommentRepo));

        when(voteCommentRepo.getVoteByUserIdCommentId(anyLong(),anyLong())).thenReturn(null);
        doReturn(vote).when(voteCommentService).createVote(anyLong(),anyLong(),isA(VoteType.class));
        doNothing().when(voteCommentService).updateComment(anyLong(),eq(null),isA(VoteComment.class));
        voteCommentService.updateVote(userId,commentId,voteType);

        verify(voteCommentRepo).add(vote);
        verify(voteCommentService).updateComment(commentId,null,vote);
    }

    @Test
    public void shouldNotUpdateVoteAndCommentWhenOldVoteAndNewVoteIsEqual() throws Exception{
        VoteCommentService voteCommentService=spy(new VoteCommentService(userRepo,commentRepo,voteCommentRepo));
        VoteComment existingVote=new VoteComment();
        existingVote.setVoteType(VoteType.VOTE_UP);
        VoteComment updateVote=new VoteComment();

        when(voteCommentRepo.getVoteByUserIdCommentId(anyLong(),anyLong())).thenReturn(existingVote);
        when(voteCommentService.createVote(existingVote)).thenReturn(updateVote);
        doNothing().when(voteCommentService).updateComment(anyLong(),isA(VoteComment.class),isA(VoteComment.class));
        voteCommentService.updateVote(1,2,VoteType.VOTE_UP);

        assertTrue(existingVote.equals(updateVote));
        verify(voteCommentRepo,never()).update(isA(VoteComment.class));
        verify(voteCommentService,never()).updateComment(2,existingVote,updateVote);
    }

    @Test
    public void shouldUpdateVoteAndCommentWhenOldVoteAndNewVoteIsNotEqual() throws Exception{
        VoteCommentService voteCommentService=spy(new VoteCommentService(userRepo,commentRepo,voteCommentRepo));
        VoteComment existingVote=new VoteComment();
        existingVote.setVoteType(VoteType.VOTE_DOWN);
        VoteComment updateVote=new VoteComment();

        when(voteCommentRepo.getVoteByUserIdCommentId(anyLong(),anyLong())).thenReturn(existingVote);
        when(voteCommentService.createVote(existingVote)).thenReturn(updateVote);
        doNothing().when(voteCommentService).updateComment(anyLong(),isA(VoteComment.class),isA(VoteComment.class));
        voteCommentService.updateVote(1,2,VoteType.VOTE_UP);

        assertTrue(!existingVote.equals(updateVote));
        verify(voteCommentRepo).update(updateVote);
        verify(voteCommentService).updateComment(2,existingVote,updateVote);
    }

    @Test
    public void shouldExecuteAddCommentVoteAndUpdateCommentWhenOldVoteIsNotExist() throws Exception {

        VoteCommentService voteCommentService=spy(new VoteCommentService(userRepo,commentRepo,voteCommentRepo));
        VoteComment updateVote=mock(VoteComment.class);
        Comment comment=mock(Comment.class);
        Comment updateComment=mock(Comment.class);

        when(commentRepo.get(anyLong())).thenReturn(comment);
        doReturn(updateComment).when(voteCommentService).createComment(isA(Comment.class));
        voteCommentService.updateComment(1,null,updateVote);

        verify(voteCommentService).addCommentVote(comment,updateVote.getVoteType());
        assertEquals(updateComment,voteCommentService.addCommentVote(comment,updateVote.getVoteType()));
        verify(commentRepo).update(updateComment);

    }
    @Test
    public void shouldExecuteRemoveDiscoveryVoteAddCommentVoteAndUpdateCommentWhenOldVoteIsExist() throws Exception {

        VoteCommentService voteCommentService=spy(new VoteCommentService(userRepo,commentRepo,voteCommentRepo));
        VoteComment oldVote=mock(VoteComment.class);
        VoteComment updateVote=mock(VoteComment.class);
        Comment comment=mock(Comment.class);
        Comment comment1=mock(Comment.class);


        when(commentRepo.get(anyLong())).thenReturn(comment);
        doReturn(comment1).when(voteCommentService).createComment(isA(Comment.class));
        voteCommentService.updateComment(1,oldVote,updateVote);

        verify(voteCommentService).removeCommentVote(comment,oldVote.getVoteType());
        verify(voteCommentService).addCommentVote(comment1,updateVote.getVoteType());
        verify(commentRepo).update(comment1);

    }
    @Test
    public void shouldDoNothingWhenNewVoteAndOldVoteIsNull() throws Exception{
        VoteCommentService voteCommentService=spy(new VoteCommentService(userRepo,commentRepo,voteCommentRepo));
        Comment comment=mock(Comment.class);

        when(commentRepo.get(anyLong())).thenReturn(comment);
        voteCommentService.updateComment(1,null,null);

        verify(commentRepo).get(1);
        verify(voteCommentService,never()).removeCommentVote(isA(Comment.class),isA(VoteType.class));
        verify(voteCommentService,never()).addCommentVote(isA(Comment.class),isA(VoteType.class));
        verify(commentRepo,never()).update(isA(Comment.class));
    }
    @Test
    public void shouldReturnCommentWithValueOfVoteUpPlusOneWhenNewVoteIsVoteUp(){
        int valueOfVoteUp=0;
        Comment comment=new Comment();
        comment.setUpVote(valueOfVoteUp);

        Comment comment1=voteCommentService.addCommentVote(comment,VoteType.VOTE_UP);

        assertEquals(valueOfVoteUp+1,comment1.getUpVote());

    }
    @Test
    public void shouldReturnCommentWithValueOfVoteDownPlusOneWhenNewVoteIsVoteDown() throws Exception{
        int valueOfVoteDown=0;
        Comment comment=new Comment();
        comment.setDownVote(valueOfVoteDown);

        Comment comment1=voteCommentService.addCommentVote(comment,VoteType.VOTE_DOWN);

        assertEquals(valueOfVoteDown+1,comment1.getDownVote());

    }
    @Test
    public void shouldReturnCommentWithValueOfVoteUpMinusOneWhenVoteTypeIsUp() throws Exception {
        int valueOfVoteUp=1;
        Comment comment=new Comment();
        comment.setUpVote(valueOfVoteUp);

        Comment comment1=voteCommentService.removeCommentVote(comment,VoteType.VOTE_UP);

        assertEquals(valueOfVoteUp-1,comment1.getUpVote());

    }
    @Test
    public void shouldReturnCommentWithValueOfVoteDownMinusOneWhenVoteTypeIDown() throws Exception {
        int valueOfVoteDown=1;
        Comment comment=new Comment();
        comment.setDownVote(valueOfVoteDown);
        Comment comment1=voteCommentService.removeCommentVote(comment,VoteType.VOTE_DOWN);
        assertEquals(valueOfVoteDown-1,comment1.getDownVote());

    }
}