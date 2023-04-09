package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityFavnasaBinding;
import algonquin.cst2335.finalproject.databinding.NasaRowBinding;

public class NasaFavActivity extends AppCompatActivity {
    private ActivityFavnasaBinding binding;
    ArrayList<RoverItem> roverList;
    RecyclerView.Adapter myAdapter;
    RoverItemDAO rDAO;
    RoverModel model;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityFavnasaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        model = new ViewModelProvider(this).get(RoverModel.class);
//        roverList = model.roverList.getValue();
//        if (roverList == null) {
//            model.roverList.postValue(roverList = new ArrayList<RoverItem>());
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute(() ->
//            {
//                roverList.addAll( rDAO.getAllRovers() );
//            });
//        }
//        recyclerView = binding.recycler;
//        RoverDatabase db = Room.databaseBuilder(getApplicationContext(), RoverDatabase.class, "database-name").build();
//        rDAO = db.rDAO();
//        class MyRowHolder extends RecyclerView.ViewHolder {
//            TextView roverName;
//            ImageView roverImage;
//
//            public MyRowHolder(@NonNull View itemView) {
//                super(itemView);
//                roverName = itemView.findViewById(R.id.roverName);
//                roverImage = itemView.findViewById(R.id.roverImage);
//                itemView.setOnClickListener((clk) -> {
//                    int position = getAbsoluteAdapterPosition();
//                    RoverItem selected = roverList.get(position);
//                    model.selectedRover.postValue(selected);
//
//                });
//            }
//
//        }
//        model.selectedRover.observe((this), (newRoverValue) -> {
//            RoverFragment fragment = new RoverFragment(newRoverValue);
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.roverFragment, fragment)
//                    .addToBackStack("")
//                    .commit();
//        });
//        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
//        binding.recycler.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
//            @NonNull
//            @Override
//            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                NasaRowBinding binding = NasaRowBinding.inflate(getLayoutInflater());
//                return new MyRowHolder(binding.getRoot());
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
//                RoverItem rover = roverList.get(position);
//                holder.roverName.setText(rover.getRoverName());
//                if (rover.getImage() != null) {
//                    holder.roverImage.setImageBitmap(rover.getImage());
//                }
//            }
//
//            @Override
//            public int getItemCount() {
//                return roverList.size();
//            }
//        });
    }
}
