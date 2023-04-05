package algonquin.cst2335.finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject.databinding.ActivityNasaBinding;

public class NasaActivity extends AppCompatActivity {
    private ActivityNasaBinding binding;
    private int date;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityNasaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.search.setOnClickListener((clk) -> {
            date = Integer.parseInt(binding.date.getText().toString());
        });

    }
    public boolean validate(int date){
        if (date >= 0 && date <= 1000){
            return true;
        }
        return false;
    }
}
