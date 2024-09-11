package developer.anurag.movitter.ui_components.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import developer.anurag.movitter.R;
import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.datatypes.User;
import developer.anurag.movitter.movitter_api.LoginSession;
import developer.anurag.movitter.utils.DateTimeUtil;
import developer.anurag.movitter.utils.DeviceUtil;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    final private Context context;
    private List<Post> posts;
    private final User loggedUser;
    private final int avatarSize;
    private PostAdapterListener listener;

    public interface PostAdapterListener {
        void onPostClick(Post post);
        void onPostLike(Post post,String email);
        void onPostDislike(Post post,String email);
        void onPostBookmark(Post post);
    }

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        this.avatarSize= DeviceUtil.convertDpToPx(this.context,50f);
        this.loggedUser= LoginSession.getLoggedUser();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.post_item,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post=this.posts.get(position);
        holder.fullnameTV.setText(post.getUserName());
        holder.titleTV.setText(post.getTitle());
        holder.descriptionTV.setText(post.getDescription());
        holder.genresTV.setText(String.join(" | ",post.getGenres()));
        holder.uploadTimeTV.setText(DateTimeUtil.getUploadTime(Long.parseLong(post.getUploadTime())));
        holder.likeCB.setText(String.valueOf(post.getLikeIds().size()));
        holder.commentCB.setText(String.valueOf(post.getCommentIds().size()));
        Glide.with(this.context).load(post.getUserAvatarUri()).override(this.avatarSize,this.avatarSize).into(holder.avatarIV);
        holder.likeCB.setChecked(post.getLikeIds().contains(this.loggedUser.getEmail()));



        if (post.getImageUris().size()==1){
            this.addOneImage(holder.imageContainerLL,post.getImageUris().get(0));
        }
        else if(post.getImageUris().size()==2){
            this.addTwoImages(holder.imageContainerLL,post.getImageUris().get(0),post.getImageUris().get(1));
        }
        else if(post.getImageUris().size()==3){
            this.addThreeImages(holder.imageContainerLL,post.getImageUris().get(0),post.getImageUris().get(1),post.getImageUris().get(2));
        }else {
            this.addThreePlusImages(holder.imageContainerLL,post.getImageUris().get(0),post.getImageUris().get(1),post.getImageUris().get(2),post.getImageUris().size()-3);
        }

        holder.likeCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                holder.likeCB.setText(String.valueOf(post.getLikeIds().size()+1));
                if(this.listener!=null)this.listener.onPostLike(post,this.loggedUser.getEmail());
            }else {
                holder.likeCB.setText(String.valueOf(post.getLikeIds().size()-1));
                if(this.listener!=null)this.listener.onPostDislike(post,this.loggedUser.getEmail());
            }
        });

        holder.mainContainerGL.setOnClickListener(view->{
            if(this.listener!=null)this.listener.onPostClick(post);
        });


    }

    private void addOneImage(LinearLayout layout,String uri){
        View view=LayoutInflater.from(this.context).inflate(R.layout.one_image_template_item,null);
        ImageView imageView=view.findViewById(R.id.oiti_imageView);
        int imageWidth=DeviceUtil.getDeviceWidth(this.context);
        Glide.with(this.context).load(uri).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                Bitmap orgBitmap= ((BitmapDrawable) drawable).getBitmap();
                float ratio= (float) orgBitmap.getWidth() /orgBitmap.getHeight();
                int height= (int) (imageWidth/ratio);
                Bitmap newBitmap=Bitmap.createScaledBitmap(orgBitmap,imageWidth,height,false);
                imageView.setImageBitmap(newBitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {}
        });
        layout.removeAllViews();
        layout.addView(view);
    }

    private void addTwoImages(LinearLayout layout,String uri1,String uri2){
        View view=LayoutInflater.from(this.context).inflate(R.layout.two_image_template_item,null);
        ImageView imageView1=view.findViewById(R.id.titi_imageView1);
        ImageView imageView2=view.findViewById(R.id.titi_imageView2);
        int imageSize=DeviceUtil.getDeviceWidth(this.context)/2-DeviceUtil.convertDpToPx(this.context,40f);
        Glide.with(this.context).load(uri1).override(imageSize).centerCrop().into(imageView1);
        Glide.with(this.context).load(uri2).override(imageSize).centerCrop().into(imageView2);
        layout.removeAllViews();
        layout.addView(view);
    }

    private void addThreeImages(LinearLayout layout,String uri1,String uri2,String uri3){
        View view=LayoutInflater.from(this.context).inflate(R.layout.three_image_template_item,null);
        ImageView imageView1=view.findViewById(R.id.thiti_imageView1);
        ImageView imageView2=view.findViewById(R.id.thiti_imageView2);
        ImageView imageView3=view.findViewById(R.id.thiti_imageView3);

        int imageSize=DeviceUtil.getDeviceWidth(this.context)/2-DeviceUtil.convertDpToPx(this.context,40f);

        Glide.with(this.context).load(uri1).override(imageSize,imageSize*2+DeviceUtil.convertDpToPx(this.context,5f)).centerCrop().into(imageView1);
        Glide.with(this.context).load(uri2).override(imageSize).centerCrop().into(imageView2);
        Glide.with(this.context).load(uri3).override(imageSize).centerCrop().into(imageView3);
        layout.removeAllViews();
        layout.addView(view);
    }

    private void addThreePlusImages(LinearLayout layout,String uri1,String uri2,String uri3,int restImagesCount){
        View view=LayoutInflater.from(this.context).inflate(R.layout.three_plus_image_template_item,null);
        ImageView imageView1=view.findViewById(R.id.thpiti_imageView1);
        ImageView imageView2=view.findViewById(R.id.thpiti_imageView2);
        ImageView imageView3=view.findViewById(R.id.thpiti_imageView3);
        TextView restImageCounterTV=view.findViewById(R.id.thpiti_restImageCounterTV);
        restImageCounterTV.setText(this.context.getString(R.string.rest_image_counter,restImagesCount));

        int imageSize=DeviceUtil.getDeviceWidth(this.context)/2-DeviceUtil.convertDpToPx(this.context,40f);
        restImageCounterTV.setWidth(imageSize);
        restImageCounterTV.setHeight(imageSize);
        Glide.with(this.context).load(uri1).override(imageSize,imageSize*2+DeviceUtil.convertDpToPx(this.context,5f)).centerCrop().into(imageView1);
        Glide.with(this.context).load(uri2).override(imageSize).centerCrop().into(imageView2);
        Glide.with(this.context).load(uri3).override(imageSize).centerCrop().into(imageView3);
        layout.removeAllViews();

        layout.addView(view);
    }

    public void addListener(PostAdapterListener listener){
        this.listener=listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<Post> posts) {
        this.posts = posts;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView fullnameTV,titleTV,descriptionTV,genresTV,uploadTimeTV;
        private final CheckBox likeCB,commentCB,bookmarkCB;
        private final GridLayout mainContainerGL;
        private final LinearLayout imageContainerLL;
        private final ImageView avatarIV;

        public PostViewHolder(@NonNull View view) {
            super(view);
            this.fullnameTV=view.findViewById(R.id.pi_fullnameTV);
            this.titleTV=view.findViewById(R.id.pi_movieTitleTV);
            this.descriptionTV=view.findViewById(R.id.pi_movieDescriptionTV);
            this.genresTV=view.findViewById(R.id.pi_movieGenresTV);
            this.uploadTimeTV=view.findViewById(R.id.pi_uploadTimeTV);
            this.likeCB=view.findViewById(R.id.pi_likeCB);
            this.commentCB=view.findViewById(R.id.pi_commentCB);
            this.bookmarkCB=view.findViewById(R.id.pi_bookmarkCB);
            this.mainContainerGL=view.findViewById(R.id.pi_mainContainerGL);
            this.imageContainerLL=view.findViewById(R.id.pi_imageContainerLL);
            this.avatarIV=view.findViewById(R.id.pi_avatarIV);
        }
    }
}
