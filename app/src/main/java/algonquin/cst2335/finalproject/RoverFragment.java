package algonquin.cst2335.finalproject;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.RoverFragmentBinding;

public class RoverFragment extends Fragment {
    RoverItem selected;
    RoverItemDAO rDAO;
    public RoverFragment(RoverItem rover){
        selected = rover;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        RoverFragmentBinding binding = RoverFragmentBinding.inflate(getLayoutInflater());
        RoverDatabase db = Room.databaseBuilder(getActivity(), RoverDatabase.class, "roverDatabase").build();
        rDAO = db.rDAO();
        binding.detailsName.setText(selected.getCameraName());
        binding.detailsURL.setText(selected.getImgURL());
        binding.detailsImage.setImageBitmap(selected.getImage());
        binding.detailsURL.setOnClickListener((click) -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, selected.getImgURL());
            startActivity(intent);
        });
        binding.save.setOnClickListener((click) -> {
            Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_SHORT).show();
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->{
                rDAO.insertRover(selected);
            });
        });
        return binding.getRoot();
    }
}
