package com.mayurshelke.rtem5;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment calculateFragment;
    private Fragment placesFragment;
    private Fragment profileFragment;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                switchFragment(homeFragment);
                return true;
            } else if (id == R.id.navigation_calculate) {
                switchFragment(calculateFragment);
                return true;
            } else if (id == R.id.navigation_places) {
                switchFragment(placesFragment);
                return true;
            } else if (id == R.id.navigation_profile) {
                switchFragment(profileFragment);
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initializeFragments();
    }

    private void initializeFragments() {
        homeFragment = new homeFragment();
        calculateFragment = new calculateFragment();
        placesFragment = new placesFragment();
        profileFragment = new profileFragment();

        switchFragment(homeFragment); // Set home fragment as default
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
