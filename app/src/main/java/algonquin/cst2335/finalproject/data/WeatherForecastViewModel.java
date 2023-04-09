package algonquin.cst2335.finalproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class WeatherForecastViewModel extends ViewModel {
    public MutableLiveData<ArrayList<WeatherMessage>> cities = new MutableLiveData<>();
}
