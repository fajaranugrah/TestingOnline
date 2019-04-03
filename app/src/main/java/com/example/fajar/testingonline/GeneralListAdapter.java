package com.example.fajar.testingonline;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fajar on 23/01/2017.
 */

public class GeneralListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<general> GeneralList;
    private List<general> originalList;
    private String[] bgColors;
    private ItemFilter filter = new ItemFilter();

    public GeneralListAdapter(Activity activity, List<general> GeneralList) {
        this.activity = activity;
        this.GeneralList = GeneralList;
        this.originalList = new ArrayList<general>(GeneralList);
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.list_bg);
    }

    //Add Below Method
    public void reloadData(){
        this.originalList = new ArrayList<general>(GeneralList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return GeneralList.size();
    }

    @Override
    public Object getItem(int location) {
        return GeneralList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_general, null);

        //TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView email_mak = (TextView) convertView.findViewById(R.id.email_mak);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);
        TextView judul = (TextView) convertView.findViewById(R.id.judul);

        //serial.setText(String.valueOf(movieList.get(position).id));
        email_mak.setText(GeneralList.get(position).email_mak);
        name.setText(GeneralList.get(position).name);
        tanggal.setText(GeneralList.get(position).tanggal);
        String jud = GeneralList.get(position).judul;
        judul.setText(jud.toString().replace("_", " "));


        //String color = bgColors[position % bgColors.length];
        //serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

    public Filter getFilter(){
        return filter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint){

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<general> list = originalList;

            int count = list.size();
            final ArrayList<general> nlist = new ArrayList<general>(count);

            String filterableString ;
            for (general quiz : originalList) {
                if (quiz.judul.toLowerCase().contains(filterString)) {
                    nlist.add(quiz);
                } else if (quiz.email_mak.toLowerCase().contains(filterString)){
                    nlist.add(quiz);
                }else if (quiz.name.toLowerCase().contains(filterString)){
                    nlist.add(quiz);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return  results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            GeneralList.clear();
            GeneralList.addAll((ArrayList<general>) results.values);
            notifyDataSetChanged();
        }
    }
}
