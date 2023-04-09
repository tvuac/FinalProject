package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoverItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;
    @ColumnInfo(name="roverName")
    private String roverName;
    @ColumnInfo(name="imgURL")
    private String imgURL;
    @ColumnInfo(name="image")
    private Bitmap image;
    @ColumnInfo(name="cameraName")
    private String cameraName;

    public RoverItem(String roverName, String imgURL, String cameraName){
        this.roverName = roverName;
        this.imgURL = imgURL;
        this.cameraName = cameraName;
    }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
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
