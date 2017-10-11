package egypt.service.governmentall;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class newDirection extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener
        {

  GoogleMap mMap;
                LatLng latLng;
        GoogleApiClient mGoogleApiClient;
        Location mLastLocation;
        Marker mCurrLocationMarker;
        LocationRequest mLocationRequest;
        int PROXIMITY_RADIUS = 10000;
        double latitude, longitude;
        double end_latitude, end_longitude;
        double LATITUDE_ID;
        double LONGITUDE_ID;
        Bundle bundle;
            String mLastUpdateTime;
            LatLng sydney;
            TextView tvDistanceDuration;
            private static final String TAG = MapsActivity.class.getSimpleName();


            @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    bundle = getIntent().getExtras();
                tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
     if (bundle != null) {
        LATITUDE_ID = bundle.getDouble("LATITUDE_ID");
        LONGITUDE_ID = bundle.getDouble("LONGITUDE_ID");
    }
                sydney= new LatLng(LATITUDE_ID,LONGITUDE_ID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
        Log.d("onCreate", "Finishing test case since Google Play Services are not available");
        finish();
        }
        else {
        Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        }

private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
        if(googleAPI.isUserResolvableError(result)) {
        googleAPI.getErrorDialog(this, result,
        0).show();
        }
        return false;
        }
        return true;
        }


/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */
@Override
public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        }
        } else {
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

    // Enable / Disable zooming controls
//    mMap.getUiSettings().setZoomControlsEnabled(false);
    bundle = getIntent().getExtras();
    if (bundle != null) {
        LATITUDE_ID = bundle.getDouble("LATITUDE_ID");
        LONGITUDE_ID = bundle.getDouble("LONGITUDE_ID");
        sydney= new LatLng(LATITUDE_ID,LONGITUDE_ID);
        mMap.addMarker(new MarkerOptions().position(sydney).title(getGeocodeName(LATITUDE_ID,LONGITUDE_ID)));
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


        }



protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
        mGoogleApiClient.connect();
        }

public void onClick(View v)
        {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();


        switch(v.getId()) {
        case R.id.B_search: {
        EditText tf_location = (EditText) findViewById(R.id.TF_location);
        String location = tf_location.getText().toString();
        List<Address> addressList = null;
        MarkerOptions markerOptions = new MarkerOptions();
        Log.d("location = ", location);

        if (!location.equals("")) {
        Geocoder geocoder = new Geocoder(this);
        try {
        addressList = geocoder.getFromLocationName(location, 5);

        } catch (IOException e) {
        e.printStackTrace();
        }

        if (addressList != null) {
        for (int i = 0; i < addressList.size(); i++) {
        Address myAddress = addressList.get(i);
                latLng= new LatLng(myAddress.getLatitude(), myAddress.getLongitude());

        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        }

        }
        }
        break;
        case R.id.B_hospital:
        mMap.clear();
        String hospital = "hospital";
        String url = getUrl(latitude, longitude, hospital);

        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(newDirection.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
        break;

        case R.id.B_restaurant:
        mMap.clear();
        dataTransfer = new Object[2];
        String restaurant = "restaurant";
        url = getUrl(latitude, longitude, restaurant);
        getNearbyPlacesData = new GetNearbyPlacesData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(newDirection.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
        break;
        case R.id.B_school:
        mMap.clear();
        String school = "school";
        dataTransfer = new Object[2];
        url = getUrl(latitude, longitude, school);
        getNearbyPlacesData = new GetNearbyPlacesData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(newDirection.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
        break;

        case R.id.B_to:
        dataTransfer = new Object[3];
        end_latitude=sydney.latitude;
        end_longitude=sydney.longitude;
        url = getDirectionsUrl();
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(end_latitude, end_longitude);
        getDirectionsData.execute(dataTransfer);

        break;


        }
        }

private String getDirectionsUrl()
        {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9g");

        return googleDirectionsUrl.toString();
        }

private String getUrl(double latitude, double longitude, String nearbyPlace)
        {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBj-cnmMUY21M0vnIKz0k3tD3bRdyZea-Y");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
        }




@Override
public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        }



@Override
public void onConnectionSuspended(int i) {

        }

@Override
public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if (mCurrLocationMarker != null) {
        mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title(getGeocodeName(location.getLatitude(),location.getLongitude()));

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//    markerOptions.setRotation(300);

        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    // Getting URL to the Google Directions API
    String url = getDirectionsUrl(sydney, latLng);

    DownloadTask downloadTask = new DownloadTask();

    // Start downloading json data from Google Directions API
    downloadTask.execute(url);
    addMarker();







        Toast.makeText(newDirection.this,"Your Current Location", Toast.LENGTH_LONG).show();


        //stop location updates
        if (mGoogleApiClient != null) {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d("onLocationChanged", "Removing Location Updates");
        }

        }

@Override
public void onConnectionFailed(ConnectionResult connectionResult) {

        }

public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

        // Asking user if explanation is needed
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.ACCESS_FINE_LOCATION)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

        //Prompt the user once explanation has been shown
        ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        MY_PERMISSIONS_REQUEST_LOCATION);


        } else {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        MY_PERMISSIONS_REQUEST_LOCATION);
        }
        return false;
        } else {
        return true;
        }
        }

