package algonquin.cst2335.finalproject.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The weather message class will save the city entered into the database
 */
@Entity
public class WeatherMessage {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name="id")
        public long id;

        @ColumnInfo(name="city")
        public String city;

        @ColumnInfo(name="DateSent")
        public String dateSent;

        @ColumnInfo(name="searchButton")
        public boolean searchButton;

        public WeatherMessage(String city, String dateSent, boolean searchButton)
        {
            this.city = city;
            this.dateSent = dateSent;
            this.searchButton = searchButton;
        }

        public String getCity() {
            return city;
        }

        public String getDateSent() {
            return dateSent;
        }

        public boolean searchButton() {
            return searchButton;
        }
}
