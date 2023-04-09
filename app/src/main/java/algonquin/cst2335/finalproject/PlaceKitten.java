package algonquin.cst2335.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import algonquin.cst2335.finalproject.databinding.ActivityPlaceKittenBinding;

public class PlaceKitten extends AppCompatActivity implements ImageAdapter.ItemClickListener {

    private ActivityPlaceKittenBinding binding;

    EditText heightEditText, widthEditText;
    Button submitButton, saveButton;
    ImageView kittenImageView;
    RecyclerView recyclerView;
    ImageAdapter adapter;
    List<Bitmap> images;

    ArrayList<String> favoriteUrls;
    RecyclerView.Adapter myAdapter;
    RecyclerView favoritesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceKittenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        heightEditText = binding.heightText;
        widthEditText = binding.widthText;
        submitButton = binding.searchButton;
        kittenImageView = binding.kittenPicture;
        saveButton = binding.saveButton;
        recyclerView = binding.favoritesViewer;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        images = new ArrayList<>();
        adapter = new ImageAdapter( images);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

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
            BitmapDrawable drawable = (BitmapDrawable) kittenImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            saveImage(bitmap);
            images.add(bitmap);
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
    public void onItemClick(View view, int position) {
        Bitmap bitmap = images.get(position);
        kittenImageView.setImageBitmap(bitmap);
    }

}