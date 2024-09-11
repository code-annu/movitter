package developer.anurag.movitter.movitter_api;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.movitter.datatypes.Comment;

public class CommentService {
    public interface CommentServiceListener{
        void onPostComment(Comment comment);
        void onGetComments(List<Comment> comments);
        void onDeleteComment(Comment comment);
        void onError(String message);
    }

    private final CommentServiceListener listener;
    private final CollectionReference commentCollection;

    public CommentService(@NonNull CommentServiceListener listener) {
        this.listener = listener;
        this.commentCollection = FirebaseFirestore.getInstance().collection("comments_data");
    }

    public void postComment(Comment comment){
        this.commentCollection.document(comment.getId()).set(comment).addOnCompleteListener(task -> {
            if(task.isSuccessful())this.listener.onPostComment(comment);
            else this.listener.onError("Error while posting comment");
        });
    }

    public void getComments(List<String> commentIds){
        this.commentCollection.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Comment> comments=new ArrayList<>();
                List<Comment> allComments=task.getResult().toObjects(Comment.class);
                for(String commentId:commentIds){
                    for(Comment comment:allComments) {
                        if (comment.getId().equals(commentId)) {
                            comments.add(comment);
                            break;
                        }
                    }
                }
                this.listener.onGetComments(comments);
            }else this.listener.onError("Failed to get comments");
        });
    }



}
