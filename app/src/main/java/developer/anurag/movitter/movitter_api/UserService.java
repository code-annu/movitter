package developer.anurag.movitter.movitter_api;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import developer.anurag.movitter.datatypes.User;

public class UserService {
    public interface UserServiceListener {
        void onUpdateUserSuccess(User user);
        void onUpdateUserFailed();
    }

    private UserServiceListener listener;
    private CollectionReference usersDataCollection;

    public UserService(UserServiceListener listener) {
        this.listener = listener;
        this.usersDataCollection = FirebaseFirestore.getInstance().collection("users_data");
    }

    public void updateUser(User user) {
        usersDataCollection.document(user.getEmail()).set(user).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               this.listener.onUpdateUserSuccess(user);
           }else {
               this.listener.onUpdateUserFailed();
           }
        });
    }

}
