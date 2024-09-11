package developer.anurag.movitter.ui_components.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.anurag.movitter.R;

public class TrendingTextAdapter extends RecyclerView.Adapter<TrendingTextAdapter.TrendingTextViewHolder> {

    public interface TrendingTextAdapterListener{
        void onClick(int position);
    }

    private final Context context;
    private List<String> trendingTextList;
    private TrendingTextAdapterListener listener;


    public TrendingTextAdapter(Context context, List<String> trendingTextList) {
        this.context = context;
        this.trendingTextList = trendingTextList;
    }

    @NonNull
    @Override
    public TrendingTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.trending_text_item,parent,false);
        return new TrendingTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingTextViewHolder holder, int position) {
        holder.trendingTextTV.setText(this.trendingTextList.get(position));

        holder.mainContainerLL.setOnClickListener(v -> {
            if (this.listener != null) {
                this.listener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.trendingTextList.size();
    }

    public void addListener(TrendingTextAdapterListener listener){
        this.listener=listener;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setTrendingTextList(List<String> trendingTextList) {
        this.trendingTextList = trendingTextList;
        this.notifyDataSetChanged();
    }

    public static class TrendingTextViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout mainContainerLL;
        private final TextView trendingTextTV;

        public TrendingTextViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainContainerLL=itemView.findViewById(R.id.tti_mainContainerLL);
            this.trendingTextTV=itemView.findViewById(R.id.tti_trendingTextTV);
        }
    }
}
