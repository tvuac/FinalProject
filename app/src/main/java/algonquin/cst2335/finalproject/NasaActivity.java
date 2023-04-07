package algonquin.cst2335.finalproject;

import android.app.Activity;
import android.content.Context;
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
import java.io.FileOutputStream;
import java.io.IOException;
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
    ArrayList<RoverItem> roverList;
    RecyclerView recyclerView;
    RequestQueue queue;
    RoverItem rover;
    File file;
    String pathname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNasaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        roverList = new ArrayList<RoverItem>();
//        model = new ViewModelProvider(this).get(RoverModel.class);
//        roverList = model.roverList.getValue();
        recyclerView = binding.recycler;
        queue = Volley.newRequestQueue(this);
//        if (roverList == null) {
//            model.roverList.postValue(roverList = new ArrayList<RoverItem>());
//        }

        binding.search.setOnClickListener((clk) -> {
            date = binding.date.getText().toString();
            String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + date +
                    "&api_key=zf1BVnH0bXnoSoQfq5RQdl39UKlyCHKXKOurI2TC";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONArray photosArray = response.getJSONArray("photos");
                            JSONObject position = photosArray.getJSONObject(20);
                            JSONObject roverObject = position.getJSONObject("rover");
                            String roverName = roverObject.getString("name");
                            String imgURL = toHttps(position.getString("img_src"));
                            rover = new RoverItem(roverName, imgURL);
                            ImageRequest imgReq = new ImageRequest(imgURL,
                                    (bitmap) -> {
                                        Bitmap image;
                                        String fileName = imgURL.substring(imgURL.length()-7);
                                        try{
                                            pathname = getFilesDir() + "/" + fileName;
                                            file = new File(pathname);
                                            if (file.exists()){
                                                image = BitmapFactory.decodeFile(pathname);
                                                rover.setImage(image);
                                            }
                                            else{
                                                image = bitmap;
                                                image.compress(Bitmap.CompressFormat.PNG, 100, NasaActivity.this.openFileOutput(
                                                        fileName, Activity.MODE_PRIVATE));
                                                rover.setImage(image);
                                            }

                                        }catch(FileNotFoundException e){e.printStackTrace();}
                                    },
                                    1024,1024,ImageView.ScaleType.CENTER, null,
                                    (error) -> {});
                            queue.add(imgReq);
                                roverList.add(rover);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) ->{});
            queue.add(request);
            myAdapter.notifyItemInserted(roverList.size()-1);

        });
        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView roverName;
            ImageView roverImage;
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                roverName = itemView.findViewById(R.id.roverName);
                roverImage = itemView.findViewById(R.id.roverImage);
            }
        }
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                NasaRowBinding binding = NasaRowBinding.inflate(getLayoutInflater());
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                RoverItem rover = roverList.get(position);
                holder.roverName.setText(rover.getRoverName());
                if(rover.getImage() != null){
                    holder.roverImage.setImageBitmap(rover.getImage());
                }

            }

            @Override
            public int getItemCount() {
                return roverList.size();
            }
        });
    }

    private boolean validate(int date) {
        if (date >= 0 && date <= 1000) {
            return true;
        }
        return false;
    }
    private String toHttps(String url){
        String sub = url.substring(4,url.length());
        return "https" + sub;
    }
}