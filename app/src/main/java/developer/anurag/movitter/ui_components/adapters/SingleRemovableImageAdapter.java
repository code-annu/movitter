package developer.anurag.movitter.ui_components.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import developer.anurag.movitter.R;
import developer.anurag.movitter.utils.DeviceUtil;

public class SingleRemovableImageAdapter extends RecyclerView.Adapter<SingleRemovableImageAdapter.SingleRemovableImageViewHolder> {

    public interface SingleRemovableImageAdapterListener{
        void onRemoveClick(int position);
    }
    private final Context context;
    private List<Uri> images;
    private final int size;
    private SingleRemovableImageAdapterListener listener;

    public SingleRemovableImageAdapter(Context context, List<Uri> images) {
        this.context = context;
        this.images = images;
        this.size= DeviceUtil.getDeviceWidth(context)/3;
    }

    @NonNull
    @Override
    public SingleRemovableImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.removable_image_item,parent,false);
        return new SingleRemovableImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleRemovableImageViewHolder holder, int position) {
        Glide.with(this.context).load(this.images.get(position)).override(size).centerCrop().into(holder.imageView);
        holder.removeIB.setOnClickListener(v->{
            try{
                this.images.remove(position);
                this.notifyItemRemoved(position);
                this.notifyItemRangeChanged(position,this.images.size());
            }catch (IndexOutOfBoundsException ignored){}
        });
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }

    public void addListener(SingleRemovableImageAdapterListener listener){
        this.listener=listener;
    }

    public static class SingleRemovableImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final ImageButton removeIB;
        public SingleRemovableImageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView=itemView.findViewById(R.id.rii_imageView);
            this.removeIB=itemView.findViewById(R.id.rii_removeIV);
        }
    }
}
