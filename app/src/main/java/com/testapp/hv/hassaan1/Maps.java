package com.testapp.hv.hassaan1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Maps extends FragmentActivity {
    GoogleMap mapa;
    ArrayList<LatLng> markerPoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.setMyLocationEnabled(false);
//        mapa.getMyLocation();
        mapa.addMarker(new MarkerOptions().position(new LatLng(37.7750, 122.4183)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_media_pause)));
        final LatLng PERTH = new LatLng(-31.90, 115.86);
        Marker perth = mapa.addMarker(new MarkerOptions()
                .position(PERTH)
                .draggable(true));
        addMarkersToMap();
        LatLng TO_DESTINATION = new LatLng(-6.33438, 106.74316);
       // mapa.setMyLocationEnabled(true);
        mapa.setContentDescription("https://maps.googleapis.com/maps/api/directions/json?origin=sydney,au&destination=perth,au&waypoints=via:-37.81223%2C144.96254%7Cvia:-34.92788%2C138.60008&key=AIzaSyBg90gEPDh7wZDSvfGK-6CX9b1AY7vle0");

        /*
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 40));

        mapa.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
*/
        //    CargarContactos(this, mapa);
        Polygons();
        Routing();
    }
    public void Polygons() {
        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(37.35, -122.0),
                        new LatLng(37.45, -122.0),
                        new LatLng(37.45, -122.2),
                        new LatLng(37.35, -122.2),
                        new LatLng(37.35, -122.0));
// Get back the mutable Polygon
        Polygon polygon = mapa.addPolygon(rectOptions);
        polygon = mapa.addPolygon(new PolygonOptions()
                .add(new LatLng(0, 0), new LatLng(0, 5), new LatLng(3, 5), new LatLng(0, 0))
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
    }


    void Routing() {
        onMapReady();
        Polyline line = mapa.addPolyline(new PolylineOptions().
                add(new LatLng(12.917745600000000000, 77.623788300000000000),
                        new LatLng(11.842056800000000000, 7.663096499999940000), new LatLng(11.917745621328917, 79.62378830812937821),
                        new LatLng(15.84205682189032791, 10.6630964999999432410))
                .width(5).color(Color.RED));

//        Polyline polylineToAdd = mapa.addPolyline(new PolylineOptions().addAll(lines).width(3).color(Color.RED));
    }

    public void onMapReady() {
        Toast.makeText(getApplicationContext(), "Ready !!", Toast.LENGTH_LONG).show();
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        mapa = fm.getMap();

        if(mapa!=null){

            // Enable MyLocation Button in the Map
            mapa.setMyLocationEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            // Setting onclick event listener for the map
            mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already two locations
                    if(markerPoints.size()>1){
                        markerPoints.clear();
                        mapa.clear();
                    }

                    // Adding new item to the ArrayList
                    markerPoints.add(point);

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    if(markerPoints.size()==1){
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_play_dark));
                    }else if(markerPoints.size()==2){
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pause_dark));
                    }

                    // Add new marker to the Google Map Android API V2
                    mapa.addMarker(options);

                    // Checks, whether start and end locations are captured
                    if(markerPoints.size() >= 2){
                        LatLng origin = markerPoints.get(0);
                        LatLng dest = markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);
                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
//                        downloadTask.execute(url);
                    }
                }
            });
        }
    }

    class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {
      @Override
      protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

          JSONObject jObject;
          List<List<HashMap<String, String>>> routes = null;

          try{
              jObject = new JSONObject(jsonData[0]);
              DirectionsJSONParser parser = new DirectionsJSONParser();

              // Starts parsing data
              routes = parser.parse(jObject);
          }catch(Exception e){
              e.printStackTrace();
          }
          return routes;
      }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            // Traversing through all the routes
if(result.size()>0 || result!=null) {
    for (int i = 0; i < result.size(); i++) {
        points = new ArrayList<LatLng>();
        lineOptions = new PolylineOptions();

        // Fetching i-th route
        List<HashMap<String, String>> path = result.get(i);

        // Fetching all the points in i-th route
        for (int j = 0; j < path.size(); j++) {
            HashMap<String, String> point = path.get(j);

            double lat = Double.parseDouble(point.get("lat"));
            double lng = Double.parseDouble(point.get("lng"));
            LatLng position = new LatLng(lat, lng);

            points.add(position);
        }

        // Adding all the points in the route to LineOptions
        lineOptions.addAll(points);
        lineOptions.width(2);
        lineOptions.color(Color.RED);
    }

    // Drawing polyline in the Google Map for the i-th route
    mapa.addPolyline(lineOptions);
}
        }
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
       //     Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            ParserTask parserTask = new ParserTask();
if(parserTask!=null){ parserTask.execute(result);}

        }
    }


    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);

    Marker mAdelaide, mPerth, mMelbourne, mSydney, mBrisbane;

    private void addMarkersToMap() {
        // Uses a colored icon.
        mBrisbane = mapa.addMarker(new MarkerOptions()
                .position(BRISBANE)
                .title("Brisbane")
                .snippet("Population: 2,074,200")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        // Uses a custom icon.
        mSydney = mapa.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("Sydney")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cast_ic_notification_0)).rotation(60));
        // Creates a draggable marker. Long press to drag.
        mMelbourne = mapa.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("Melbourne")
                .snippet("Population: 4,137,400")
                .draggable(true));
        // A few more markers for good measure.
        mPerth = mapa.addMarker(new MarkerOptions()
                .position(PERTH)
                .title("Perth")
                .snippet("Population: 1,738,800"));
        mAdelaide = mapa.addMarker(new MarkerOptions()
                .position(ADELAIDE)
                .title("Adelaide")
                .snippet("Population: 1,213,000"));
        int numMarkersInRainbow = 12;
        for (int i = 0; i < numMarkersInRainbow; i++) {
            mapa.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            -30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
                            135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
                    .title("Marker " + i)
                    .icon(BitmapDescriptorFactory.defaultMarker(i * 360 / numMarkersInRainbow)));
        }
    }
 /*   private  void  CargarContactos(Context context, GoogleMap mapa){
        myApp.DBManager manager = new myApp.DBManager(context);
        Cursor cursor = manager.CargarMapa();
        if (cursor.moveToFirst()) {
            do
            {
                if (cursor.getString(4).toString().contains("1")) {
                    mapa.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(Double.parseDouble(cursor.getString(2)),
                                            Double.parseDouble(cursor.getString(3)))
                            )
                            .title(cursor.getString(7) + " - " + cursor.getString(1))
                            .snippet("Fecha: " + cursor.getString(5) + " Monto: " + cursor.getString(6))
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.ic_media_play)));
                }
                else
                {
                    mapa.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(Double.parseDouble(cursor.getString(2)),
                                            Double.parseDouble(cursor.getString(3)))
                            )
                            .title(cursor.getString(7) + " - " + cursor.getString(1))
                            .snippet("Fecha: " + cursor.getString(5) + " Monto: " + cursor.getString(6))
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.ic_media_pause)));
                }
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        manager.CloseManager();
    }
*/

}

