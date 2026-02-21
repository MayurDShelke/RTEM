package com.mayurshelke.rtem5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;



import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class homeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView searchView;
    private TextView distanceTextView;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private Polyline currentPolyline;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 101;
    private static final LatLng INDIA_LOCATION = new LatLng(20.5937, 78.9629);
    private static final float DEFAULT_ZOOM_LEVEL = 5.0f;

    private EditText distanceEditText;
    private EditText tollEditText;
    private EditText foodCostEditText;
    private EditText tripTitleEditText;
    private TextView totalExpenseTextView;

    private Spinner carModelSpinner;

    // Fixed petrol price
    private static final double PETROL_PRICE = 103;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchView = view.findViewById(R.id.search_view);
        distanceTextView = view.findViewById(R.id.distance_text);

        distanceEditText = view.findViewById(R.id.distanceEditText);
        tollEditText = view.findViewById(R.id.tollEditText);
        foodCostEditText = view.findViewById(R.id.foodCostEditText);
        totalExpenseTextView = view.findViewById(R.id.totalExpenseTextView);
        tripTitleEditText = view.findViewById(R.id.tripTitleEditText);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize carModelSpinner and set its adapter
        carModelSpinner = view.findViewById(R.id.carModelSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.car_models_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carModelSpinner.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submission
                // Calculate distance between current location and searched location
                if (currentLocation != null) {
                    // Get destination location from query (You need to implement this part)
                    // For example, convert the query to LatLng using Geocoding
                    LatLng destinationLatLng = getLatLngFromQuery(query);

                    if (destinationLatLng != null) {
                        // Calculate distance between current and destination locations
                        float distance = calculateDistance(currentLocation.getLatitude(), currentLocation.getLongitude(),
                                destinationLatLng.latitude, destinationLatLng.longitude);
                        // Update distanceEditText with calculated distance
                        distanceEditText.setText(String.valueOf(distance));
                        // Display distance in kilometers
                        distanceTextView.setText("Distance: " + distance + " kilometers");
                        distanceTextView.setVisibility(View.VISIBLE);

                        // Add marker for the searched location on the map
                        mMap.clear(); // Clear existing markers
                        mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Searched Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, DEFAULT_ZOOM_LEVEL));

                        // Fetch and draw route between current and destination locations
                        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        fetchRouteAndDraw(currentLatLng, destinationLatLng);
                    } else {
                        Toast.makeText(requireContext(), "Destination not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Current location not available", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change
                return false;
            }
        });

        // Calculate and display total expense when the button is clicked
        Button calculateButton = view.findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTotalExpense();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }

        // Enable current location on the map
        mMap.setMyLocationEnabled(true);

        // Move camera to India
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INDIA_LOCATION, DEFAULT_ZOOM_LEVEL));

        // Get current location
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                }
            }
        });
    }

    private float calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] / 1000; // Distance in kilometers
    }

    private LatLng getLatLngFromQuery(String query) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fetchRouteAndDraw(final LatLng origin, final LatLng destination) {
        // Use Directions API to fetch route data
        String url = getDirectionsUrl(origin, destination);

        // Execute AsyncTask to fetch and parse route data
        new FetchRouteTask().execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        // Build URL for Directions API request
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destStr = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=driving"; // You can specify the mode of transportation here
        String apiKey = ""; // Replace with your API key
        String params = originStr + "&" + destStr + "&" + mode + "&key=" + apiKey;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + params;
        return url;
    }

    private class FetchRouteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                // Fetch route data from Directions API
                data = fetchRouteData(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Parse route data and draw polyline
            if (result != null) {
                List<LatLng> points = parseRouteData(result);
                drawPolyline(points);
            }
        }
    }

    private String fetchRouteData(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert iStream != null;
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private List<LatLng> parseRouteData(String jsonData) {
        List<LatLng> points = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject route = routesArray.getJSONObject(i);
                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                points.addAll(decodePolyline(polyline));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return points;
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    private void drawPolyline(List<LatLng> points) {
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(Color.RED);
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline(lineOptions);
    }

    private void calculateTotalExpense() {
        // Get input values
        double distance = Double.parseDouble(distanceEditText.getText().toString());
        double tollExpense = Double.parseDouble(tollEditText.getText().toString());
        double foodCost = Double.parseDouble(foodCostEditText.getText().toString());

        // Get selected car model from spinner
        String selectedCarModel = carModelSpinner.getSelectedItem().toString();
        // Get mileage for the selected car model
        double carModelMileage = getMileageForCarModel(selectedCarModel);

        String tripTitle = tripTitleEditText.getText().toString();

        // Perform calculations
        double petrolExpense = (distance / carModelMileage) * PETROL_PRICE;
        double totalExpense = petrolExpense + tollExpense + foodCost;

        // Display total expense
        totalExpenseTextView.setText(String.format(Locale.getDefault(), "Total Expense: %.2f", totalExpense));

        // Save data to Firebase
        saveExpenseToFirebase(tripTitle, distance, carModelMileage, PETROL_PRICE, tollExpense, foodCost, totalExpense);
    }

    // Method to retrieve mileage for a given car model (You need to implement this)
    private double getMileageForCarModel(String carModel) {
        // Add your logic to retrieve mileage based on the selected car model
        switch (carModel) {
            case "Punch":
                return 20; // Predefined mileage for Car Model 1
            case "Thar":
                return 14; // Predefined mileage for Car Model 2
            case "Vitara Brezza":
                return 18; // Predefined mileage for Car Model 3
            case "Altroz":
                return 23;
            case "Alto 800":
                return 29;
            case "Xuv 700":
                return 15;
            case "Creta":
                return 21;
            case "Verna":
                return 21;
            case "Innova Crysta":
                return 8;
            case "Swift Dzire":
                return 23;
            default:
                return 0; // Default mileage value
        }
    }

    private void saveExpenseToFirebase(String tripTitle, double distance, double mileage, double petrolPrice,
                                       double tollExpense, double foodCost, double totalExpense) {
        // Create a reference to your Firebase database
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("expenses");

        // Create a unique key for the expense entry
        String expenseId = expensesRef.push().getKey();

        // Create an Expense object
        Expense expense = new Expense(tripTitle, distance, mileage, petrolPrice, tollExpense, foodCost, totalExpense);

        // Save the expense object to Firebase Realtime Database
        expensesRef.child(expenseId).setValue(expense)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Expense saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Failed to save expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
