package pl.przemek.repository.inMemoryRepository;

import pl.przemek.model.Comment;
import pl.przemek.model.Discovery;
import pl.przemek.model.User;
import pl.przemek.repository.JpaCommentRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class JpaCommentRepositoryInMemoryImpl implements JpaCommentRepository {

    private List<Comment> listOfComments;

    public JpaCommentRepositoryInMemoryImpl(){
        listOfComments=new ArrayList<>();
        populateCommentList();
    }
    @Override
    public void add(Comment commment) {
        listOfComments.add(commment);
    }

    @Override
    public void remove(Comment comment) {
        listOfComments.remove(comment);
    }

    @Override
    public void removeByDiscoveryId(long discoveryId) {
        Comment comment;
        for(int i=0;i<listOfComments.size();i++){
            comment=listOfComments.get(i);
            if(comment.getDiscovery().getId()==discoveryId){
                listOfComments.remove(comment);
            }
        }
    }

    @Override
    public Comment update(Comment comment) {
        for(int i=0;i<listOfComments.size();i++){
            if(comment.getId()==listOfComments.get(i).getId()){
                listOfComments.set(i,comment);
            }
        }
        return comment;
    }

    @Override
    public List<Comment> getAll(String nameOfQuery, Class<Comment> clazz) {
        return listOfComments;
    }

    @Override
    public List<Comment> getByDiscoveryName(String name) {
        List<Comment> temporaryList=new ArrayList<>();
        listOfComments.forEach(comment -> {
            if(comment.getDiscovery().getName().equals(name))
                temporaryList.add(comment);
        });
        return temporaryList;
    }

    @Override
    public List<Comment> getByDiscoveryId(long id) {
        List<Comment> temporaryList=new ArrayList<>();
        listOfComments.forEach(comment -> {
            if(comment.getDiscovery().getId()==id)
                temporaryList.add(comment);
        });
        return temporaryList;
    }

    @Override
    public Comment get(Class<Comment> clazz, long id) {
        for (Comment listOfComment : listOfComments) {
            if (listOfComment.getId() == id)
                return listOfComment;
        }
        return null;
    }
    private void populateCommentList(){
        Comment comment;
        for(int i=0;i<10;i++){
            comment=new Comment();
            comment.setId(i);
            comment.setDownVote(i+ new Random().nextInt(5));
            comment.setUpVote(i+ new Random().nextInt(5));
            comment.setComment("comment"+i);
            comment.setDiscovery(new Discovery());
            comment.setUser(new User());
            listOfComments.add(comment);
        }
    }
}
