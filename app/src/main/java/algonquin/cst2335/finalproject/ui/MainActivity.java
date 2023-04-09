package algonquin.cst2335.finalproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.data.WeatherForecastViewModel;
import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

/**
 * The Main Activity class
 * @author Teresa Vu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The variable for binding
     */
    private ActivityMainBinding binding; // viewBinding must true in build.gradle

    /**
     * The variable for weatherModel
     */
    private WeatherForecastViewModel weatherModel;

    /**
     * The variable for cityName
     */
    protected String cityName;

    /**
     * This holds the text on top of the screen for the weather forecast
     */
    private TextView mytext = null;

    /**
     * This holds the
     */
    private Button searchWeather = null;

    /**
     * This holds the
     */
    private EditText enterCity = null;

    /**
     * The Volley object will connect to the server
     */
    protected RequestQueue queue = Volley.newRequestQueue(this);

    /**
     * The variable stores the image
     */
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //weatherModel = new ViewModelProvider(this).get(WeatherForecastViewModel.class);//Weather Forecast View Model

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//loads the XML on screen

        binding.recycleView.setAdapter(new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        })
        //weatherModel.editString.observe(this, s -> {
        //    binding.enterCity.setText("Your location is " + s);
        //});

        //mytext = findViewById(R.id.textview);
        //searchWeather = findViewById(R.id.searchButton);
        //enterCity = findViewById(R.id.enterCity);

        //SharedPreferences Lab 4 Part 2 of 2
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String location =  prefs.getString("userLocation", "");
        editor.putString("userLocation", location);

        editor.apply();

        binding.searchButton.setOnClickListener(button -> { //Search button for the weather forecast
            //StartingActivities Part 1 of 2
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            String userInput = binding.enterCity.getText().toString();
            intent.putExtra("userLocation", userInput);
            startActivity(intent);
        });
    }

    /**
     * The MyRowHolder is an object that represents everything that goes on the row
     */
    class MyRowHolder extends RecyclerView.ViewHolder {

        /**
         *
         * @param itemView
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}