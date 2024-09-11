package developer.anurag.movitter.main.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ipsec.ike.SaProposal;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.search.SearchView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.databinding.FragmentSearchBinding;
import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.main.data_models.SearchPostDataModel;
import developer.anurag.movitter.movitter_api.PostRepository;
import developer.anurag.movitter.movitter_api.PostService;
import developer.anurag.movitter.posts_management.PostDetailsActivity;
import developer.anurag.movitter.ui_components.adapters.PostAdapter;
import developer.anurag.movitter.ui_components.adapters.SingleTextAdapter;
import developer.anurag.movitter.ui_components.adapters.TrendingTextAdapter;
import developer.anurag.movitter.utils.DeviceUtil;
import developer.anurag.movitter.utils.ListUtil;
import developer.anurag.movitter.utils.ShowHideUtil;


public class SearchFragment extends Fragment implements PostService.PostServiceListener {
    private FragmentSearchBinding binding;
    private TrendingTextAdapter trendingTextAdapter;
    private SingleTextAdapter autofillHintAdapter;
    private SearchPostDataModel searchPostDataModel;
    private List<String> trendingTextList=new ArrayList<>(),searchAutofillHintList=new ArrayList<>();
    private List<String> allTitles=new ArrayList<>();
    private PostAdapter searchPostAdapter;
    private List<Post> searchedResultPosts=new ArrayList<>();
    private PostService postService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        this.searchPostDataModel=new ViewModelProvider(requireActivity()).get(SearchPostDataModel.class);
        this.postService=new PostService(this);

        this.init();
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity appCompatActivity= (AppCompatActivity) requireActivity();
        ActionBar actionBar=appCompatActivity.getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity=(MainActivity) requireContext();
        mainActivity.hideFAB();
        mainActivity.hideAppbarBottomBorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding=null;
    }

    private void init(){
        this.setupSearchView();
        this.initAutoFillHintAdapter();
        this.initSearchPostAdapter();
        this.setAllPostTitleObserver();
        this.setSearchPostObserver();
    }

    private void setupSearchView(){
        View divider=this.binding.fsSearchPostSV.findViewById(com.google.android.material.R.id.open_search_view_divider);
        divider.setBackgroundColor(Color.TRANSPARENT);

        FrameLayout frameLayout= (FrameLayout) this.binding.fsSearchPostSV.getToolbar().getParent();
        View searchView=LayoutInflater.from(requireContext()).inflate(R.layout.custom_search_view,frameLayout,false);
        frameLayout.removeAllViews();
        frameLayout.addView(searchView);
        searchView.findViewById(R.id.csv_backNavigationIB).setOnClickListener(view->{
            this.binding.fsSearchPostSV.hide();
        });

       EditText editText= searchView.findViewById(R.id.csv_searchEtT);
       editText.requestFocus();
       editText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               String query=s.toString().toLowerCase().trim();
               filterAutoFillHint(query);
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               String query=s.toString().toLowerCase().trim();
               filterAutoFillHint(query);
           }

           @Override
           public void afterTextChanged(Editable s) {
               String query=s.toString().toLowerCase().trim();
               filterAutoFillHint(query);}
       });
       this.binding.fsSearchPostSV.addTransitionListener((sv, previousState, newState) -> {
           if(newState==SearchView.TransitionState.SHOWN){
               editText.requestFocus();
               new Handler().postDelayed(() -> {
                   InputMethodManager imm = getSystemService(this.requireContext(), InputMethodManager.class);
                   if(imm!=null)imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
               }, 100);

           }else {
               editText.clearFocus();
               new Handler().postDelayed(() -> {
                   InputMethodManager imm = getSystemService(this.requireContext(), InputMethodManager.class);
                   if(imm!=null)imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
               }, 100);
           }
       });
    }

    private void filterAutoFillHint(String query){
//        if(this.binding.fsAutoFillHintContainerRV.getVisibility()==View.GONE){
//            this.binding.fsAutoFillHintContainerRV.setVisibility(View.VISIBLE);
//            this.binding.fsAutoFillHintContainerRV.animate().alpha(1).setDuration(ShowHideUtil.SHORT_DURATION).start();
//        }
        this.binding.fsSearchedResultsContainerRV.setVisibility(View.GONE);
        if(query.isEmpty()){
            this.searchAutofillHintList.clear();
            this.autofillHintAdapter.notifyItemRangeChanged(0,this.searchAutofillHintList.size());
            return;
        }
        this.searchAutofillHintList=this.allTitles.stream().filter(s->s.toLowerCase().contains(query)).collect(Collectors.toList());
        this.autofillHintAdapter.setTitles(this.searchAutofillHintList);
    }

    private void initAutoFillHintAdapter(){
        autofillHintAdapter=new SingleTextAdapter(requireContext(),allTitles);
        this.autofillHintAdapter.addListener(title -> {
            int size=this.searchAutofillHintList.size();
           this.searchAutofillHintList.clear();
           this.autofillHintAdapter.notifyItemRangeChanged(0,size);
           this.searchPostDataModel.searchPost(title);
        });
        this.binding.fsAutoFillHintContainerRV.setAdapter(autofillHintAdapter);
    }

    private void initSearchPostAdapter(){
        this.searchPostAdapter=new PostAdapter(requireContext(),this.searchedResultPosts);
        this.searchPostAdapter.addListener(new PostAdapter.PostAdapterListener() {
            @Override
            public void onPostClick(Post post) {
                PostDetailsActivity.post=post;
                Intent intent=new Intent(requireContext(), PostDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onPostLike(Post post, String email) {
                post.getLikeIds().add(email);
            }

            @Override
            public void onPostDislike(Post post, String email) {
                post.getLikeIds().remove(email);
            }

            @Override
            public void onPostBookmark(Post post) {

            }
        });
        this.binding.fsSearchedResultsContainerRV.setAdapter(this.searchPostAdapter);
    }

    private void setAllPostTitleObserver(){
        this.searchPostDataModel.getAllPostsTitleList().observe(requireActivity(),list->{
            this.allTitles=list;
        });
    }

    private void setSearchPostObserver(){
        this.searchPostDataModel.getSearchResults().observe(requireActivity(),posts->{
//            Toast.makeText(requireContext(), "size is:- "+list.size(), Toast.LENGTH_SHORT).show();
            this.searchPostAdapter.setPosts(posts);
            if(this.binding!=null) {
                this.binding.fsSearchedResultsContainerRV.setVisibility(View.VISIBLE);
                this.binding.fsSearchedResultsContainerRV.animate().alpha(1).setDuration(ShowHideUtil.SHORT_DURATION).start();
            }
        });
    }


    @Override
    public void onPostCreatedSuccess(Post post) {

    }

    @Override
    public void onPostDeletedSuccess() {

    }

    @Override
    public void onPostUpdatedSuccess() {

    }

    @Override
    public void onPostCreatedFailed() {

    }

    @Override
    public void onPostDeletedFailed() {

    }

    @Override
    public void onPostUpdatedFailed() {

    }
}