package algonquin.cst2335.finalproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.data.WeatherDatabase;
import algonquin.cst2335.finalproject.data.WeatherForecastViewModel;
import algonquin.cst2335.finalproject.data.WeatherMessage;
import algonquin.cst2335.finalproject.data.WeatherMessageDAO;
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
    private TextView mytext;

    /**
     * This holds the value for the search button
     */
    private Button searchWeather;

    /**
     * This holds the value of the city
     */
    private EditText enterCity;

    public ArrayList<WeatherMessage> cities;


    /**
     * This holds the value for the recycle view adapter
     */
    private RecyclerView.Adapter myAdapter;
    WeatherMessageDAO weatherDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Open the WeatherDatabase
        WeatherDatabase weatherDatabase = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "CityDatabase").build();
        WeatherMessageDAO weatherMessageDAO = weatherDatabase.weatherMessageDAO();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        //WeatherForecastViewModel
        WeatherForecastViewModel weatherForecastModel = new ViewModelProvider(this).get(WeatherForecastViewModel.class);
        cities = weatherForecastModel.cities.getValue(); //survive rotation

        setContentView(binding.getRoot());//loads the XML on screen

        if(cities == null) {
            weatherForecastModel.cities.postValue(cities = new ArrayList<WeatherMessage>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                //on a second thread
                List<WeatherMessage> previousMessages = weatherMessageDAO.getAllMessages();//loads all of the message from weather message class
                cities.addAll(previousMessages);//adds all the cities stored in the database
                runOnUiThread(() -> {
                    binding.recycleView.setAdapter(myAdapter);//this is the main thread
                });
            });}

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
                holder.cityText.setText(city.getCity());//set the city in onBindViewHolder

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy");
                String setDate = sdf.format(new Date());
                holder.dateText.setText(setDate);
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

        //SharedPreferences
        SharedPreferences prefs = getSharedPreferences("CityData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit(); //Will saved the city typed into the search bar
        String city = prefs.getString("City", "");
        editor.putString("City", city); //Saves the city into the CityData file
        editor.apply();

        binding.searchButton.setOnClickListener(button -> { //Search button for the weather forecast
            //Intent
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

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                long id = weatherMessageDAO.insertMessage(newMessage);//Insert into the database
                newMessage.id = id; //shows the id
            });

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

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();

                WeatherMessage clickMessage = cities.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to delete the message" + cityText.getText())
                        .setTitle("Warning!")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Executor thread1 = Executors.newSingleThreadExecutor();
                            thread1.execute(() -> {
                                //weatherDAO.deleteMessage(clickMessage);//delete the city from database /Program crash on this line
                                cities.remove(position);

                                runOnUiThread(() -> {
                                    myAdapter.notifyItemRemoved(position);//update Recycle View
                                    Snackbar.make(dateText, "Item deleted", Snackbar.LENGTH_LONG)
                                            .setAction("Undo", clk -> {
                                                Executor thread2 = Executors.newSingleThreadExecutor();
                                                thread2.execute(() -> {
                                                    weatherDAO.insertMessage(clickMessage);//insert the city from database
                                                    cities.add(position, clickMessage);

                                                    runOnUiThread(() -> {
                                                        myAdapter.notifyItemInserted(position);
                                                    });
                                                });
                                            })
                                            .show();
                                });
                            });
                        })//positiveButton
                        .setNegativeButton("No", (dialog, which) -> {

                        })//negativeButton
                        .create()
                        .show();
            });//setOnClickListener

            cityText = itemView.findViewById(R.id.cityTextView);
            dateText = itemView.findViewById(R.id.datetextView);
        }//MyRowHolder
    }
}
