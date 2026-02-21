package com.mayurshelke.rtem5;

import android.os.Bundle;
import android.widget.TextView;
import java.util.Locale;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class totalActivity extends AppCompatActivity {

    private TextView totalExpenseTextView;
    private TextView petrolExpenseTextView;
    private TextView tollExpenseTextView;
    private TextView foodExpenseTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        petrolExpenseTextView = findViewById(R.id.petrolExpenseTextView);
        tollExpenseTextView = findViewById(R.id.tollExpenseTextView);
        foodExpenseTextView = findViewById(R.id.foodExpenseTextView);

        // Retrieve all trip data from Firebase
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("expenses");
        expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalPetrolExpense = 0;
                double totalTollExpense = 0;
                double totalFoodExpense = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    if (expense != null) {
                        //totalPetrolExpense += expense.getPetrolExpense();
                        totalTollExpense += expense.getTollExpense();
                        totalFoodExpense += expense.getFoodCost();
                    }
                }

                // Calculate total expense
                double totalExpense = totalPetrolExpense + totalTollExpense + totalFoodExpense;


// Format the double values as strings
                String totalExpenseString = String.format(Locale.getDefault(), "%.2f", totalExpense);
                String petrolExpenseString = String.format(Locale.getDefault(), "%.2f", totalPetrolExpense);
                String tollExpenseString = String.format(Locale.getDefault(), "%.2f", totalTollExpense);
                String foodCostString = String.format(Locale.getDefault(), "%.2f", totalFoodExpense);

// Set the formatted values to string resources
                totalExpenseTextView.setText(getString(R.string.total_expense, totalExpenseString));
                petrolExpenseTextView.setText(getString(R.string.petrol_expense, petrolExpenseString));
                tollExpenseTextView.setText(getString(R.string.toll_expense, tollExpenseString));
                foodExpenseTextView.setText(getString(R.string.food_expense, foodCostString));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
