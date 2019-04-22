package com.example.offertaadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.offertaadmin.Print.PrintActivity;
import com.example.offertaadmin.adapter.ProductAdapter;
import com.example.offertaadmin.model.OrderDetails;
import com.example.offertaadmin.model.Product;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBackOrderDetails, printImage;
    private ProgressBar loadingDetails;
    private View layoutDetails;
    private RecyclerView rvProducts;
    private  ProductAdapter adapter;
    private  ArrayList<Product> list;

    private  TextView tvUserName, tvUserPhone1, tvUserPhone2, tvDestination, tvPayment,
            tvOrderDate, tvOrderPrice, tvShippingPrice, tvCopoun, tvDiscountCopoun,
            tvDiscount, tvTotalPrice;

    private OrderDetails orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        findViews();


        getOrderDetails();
    }

    private void getOrderDetails() {
        list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.ORDER_DETAILS,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject orderDetailsObj = new JSONObject(response);
                            Gson gson = new Gson();

                            if (orderDetailsObj.getBoolean("status")) {

                                JSONObject data = orderDetailsObj.getJSONObject("data");
                                JSONArray products = data.getJSONArray("products");

                                tvUserName.setText(data.getString("user_name"));
                                tvUserPhone1.setText(data.getString("user_phone1"));
                                tvUserPhone2.setText(data.getString("user_phone2"));
                                tvDestination.setText(data.getString("destination"));
                                tvPayment.setText(data.getString("payment"));
                                tvOrderPrice.setText(data.getString("price"));
                                tvShippingPrice.setText(data.getString("shipping"));
                                tvCopoun.setText(data.getString("copoun") + " ");
                                tvDiscount.setText("-" + data.getString("tax"));
                                tvDiscountCopoun.setText("(-" + data.getString("tax") + ")");
                                tvTotalPrice.setText(data.getString("total_price"));
                                orderDetails = gson.fromJson(data.toString(), OrderDetails.class);

                                Product product;

                                for (int i = 0; i < products.length(); i++) {
                                    JSONObject productObj = products.getJSONObject(i);

                                    product = gson.fromJson(productObj.toString(), Product.class);

                                    list.add(product);
                                }

                                setAdapter(list);

                            } else
                                Toast.makeText(OrderDetailsActivity.this, orderDetailsObj.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        loadingDetails.setVisibility(View.GONE);
                        layoutDetails.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("lang", "1");
                params.put("order_id", getIntent().getExtras().getString("orderId"));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("preferences", MODE_PRIVATE).getString("token", "");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    private void findViews() {
        rvProducts = findViewById(R.id.rvProducts);
        imgBackOrderDetails = findViewById(R.id.imgBackOrderDetails);
        printImage = findViewById(R.id.printImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserPhone1 = findViewById(R.id.tvUserPhone1);
        tvUserPhone2 = findViewById(R.id.tvUserPhone2);
        tvDestination = findViewById(R.id.tvDestination);
        tvPayment = findViewById(R.id.tvPayment);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderPrice = findViewById(R.id.tvOrderPrice);
        tvShippingPrice = findViewById(R.id.tvShippingPrice);
        tvCopoun = findViewById(R.id.tvCopoun);
        tvDiscountCopoun = findViewById(R.id.tvDiscountCopoun);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        loadingDetails = findViewById(R.id.loadingDetails);
        layoutDetails = findViewById(R.id.layoutDetails);


        imgBackOrderDetails.setOnClickListener(this);
        printImage.setOnClickListener(this);
    }

    private void setAdapter(ArrayList<Product> list) {

        adapter = new ProductAdapter(this, list);
        rvProducts.setAdapter(adapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBackOrderDetails:
                onBackPressed();
                break;

            case R.id.printImage:
                Intent intent = new Intent(this, PrintActivity.class);
                intent.putExtra("orderDetails", orderDetails);
                startActivity(intent);
                break;


        }
    }


}
