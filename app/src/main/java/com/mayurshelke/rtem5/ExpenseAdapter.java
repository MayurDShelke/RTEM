package com.mayurshelke.rtem5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Double> expensesList;

    public ExpenseAdapter(List<Double> expensesList) {
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double expense = expensesList.get(position);
        holder.expenseTextView.setText(String.format("$%.2f", expense));
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView expenseTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTextView = itemView.findViewById(R.id.text_view_expense);
        }
    }
}

