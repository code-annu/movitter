package developer.anurag.movitter.movitter_api;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import developer.anurag.movitter.datatypes.User;

public class MovitterAuthentication {
    public static final int SOMETHING_WENT_WRONG=101;
    public static final int USER_NOT_FOUND=102;

    public interface MovitterAuthenticationListener{
        void onCreateAccount(@NonNull User user);
        void onCreateAccountFailed(int reason);
        void onLoginFailed(int reason);
        void onLogin(User user);
    }

    final private CollectionReference userDataCollection;
    final private MovitterAuthenticationListener listener;

    public MovitterAuthentication(@NonNull MovitterAuthenticationListener listener) {
        this.listener = listener;
        this.userDataCollection= FirebaseFirestore.getInstance().collection("users_data");
    }

    public void createAccount(User user){
        this.userDataCollection.document(user.getEmail()).set(user).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                this.listener.onCreateAccount(user);
            }else{
                this.listener.onCreateAccountFailed(SOMETHING_WENT_WRONG);
            }
        });
    }

    public void loginUser(String email){
        this.userDataCollection.document(email).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if (task.getResult().exists()) {
                    User user = task.getResult().toObject(User.class);
                    if(user!=null){
                        this.listener.onLogin(user);
                    }else {
                        this.listener.onLoginFailed(SOMETHING_WENT_WRONG);
                    }
                }else {
                    this.listener.onLoginFailed(USER_NOT_FOUND);
                }
            }else {
                this.listener.onLoginFailed(SOMETHING_WENT_WRONG);
            }
        });
    }


}
