package algonquin.cst2335.finalproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

        weatherModel = new ViewModelProvider(this).get(WeatherForecastViewModel.class);//Weather Forecast View Model

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//loads the XML on screen

        weatherModel.editString.observe(this, s -> {
            binding.enterCity.setText("Your location is " + s);
        });

        mytext = findViewById(R.id.textview);
        searchWeather = findViewById(R.id.searchButton);
        enterCity = findViewById(R.id.enterCity);

        //SharedPreferences Lab 4 Part 2 of 2
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String location =  prefs.getString("userLocation", "");
        editor.putString("userLocation", location);

        editor.apply();

        binding.searchButton.setOnClickListener(button -> { //Search button for the weather forecast
            cityName = binding.enterCity.getText().toString(); //Week10_ParsingJSON
            String stringURL = null;

            try {
                stringURL = new StringBuilder()
                        .append("https://api.openweathermap.org/data/2.5/weather?q=")
                        .append(URLEncoder.encode(cityName, "UTF-8"))
                        .append("&appid=7e943c97096a9784391a981c4d878b22&units=metric").toString();
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, stringURL,
                    null,
                    (response) -> {
                        try {
                            JSONObject coord = response.getJSONObject( "coord" );
                            JSONArray weatherArray = response.getJSONArray ( "weather" );
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");
                            JSONObject mainObject = response.getJSONObject( "main" );
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            runOnUiThread( (  )  -> {
                                binding.temp.setText("The current temperature is " + current); //temp
                                binding.temp.setVisibility(View.VISIBLE);
                                binding.minTemp.setText("The min temperature is " + min);//min temperature
                                binding.minTemp.setVisibility(View.VISIBLE);
                                binding.maxTemp.setText("The max temperature is " + max);//max temperature
                                binding.maxTemp.setVisibility(View.VISIBLE);
                                binding.humidity.setText("The humidity is " + humidity);//humidity
                                binding.humidity.setVisibility(View.VISIBLE);
                                binding.icon.setImageBitmap(image);//icon
                                binding.icon.setVisibility(View.VISIBLE);
                                binding.description.setText(description);//description
                                binding.description.setVisibility(View.VISIBLE);
                            });

                            int vis = response.getInt("visibility");
                            String name = response.getString( "name" );

                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
                            if(file.exists()) {
                                image = BitmapFactory.decodeFile(pathname);
                            } else {
                                ImageRequest imgReq = new ImageRequest("https://openweathermap.org/img/img/w" +
                                        iconName, new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap bitmap) {
                                                try{
                                                    image = bitmap;
                                                    image.compress(Bitmap.CompressFormat.PNG, 100,
                                                            MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                                    binding.icon.setImageBitmap(image);
                                                }catch (Exception e) {
                                                    e.printStackTrace();
                                                }//end of catch
                                            }//end of onResponse function
                                        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error ) -> {
                                    Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                });
                                queue.add(imgReq);
                            }//end of else
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {});
            queue.add(request);

            //StartingActivities Part 1 of 2
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            String userInput = binding.enterCity.getText().toString();
            intent.putExtra("userLocation", userInput);
            startActivity(intent);

            //Toast message shows the location when user enter the location
            Toast
                    .makeText(MainActivity.this, "Your location is " + enterCity.getText().toString(), Toast.LENGTH_SHORT) //Output is your location is...
                    .show();
        });
    }
}