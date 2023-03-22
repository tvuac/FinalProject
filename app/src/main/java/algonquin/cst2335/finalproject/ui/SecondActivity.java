package algonquin.cst2335.finalproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;
import algonquin.cst2335.finalproject.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent fromPrevious = getIntent();
        String userLocation = fromPrevious.getStringExtra("userLocation");
        binding.textView.setText("Your location is " + userLocation);
    }
}