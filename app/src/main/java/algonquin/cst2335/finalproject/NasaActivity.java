package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.finalproject.databinding.ActivityNasaBinding;

public class NasaActivity extends AppCompatActivity {
    private ActivityNasaBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private int date;
    RoverModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityNasaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView =( RecyclerView ) binding.recycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.search.setOnClickListener((clk) -> {
            date = Integer.parseInt(binding.date.getText().toString());
            if (validate(date)){

            }
            else{
                Toast.makeText(this, "Please Enter a number between 0 and 1000", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }
    public boolean validate(int date){
        if (date >= 0 && date <= 1000){
            return true;
        }
        return false;
    }
}
