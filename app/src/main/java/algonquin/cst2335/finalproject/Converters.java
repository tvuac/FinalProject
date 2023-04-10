package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class Converters {
    @TypeConverter
    public byte[] fromBitmap(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }
    @TypeConverter
    public Bitmap toBitmap(byte[] byteArray){
       return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
