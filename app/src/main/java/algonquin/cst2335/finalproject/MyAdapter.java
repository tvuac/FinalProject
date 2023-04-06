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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RoverItem> roverItems;

    public MyAdapter(ArrayList<RoverItem> roverItems, Context context){
        this.context = context;
        this.roverItems = roverItems;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView roverImage;
        TextView roverName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            roverName = itemView.findViewById(R.id.roverName);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nasa_row,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoverItem rover = roverItems.get(position);

        holder.roverName.setText(rover.getRoverName());
        if(rover.getImage() != null){
            holder.roverImage.setImageBitmap(rover.getImage());
        }

    }

    @Override
    public int getItemCount() {
        return roverItems.size();
    }
}
