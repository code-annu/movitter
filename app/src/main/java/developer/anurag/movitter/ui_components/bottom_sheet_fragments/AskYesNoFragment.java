package developer.anurag.movitter.ui_components.bottom_sheet_fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import developer.anurag.movitter.R;
import developer.anurag.movitter.databinding.FragmentAskYesNoBinding;
import developer.anurag.movitter.utils.ButtonUtil;


public class AskYesNoFragment extends BottomSheetDialogFragment {
    private FragmentAskYesNoBinding binding;
    public interface AskYesNoFragmentListener{
        void onYesClick();
        void onNoClick();
    }
    private AskYesNoFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentAskYesNoBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle=this.getArguments();
        if(bundle!=null){
            String title=bundle.getString("title");
            String message=bundle.getString("message");
            this.setTitleAndMessage(title,message);
        }

        ButtonUtil.applyPushEffect(this.binding.acpYesBtn);
        ButtonUtil.applyPushEffect(this.binding.acpNoBtn);

        this.binding.acpYesBtn.setOnClickListener(v->{
            this.listener.onYesClick();
            this.dismiss();
        });
        this.binding.acpNoBtn.setOnClickListener(v->{
            this.listener.onNoClick();
            this.dismiss();
        });
    }

    private void setTitleAndMessage(String title,String message){
        this.binding.faynTitleTV.setText(title);
        this.binding.faynMessageTV.setText(message);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            this.listener=(AskYesNoFragmentListener) context;
        }catch (ClassCastException ignored){}
    }
}