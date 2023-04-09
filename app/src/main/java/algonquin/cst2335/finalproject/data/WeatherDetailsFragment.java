package algonquin.cst2335.finalproject.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.DetailsLayoutBinding;

public class WeatherDetailsFragment extends Fragment {

    WeatherMessage clickedMessage; //changed to selected if it creashes

    public WeatherDetailsFragment(WeatherMessage weather) {
        clickedMessage = weather;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        binding.cityText.setText(clickedMessage.city);
        binding.databaseText.setText(clickedMessage.dateSent);
        binding.databaseText.setText("Id = " +  clickedMessage.id);

        return binding.getRoot();
    }
}
