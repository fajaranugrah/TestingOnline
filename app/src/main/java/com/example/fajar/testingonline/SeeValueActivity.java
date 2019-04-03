package com.example.fajar.testingonline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeeValueActivity extends AppCompatActivity {

    private String TAG = SeeValueActivity.class.getSimpleName();

    private String URL_TOP_250 = "http://aplikasito.hol.es/nilai_ans.php?email=";

    //private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeListValueAdapter adapter;
    private List<SeeValue> valueList;
    String email_ans;

    private TextView txtemail;

    private SQLiteHandler db;

    // initially offset will be 0, later will be updated while parsing the json
    //private int offSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_value);

        listView = (ListView) findViewById(R.id.listnilai);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        valueList = new ArrayList<>();
        adapter = new SwipeListValueAdapter(this, valueList);
        listView.setAdapter(adapter);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        email_ans = b.getString("email_set");

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String email_mak = user.get("email");

        //swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        //swipeRefreshLayout.setRefreshing(true);

                                        fetchMovies();
                                    }
                                }
        );*/
        fetchMovies(email_ans,email_mak);

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
    private void fetchMovies(final String email_ans, final String email_mak) {

        // showing refresh animation before making http call
        //swipeRefreshLayout.setRefreshing(true);

        // appending offset to url
        String url = URL_TOP_250 + email_ans + "&email_mak=" + email_mak;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                //if (response.length() > 0) {
                try {

                valueList.clear();

                // looping through json and adding to movies list
                for(int i = 0; i < response.length(); i++) {

                        JSONObject movieObj = response.getJSONObject(i);

                        int nilai = movieObj.getInt("nilai");
                        String judul = movieObj.getString("judul");
                        String tanggal = movieObj.getString("tanggal");

                        SeeValue m = new SeeValue(nilai, judul, tanggal);

                        valueList.add(0, m);

                        // updating offset value to highest value
                        //if (nilai >= offSet)
                        //    offSet = nilai;
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                }

                txtemail = (TextView) findViewById(R.id.email_ans);
                txtemail.setText(email_ans);


                adapter.notifyDataSetChanged();
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
