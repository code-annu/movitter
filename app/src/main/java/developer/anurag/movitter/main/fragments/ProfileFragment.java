package developer.anurag.movitter.main.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.databinding.FragmentProfileBinding;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.movitter_api.LoginSession;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentProfileBinding.inflate(inflater,container,false);
        initView();
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity= (AppCompatActivity) requireActivity();
        ActionBar actionBar=activity.getSupportActionBar();
        if(actionBar!=null)actionBar.hide();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity=(MainActivity) requireContext();
        mainActivity.hideFAB();
        mainActivity.hideAppbarBottomBorder();
    }

    private void initView(){
        User user= LoginSession.getLoggedUser();
        Glide.with(requireContext()).load(user.getAvatarUri()).into(this.binding.fpAvatarIV);
        this.binding.fpNameTV.setText(user.getFullname());
    }
}