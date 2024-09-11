package developer.anurag.movitter.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.databinding.FragmentMyPostsBinding;
import developer.anurag.movitter.main.data_models.MyPostsDataModel;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.posts_management.CreatePostActivity;
import developer.anurag.movitter.ui_components.adapters.PostAdapter;


public class MyPostsFragment extends Fragment {
    private FragmentMyPostsBinding binding;
    private MyPostsDataModel myPostsDataModel;
    private PostAdapter myPostsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentMyPostsBinding.inflate(inflater,container,false);
        this.myPostsDataModel=new ViewModelProvider(requireActivity()).get(MyPostsDataModel.class);

        this.hideActionBar();
        this.initMyPostsAdapter();

        return this.binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity=(MainActivity) requireContext();
        mainActivity.showFAB();
        mainActivity.hideAppbarBottomBorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding=null;
    }

    private void hideActionBar(){
        AppCompatActivity activity=(AppCompatActivity) this.requireActivity();
        ActionBar actionBar=activity.getSupportActionBar();
        if(actionBar!=null)actionBar.hide();
    }

    private void startCreatePostActivity(){
        Intent intent=new Intent(this.requireContext(), CreatePostActivity.class);
        this.startActivity(intent);
    }

    private void initMyPostsAdapter(){
        this.myPostsAdapter=new PostAdapter(requireContext(),new ArrayList<>());
        this.binding.fmpPostsContainerRV.setAdapter(this.myPostsAdapter);
        this.myPostsDataModel.getPostsOfIds(LoginSession.getLoggedUser().getMyPostIds());

        this.myPostsDataModel.getMyPosts().observe(requireActivity(),posts -> {
            this.myPostsAdapter.setPosts(posts);
        });

    }
}