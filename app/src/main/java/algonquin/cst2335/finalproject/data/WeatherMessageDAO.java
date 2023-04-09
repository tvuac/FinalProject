package algonquin.cst2335.finalproject.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * This is the data access object for the room
 */
@Dao
public interface WeatherMessageDAO {

    /**
     * This will insert a weather message object and return the id
     * @param city The city entered into the database
     * @return Return the id
     */
    @Insert
    public long insertMessage(WeatherMessage city);

    /**
     * This will get all the messages from weather message class
     * @return Return the messages
     */
    @Query("Select * from WeatherMessage")
    public List<WeatherMessage> getAllMessages();

    /**
     * Delete the city from the database
     * @param city The city entered into the database
     */
    @Delete
    public void deleteMessage(WeatherMessage city);
}
