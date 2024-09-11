package developer.anurag.movitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;

import developer.anurag.movitter.databinding.ActivityMainBinding;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.posts_management.CreatePostActivity;
import developer.anurag.movitter.utils.DateTimeUtil;
import developer.anurag.movitter.utils.DeviceUtil;

public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,0);
            return insets;
        });

        setSupportActionBar(this.binding.amToolbar);
        NavController navController= Navigation.findNavController(this,R.id.am_navHostFragmentView);
        NavigationUI.setupWithNavController(this.binding.amBottomNavigationView,navController);

        this.binding.amCreatePostFAB.setOnClickListener(view->this.startCreatePostActivity());
        this.setUserProfile();
    }

    private void setUserProfile(){
        User loggedUser= LoginSession.getLoggedUser();
        Glide.with(this).load(loggedUser.getAvatarUri()).override(DeviceUtil.convertDpToPx(this,50f)).into(this.binding.amAvatarIV);
        this.binding.amHelloUserTV.setText(getString(R.string.hello_user,loggedUser.getFullname().split(" ")[0]));
        this.binding.amGreetingTV.setText(DateTimeUtil.getGreeting());
    }

    private void startCreatePostActivity(){
        Intent intent=new Intent(this, CreatePostActivity.class);
        this.startActivity(intent);
    }

    public void hideFAB(){
        this.binding.amCreatePostFAB.hide();
    }

    public void showFAB(){
        this.binding.amCreatePostFAB.show();
    }

    public void hideAppbarBottomBorder(){
        this.binding.amToolbarBottomBorder.setVisibility(View.GONE);
    }

    public void showAppbarBottomBorder(){
        try{
            this.binding.amToolbarBottomBorder.setVisibility(View.VISIBLE);
        }catch (NullPointerException ignored){}
    }


}