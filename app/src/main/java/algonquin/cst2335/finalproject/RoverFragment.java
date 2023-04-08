package algonquin.cst2335.finalproject;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.RoverFragmentBinding;

public class RoverFragment extends Fragment {
    RoverItem selected;
    public RoverFragment(RoverItem rover){
        selected = rover;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        RoverFragmentBinding binding = RoverFragmentBinding.inflate(getLayoutInflater());
        binding.detailsName.setText(selected.getCameraName());
        binding.detailsURL.setText(selected.getImgURL());
        binding.detailsImage.setImageBitmap(selected.getImage());
        binding.detailsURL.setOnClickListener((click) -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, selected.getImgURL());
            startActivity(intent);
        });
        return binding.getRoot();
    }
}
