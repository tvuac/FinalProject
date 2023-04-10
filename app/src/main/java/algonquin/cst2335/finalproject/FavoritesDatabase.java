package algonquin.cst2335.finalproject;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImageDetails.class}, version = 1)
public abstract class FavoritesDatabase extends RoomDatabase
{
    public abstract ImageDetailsDao imageDetailsDao();
}
