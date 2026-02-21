package com.mayurshelke.rtem5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class calculateFragment extends Fragment {

    private LinearLayout expenseLinearLayout;
    private static final String TAG = "calculateFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculate, container, false);

        expenseLinearLayout = view.findViewById(R.id.expenseLinearLayout);

        // Retrieve expense data from Firebase
        retrieveExpenseData();

        // Add Button and set OnClickListener
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start TotalActivity
                startActivity(new Intent(requireContext(), totalActivity.class));
            }
        });

        // Find and set OnClickListener for the food cost button
        Button foodCostButton = view.findViewById(R.id.buttonFoodCostActivity);
        foodCostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to MainActivity2
                startActivity(new Intent(requireContext(), MainActivity2.class));
            }
        });

        return view;
    }

    private void retrieveExpenseData() {
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("expenses");

        expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through expense data
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    if (expense != null) {
                        // Create a new CardView for each expense item
                        CardView cardView = new CardView(requireContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 0, 0, 16); // Set margin bottom to create space between card views
                        cardView.setLayoutParams(params);
                        cardView.setRadius(16); // Set corner radius
                        cardView.setCardElevation(8); // Set card elevation
                        cardView.setContentPadding(32, 32, 32, 32); // Set content padding
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.card_background_color)); // Set card background color

                        // Create a new TextView to display the expense details including title
                        TextView textView = new TextView(requireContext());
                        textView.setLayoutParams(new CardView.LayoutParams(
                                CardView.LayoutParams.MATCH_PARENT,
                                CardView.LayoutParams.WRAP_CONTENT
                        ));
                        if (expense.getTripTitle() != null) {
                            textView.setText("Trip Title: " + expense.getTripTitle() + "\n" +
                                    "Distance: " + expense.getDistance() + "\n" +
                                    "Mileage: " + expense.getMileage() + "\n" +
                                    "Toll: " + expense.getTollExpense() + "\n" +
                                    "Food Cost: " + expense.getFoodCost() + "\n" +
                                    "Total Expense: " + expense.getTotalExpense());
                        }
                        textView.setTextColor(getResources().getColor(R.color.text_color)); // Set text color

                        // Add the TextView to the CardView
                        cardView.addView(textView);

                        // Add the CardView to the LinearLayout
                        expenseLinearLayout.addView(cardView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void saveExpenseToFirebase(String triptitle, double distance, double mileage, double petrolPrice,
                                       double tollExpense, double foodCost, double totalExpense) {
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("expenses");
        String expenseId = expensesRef.push().getKey();
        Expense expense = new Expense(triptitle,distance, mileage, petrolPrice, tollExpense, foodCost, totalExpense);
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
