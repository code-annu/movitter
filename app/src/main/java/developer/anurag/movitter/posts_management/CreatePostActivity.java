package developer.anurag.movitter.posts_management;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import developer.anurag.movitter.MainActivity;
import developer.anurag.movitter.R;
import developer.anurag.movitter.databinding.ActivityCreatePostBinding;
import developer.anurag.movitter.datatypes.Genre;
import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.movitter_api.PostService;
import developer.anurag.movitter.movitter_api.UserService;
import developer.anurag.movitter.ui_components.adapters.SingleRemovableImageAdapter;
import developer.anurag.movitter.ui_components.bottom_sheet_fragments.AskYesNoFragment;
import developer.anurag.movitter.utils.AuthenticationUtil;
import developer.anurag.movitter.utils.ButtonUtil;
import developer.anurag.movitter.utils.DeviceUtil;
import developer.anurag.movitter.utils.SnackbarUtil;

public class CreatePostActivity extends AppCompatActivity implements PostService.PostServiceListener, UserService.UserServiceListener, AskYesNoFragment.AskYesNoFragmentListener {
    private ActivityCreatePostBinding binding;
    private String title="",description="";
    private final List<Uri> photosLocalUris=new ArrayList<>();
    private final Set<String> selectedGenreSet=new HashSet<>();
    private SingleRemovableImageAdapter imagesAdapter;
    private boolean creatingPost=false;
    private final ActivityResultLauncher<Intent> pickImagesLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
        if(result.getResultCode()==RESULT_OK){
            Intent data=result.getData();
            if(data!=null){
                ClipData clipData=data.getClipData();
                if(clipData!=null){
                    int count=clipData.getItemCount();
                    for(int i=0;i<count;i++){
                        Uri uri=clipData.getItemAt(i).getUri();
                        this.photosLocalUris.add(uri);
                        this.imagesAdapter.notifyItemInserted(this.photosLocalUris.size()-1);
                    }
                }
            }
        }
    });
    private final OnBackPressedCallback backPressedCallback=new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if(creatingPost){
                return;
            }
            if(!title.isEmpty() || !description.isEmpty() || !photosLocalUris.isEmpty() || !selectedGenreSet.isEmpty()){
                AskYesNoFragment fragment=new AskYesNoFragment();
                Bundle bundle=new Bundle();
                bundle.putString("title", getApplicationContext().getString(R.string.discard_changes));
                bundle.putString("message",getApplicationContext().getString(R.string.discard_changes_message));
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(),null);
            }else finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        this.getOnBackPressedDispatcher().addCallback(this,backPressedCallback);

        this.getWindow().setStatusBarColor(this.getColor(R.color.primary));
        this.initImagesAdapter();
        this.initETListener();

        ButtonUtil.applyPushEffect(this.binding.acpPickPhotosBtn);
        this.binding.acpPickPhotosBtn.setOnClickListener(view->{
            this.onPickPhotosButtonClick();
        });

        ButtonUtil.applyPushEffect(this.binding.acpCreatePostBtn);
        this.binding.acpCreatePostBtn.setOnClickListener(view-> this.createPost());

        this.binding.acpSubmitBtnIB.setOnClickListener(view-> this.createPost());

        this.binding.acpCancelIB.setOnClickListener(view->this.closeActivity());

        this.addGenres();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }

    private void initETListener(){
        this.binding.acpTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                title=s.toString().trim();
                binding.acpTitleTIL.setError(null);
            }
        });

        this.binding.acpDescriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                description=s.toString().trim();
                binding.acpDescriptionTIL.setError(null);
            }
        });

    }

    private void initImagesAdapter(){
        this.imagesAdapter=new SingleRemovableImageAdapter(this,this.photosLocalUris);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3, RecyclerView.VERTICAL,false);
        this.binding.acpPhotosContainerRv.setLayoutManager(gridLayoutManager);
        this.binding.acpPhotosContainerRv.setAdapter(this.imagesAdapter);
    }

    private void addGenres(){
        int padding= DeviceUtil.convertDpToPx(this, 10);
        int marginStartEnd= DeviceUtil.convertDpToPx(this, 10);
        int marginTopBottom= DeviceUtil.convertDpToPx(this, 10);
//        List<Genre> genres = AuthenticationUtil.getAllGenres();
        List<String> genres=AuthenticationUtil.getAllGenres();

        for(String genre:genres){
            TextView genreTV = new TextView(this);
            genreTV.setText(genre);
            genreTV.setGravity(Gravity.CENTER);
            genreTV.setPadding(0,padding,0,padding);
            genreTV.setTextAppearance(R.style.MediumText);
            genreTV.setClickable(true);
            genreTV.setFocusable(true);
            GridLayout.LayoutParams layoutParams= new GridLayout.LayoutParams();
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            if(genre.length()>10){
                layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2,2f);
            }else {
                layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1,1f);
            }
            layoutParams.setMargins(marginStartEnd, marginTopBottom, marginStartEnd, marginTopBottom);
            genreTV.setLayoutParams(layoutParams);
            genreTV.setBackgroundResource(R.drawable.bg_primary_dark_stroked_ripple);
            genreTV.setTextColor(this.getColor(R.color.dominant));

            genreTV.setOnClickListener(view->{
                if(!genreTV.isSelected()){
                    view.setBackgroundResource(R.drawable.bg_primary_dark);
                    view.setSelected(true);
                    selectedGenreSet.add(genre);
                }else {
                    view.setSelected(false);
                    view.setBackgroundResource(R.drawable.bg_primary_dark_stroked_ripple);
                    selectedGenreSet.remove(genre);
                }
            });

            this.binding.acpGenresContainerGL.addView(genreTV);

        }
    }

    private void onPickPhotosButtonClick(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.pickImagesLauncher.launch(intent);
    }

    private void createPost(){
        if(this.validatePost()){
            this.creatingPost=true;
            this.binding.acpPostInputContainerNSV.animate().alpha(0).setDuration(200).withEndAction(()-> this.binding.acpPostInputContainerNSV.setVisibility(View.GONE)).start();
            this.binding.acpAppBarLayout.animate().alpha(0).setDuration(200).withEndAction(()-> this.binding.acpAppBarLayout.setVisibility(View.GONE)).start();
            this.binding.acpCreatingPostAnimationFL.setVisibility(View.VISIBLE);
            this.binding.acpCreatingPostAnimationFL.animate().alpha(1).setDuration(200).start();

            User loggedUser=LoginSession.getLoggedUser();
            PostService postService=new PostService(this);
            Post post=new Post();
            post.setId(String.valueOf(System.currentTimeMillis()));
            post.setTitle(this.title);
            post.setDescription(this.description);
            post.setUserName(loggedUser.getFullname());
            post.setUserAvatarUri(loggedUser.getAvatarUri());
            post.setUploadTime(String.valueOf(System.currentTimeMillis()));
            post.setLikeIds(new ArrayList<>());
            post.setCommentIds(new ArrayList<>());
            post.setGenres(new ArrayList<>(this.selectedGenreSet));
            postService.createPost(post,this.photosLocalUris);
        }
    }

    private boolean validatePost(){
        if(this.title.isEmpty()){
            this.binding.acpTitleTIL.setError(this.getString(R.string.title_required));
            this.binding.acpTitleET.requestFocus();
            this.binding.acpPostInputContainerNSV.setScrollY(0);
            return false;
        }
        if(this.description.isEmpty()){
            this.binding.acpDescriptionTIL.setError(this.getString(R.string.description_required));
            this.binding.acpDescriptionET.requestFocus();
            this.binding.acpPostInputContainerNSV.setScrollY(0);
            return false;
        }
        if(this.photosLocalUris.isEmpty()){
            SnackbarUtil.makeTopSnackbar(this.binding.getRoot(),R.string.add_minimum_one_image).show();
            this.binding.acpPostInputContainerNSV.setScrollY(100);
            return false;
        }
        if(this.selectedGenreSet.isEmpty()){
            Snackbar snackbar= SnackbarUtil.makeTopSnackbar(this.binding.getRoot(),R.string.add_minimum_one_genre);
            snackbar.show();
            return false;
        }
        return  true;
    }

    private void closeActivity(){
        if(!this.title.isEmpty() || !this.description.isEmpty() || !this.photosLocalUris.isEmpty() || !this.selectedGenreSet.isEmpty()){
            AskYesNoFragment fragment=new AskYesNoFragment();
            Bundle bundle=new Bundle();
            bundle.putString("title",this.getString(R.string.discard_changes));
            bundle.putString("message",this.getString(R.string.discard_changes_message));
            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(),null);
        }
        else {
            finish();
        }
    }


    @Override
    public void onPostCreatedSuccess(Post post) {
        SnackbarUtil.makeTopSnackbar(this.binding.getRoot(), R.string.post_created_successfully).show();
        UserService userService = new UserService(this);
        User loggedUser = LoginSession.getLoggedUser();
        loggedUser.getMyPostIds().add(post.getId());
        userService.updateUser(loggedUser);
        this.creatingPost=false;
    }

    @Override
    public void onPostDeletedSuccess() {}

    @Override
    public void onPostUpdatedSuccess() {}

    @Override
    public void onPostCreatedFailed() {
       SnackbarUtil.makeTopSnackbar(this.binding.getRoot(),R.string.something_went_wrong).show();
        this.creatingPost=false;
       this.binding.acpCreatePostBtn.setEnabled(true);
       this.binding.acpCreatePostBtn.setText(R.string.create_post);
        this.binding.acpAppBarLayout.setVisibility(View.VISIBLE);
        this.binding.acpPostInputContainerNSV.setVisibility(View.VISIBLE);
        this.binding.acpAppBarLayout.animate().alpha(1).setDuration(200).start();
        this.binding.acpPostInputContainerNSV.animate().alpha(1).setDuration(200).start();
        this.binding.acpCreatingPostAnimationFL.animate().alpha(0).setDuration(200).withEndAction(()-> this.binding.acpCreatingPostAnimationFL.setVisibility(View.GONE)).start();
    }

    @Override
    public void onPostDeletedFailed() {}

    @Override
    public void onPostUpdatedFailed() {}

    @Override
    public void onUpdateUserSuccess(User user) {
        LoginSession.setLoggedUser(user);
        this.finish();
    }

    @Override
    public void onUpdateUserFailed() {}

    @Override
    public void onYesClick() {
        finish();
    }

    @Override
    public void onNoClick() {}
}