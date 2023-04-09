package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoverItemDAO {
    @Insert
    public long insertRover(RoverItem rover);
    @Query("Select * from RoverItem")
    public List<RoverItem> getAllRovers();
    @Delete
    public void deleteRover(RoverItem rover);
}
