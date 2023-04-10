package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class RoverModel extends ViewModel {
    public MutableLiveData<ArrayList<RoverItem>> roverList = new MutableLiveData<>();
    public MutableLiveData<RoverItem> selectedRover = new MutableLiveData<>();
}
