package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDetailsDao
{
    @Insert
    void insert(ImageDetails imageDetails);

    @Query("SELECT * FROM image_details")
    List<ImageDetails> getAll();
}
