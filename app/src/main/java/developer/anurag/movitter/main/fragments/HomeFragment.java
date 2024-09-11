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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.databinding.FragmentHomeBinding;
import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.main.data_models.HomePostsDataModel;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.movitter_api.PostService;
import developer.anurag.movitter.posts_management.PostDetailsActivity;
import developer.anurag.movitter.ui_components.adapters.PostAdapter;


public class HomeFragment extends Fragment implements PostService.PostServiceListener {
    private FragmentHomeBinding binding;
    private HomePostsDataModel homePostsDataModel;
    private PostAdapter postAdapter;
    private PostService postService;
    public static Post updatedPost=null;
    public static int queriedPostIndex=-1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentHomeBinding.inflate(inflater,container,false);
        this.homePostsDataModel=new ViewModelProvider(requireActivity()).get(HomePostsDataModel.class);
        this.postService=new PostService(this);
        this.initPostAdapter();
        this.setFetchedPostsObserver();
        if(this.homePostsDataModel.getHomePosts().isEmpty())this.homePostsDataModel.fetchPosts(0);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity compatActivity= (AppCompatActivity) requireActivity();
        ActionBar actionBar=compatActivity.getSupportActionBar();
        if(actionBar!=null){
            actionBar.show();
        }
        MainActivity mainActivity=(MainActivity) requireActivity();
        mainActivity.showAppbarBottomBorder();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding=null;
    }

    private void setFetchedPostsObserver(){
        this.homePostsDataModel.getFetchedPosts().observe(getViewLifecycleOwner(), posts -> {
            if(posts==null){
                //this.binding.fhPostsFetchingPB.animate().alpha(0).withEndAction(()->this.binding.fhPostsFetchingPB.setVisibility(View.GONE)).setDuration(200).start();
            }else {
                this.postAdapter.notifyItemInserted(this.homePostsDataModel.getHomePosts().size());
            }
        });
    }

    private void initPostAdapter(){
        this.postAdapter=new PostAdapter(requireContext(),this.homePostsDataModel.getHomePosts());
        this.postAdapter.addListener(new PostAdapter.PostAdapterListener() {
            @Override
            public void onPostClick(Post post) {
                PostDetailsActivity.post=post;
                queriedPostIndex=homePostsDataModel.getHomePosts().indexOf(post);
                Intent intent=new Intent(requireContext(),PostDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onPostLike(Post post,String email) {
                post.getLikeIds().add(email);
                postService.updatePost(post);
            }

            @Override
            public void onPostDislike(Post post,String email) {
                post.getLikeIds().remove(email);
                postService.updatePost(post);
            }

            @Override
            public void onPostBookmark(Post post) {

            }
        });
        this.binding.fhPostsContainerRV.setAdapter(this.postAdapter);
    }

    @Override
    public void onPostCreatedSuccess(Post post) {}

    @Override
    public void onPostDeletedSuccess() {}

    @Override
    public void onPostUpdatedSuccess() {}

    @Override
    public void onPostCreatedFailed() {}

    @Override
    public void onPostDeletedFailed() {}

    @Override
    public void onPostUpdatedFailed() {}
}