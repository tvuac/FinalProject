package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;

public class RoverItem {
    private String roverName;
    private Bitmap image;

    public RoverItem(String roverName, Bitmap image){
        this.roverName = roverName;
        this.image = image;
    }
    public String getRoverName(){
        return roverName;
    }
    public Bitmap getImage(){
        return image;
    }
}
