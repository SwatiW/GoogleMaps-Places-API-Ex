package com.swati.s_labs;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Map extends AppCompatActivity implements OnMapReadyCallback{

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
       try {
               MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

                mapFragment.getMapAsync(this);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle b = getIntent().getExtras();
        String startp = b.getString("start");
        String dropp = b.getString("drop");
        String response = b.getString("response");

        String latitude1 = null;
        String latitude2 = null;
        String longitude1 = null;
        String longitude2 = null;

        try {
            JSONObject jsonObject = new JSONObject(response);

            String str = "";
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {

                if (jsonArray.getJSONObject(i).has("name")) {
                    str = jsonArray.getJSONObject(i).getString("name");
                    if (str.equals(startp)) {
                        latitude1 = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                        longitude1 = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                    }
                    if (str.equals(dropp)) {
                        latitude2 = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                        longitude2 = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final LatLng start = new LatLng(Double.parseDouble(latitude1), Double.parseDouble(longitude1));
        final LatLng drop = new LatLng(Double.parseDouble(latitude2), Double.parseDouble(longitude2));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Marker startpoint = googleMap.addMarker(new MarkerOptions().position(start)
                .title(startp));

        Marker droppoint = googleMap.addMarker(new MarkerOptions().position(drop)
                .title(dropp));

        googleMap.addPolyline(new PolylineOptions().geodesic(true)
                        .add(start)
                        .add(drop)
                .width(6)
                .color(Color.RED)
        );


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15));


    }
}
