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

public class SingleTextAdapter extends RecyclerView.Adapter<SingleTextAdapter.SingleTextViewHolder> {
    public interface SingleTextAdapterListener{
        void onClick(String title);
    }

    private final Context context;
    private List<String> titles;
    private SingleTextAdapterListener listener;

    public SingleTextAdapter(Context context, List<String> titles) {
        this.context = context;
        this.titles = titles;
    }

    @NonNull
    @Override
    public SingleTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.single_text_item,parent,false);
        return new SingleTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleTextViewHolder holder, int position) {
        String title=this.titles.get(position);
        holder.textView.setText(title);

        holder.mainContainerLL.setOnClickListener(v -> {
            if(this.listener!=null){
                this.listener.onClick(title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.titles.size();
    }

    public void addListener(SingleTextAdapterListener listener){
        this.listener=listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTitles(List<String> titles){
        this.titles=titles;
        this.notifyDataSetChanged();
    }

    public static class SingleTextViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final LinearLayout mainContainerLL;
        public SingleTextViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainContainerLL=itemView.findViewById(R.id.sti_mainContainerLL);
            this.textView=itemView.findViewById(R.id.sti_textView);
        }
    }
}
