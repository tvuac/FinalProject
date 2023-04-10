package algonquin.cst2335.finalproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.data.WeatherDatabase;
import algonquin.cst2335.finalproject.data.WeatherDetailsFragment;
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

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//load a menu layout file
        super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    //Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId())
        {
            case R.id.item_1:

                //displays an AlertDialog with instructions for how to use the interface
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("To use this app, enter the name of the city to find the weather forecast")
                        .setTitle("About this app")
                        .create()
                        .show();
                break;
        }
        return true;
    }

    /**
     * This will create a connection to a server
     */
    protected RequestQueue queue = null;

    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Open the WeatherDatabase
        WeatherDatabase weatherDatabase = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "CityDatabase").build();
        WeatherMessageDAO weatherMessageDAO = weatherDatabase.weatherMessageDAO();

        //Volley
        queue = Volley.newRequestQueue(this);

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

        mytext = findViewById(R.id.textview);
        searchWeather = findViewById(R.id.searchButton);
        enterCity = findViewById(R.id.enterCity);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        //SharedPreferences
        SharedPreferences prefs = getSharedPreferences("CityData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit(); //Will saved the city typed into the search bar
        String city = prefs.getString("City", "");
        editor.putString("City", city); //Saves the city into the CityData file
        editor.apply();

        binding.searchButton.setOnClickListener(button -> { //Search button for the weather forecast
            //Volley
            cityName = binding.enterCity.getText().toString();
            String stringURL = null;

            try{
                stringURL = new StringBuilder()
                        .append("https://api.openweathermap.org/data/2.5/weather?q=")
                        .append(URLEncoder.encode(cityName, "UTF-8"))
                        .append("&appid=7e943c97096a9784391a981c4d878b22&units=metric").toString(); //API key
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //Volley JSON
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                    try {
                        JSONObject coord = response.getJSONObject("coord");
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject position0 = weatherArray.getJSONObject(0);
                        String description = position0.getString("description");
                        String iconName = position0.getString("icon");
                        //String pathname = getFilesDir() + "/" + iconName + ".png";
                        JSONObject mainObject = response.getJSONObject("main");
                        double current = mainObject.getDouble("temp");
                        double min = mainObject.getDouble("temp_min");
                        double max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");

                        try {
                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
                            if(file.exists())
                            {
                                Bitmap image = BitmapFactory.decodeFile(pathname);
                            }else {
                                ImageRequest imgReq = new ImageRequest("https://openweathermap.org/img/img/w/" + iconName + ".png", new Response.Listener<Bitmap>(){
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                            image =  bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100,
                                                    MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                            binding.icon.setImageBitmap(image);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }//end of catch block
                                    } //end of onResponse
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error ) -> {
                                    Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                });
                                queue.add(imgReq);
                            }//end of else//Place the GUI code inside the {} so that the Main GUI Thread will run that code
                        }//end of try for string path name
                        catch (Exception e) {
                            e.printStackTrace();
                        }//end of catch for string path name
                        runOnUiThread( () -> {
                            //Set the text for temperature to visible
                            binding.temp.setText("The current temperature is " + current);
                            binding.temp.setVisibility(View.VISIBLE);
                            binding.maxTemp.setText("The max temperature is " + max); //Set the text for max temperature to visible
                            binding.maxTemp.setVisibility(View.VISIBLE);
                            binding.minTemp.setText("The min temperature is " +  min);//Set the text for min temperature to visible
                            binding.minTemp.setVisibility(View.VISIBLE);
                            binding.humidity.setText("The humidity is " + humidity + "%");//Set the text for humidity to visible
                            binding.humidity.setVisibility(View.VISIBLE);
                            binding.icon.setImageBitmap(image);//Set the IMAGE for description to visible
                            binding.icon.setVisibility(View.VISIBLE);
                            binding.description.setText(description);//Set the text for description to visible
                            binding.description.setVisibility(View.VISIBLE);
                        });//runOnUiThread

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                        }, (error) -> { } );
            queue.add(request);

            //Toast message
            Toast.makeText(MainActivity.this, "Your location is", Toast.LENGTH_LONG);

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
        });//searchButton

        weatherForecastModel.selectedCity.observe(this,(newValue) -> {
            //Fragment
            WeatherDetailsFragment weatherFragment = new WeatherDetailsFragment(newValue);//to add or replace function

            FragmentManager fMgr = getSupportFragmentManager(); //loads the fragment
            FragmentTransaction tx = fMgr.beginTransaction(); //can add, remove or replace fragment
            tx.add(R.id.fragmentLocation, weatherFragment);
            tx.commit();

            //weatherFragment.displayMessage(newValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLocation, weatherFragment)
                    .commit();

            setSupportActionBar(binding.myToolBar); //Toolbar
        });//observe()
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

                WeatherMessage clickedMessage = cities.get(position);
                //weatherForecastModel.selectedCity.postValue(clickedMessage);//The app crash

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to delete the city " + cityText.getText())
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
                                                    weatherDAO.insertMessage(clickedMessage);//insert the city from database
                                                    cities.add(position, clickedMessage);

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
