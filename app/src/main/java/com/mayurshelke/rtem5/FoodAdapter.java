package com.mayurshelke.rtem5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<String> foodItems;
    private List<Integer> foodCosts; // Add a list to store the costs of each food item
    private List<Boolean> selectedItems;

    public FoodAdapter(List<String> foodItems, List<Integer> foodCosts) {
        this.foodItems = foodItems;
        this.foodCosts = foodCosts;
        this.selectedItems = new ArrayList<>();
        for (int i = 0; i < foodItems.size(); i++) {
            selectedItems.add(false);
        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        String foodItem = foodItems.get(position);
        int foodCost = foodCosts.get(position); // Get the cost for the current food item
        holder.bind(foodItem, foodCost, position);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public List<Boolean> getSelectedItems() {
        return selectedItems;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView costTextView; // Add a TextView to display the cost

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            costTextView = itemView.findViewById(R.id.costTextView); // Initialize the cost TextView
        }

        public void bind(final String foodItem, int foodCost, final int position) {
            checkBox.setText(foodItem);
            checkBox.setChecked(selectedItems.get(position));
            costTextView.setText("$" + foodCost); // Set the cost text

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectedItems.set(position, isChecked);
                }
            });
        }
    }
}

