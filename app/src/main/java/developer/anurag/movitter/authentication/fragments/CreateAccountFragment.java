package developer.anurag.movitter.authentication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.authentication.AuthenticationActivity;
import developer.anurag.movitter.databinding.FragmentCreateAccountBinding;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.movitter_api.MovitterAuthentication;
import developer.anurag.movitter.utils.AuthenticationUtil;
import developer.anurag.movitter.utils.ButtonUtil;
import developer.anurag.movitter.utils.DeviceUtil;
import developer.anurag.movitter.utils.ShowHideUtil;


public class CreateAccountFragment extends Fragment implements MovitterAuthentication.MovitterAuthenticationListener {
    private FragmentCreateAccountBinding binding;
    private List<String> avatarUris;
    private int currentSelectedAvatarIndex=0;
    private MovitterAuthentication movitterAuthentication;
    private String fullname="",selectedAvatarUri="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentCreateAccountBinding.inflate(inflater,container,false);

        this.movitterAuthentication=new MovitterAuthentication(this);

        this.initFullnameETListener();
        ButtonUtil.applyPushEffect(this.binding.fcaContinueBtn);
        this.binding.fcaContinueBtn.setOnClickListener(view-> this.onContinueBtnClick());
        this.showAvatars();
        return this.binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding=null;
    }


    private void initFullnameETListener(){
        this.binding.fcaFullnameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                fullname=s.toString().trim();
                binding.fcaFullnameTIL.setError(null);
            }
        });
    }

    private void onContinueBtnClick(){
        if(this.fullname.isEmpty()){
            this.binding.fcaFullnameTIL.setError(this.requireContext().getString(R.string.fullname_is_required));
            return;
        }
        if(this.selectedAvatarUri.isEmpty()){
            Snackbar.make(this.binding.getRoot(),this.requireContext().getString(R.string.avatar_is_required),Snackbar.LENGTH_SHORT).show();
            return;
        }
        User user=LoginSession.getUserToCreateAccount();
        user.setEmail(LoginSession.getUserToCreateAccount().getEmail());
        user.setFullname(this.fullname);
        user.setAvatarUri(this.selectedAvatarUri);
        user.setMyPostIds(new ArrayList<>());
        user.setMySavedPostIds(new ArrayList<>());
        this.movitterAuthentication.createAccount(user);
        this.binding.fcaContinueBtn.setEnabled(false);
        this.binding.fcaContinueBtn.setText(null);
        this.binding.fcaCreatingAccountPB.setVisibility(View.VISIBLE);
        this.binding.fcaCreatingAccountPB.animate().alpha(1).setDuration(ShowHideUtil.VERY_SHOR_DURATION).start();

    }

    private void showAvatars(){
        int imageSize= DeviceUtil.getDeviceWidth(this.requireContext())/3;
        int padding=DeviceUtil.convertDpToPx(this.requireContext(),15f);
        this.avatarUris= AuthenticationUtil.getAvatarUris();

        for(int i=0;i<avatarUris.size();i++){
            ImageView imageView = new ImageView(this.requireContext());
            imageView.setClickable(true);
            imageView.setFocusable(true);
            imageView.setPadding(padding,padding,padding,padding);
            imageView.setBackgroundResource(R.drawable.bg_secondary_stroked_transparent_clickable);
            imageView.setAlpha(0f);
            GridLayout.LayoutParams params=new GridLayout.LayoutParams();
            imageView.setForegroundGravity(Gravity.CENTER_VERTICAL);
            params.width=imageSize;
            params.height=imageSize;
            Glide.with(this.requireContext()).load(avatarUris.get(i)).override(imageSize).centerCrop().into(imageView);
            imageView.setLayoutParams(params);
            imageView.setOnClickListener(this::onAvatarClick);
            this.binding.fcsAvatarContainerGL.addView(imageView);
            imageView.animate().alpha(1).setDuration(ShowHideUtil.MEDIUM_DURATION).start();
        }
    }

    private void onAvatarClick(View view){
        ImageView previousSelectedAvatar= (ImageView) this.binding.fcsAvatarContainerGL.getChildAt(this.currentSelectedAvatarIndex);
        previousSelectedAvatar.setBackgroundResource(R.drawable.bg_secondary_stroked_transparent_clickable);
        ImageView currentSelectedAvatar= (ImageView) view;
        currentSelectedAvatar.setBackgroundResource(R.drawable.bg_secondary_stroked_transparent);
        this.currentSelectedAvatarIndex=this.binding.fcsAvatarContainerGL.indexOfChild(view);
        this.selectedAvatarUri=this.avatarUris.get(this.currentSelectedAvatarIndex);
    }


    @Override
    public void onCreateAccount(@NonNull User user) {
        SharedPreferences preferences=requireActivity().getSharedPreferences(AuthenticationActivity.AUTHENTICATION_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putString("email",user.getEmail()).apply();
        LoginSession.setLoggedUser(user);
        Intent intent=new Intent(this.requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreateAccountFailed(int reason) {
        Snackbar.make(this.binding.getRoot(),this.requireContext().getString(R.string.something_went_wrong),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailed(int reason) {}

    @Override
    public void onLogin(User user) {}
}