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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.data.WeatherForecastViewModel;
import algonquin.cst2335.finalproject.data.WeatherMessage;
import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;
import algonquin.cst2335.finalproject.databinding.CityWeatherforecastBinding;

/**
 * The Main Activity class
 * @author Teresa Vu
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The variable for binding
     * viewBinding must true in build.gradle
     */
    ActivityMainBinding binding;

    /**
     * The variable for weatherModel
     */
    private WeatherForecastViewModel weatherForecastModel;

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
     * This holds the value of the city
     */
    private EditText enterCity;

    /**
     * The Volley object will connect to the server
     */
    //protected RequestQueue queue = Volley.newRequestQueue(this);

    /**
     * The variable stores the image
     */
    Bitmap image;

    public ArrayList<WeatherMessage> cities;

    /**
     * This holds the value for the recycle view adapter
     */
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //weatherModel = new ViewModelProvider(this).get(WeatherForecastViewModel.class);//Weather Forecast View Model

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        WeatherForecastViewModel weatherForecastModel = new ViewModelProvider(this).get(WeatherForecastViewModel.class);
        cities = weatherForecastModel.cities.getValue(); //survive rotation

        setContentView(binding.getRoot());//loads the XML on screen

        if(cities == null) {
            weatherForecastModel.cities.postValue(cities = new ArrayList<WeatherMessage>());
        }

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CityWeatherforecastBinding binding = CityWeatherforecastBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                WeatherMessage city = cities.get(position);
                holder.cityText.setText(city.getCity()); //set the city in onBindViewHolder

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy");
                String setDate = sdf.format(new Date());
                holder.dateText.setText(city.getDateSent());
            }

            @Override
            public int getItemCount() {
                return cities.size(); //the number of cities in the list
            }

            /**
           * The function returns an int and determine which layout to load
           * @param position position to query
           * @return return the cities to represent a layout
           */
            public int getItemViewType(int position) {
                return position % 2;
            }
        });//setAdapter for MyRowHolder

        //mytext = findViewById(R.id.textview);
        //searchWeather = findViewById(R.id.searchButton);
        //enterCity = findViewById(R.id.enterCity);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        //Week4 Did not include intent but will test it
        SharedPreferences prefs = getSharedPreferences("CityData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit(); //Will saved the city typed into the search bar
        String city = prefs.getString("City", "");
        editor.putString("City", city); //Saves the city into the CityData file
        editor.apply();

        binding.searchButton.setOnClickListener(button -> { //Search button for the weather forecast
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);//transition from main activity to second activity
            String cityTyped = binding.enterCity.getText().toString();
            intent.putExtra("City", cityTyped);
            startActivity(intent);//Start a new activity

            //RecycleView
            String cityText = binding.enterCity.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy");
            String setDate = sdf.format(new Date());

            WeatherMessage newMessage = new WeatherMessage(cityText, setDate, true);
            cities.add(newMessage);

            myAdapter.notifyItemInserted(cities.size()-1);
            binding.enterCity.setText("");
        });
    }

    /**
     * The MyRowHolder is an object that represents everything that goes on the row
     */
    class MyRowHolder extends RecyclerView.ViewHolder {

        protected TextView cityText;
        protected TextView dateText;

        /**
         * @param itemView
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            cityText = itemView.findViewById(R.id.cityTextView);
            dateText = itemView.findViewById(R.id.datetextView);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();

                WeatherMessage clickMessage = cities.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
            });
        }
    }
}