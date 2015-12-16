package com.swati.s_labs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Start extends AppCompatActivity {

     ListView listView;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        listView=(ListView)findViewById(R.id.startList);
        arrayList=new ArrayList();
        arrayAdapter=new ArrayAdapter(Start.this,R.layout.list_ayout,R.id.text,arrayList);
        listView.setAdapter(arrayAdapter);

       new Googleplaces().execute();

    }

    private class Googleplaces extends AsyncTask<String,String,String> {
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            SendingRequest sq=new SendingRequest();
            String response=sq.send();
                return response;
        }

        @Override
        protected void onPostExecute(String response) {
            TextView t=(TextView)findViewById(R.id.select);
            t.setVisibility(View.VISIBLE);
            super.onPostExecute(response);
            progressBar.setVisibility(View.GONE);

            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            try {
                JSONObject jsonObject = new JSONObject(response);

                String str = "";

                if (jsonObject.has("results")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {;
                        if (jsonArray.getJSONObject(i).has("name")) {
                            str =jsonArray.getJSONObject(i).getString("name");
                            arrayList.add(str);
                            arrayAdapter.notifyDataSetChanged();


                        }
                    }
                }

                final String finalResponse = response;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String start = (String) arrayList.get((int) id);
                       // Toast.makeText(Start.this,start, Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(Start.this, Drop.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("response", finalResponse);
                       bundle.putString("startpoint",start);
                        i.putExtras(bundle);
                        startActivity(i);
                   }
                });


               final EditText editsearch = (EditText) findViewById(R.id.startSearch);
                listView.setTextFilterEnabled(true);


                // Capture Text in EditText and filter the list accordingly
                editsearch.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        arrayAdapter.getFilter().filter(s.toString());
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
