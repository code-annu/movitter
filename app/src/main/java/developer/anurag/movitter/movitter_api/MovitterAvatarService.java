package developer.anurag.movitter.movitter_api;

import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MovitterAvatarService {
    public interface MovitterAvatarServiceListener{
        void onGetAllAvatars(List<String> avatars);
        void onGetAllAvatarsFailed(int reason);
        void onGetAvatar(int id);
        void onGetAvatarFailed(int reason);
    }

    private StorageReference avatarStorage;
    private MovitterAvatarServiceListener listener;

    public MovitterAvatarService(@NonNull MovitterAvatarServiceListener listener){
        this.avatarStorage= FirebaseStorage.getInstance().getReference().child("avatars");
        this.listener=listener;
    }

    public void getAllAvatars(){
        this.avatarStorage.listAll().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                List<StorageReference> items=task.getResult().getItems();
                List<String> avatarUris=new ArrayList<>();
                for(StorageReference item:items){
                    avatarUris.add(item.getDownloadUrl().toString());
                }
                this.listener.onGetAllAvatars(avatarUris);

            }
        });
    }






}
