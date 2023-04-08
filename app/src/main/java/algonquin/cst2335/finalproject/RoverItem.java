package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;

public class RoverItem {
    private String roverName;
    private String imgURL;
    private Bitmap image;
    private String cameraName;

    public RoverItem(String roverName, String imgURL, String cameraName){
        this.roverName = roverName;
        this.imgURL = imgURL;
        this.cameraName = cameraName;
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
    public String getCameraName(){return cameraName;}
    public void setImage(Bitmap image){
        this.image = image;
    }
}
