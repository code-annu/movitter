package developer.anurag.movitter.movitter_api;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.stream.Collectors;

import developer.anurag.movitter.datatypes.Post;

public class PostRepository {
    public interface PostRepositoryListener{
        void onGetPosts(@Nullable List<Post> posts);
        void onGetSearchedPosts(List<Post> posts);
        void onGetAllPostsTitle(List<String> titles);
    }

    private final CollectionReference postsCollection;
    private final PostRepositoryListener listener;

    public PostRepository(PostRepositoryListener listener) {
        this.postsCollection = FirebaseFirestore.getInstance().collection("posts_data");
        this.listener = listener;
    }

    public void getPosts(int from){
        this.postsCollection.orderBy("id", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents=task.getResult().getDocuments();
                if(from==documents.size()){
                    this.listener.onGetPosts(null);
                    return;
                }
                int to=from+20;
                if(to>documents.size()){
                    to=documents.size();
                }
                List<Post> posts=documents.subList(from,to).stream().map(doc->doc.toObject(Post.class)).collect(Collectors.toList());
                this.listener.onGetPosts(posts);
            }
        });
    }

    public void searchPosts(String query){
        this.postsCollection.whereEqualTo("title",query).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents=task.getResult().getDocuments();
                List<Post> posts=documents.stream().map(doc->doc.toObject(Post.class)).collect(Collectors.toList());
                this.listener.onGetSearchedPosts(posts);
            }
        });
    }

    public void getAllPostsTitle(){
        this.postsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents=task.getResult().getDocuments();
                List<String> titles=documents.stream().map(doc-> doc.getString("title")).collect(Collectors.toList());
                this.listener.onGetAllPostsTitle(titles);
            }
        });
    }


}
