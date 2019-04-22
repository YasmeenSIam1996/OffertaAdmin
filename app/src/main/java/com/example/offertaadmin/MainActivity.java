package com.example.offertaadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.offertaadmin.adapter.OrdersAdapter;
import com.example.offertaadmin.model.Order;
import com.example.offertaadmin.model.OrderStatus;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar loading;

    RecyclerView rvOrders;
    OrdersAdapter adapter;

    ImageView imgFilter, imgSignOut;
    EditText txtSearch;


    // filter dialog
    Dialog filterDialog;
    FrameLayout layoutProgress;
    LinearLayout layoutFrom, layoutTo;
    TextView tvCloseDialog, tvStates, tvCalTo, tvCalFrom;
    Button btnFilter;
    int statusId;

    ArrayList<Order> allOrders;
    ArrayList<OrderStatus> allStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allOrders = new ArrayList<>();
        loading = findViewById(R.id.loading);

        findViews();

        //orders volley request
        getAllOrders("");
    }

    private void findViews() {
        rvOrders = findViewById(R.id.rvOrders);
        imgFilter = findViewById(R.id.imgFilter);
        imgSignOut = findViewById(R.id.imgSignOut);
        txtSearch = findViewById(R.id.txtSearch);


        imgFilter.setOnClickListener(this);
        imgSignOut.setOnClickListener(this);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rvOrders.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                searchRequest();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFilter:
                showFilterDialog();
                break;

            case R.id.tvCloseDialog:
                filterDialog.dismiss();
                break;

            case R.id.tvStates:
                showStateList(v);
                break;

            case R.id.imgSignOut:
                showAlertDialog();
                break;

            case R.id.layoutFrom:
                showCalendar("from");
                break;

            case R.id.layoutTo:
                showCalendar("to");
                break;

            case R.id.btnFilter:
                loading.setVisibility(View.VISIBLE);
                filterDialog.dismiss();
                rvOrders.setVisibility(View.GONE);
                getAllOrders("filter");

                break;
        }
    }

    private void setAdapter(ArrayList<Order> orders) {

        adapter = new OrdersAdapter(this, orders);
        rvOrders.setAdapter(adapter);

        rvOrders.setLayoutManager(new LinearLayoutManager(this));

    }

    // search request method
    private void searchRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SEARCH_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);

                        allOrders.clear();

                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {

                                JSONArray data = object.getJSONArray("data");
                                JSONObject orderObject;
                                Gson gson = new Gson();
                                Order order;

                                for (int i = 0; i < data.length(); i++) {
                                    orderObject = data.getJSONObject(i);
                                    order = gson.fromJson(orderObject.toString(), Order.class);
                                    allOrders.add(order);
                                }

                                loading.setVisibility(View.GONE);
                                rvOrders.setVisibility(View.VISIBLE);

                                setAdapter(allOrders);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("response", error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("order_id", txtSearch.getText().toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("preferences", MODE_PRIVATE).getString("token", "");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    // get orders request
    private void getAllOrders(final String type) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.ORDERS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getBoolean("status")) {
                                Gson gson = new Gson();
                                allOrders.clear();

                                JSONArray data = response.getJSONArray("data");
                                JSONObject orderObject;
                                Order order;

                                for (int i = 0; i < data.length(); i++) {
                                    orderObject = data.getJSONObject(i);
                                    order = gson.fromJson(orderObject.toString(), Order.class);

                                    allOrders.add(order);
                                    loading.setVisibility(View.GONE);
                                    rvOrders.setVisibility(View.VISIBLE);
                                    setAdapter(allOrders);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                if (type.equals("")) {

                    params.put("from_date", "");
                    params.put("to_date", "");
                    params.put("status", "");
                    params.put("lang", "");

                } else {

                    params.put("from_date", tvCalFrom.getText().toString());
                    params.put("to_date", tvCalTo.getText().toString());
                    params.put("status", statusId + "");
                    params.put("lang", "1");

                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("preferences", MODE_PRIVATE).getString("token", "");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    // get order status request
    private void getAllOrderStatus() {
        allStatus = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, Constants.GET_ORDER_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject statusObj = new JSONObject(response);

                            Log.e("response", response);
                            if (statusObj.getBoolean("status")) {

                                JSONArray data = statusObj.getJSONArray("data");
                                Gson gson = new Gson();
                                JSONObject statusObject;
                                OrderStatus orderStatus;

                                for (int i = 0; i < data.length(); i++) {

                                    statusObject = data.getJSONObject(i);
                                    orderStatus = gson.fromJson(statusObject.toString(), OrderStatus.class);
                                    allStatus.add(orderStatus);
                                }

                            } else {
                                Toast.makeText(MainActivity.this, statusObj.getString("message"), Toast.LENGTH_SHORT).show();
                                filterDialog.dismiss();
                            }

                            layoutProgress.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lang", "0");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("preferences", MODE_PRIVATE).getString("token", "");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    private void showFilterDialog() {
        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.dialog_filter);
        filterDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        filterDialog.setCancelable(false);

        tvCloseDialog = filterDialog.findViewById(R.id.tvCloseDialog);
        tvStates = filterDialog.findViewById(R.id.tvStates);
        layoutProgress = filterDialog.findViewById(R.id.layoutProgress);
        layoutFrom = filterDialog.findViewById(R.id.layoutFrom);
        layoutTo = filterDialog.findViewById(R.id.layoutTo);
        tvCalTo = filterDialog.findViewById(R.id.tvCalTo);
        tvCalFrom = filterDialog.findViewById(R.id.tvCalFrom);
        btnFilter = filterDialog.findViewById(R.id.btnFilter);

        //all status request
        getAllOrderStatus();

        tvCloseDialog.setOnClickListener(this);
        tvStates.setOnClickListener(this);
        layoutFrom.setOnClickListener(this);
        layoutTo.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        filterDialog.show();
    }

    public void showStateList(View view) {

        statusId = -1;

        List<String> languageList = new ArrayList<>();

        for (int i = 0; i < allStatus.size(); i++) {
            languageList.add(allStatus.get(i).getStatus());
        }
        Log.e("languageList", allStatus.size() + "");


        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, languageList);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(MainActivity.this);
        listPopupWindow.setAnchorView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            listPopupWindow.setDropDownGravity(Gravity.END);
        }

        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        listPopupWindow.setWidth(600);
        listPopupWindow.setAdapter(adapter);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String status = (String) parent.getItemAtPosition((int) id);
                tvStates.setText(status);
                tvStates.setTextColor(getResources().getColor(R.color.colorFilterDialog));

                statusId = (int) id;

                Log.e("statusId", statusId + "");
                Log.e("statusId", status);

                listPopupWindow.dismiss();
            }
        });

        listPopupWindow.show();
    }

    private void showCalendar(final String interval) {

        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                if (interval.equals("from"))
                    tvCalFrom.setText(year + "-" + month + "-" + dayOfMonth);
                else
                    tvCalTo.setText(year + "-" + month + "-" + dayOfMonth);

            }
        }, YEAR, MONTH, DAY_OF_MONTH);

        dialog.show();

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

        builder.setMessage("هل أنت متأكد من تسجيل الخروج؟").setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences.Editor editor = getSharedPreferences("preferences", MODE_PRIVATE).edit();
                editor.remove("token").apply();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();

            }
        })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}


//angular js
