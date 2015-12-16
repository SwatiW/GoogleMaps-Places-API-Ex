package com.swati.s_labs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Drop extends AppCompatActivity {
    ListView listView;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop);


        Bundle b=getIntent().getExtras();
        String response= b.getString("response");
        final String startppoint= b.getString("startpoint");
       // Toast.makeText(Drop.this, startppoint, Toast.LENGTH_SHORT).show();

       EditText startagain=(EditText)findViewById(R.id.startagain);
        startagain.setText(startppoint);



        TextView t=(TextView)findViewById(R.id.select);
        t.setVisibility(View.VISIBLE);
        listView=(ListView)findViewById(R.id.dropList);
        arrayList=new ArrayList();
        arrayAdapter=new ArrayAdapter(Drop.this,R.layout.list_ayout,R.id.text,arrayList);
        listView.setAdapter(arrayAdapter);

        if (response == null) {
            response = "THERE WAS AN ERROR";
        }

        try {
            JSONObject jsonObject = new JSONObject(response);

            String str = "";

            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {

                    if (jsonArray.getJSONObject(i).has("name")) {
                        str = jsonArray.getJSONObject(i).getString("name");
                        if (!str.equals(startppoint))
                              arrayList.add(str);
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        final String finalResponse = response;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String drop = (String) arrayList.get((int) id);
                //  Toast.makeText(Drop.this,drop, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Drop.this, Map.class);
                Bundle bundle = new Bundle();
                bundle.putString("start", startppoint);
                bundle.putString("drop", drop);
                bundle.putString("response", finalResponse);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


       final EditText dropsearch = (EditText) findViewById(R.id.dropSearch);
        listView.setTextFilterEnabled(true);


        // Capture Text in EditText and filter the list accordingly
        dropsearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s.toString());
            }
        });

        startagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(Drop.this,Start.class);
                startActivity(it);
            }
        });
    }
}
