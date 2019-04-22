package com.example.offertaadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.offertaadmin.OrderDetailsActivity;
import com.example.offertaadmin.R;
import com.example.offertaadmin.model.Order;

import java.util.List;



public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {
    private Context context;
    private List<Order> objects;

    public OrdersAdapter(Context context, List<Order> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {


        // row background color
        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.greyLayoutBg);
        } else
            holder.itemView.setBackgroundResource(android.R.color.white);
        //


        holder.tvOrderId.setText(objects.get(i).getId() + "");
        holder.tvDate.setText(objects.get(i).getDate());
        holder.tvUsername.setText(objects.get(i).getUser_name());
        holder.tvStatus.setText(objects.get(i).getStatus_text());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context , OrderDetailsActivity.class);
                Log.e("orderIDAdapter",objects.get(i).getId()+"");
                intent.putExtra("orderId" , objects.get(i).getId()+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvUsername, tvStatus, tvOrderId;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }

}
