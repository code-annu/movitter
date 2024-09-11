package developer.anurag.movitter.ui_components.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import developer.anurag.movitter.R;

public class SingleRoundedImageAdapter extends RecyclerView.Adapter<SingleRoundedImageAdapter.SingleRoundedImageViewHolder> {
    private final Context context;
    private List<String> imageUris;

    public SingleRoundedImageAdapter(Context context, List<String> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }


    @NonNull
    @Override
    public SingleRoundedImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_rounded_image_item, parent, false);
        return new SingleRoundedImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleRoundedImageViewHolder holder, int position) {
        Glide.with(this.context).load(this.imageUris.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.imageUris.size();
    }

    public static class SingleRoundedImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final CardView mainContainerCV;
        public SingleRoundedImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.srii_imageView);
            mainContainerCV = itemView.findViewById(R.id.srii_mainContainerCV);
        }
    }
}
