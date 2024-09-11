package developer.anurag.movitter.posts_management;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.databinding.ActivityPostDetailsBinding;
import developer.anurag.movitter.datatypes.Comment;
import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.main.fragments.HomeFragment;
import developer.anurag.movitter.movitter_api.CommentService;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.movitter_api.PostService;
import developer.anurag.movitter.ui_components.adapters.CommentAdapter;
import developer.anurag.movitter.ui_components.adapters.SingleImageAdapter;
import developer.anurag.movitter.utils.ButtonUtil;
import developer.anurag.movitter.utils.DateTimeUtil;
import developer.anurag.movitter.utils.SnackbarUtil;

public class PostDetailsActivity extends AppCompatActivity implements CommentService.CommentServiceListener, PostService.PostServiceListener {
    private ActivityPostDetailsBinding binding;
    public static Post post;
    private User loggedUser;
    private CommentAdapter commentAdapter;
    private List<Comment> comments=new ArrayList<>();
    private CommentDataModel commentDataModel;
    private CommentService commentService;
    private PostService postService;

    private final OnBackPressedCallback backPressedCallback=new OnBackPressedCallback(true) {

        @Override
        public void handleOnBackPressed() {
            HomeFragment.updatedPost=post;
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.binding=ActivityPostDetailsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,0);
            return insets;
        });

        this.getOnBackPressedDispatcher().addCallback(this.backPressedCallback);

        this.loggedUser= LoginSession.getLoggedUser();
        this.commentDataModel=new ViewModelProvider(this).get(CommentDataModel.class);
        this.commentService=new CommentService(this);
        this.postService=new PostService(this);
        this.init();

        setSupportActionBar(this.binding.apdToolbar);
        this.binding.apdToolbar.setNavigationOnClickListener(v -> {
           this.finish();
        });

    }


    private void init(){
        this.setBasicDetails();
        this.setupLikeFunctionality();
        this.setImages();
        this.setupCommentFunctionality();

        ButtonUtil.applyPushEffect(this.binding.adpReplyBtn);
        this.binding.adpReplyBtn.setOnClickListener(view->this.postComment());
    }
    private void setBasicDetails(){
        this.binding.apdMovieTitleTV.setText(post.getTitle());
        this.binding.apdMovieDescriptionTV.setText(post.getDescription());
        this.binding.apdMovieGenresTV.setText(String.join(" | ",post.getGenres()));
        this.binding.apdFullnameTV.setText(post.getUserName());
        this.binding.apdUploadTimeTV.setText(DateTimeUtil.getUploadTime(Long.parseLong(post.getUploadTime())));
        this.binding.apdCommentCB.setText(String.valueOf(post.getCommentIds().size()));
    }

    private void setImages(){
        SingleImageAdapter postImageAdapter=new SingleImageAdapter(this,post.getImageUris());
        this.binding.apdPostImagesVP.setAdapter(postImageAdapter);
        binding.apdImageIndicatorTV.setText(getApplicationContext().getString(R.string.image_indicator,1,post.getImageUris().size()));
        this.binding.apdPostImagesVP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                binding.apdImageIndicatorTV.setText(getApplicationContext().getString(R.string.image_indicator,position+1,post.getImageUris().size()));

            }
        });
    }

    private void setupLikeFunctionality(){
        this.binding.apdLikeCB.setText(String.valueOf(post.getLikeIds().size()));
        this.binding.apdLikeCB.setChecked(post.getLikeIds().contains(loggedUser.getEmail()));

        this.binding.apdLikeCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                post.getLikeIds().add(loggedUser.getEmail());
                this.binding.apdLikeCB.setText(String.valueOf(post.getLikeIds().size()));
            }else{
                post.getLikeIds().remove(loggedUser.getEmail());
                this.binding.apdLikeCB.setText(String.valueOf(post.getLikeIds().size()));
            }
        });
    }

    private void setupCommentFunctionality(){
        this.commentAdapter=new CommentAdapter(this,comments);
        this.binding.apdCommentContainerRV.setAdapter(commentAdapter);

        this.commentDataModel.getPostComments(post.getCommentIds()).observe(this,commentList->{
            this.comments=commentList;
            this.commentAdapter.setComments(commentList);
        });
    }

    private void postComment(){
        Editable editable=this.binding.adpCommentET.getText();
        if(editable!=null){
            String commentText=editable.toString().trim();
            if(!commentText.isEmpty()){
                Comment comment=new Comment();
                comment.setId(String.valueOf(System.currentTimeMillis()));
                comment.setAvatar(this.loggedUser.getAvatarUri());
                comment.setUsername(this.loggedUser.getFullname());
                comment.setEmail(loggedUser.getEmail());
                comment.setDescription(commentText);
                comment.setUploadTime(System.currentTimeMillis());
                this.commentService.postComment(comment);
                this.binding.adpCommentET.setText(null);
            }
        }
    }


    @Override
    public void onPostComment(Comment comment) {
        this.commentAdapter.insertItem(comment);
        post.getCommentIds().add(comment.getId());
        this.postService.updatePost(post);
        this.binding.apdCommentCB.setText(String.valueOf(this.commentAdapter.getItemCount()));
    }

    @Override
    public void onGetComments(List<Comment> comments) {

    }

    @Override
    public void onDeleteComment(Comment comment) {

    }

    @Override
    public void onError(String message) {
        Snackbar.make(this.binding.getRoot(),message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPostCreatedSuccess(Post post) {}

    @Override
    public void onPostDeletedSuccess() {}

    @Override
    public void onPostUpdatedSuccess() {
        this.binding.apdNestedScrollView.smoothScrollTo(0,this.binding.apdNestedScrollView.getChildAt(0).getHeight());
        SnackbarUtil.makeTopSnackbar(this.binding.getRoot(),R.string.comment_posted).show();
    }

    @Override
    public void onPostCreatedFailed() {}

    @Override
    public void onPostDeletedFailed() {}

    @Override
    public void onPostUpdatedFailed() {}
}