package com.example.offertaadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.offertaadmin.model.OrderStatus;

import java.util.ArrayList;

public class StatusAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OrderStatus> objects;
    LayoutInflater inflater;

    public StatusAdapter(Context context, ArrayList<OrderStatus> objects) {
        this.context = context;
        this.objects = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null){

        }

        return view;
    }
}
