package algonquin.cst2335.finalproject.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The database for the weather forecast app and stores the weather message objects
 */
@Database(entities = {WeatherMessage.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    /**
     * The abstraction function for WeatherMessageDAO class
     * @return Returns the weatherMessageDAO
     */
    public abstract WeatherMessageDAO weatherMessageDAO();
}
