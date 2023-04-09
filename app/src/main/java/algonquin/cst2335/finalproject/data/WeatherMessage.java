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
        public int id;

        @ColumnInfo(name="city")
        private String city;

        @ColumnInfo(name="DateSent")
        private String dateSent;

        @ColumnInfo(name="searchButton")
        private boolean searchButton;

        public WeatherMessage(String city, String date, boolean search)
        {
            this.city = city;
            this.dateSent = date;
            this.searchButton = search;
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
