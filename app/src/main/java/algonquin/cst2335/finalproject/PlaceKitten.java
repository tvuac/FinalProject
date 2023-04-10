package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import algonquin.cst2335.finalproject.databinding.ActivityPlaceKittenBinding;
import algonquin.cst2335.finalproject.ui.MainActivity;

public class PlaceKitten extends AppCompatActivity implements ImageAdapter.ItemClickListener {

    private ActivityPlaceKittenBinding binding;

    EditText heightEditText, widthEditText;
    Button submitButton, saveButton;
    ImageView kittenImageView;
    RecyclerView recyclerView;
    ImageAdapter adapter;
    List<Bitmap> images;
    List<ImageDetails> imageDetailsList;

    FavoritesDatabase favoritesDatabase;
    ImageDetailsDao imageDetailsDao;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.kitten_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() )
        {
            case R.id.nasaTool:
                Intent newApp = new Intent(this, NasaActivity.class);
                startActivity(newApp);
                break;
            case R.id.weatherTool:
                Intent newApp2 = new Intent(this, MainActivity.class);
                startActivity(newApp2);
        }

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceKittenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar2);

        heightEditText = binding.heightText;
        widthEditText = binding.widthText;
        submitButton = binding.searchButton;
        kittenImageView = binding.kittenPicture;
        saveButton = binding.saveButton;
        recyclerView = binding.favoritesViewer;


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (imageDetailsList == null)
        {
            imageDetailsList = new ArrayList<ImageDetails>();
        }
        else
        {
            imageDetailsList = imageDetailsDao.getAll();
        }

        images = new ArrayList<>();
        adapter = new ImageAdapter( images);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        favoritesDatabase = Room.databaseBuilder(getApplicationContext(),
                FavoritesDatabase.class, "my-db").build();
        imageDetailsDao = favoritesDatabase.imageDetailsDao();

        submitButton.setOnClickListener(clk ->
        {
            int height = Integer.parseInt(heightEditText.getText().toString());
            int width = Integer.parseInt(widthEditText.getText().toString());
            String imageUrl = "https://placekitten.com/" + width + "/" + height;

            RequestQueue requestQueue = Volley.newRequestQueue(PlaceKitten.this);
            ImageRequest imageRequest = new ImageRequest(imageUrl,
                    new Response.Listener<android.graphics.Bitmap>()
                    {
                        @Override
                        public void onResponse(android.graphics.Bitmap response)
                        {
                            kittenImageView.setImageBitmap(response);
                        }
                    }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    // Handle error
                }
            });
            requestQueue.add(imageRequest);
        });

        saveButton.setOnClickListener(clk ->
        {
            int height = Integer.parseInt(heightEditText.getText().toString());
            int width = Integer.parseInt(widthEditText.getText().toString());

            BitmapDrawable drawable = (BitmapDrawable) kittenImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            saveImage(bitmap);
            images.add(bitmap);

            Date date = Calendar.getInstance().getTime();
            String dateTime = date.toString();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                ImageDetails imageDetails = new ImageDetails(width, height, dateTime);
                imageDetailsDao.insert(imageDetails);
                imageDetailsList.add(imageDetails);
            });

            adapter.notifyDataSetChanged();
            
        });
    }

    private void saveImage(Bitmap bitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/placekitten");
        myDir.mkdirs();
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Bitmap bitmap = images.get(position);

    }

}


