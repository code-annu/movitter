package developer.anurag.movitter.posts_management;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import developer.anurag.movitter.datatypes.Comment;
import developer.anurag.movitter.movitter_api.CommentService;

public class CommentDataModel extends ViewModel implements CommentService.CommentServiceListener {
    private MutableLiveData<List<Comment>> postComments=new MutableLiveData<>();
    private final CommentService commentService;

    public CommentDataModel(){
        this.commentService=new CommentService(this);
    }

    public MutableLiveData<List<Comment>> getPostComments(List<String> commentIds) {
        this.commentService.getComments(commentIds);
        return postComments;
    }

    @Override
    public void onPostComment(Comment comment) {

    }

    @Override
    public void onGetComments(List<Comment> comments) {
        this.postComments.setValue(comments);
    }

    @Override
    public void onDeleteComment(Comment comment) {

    }

    @Override
    public void onError(String message) {

    }
}
