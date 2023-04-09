package algonquin.cst2335.finalproject.data;
public class WeatherMessage {
        private String city;
        private String dateSent;
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
