package algonquin.cst2335.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityNasaBinding;
import algonquin.cst2335.finalproject.databinding.NasaRowBinding;
import algonquin.cst2335.finalproject.ui.MainActivity;

public class NasaActivity extends AppCompatActivity {
    private ActivityNasaBinding binding;
    private RecyclerView.Adapter myAdapter;
    private String date;
    ArrayList<RoverItem> roverList;
    RecyclerView recyclerView;
    RequestQueue queue;
    RoverItem rover;
    RoverModel model;
    RoverItemDAO rDAO;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.nasa_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() )
        {
            case R.id.kittenTool:
                Intent newApp = new Intent(this, PlaceKitten.class);
                startActivity(newApp);
                break;
            case R.id.weatherTool2:
                Intent newApp2 = new Intent(this, MainActivity.class);
                startActivity(newApp2);
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNasaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(RoverModel.class);
        roverList = model.roverList.getValue();
        if (roverList == null) {
            model.roverList.postValue(roverList = new ArrayList<RoverItem>());
        }
        queue = Volley.newRequestQueue(this);
        binding.fav.setOnClickListener((click) -> {
            Intent nextPage = new Intent( NasaActivity.this, NasaFavActivity.class);
            NasaActivity.this.startActivity(nextPage);
        });
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        binding.date.setText(prefs.getString("date_value", ""));
        binding.search.setOnClickListener((clk) -> {
            roverList = null;
            model.roverList.setValue(roverList = new ArrayList<RoverItem>());
            myAdapter.notifyDataSetChanged();
            date = binding.date.getText().toString();
            if (!date.equals("")) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("date_value", date);
                editor.apply();
                int numDate = Integer.valueOf(date);
                binding.date.setText("");
                if (validate(numDate)) {
                    String url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + date +
                            "&api_key=zf1BVnH0bXnoSoQfq5RQdl39UKlyCHKXKOurI2TC";
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response) -> {
                                try {
                                    ArrayList<RoverItem> temp = new ArrayList<>();
                                    JSONArray photosArray = response.getJSONArray("photos");
                                    for (int i = 0; i < photosArray.length(); i++) {
                                        JSONObject position = photosArray.getJSONObject(i);
                                        JSONObject roverObject = position.getJSONObject("rover");
                                        JSONObject camera = position.getJSONObject("camera");
                                        String roverName = "Rover Name: " + roverObject.getString("name");
                                        String cameraName = "Camera Name: " + camera.getString("name");
                                        String imgURL = toHttps(position.getString("img_src"));
                                        temp.add(new RoverItem(roverName, imgURL, cameraName));
                                    }
                                    for (RoverItem tempItem : temp) {
                                        String tempURL = tempItem.getImgURL();
                                        ImageRequest imgReq = new ImageRequest(tempURL,
                                                (bitmap) -> {

                                                    Executor thread = Executors.newSingleThreadExecutor();
                                                    thread.execute(() -> {
                                                        Bitmap image;
                                                        String fileName = getFilename(tempURL);
                                                        try {
                                                            String pathname = getFilesDir() + "/" + fileName;
                                                            tempItem.setPathname(pathname);
                                                            File file = new File(pathname);
                                                            if (file.exists()) {
                                                                image = BitmapFactory.decodeFile(pathname);
                                                                tempItem.setImage(image);
                                                            } else {
                                                                image = bitmap;
                                                                image.compress(Bitmap.CompressFormat.PNG, 50, NasaActivity.this.openFileOutput(
                                                                        fileName, Activity.MODE_PRIVATE));
                                                                tempItem.setImage(image);
                                                            }

                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        }
                                                    });
                                                },
                                                1024, 1024, ImageView.ScaleType.CENTER, null,
                                                (error) -> {
                                                });
                                        queue.add(imgReq);
                                        roverList.add(tempItem);
                                        runOnUiThread(() -> {
                                            myAdapter.notifyItemInserted(roverList.size() - 1);
                                        });
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            (error) -> {
                            });
                    queue.add(request);
                } else {
                    Toast.makeText(this, "Please enter a number between 0 and 1000", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setSupportActionBar(binding.toolbarNasa);
        binding.help.setOnClickListener((clk) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This is a NASA Mars Rover photos application," +
                    " you can type a date from 0 to 1000 into the input bar, then press search" +
                    " to retrieve all photos from that day along with their information.\n\n" +
                    "You can also click on any photo to see the entire photo along with more information " +
                    "about the photo.\n\n" +
                    "To view your saved photos, press favourites at the top left");
            builder.setTitle("Help").
                    setPositiveButton("Ok", (dialog, cl) -> {}).create().show();
        });

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView roverName;
            ImageView roverImage;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                roverName = itemView.findViewById(R.id.roverName);
                roverImage = itemView.findViewById(R.id.roverImage);
                itemView.setOnClickListener((clk) -> {
                    int position = getAbsoluteAdapterPosition();
                    RoverItem selected = roverList.get(position);
                    model.selectedRover.postValue(selected);

                });
            }

        }
        model.selectedRover.observe((this), (newRoverValue) -> {
            RoverFragment fragment = new RoverFragment(newRoverValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.roverFragment, fragment)
                    .addToBackStack("")
                    .commit();
        });
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
                if (rover.getImage() != null) {
                    Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(rover.getPathname()), 100, 100);
                    holder.roverImage.setImageBitmap(thumbnail);
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
    public String getFilename(String url){
        String[] splitURL = url.split("/");
        return splitURL[splitURL.length - 1];
    }

}