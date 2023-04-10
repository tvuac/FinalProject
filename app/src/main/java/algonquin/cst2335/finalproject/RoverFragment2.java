package algonquin.cst2335.finalproject;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.RoverFragment2Binding;
import algonquin.cst2335.finalproject.databinding.RoverFragmentBinding;

    public class RoverFragment2 extends Fragment {
        RoverItem selected;
        RoverItemDAO rDAO;
        public RoverFragment2(RoverItem rover){
            selected = rover;
        }
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            RoverFragment2Binding binding = RoverFragment2Binding.inflate(getLayoutInflater());
            RoverDatabase db = Room.databaseBuilder(getActivity(), RoverDatabase.class, "rovers").build();
            rDAO = db.rDAO();
            binding.detailsName.setText(selected.getCameraName());
            binding.detailsURL.setText(selected.getImgURL());
            binding.detailsImage.setImageBitmap(selected.getImage());
            binding.detailsURL.setOnClickListener((click) -> {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, selected.getImgURL());
                startActivity(intent);
            });
            binding.delete.setOnClickListener((click) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this picture from favourites? ")
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .setPositiveButton("Yes", (dialog, cl) -> {
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->{
                        rDAO.deleteRover(selected);
                    });
                    getFragmentManager().beginTransaction().remove(this).commit();
                    NasaFavActivity activity = (NasaFavActivity)getActivity();
                    int position = activity.roverList.indexOf(selected);
                    activity.roverList.remove(selected);
                    activity.myAdapter.notifyDataSetChanged();
                    Snackbar.make(activity.findViewById(R.id.roverFragment2), "Deleted from favourites", Snackbar.LENGTH_LONG)
                            .setAction("Undo", (clk) -> {
                                activity.roverList.add(position, selected);
                                Executor thread2 = Executors.newSingleThreadExecutor();
                                thread2.execute(() -> {
                                    selected.setId((int)rDAO.insertRover(selected));
                                });
                                activity.myAdapter.notifyDataSetChanged();
                            }).show();
                }).create().show();
            });
            return binding.getRoot();
        }
    }
