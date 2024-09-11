package developer.anurag.movitter.ui_components.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.lights.LightState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import developer.anurag.movitter.R;
import developer.anurag.movitter.datatypes.Comment;
import developer.anurag.movitter.utils.DateTimeUtil;
import developer.anurag.movitter.utils.DeviceUtil;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final Context context;
    private List<Comment> comments;
    private final int avatarSize;
    private CommentAdapterListener listener;

    public interface CommentAdapterListener{
        void onCommentClick(Comment comment);
        void onCommentLongClick(Comment comment);
    }

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.avatarSize= DeviceUtil.convertDpToPx(this.context,50f);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.comment_item,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment=comments.get(position);
        holder.commentTV.setText(comment.getDescription());
        holder.fullnameTV.setText(comment.getUsername());
        holder.uploadTimeTV.setText(DateTimeUtil.getUploadTime(comment.getUploadTime()));
        Glide.with(this.context).load(comment.getAvatar()).centerCrop().override(this.avatarSize).into(holder.avatarIV);
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public void addListener(CommentAdapterListener listener){
        this.listener=listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.notifyDataSetChanged();
    }

    public void insertItem(Comment comment){
        this.comments.add(comment);
        this.notifyItemInserted(this.comments.size()-1);
        this.notifyItemRangeChanged(0,this.comments.size());
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView commentTV,fullnameTV,uploadTimeTV;
        private final GridLayout mainContainerGL;
        private final ImageView avatarIV;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTV=itemView.findViewById(R.id.ci_commentTV);
            fullnameTV=itemView.findViewById(R.id.ci_fullnameTV);
            uploadTimeTV=itemView.findViewById(R.id.ci_uploadTimeTV);
            mainContainerGL=itemView.findViewById(R.id.ci_mainContainerGL);
            avatarIV=itemView.findViewById(R.id.ci_avatarIV);
        }
    }
}
