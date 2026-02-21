package com.mayurshelke.rtem5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

// placesFragment.java
public class placesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places, container, false);

        Button buttonPlaceActivity = rootView.findViewById(R.id.buttonPlaceActivity);
        buttonPlaceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click to launch PlaceActivity
                Intent intent = new Intent(getActivity(), placeActivity.class);
                startActivity(intent);
            }
        });

        Button buttonHotelActivity = rootView.findViewById(R.id.buttonHotelActivity);
        buttonHotelActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click to launch HotelActivity
                Intent intent = new Intent(getActivity(), hotelActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}

