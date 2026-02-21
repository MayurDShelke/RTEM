package com.mayurshelke.rtem5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity {

    private RecyclerView foodRecyclerView;
    private EditText quantityEditText;
    private Button addButton;
    private TextView totalCostTextView;

    private List<String> foodItems;
    private List<Integer> foodCosts;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        foodRecyclerView = findViewById(R.id.foodRecyclerView);
        quantityEditText = findViewById(R.id.quantityEditText);
        addButton = findViewById(R.id.addButton);
        totalCostTextView = findViewById(R.id.totalCostTextView);

        // Initialize food items and costs
        foodItems = new ArrayList<>();
        foodItems.add("Burger");
        foodItems.add("Pizza");
        foodItems.add("Sandwich");
        foodItems.add("Spl Tea");
        foodItems.add("Coffee");
        foodItems.add("Idli");
        foodItems.add("Mendu Wada");
        foodItems.add("Dosa");
        foodItems.add("Masala Dosa");
        foodItems.add("Upma");
        foodItems.add("Vadapav");
        foodItems.add("Usal");
        foodItems.add("Misal");
        foodItems.add("Finger Chips");



        foodCosts = new ArrayList<>();
        foodCosts.add(40); // Burger cost
        foodCosts.add(70); // Pizza cost
        foodCosts.add(30); // Sandwich cost
        foodCosts.add(25); //Spl Tea
        foodCosts.add(30);//cofee
        foodCosts.add(50);//idli
        foodCosts.add(60);//mendu wada
        foodCosts.add(40);//dosa
        foodCosts.add(60);//Masala dosa
        foodCosts.add(40);//upma
        foodCosts.add(30);//vadpav
        foodCosts.add(25);//usal
        foodCosts.add(40);//misal
        foodCosts.add(75);//finger chips


        // Initialize RecyclerView and adapter
        foodAdapter = new FoodAdapter(foodItems, foodCosts);
        foodRecyclerView.setAdapter(foodAdapter);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected food items
                List<Boolean> selectedItems = foodAdapter.getSelectedItems();
                List<String> selectedFoodItems = new ArrayList<>();
                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i)) {
                        selectedFoodItems.add(foodItems.get(i));
                    }
                }

                if (selectedFoodItems.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "Please select at least one food item", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get quantity
                int quantity = Integer.parseInt(quantityEditText.getText().toString());

                // Calculate total cost and display
                int totalCost = calculateTotalCost(selectedFoodItems, quantity);
                totalCostTextView.setText("Total Cost: ₹" + totalCost);
            }
        });
    }

    private int calculateTotalCost(List<String> selectedFoodItems, int quantity) {
        int totalCost = 0;
        for (int i = 0; i < selectedFoodItems.size(); i++) {
            totalCost += foodCosts.get(foodItems.indexOf(selectedFoodItems.get(i))) * quantity;
        }
        return totalCost;
    }
}
