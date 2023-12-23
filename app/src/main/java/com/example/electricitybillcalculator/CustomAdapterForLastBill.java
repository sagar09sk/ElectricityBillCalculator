package com.example.electricitybillcalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterForLastBill extends RecyclerView.Adapter<CustomAdapterForLastBill.MyViewHolder> {

    private Context context;
    private ArrayList profile_id , profile_name ,profile_amount ;

    CustomAdapterForLastBill(Context context , ArrayList bill_id , ArrayList bill_name , ArrayList bill_amount){
        this.context = context;
        this.profile_id = bill_id;
        this.profile_name = bill_name;
        this.profile_amount = bill_amount;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_for_last_bill,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.idInfoTextView.setText(String.valueOf(profile_id.get(position)));
        holder.nameInfoTextView.setText(String.valueOf(profile_name.get(position)));
        holder.amountInfoTextView.setText(String.valueOf(profile_amount.get(position)));
    }

    @Override
    public int getItemCount() {
        return profile_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView idInfoTextView, nameInfoTextView, amountInfoTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idInfoTextView = itemView.findViewById(R.id.idInfoTextView);
            nameInfoTextView = itemView.findViewById(R.id.nameInfoTextView);
            amountInfoTextView = itemView.findViewById(R.id.amountInfoTextView);
        }
    }
}
