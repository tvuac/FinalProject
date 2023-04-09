package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {RoverItem.class}, version = 3)
@TypeConverters(Converters.class)
public abstract class RoverDatabase extends RoomDatabase {
    public abstract RoverItemDAO rDAO();
}
