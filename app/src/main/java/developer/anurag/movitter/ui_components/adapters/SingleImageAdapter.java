package developer.anurag.movitter.ui_components.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import developer.anurag.movitter.R;

public class SingleImageAdapter extends RecyclerView.Adapter<SingleImageAdapter.SingleImageViewHolder> {
    private final Context context;
    private List<String> imageUris;

    public SingleImageAdapter(Context context, List<String> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public SingleImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.single_image_item,parent,false);
        return new SingleImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleImageViewHolder holder, int position) {
        Glide.with(this.context).load(this.imageUris.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.imageUris.size();
    }

    public static class SingleImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public SingleImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.sii_imageView);
        }
    }
}
