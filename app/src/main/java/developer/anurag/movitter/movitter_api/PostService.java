package developer.anurag.movitter.movitter_api;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.movitter.datatypes.Post;

public class PostService {
    public interface PostServiceListener{
        void onPostCreatedSuccess(Post post);
        void onPostDeletedSuccess();
        void onPostUpdatedSuccess();
        void onPostCreatedFailed();
        void onPostDeletedFailed();
        void onPostUpdatedFailed();

    }

    private List<String> photosUploadedUriList=new ArrayList<>();
    private StorageReference photosStorage;
    private CollectionReference postsDataCollection;
    private final PostServiceListener listener;
    private Post postToUpload;

    public PostService(PostServiceListener listener) {
        this.photosStorage = FirebaseStorage.getInstance().getReference("photos");
        this.postsDataCollection = FirebaseFirestore.getInstance().collection("posts_data");
        this.listener = listener;
    }

    private void uploadPhotos(List<Uri> photosUris){
        if(photosUris.isEmpty()){
            this.postToUpload.setImageUris(this.photosUploadedUriList);
            this.uploadPost();
            return;
        }
        Uri photoUri=photosUris.remove(0);
        this.photosStorage.child(this.postToUpload.getId()+photosUris.size()).putFile(photoUri).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    this.photosUploadedUriList.add(downloadUri.toString());
                    this.uploadPhotos(photosUris);
                });
            }
        });
    }

    public void createPost(Post post,List<Uri> photosUris){
        this.postToUpload=post;
        this.uploadPhotos(photosUris);
    }

    private void uploadPost(){
        this.postsDataCollection.document(this.postToUpload.getId()).set(this.postToUpload).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               this.listener.onPostCreatedSuccess(postToUpload);
           }else{
               this.listener.onPostCreatedFailed();
           }
        });
    }

    public void updatePost(Post post){
        this.postsDataCollection.document(post.getId()).set(post).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                this.listener.onPostUpdatedSuccess();
            }else{
                this.listener.onPostUpdatedFailed();
            }
        });
    }



}
