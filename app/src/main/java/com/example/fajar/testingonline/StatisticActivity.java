package com.example.fajar.testingonline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends Activity {

    private String TAG = StatisticActivity.class.getSimpleName();

    private String URL_TOP_250 = "http://aplikasito.hol.es/take_user.php/?email_mak=";

    //private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeListAdapter adapter;
    private List<statistic> movieList;
    String email_mak;

    //private SQLiteHandler db;

    // initially offset will be 0, later will be updated while parsing the json
    //private int offSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        listView = (ListView) findViewById(R.id.listView);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        movieList = new ArrayList<>();
        adapter = new SwipeListAdapter(this, movieList);
        listView.setAdapter(adapter);


        Bundle a = new Bundle();
        a = getIntent().getExtras();
        email_mak = a.getString("email");

        fetchMovies(email_mak);

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    /*@Override
    public void onRefresh() {
        fetchMovies();
    }*/

    /**
     * Fetching movies json by making http call
     */
    private void fetchMovies(final String email_mak) {

        // showing refresh animation before making http call
        //swipeRefreshLayout.setRefreshing(true);

        // appending offset to url
        String url = URL_TOP_250 + email_mak;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        //if (response.length() > 0) {
                        try {
                            movieList.clear();

                            // looping through json and adding to movies list
                            for(int i = 0; i < response.length(); i++) {

                                    JSONObject movieObj = response.getJSONObject(i);

                                    //int nilai = movieObj.getInt("nilai");
                                    String email_ans = movieObj.getString("email_ans");
                                    String name = movieObj.getString("name");

                                    statistic m = new statistic(email_ans, name);

                                    movieList.add(0, m);

                                // updating offset value to highest value
                                    //if (nilai >= offSet)
                                    //    offSet = nilai;
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        }
                        adapter.notifyDataSetChanged();

                        //final String email_set = adapter.getView(i).;

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent asd = new Intent(getApplicationContext(), SeeValueActivity.class);
                                asd.putExtra("email_set", movieList.get(i).email_ans);
                                startActivity(asd);
                            }
                        });
                        //}

                        // stopping swipe refresh
                        //swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
}