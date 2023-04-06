package algonquin.cst2335.finalproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityNasaBinding;
import algonquin.cst2335.finalproject.databinding.NasaRowBinding;

public class NasaActivity extends AppCompatActivity {
    private ActivityNasaBinding binding;
    private RecyclerView.Adapter myAdapter;
    private String date;
    RoverModel model;
    Queue request;
    ArrayList<RoverItem> roverList;
    ArrayList<RoverItem> tempList;
    String url;
    String imgURL;
    String roverName;
    Bitmap image;
    RequestQueue queue;
    RecyclerView recyclerView;
    RoverItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNasaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        roverList = new ArrayList<RoverItem>();
//        model = new ViewModelProvider(this).get(RoverModel.class);
//        roverList = model.roverList.getValue();
        recyclerView = binding.recycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        if (roverList == null) {
//            model.roverList.postValue(roverList = new ArrayList<RoverItem>());
//        }

        binding.search.setOnClickListener((clk) -> {
            date = binding.date.getText().toString();
            ArrayList<RoverItem> tempList=parseJSON(date);
            roverList.add(item);
            binding.date.setText(String.valueOf(tempList.size()));
//            if (validate(date)) {

//            } else {
//                Toast.makeText(this, "Please Enter a number between 0 and 1000", Toast.LENGTH_SHORT)
//                        .show();
//            }
        });
        myAdapter = new MyAdapter(roverList, this);
        recyclerView.setAdapter(myAdapter);

    }

    private boolean validate(int date) {
        if (date >= 0 && date <= 1000) {
            return true;
        }
        return false;
    }

    private ArrayList<RoverItem> parseJSON(String date) {
        url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + date + "&api_key=zf1BVnH0bXnoSoQfq5RQdl39UKlyCHKXKOurI2TC";
        ArrayList<RoverItem> tempList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray photosArray = response;
                            for (int i = 0; i < photosArray.length(); i++) {
                                JSONObject position = photosArray.getJSONObject(i);
                                JSONObject camera = position.getJSONObject("rover");
                                imgURL = switchToHttps(position.getString("img_src"));
                                roverName = camera.getString("name");
                                tempList.add(new RoverItem(roverName, imgURL));
                            }
                            for (RoverItem rover : tempList) {
                                    ImageRequest imgReq = new ImageRequest(rover.getImgURL(), new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            try {
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                                        NasaActivity.this.openFileOutput("image" + rover.getImgURL().substring
                                                                (rover.getImgURL().length() - 10, rover.getImgURL().length()), Activity.MODE_PRIVATE));
                                                rover.setImage(bitmap);
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                    });
                                    queue.add(imgReq);
                                }
                        } catch (JSONException e) {
                            throw new RuntimeException();
                        }
                    }
                },
                (error) -> {
                    Toast.makeText(this, "failure to reach server", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);
        return tempList;
    }
    private String switchToHttps(String url){
        String substring = url.substring(4, url.length());
        return "https" + substring;
    }
}