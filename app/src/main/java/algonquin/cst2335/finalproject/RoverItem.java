package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;

public class RoverItem {
    private String roverName;
    private String imgURL;
    private Bitmap image;

    public RoverItem(String roverName, String imgURL){
        this.roverName = roverName;
        this.imgURL = imgURL;
    }
    public String getRoverName(){
        return roverName;
    }
    public Bitmap getImage(){
        return image;
    }
    public String getImgURL(){
        return imgURL;
    }
    public void setImage(Bitmap image){
        this.image = image;
    }
}
