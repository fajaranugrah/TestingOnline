package com.example.fajar.testingonline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fajar on 05/01/2017.
 */

public class SwipeListValueAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<SeeValue> valueList;
    private String[] bgColors;

    public SwipeListValueAdapter(Activity activity, List<SeeValue> valueList) {
        this.activity = activity;
        this.valueList = valueList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.list_bg);
    }

    @Override
    public int getCount() {
        return valueList.size();
    }

    @Override
    public Object getItem(int location) {
        return valueList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_value, null);

        TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView judul = (TextView) convertView.findViewById(R.id.judul);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);

        serial.setText(String.valueOf(valueList.get(position).id));
        judul.setText(valueList.get(position).judul);
        tanggal.setText(valueList.get(position).tanggal);

        String color = bgColors[position % bgColors.length];
        serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }
}