@Override
public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        switch (requestCode) {
        case MY_PERMISSIONS_REQUEST_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        // permission was granted. Do the
        // contacts-related task you need to do.
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {

        if (mGoogleApiClient == null) {
        buildGoogleApiClient();
        }
        mMap.setMyLocationEnabled(true);
        }

        } else {

        // Permission denied, Disable the functionality that depends on this permission.
        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
        return;
        }

        // other 'case' lines to check for other permissions this app might request.
        // You can add here other case statements according to your requirement.
        }
        }


@Override
public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
        }

@Override
public void onMarkerDragStart(Marker marker) {

        }

@Override
public void onMarkerDrag(Marker marker) {

        }

@Override
public void onMarkerDragEnd(Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude =  marker.getPosition().longitude;

        Log.d("end_lat",""+end_latitude);
        Log.d("end_lng",""+end_longitude);
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
    private String downloadUrl(String strUrl) throws IOException{
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

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



// Fetches data from url passed
private class DownloadTask extends AsyncTask<String, Void, String> {

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
        parserTask.execute(result);
        DataParser parser1= new DataParser();
        HashMap<String,String> durishion = parser1.getDuration(result);
        String distance =durishion.get("distance");
        String duration =durishion.get("duration");
        tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

    }
}


            private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

                // Parsing the data in non-ui thread
                @Override
                protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                    JSONObject jObject;
                    List<List<HashMap<String, String>>> routes = null;

                    try {
                        jObject = new JSONObject(jsonData[0]);
                        Log.d("ParserTask",jsonData[0].toString());
                        DataParser_Refaxctor parser = new DataParser_Refaxctor();
                        Log.d("ParserTask", parser.toString());

                        // Starts parsing data
                        routes = parser.parse(jObject);
                        Log.d("ParserTask","Executing routes");
                        Log.d("ParserTask",routes.toString());

                    } catch (Exception e) {
                        Log.d("ParserTask",e.toString());
                        e.printStackTrace();
                    }
                    return routes;
                }

                // Executes in UI thread, after the parsing process
                @Override
                protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                    ArrayList<LatLng> points;
                    PolylineOptions lineOptions = null;

                    // Traversing through all the routes
                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<>();
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
                        lineOptions.width(7);
                        lineOptions.color(Color.BLACK);

                        Log.d("onPostExecute","onPostExecute lineoptions decoded");

                    }

                    // Drawing polyline in the Google Map for the i-th route
                    if(lineOptions != null) {
                        mMap.addPolyline(lineOptions);
                    }
                    else {
                        Log.d("onPostExecute","without Polylines drawn");
                    }
                }
            }
            private void addMarker() {
                MarkerOptions options = new MarkerOptions();

                // following four lines requires 'Google Maps Android API Utility Library'
                // https://developers.google.com/maps/documentation/android/utility/
                // I have used this to display the time as title for location markers
                // you can safely comment the following four lines but for this info
                IconGenerator iconFactory = new IconGenerator(this);
                iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
                options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
                options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

                LatLng currentLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                options.position(currentLatLng);
                Marker mapMarker = mMap.addMarker(options);
                long atTime = mLastLocation.getTime();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
                mapMarker.setTitle(mLastUpdateTime);
                mapMarker.setRotation(-100);
                Log.d(TAG, "Marker added.............................");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                        13));
                Log.d(TAG, "Zoom done.............................");
            }
            public String getGeocodeName(double latitude, double longitude) {
                Context context = getApplicationContext();

                Geocoder geocoder = new Geocoder( context);
                List<Address> addresses = null;
                String unknown ="";
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return unknown;
                }
                if ( addresses == null ||addresses.size() == 0) {
                    return unknown;
                }
                Address address = addresses.get(0);



                String cn = address.getCountryName();

                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String mainLocality = addresses.get(0).getSubAdminArea();
                 int max =addresses.get(0).getMaxAddressLineIndex();

                return   max +""+mainLocality +","+city + ", " + state+ ", " +country;
            }

        }

