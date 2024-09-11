package developer.anurag.movitter.authentication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.authentication.AuthenticationActivity;
import developer.anurag.movitter.databinding.FragmentSearchBinding;
import developer.anurag.movitter.databinding.FragmentSplashBinding;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.movitter_api.MovitterAuthentication;
import developer.anurag.movitter.posts_management.CreatePostActivity;


public class SplashFragment extends Fragment implements MovitterAuthentication.MovitterAuthenticationListener {
    private FragmentSplashBinding binding;
    private MovitterAuthentication movitterAuthentication;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);

        movitterAuthentication = new MovitterAuthentication(this);

        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences= requireActivity().getSharedPreferences(AuthenticationActivity.AUTHENTICATION_PREFERENCES, Context.MODE_PRIVATE);
        String email=preferences.getString("email",null);
        if(email!=null){
            movitterAuthentication.loginUser(email);
        }else {
            NavOptions navOptions=new NavOptions.Builder().setPopUpTo(R.id.navigation_splash,true).build();
            Navigation.findNavController(view).navigate(R.id.action_navigation_splash_to_navigation_signupLogin,null,navOptions);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding=null;
    }

    @Override
    public void onCreateAccount(@NonNull User user) {}

    @Override
    public void onCreateAccountFailed(int reason) {}

    @Override
    public void onLoginFailed(int reason) {
        NavOptions navOptions=new NavOptions.Builder().setPopUpTo(R.id.navigation_splash,true).build();
        Navigation.findNavController(requireView()).navigate(R.id.action_navigation_splash_to_navigation_signupLogin,null,navOptions);
    }

    @Override
    public void onLogin(User user) {
        LoginSession.setLoggedUser(user);
        Intent intent=new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}