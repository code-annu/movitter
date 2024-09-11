package developer.anurag.movitter.authentication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.databinding.FragmentSignupLoginBinding;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.movitter_api.MovitterAuthentication;
import developer.anurag.movitter.ui_components.adapters.SingleRoundedImageAdapter;
import developer.anurag.movitter.utils.AuthenticationUtil;
import developer.anurag.movitter.utils.ButtonUtil;
import developer.anurag.movitter.utils.ShowHideUtil;


public class SignupLoginFragment extends Fragment implements MovitterAuthentication.MovitterAuthenticationListener {
    private FragmentSignupLoginBinding binding;
    private List<String> houseOfDragonImageUris;
    private List<String> breakingBadImageUris;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private MovitterAuthentication movitterAuthentication;
    private String email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupLoginBinding.inflate(inflater, container, false);
        this.initImageSlideShow();
        this.initGoogleSignInLauncher();
        ButtonUtil.applyPushEffect(this.binding.fslContinueWithGoogleBtn);
        this.binding.fslContinueWithGoogleBtn.setOnClickListener(view-> this.onContinueWithGoogleBtnClick());

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        this.googleSignInClient= GoogleSignIn.getClient(requireContext(), googleSignInOptions);
        this.movitterAuthentication=new MovitterAuthentication(this);

        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void initGoogleSignInLauncher(){
        // init launcher
        if(this.googleSignInLauncher==null){
            this.googleSignInLauncher=this.requireActivity().registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
                if(result.getResultCode()== AppCompatActivity.RESULT_OK){
                    Intent data=result.getData();
                    if(data!=null){
                        Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            this.email=task.getResult(ApiException.class).getEmail();
                            this.binding.fslContinueWithGoogleBtn.setEnabled(false);
                            ShowHideUtil.showViewWithFadeIn(this.binding.fslMainCoverFL,ShowHideUtil.SHORT_DURATION);
                            this.movitterAuthentication.loginUser(email);
                        } catch (ApiException e) {
                            Snackbar.make(this.binding.getRoot(),"Something went wrong",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding=null;
    }

    private void initImageSlideShow(){
        SingleRoundedImageAdapter adapter = new SingleRoundedImageAdapter(this.getContext(), AuthenticationUtil.getImageUris());
        this.binding.fslImageSlideShowVP.setAdapter(adapter);
        this.binding.fslImageSlideShowVP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                updateActiveIndicator(position);
            }
        });
    }

    private void updateActiveIndicator(int index){
        if(index==0){
            this.binding.fslImageSlideShowIndicator1.setBackgroundResource(R.drawable.secondary_circle);
            this.binding.fslImageSlideShowIndicator2.setBackgroundResource(R.drawable.helper_circle);
            this.binding.fslImageSlideShowIndicator3.setBackgroundResource(R.drawable.helper_circle);
        }
        else if(index==1){
            this.binding.fslImageSlideShowIndicator1.setBackgroundResource(R.drawable.helper_circle);
            this.binding.fslImageSlideShowIndicator2.setBackgroundResource(R.drawable.secondary_circle);
            this.binding.fslImageSlideShowIndicator3.setBackgroundResource(R.drawable.helper_circle);
        } else if (index==2) {
            this.binding.fslImageSlideShowIndicator1.setBackgroundResource(R.drawable.helper_circle);
            this.binding.fslImageSlideShowIndicator2.setBackgroundResource(R.drawable.helper_circle);
            this.binding.fslImageSlideShowIndicator3.setBackgroundResource(R.drawable.secondary_circle);
        }
    }

    private void onContinueWithGoogleBtnClick(){
        Intent signInIntent=this.googleSignInClient.getSignInIntent();
        this.googleSignInLauncher.launch(signInIntent);

    }


    @Override
    public void onCreateAccount(@NonNull User user) {}

    @Override
    public void onCreateAccountFailed(int reason) {}

    @Override
    public void onLoginFailed(int reason) {
        if(reason==MovitterAuthentication.USER_NOT_FOUND){
            User user=new User();
            user.setEmail(this.email);
            LoginSession.setUserToCreateAccount(user);
            NavOptions navOptions=new NavOptions.Builder().setPopUpTo(R.id.navigation_signupLogin,true).build();
            Navigation.findNavController(requireActivity(),R.id.aa_navHostFragmentView).navigate(R.id.action_navigation_signupLogin_to_navigation_createAccount,null,navOptions);
        }else {
            Snackbar.make(this.binding.getRoot(),"Something went wrong: "+reason,Snackbar.LENGTH_SHORT).show();
            this.binding.fslContinueWithGoogleBtn.setEnabled(true);
            ShowHideUtil.hideViewWithFadeOut(this.binding.fslMainCoverFL,ShowHideUtil.SHORT_DURATION);
        }
    }

    @Override
    public void onLogin(User user) {
        LoginSession.setLoggedUser(user);
        Intent intent=new Intent(this.requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}