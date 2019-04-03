package com.example.fajar.testingonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizSDActivity extends AppCompatActivity {

    private Shareprefered mPreferenceSetting;
    private sd sekolah;

    private String TAG = StatisticActivity.class.getSimpleName();

    private String URL_TOP_250 = "http://aplikasito.hol.es/take_soal.php?kategori=1";

    private ListView listView;
    private SDListAdapter adapter;
    private ArrayList<sd> SDList;
    EditText inputsearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_sd);

        listView = (ListView) findViewById(R.id.listViewSD);
        inputsearch = (EditText) findViewById(R.id.inputSearch);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        SDList = new ArrayList<>();
        adapter = new SDListAdapter(this, SDList);
        listView.setAdapter(adapter);

        //listView.setTextFilterEnabled(true);

        mPreferenceSetting = new Shareprefered(getApplicationContext());

        fetchMovies();

        inputsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                QuizSDActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
    private void fetchMovies() {

        // showing refresh animation before making http call
        //swipeRefreshLayout.setRefreshing(true);

        // appending offset to url
        String url = URL_TOP_250;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                //if (response.length() > 0) {
                try {
                    SDList.clear();

                    // looping through json and adding to movies list
                    for(int i = 0; i < response.length(); i++) {

                        JSONObject movieObj = response.getJSONObject(i);

                        //int nilai = movieObj.getInt("nilai");
                        String email_mak = movieObj.getString("email_mak");
                        String name = movieObj.getString("name");
                        String judul = movieObj.getString("judul");
                        String tanggal = movieObj.getString("tanggal");
                        String id_quiz = movieObj.getString("id_quiz");
                        String need_score = movieObj.getString("need_score");

                        sd m = new sd(email_mak, name, judul, tanggal, id_quiz, need_score);

                        SDList.add(0, m);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                }
                adapter.reloadData();

                //final String email_set = adapter.getView(i).;

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent asd = new Intent(getApplicationContext(), SoalQuizSDActivity.class);
                        asd.putExtra("email_ma", SDList.get(i).email_mak);
                        asd.putExtra("judu", SDList.get(i).judul);
                        asd.putExtra("id_quiz", SDList.get(i).id_quiz);
                        String as = SDList.get(i).email_mak;
                        String ad = SDList.get(i).judul;
                        String aw = SDList.get(i).id_quiz;
                        String sc = SDList.get(i).need_score;
                        startActivity(asd);

                        mPreferenceSetting.setEmail_mas(as);
                        mPreferenceSetting.setJuduls(ad);
                        mPreferenceSetting.setId_quiz(aw);
                        mPreferenceSetting.setNeedScore(sc);
                        Log.d("jhgjhgh :",mPreferenceSetting.getNeedScore());

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
