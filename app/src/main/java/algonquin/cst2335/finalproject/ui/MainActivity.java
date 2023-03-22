package algonquin.cst2335.finalproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.data.WeatherForecastViewModel;
import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // viewBinding must true in build.gradle
    private WeatherForecastViewModel weatherModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Weather Forecast View Model
        weatherModel = new ViewModelProvider(this).get(WeatherForecastViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//loads the XML on screen

        weatherModel.editString.observe(this, s -> {
            binding.enterLocation.setText("Your location is " + s);
        });

        TextView mytext = findViewById(R.id.textview);
        Button searchWeather = findViewById(R.id.searchButton);
        EditText enterCity = findViewById(R.id.enterLocation);

        //SharedPreferences Lab 4 Part 2 of 2
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String location =  prefs.getString("userLocation", "");
        editor.putString("userLocation", location);

        editor.apply();

        binding.searchButton.setOnClickListener(button -> { //Search button for the weather forecast
            //StartingActivities Part 1 of 2
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            String userInput = binding.enterLocation.getText().toString();
            intent.putExtra("userLocation", userInput);
            startActivity(intent);

            //Toast message shows the location when user enter the location
            Toast
                    .makeText(MainActivity.this, "Your location is " + enterCity.getText().toString(), Toast.LENGTH_SHORT) //Output is your location is...
                    .show();
        });
    }
}