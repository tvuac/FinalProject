package algonquin.cst2335.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.finalproject.databinding.NasaRowBinding;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyRowHolder> {
    private Context context;
    private ArrayList<RoverItem> roverItems;

    public MyAdapter(ArrayList<RoverItem> roverItems, Context context){
        this.context = context;
        this.roverItems = roverItems;
    }
    public class MyRowHolder extends RecyclerView.ViewHolder{
        ImageView roverImage;
        TextView roverName;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            roverImage = itemView.findViewById(R.id.roverImage);
            roverName = itemView.findViewById(R.id.roverName);
        }
    }
    @NonNull
    @Override
    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nasa_row,parent,false);
        return new MyRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
        RoverItem rover = roverItems.get(position);

        holder.roverName.setText(rover.getRoverName());
        holder.roverImage.setImageBitmap(rover.getImage());
    }

    @Override
    public int getItemCount() {
        return roverItems.size();
    }
}
