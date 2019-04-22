package com.example.offertaadmin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.offertaadmin.R;
import com.example.offertaadmin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private Context context;
    private List<Product> objects;

    public ProductAdapter(Context context, List<Product> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        holder.tvPName.setText(objects.get(i).getName());
        holder.tvPID.setText(objects.get(i).getId() + "");
        holder.tvPPrice.setText(objects.get(i).getPrice() + "");
        holder.tvPQuantity.setText("X" + objects.get(i).getQuantity());

        Picasso.get().load(objects.get(i).getImage()).fit().centerInside().into(holder.imgProduct);

        if (i == objects.size() - 1)
            holder.dividerItemProduct.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPName, tvPID, tvPPrice, tvPQuantity;
        ImageView imgProduct;
        View dividerItemProduct;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPName = itemView.findViewById(R.id.tvPName);
            tvPID = itemView.findViewById(R.id.tvPID);
            tvPPrice = itemView.findViewById(R.id.tvPPrice);
            tvPQuantity = itemView.findViewById(R.id.tvPQuantity);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            dividerItemProduct = itemView.findViewById(R.id.dividerItemProduct);

        }
    }
}
