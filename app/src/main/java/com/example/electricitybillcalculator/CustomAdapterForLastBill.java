package com.example.electricitybillcalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterForLastBill extends RecyclerView.Adapter<CustomAdapterForLastBill.MyViewHolder> {

    private Context context;
    private ArrayList nameList , dateList ,amountList ;

    CustomAdapterForLastBill(Context context , ArrayList nameList , ArrayList dateList , ArrayList amountList){
        this.context = context;
        this.nameList = nameList;
        this.dateList = dateList;
        this.amountList = amountList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.last_all_bills_row_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewName.setText("ID : "+nameList.get(position));
        holder.textViewDate.setText(""+dateList.get(position));
        holder.textViewAmount.setText("Rs : "+amountList.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            String name = (String) nameList.get(position);
            String date = (String) dateList.get(position);
            String amount = (String) amountList.get(position);
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(" ID :  "+name);
                builder.setMessage(" Date :  "+date+"\n Rs :  "+amount);
                builder.setPositiveButton(" add new Bill ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context,BillGenerateActivity.class);
                        intent.putExtra("Name" ,name);
                        intent.putExtra("Date",date);
                        context.startActivity(intent);
                    }
                });

                builder.setNeutralButton(" OK ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName, textViewDate, textViewAmount;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
