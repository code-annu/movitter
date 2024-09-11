package developer.anurag.movitter.movitter_api;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import developer.anurag.movitter.datatypes.Post;

public class UserPostService {
    public interface UserPostServiceListener{
        void onGetMyPosts(List<Post> posts);
        void onError(String message);
    }

    final private UserPostServiceListener listener;
    private CollectionReference postsCollection;

    public UserPostService(UserPostServiceListener listener) {
        this.listener = listener;
        this.postsCollection = FirebaseFirestore.getInstance().collection("posts_data");
    }


    public void getMyPosts(List<String> postIds){
        this.postsCollection.whereIn("id",postIds).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Post> posts=task.getResult().toObjects(Post.class);
                this.listener.onGetMyPosts(posts);
            }else{
                this.listener.onError("Error getting posts");
            }
        });
    }
}
