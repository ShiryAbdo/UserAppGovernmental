package egypt.service.governmentall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShowNearPLACES extends AppCompatActivity {
    Bundle bundle;
    String typeService, Tyep_OfTypeService, gavernorate, area;
    ArrayList<String> AreastArarry;
    RecyclerView recyclerView;
    ArrayList<String> data;
    ChoseServiceDataAdapter adapter;
    DatabaseReference mDatabase;
    double latitude, longtitude;
    LatLng pos;
    TextView latLongTV;
    String address, result;
    ArrayList<LatLng> locationArrayList, distanceArray;
    ArrayList<GavernoratWithLocation> gavernoratWithLocations;
    AdatorOfareas adatorOfareas;
    LatLng LOH, CourrentLONGTUT;
    Location targetLocation;
    ArrayList<DataLocation> dataLocationsArray;
    Button ShowALLnMap;
    TextView textView2;
    LocationManager locationManager;
    Location currentLocation;

    ArrayList<String> distanceNm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_near_places);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bundle = getIntent().getExtras();
        distanceNm = new ArrayList<>();
        AreastArarry = new ArrayList<>();
        locationArrayList = new ArrayList<>();
        distanceArray = new ArrayList<>();
        dataLocationsArray = new ArrayList<>();
        gavernoratWithLocations = new ArrayList<>();
        ShowALLnMap = (Button) findViewById(R.id.ShowALLinMap);
        textView2 = (TextView) findViewById(R.id.textView2);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 20, new MyLocationListener());

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            currentLocation=location;
            CourrentLONGTUT= new LatLng(location.getLatitude(),  location.getLongitude());
            String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
 //            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.i("Location Details", message);

        }else{
            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
        }



        mDatabase = FirebaseDatabase.getInstance().getReference();
        data = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        if (bundle != null) {

            typeService = bundle.getString("serviceName");
            Tyep_OfTypeService = bundle.getString("ChoseTypeService");
            gavernorate = bundle.getString("gavernorate");
            area = bundle.getString("area");

        }
 address ="مدينة نصر, محافظة القاهر";
        GeocodingLocation locationAddress = new GeocodingLocation();

        StringBuilder sb = new StringBuilder();
         sb.append(area).append(",");
        sb.append("محافظة").append(gavernorate);
        result = sb.toString();
        result = area+","+"محافظة"+gavernorate;
        if(result!=null){
            LOH=  LocationFromAddress(result);
        }

//   Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

        targetLocation= new Location("");//provider name is unnecessary
        targetLocation.setLatitude(LOH.latitude);//your coords of course
        targetLocation.setLongitude(LOH.longitude);

//        float distanceInMeters =  targetLocation.distanceTo(myLocation);

//        Toast.makeText(getApplicationContext(),typeService,Toast.LENGTH_LONG).show();
//        locationAddress.getAddressFromLocation(address,
//                getApplicationContext(), new GeocoderHandler());
        mDatabase.child("users").child("Service").child(typeService).child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String value = dataSnapshot1.getKey();
                        places places = dataSnapshot1.getValue(places.class);
//                        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
//                        String value=places.getName();
                        double latitude = places.getLatitude();
                        Location location = new Location("");//provider name is unnecessary
                        location.setLatitude(places.getLatitude());//your coords of course
                        location.setLongitude(places.getLongitude());
                        LatLng sydney = new LatLng(places.getLatitude(), places.getLongitude());
                        distanceArray.add(sydney);
                        GavernoratWithLocation gavernoratWithLocation = new GavernoratWithLocation(value, sydney);
                        if (!gavernoratWithLocations.contains(gavernoratWithLocation)) {
                            gavernoratWithLocations.add(gavernoratWithLocation);
                        }
                        AreastArarry.add(value);
                        data.add(value);


                    }


                    for (int i = 0; i < gavernoratWithLocations.size(); i++) {
//                    distanceArray.add(gavernoratWithLocations.get(i).getLocatio());


                        if ( CalculationByDistance(gavernoratWithLocations.get(i).getLocatio(),CourrentLONGTUT) < 6371) {

                            String adress = gavernoratWithLocations.get(i).getGovernoratname();
                            Location newLocation = new Location("");//provider name is unnecessary
                            newLocation.setLatitude(gavernoratWithLocations.get(i).getLocatio().latitude);//your coords of course
                            newLocation.setLongitude(gavernoratWithLocations.get(i).getLocatio().longitude);
                            LatLng sydney = new LatLng(gavernoratWithLocations.get(i).getLocatio().latitude, gavernoratWithLocations.get(i).getLocatio().longitude);
                            float distanceInMeters = currentLocation.distanceTo(newLocation);
//                            double distanceInMeters =CalculationByDistance(gavernoratWithLocations.get(i).getLocatio(),CourrentLONGTUT);
                            String url = getDirectionsUrl(CourrentLONGTUT, sydney);
                            DownloadTask downloadTask = new DownloadTask();

                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);

                            DataLocation dataLocation = new DataLocation(adress,  "", sydney);
//                        if(!dataLocationsArray.contains(dataLocation)){
                            dataLocationsArray.add(dataLocation);
//                        }


                        }
                        adatorOfareas = new AdatorOfareas(dataLocationsArray, ShowNearPLACES.this, LOH ,distanceNm);
                        recyclerView.setAdapter(adatorOfareas);
                        adatorOfareas.notifyDataSetChanged();


                    }
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });








        ShowALLnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowNearPLACES.this,MapsActivityShow.class);
//                intent.putIntegerArrayListExtra("mylist", distanceArray);
                intent.putParcelableArrayListExtra("mylist", distanceArray);

//                intent.putExtra("mylist", distanceArray);
                startActivity(intent);
            }
        });








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


    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist;
    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }


    public void getLocationFromAddress(String strAddress) {


//String addressStr = "faisalabad";/// this give me correct address
        String addressStr = "Sainta Augustine,FL,4405 Avenue A";
        Geocoder geoCoder = new Geocoder(ShowNearPLACES.this, Locale.getDefault());


        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(addressStr, 1);
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longtitude =
                        addresses.get(0).getLongitude();
            }

        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }


        pos = new LatLng(latitude, longtitude);
    }

    public LatLng LocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((double) (location.getLatitude()),
                    (double) (location.getLongitude()));


        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return p1;
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
        String mainLocality = address.getSubAdminArea();

        return city + ", " + state+ ", " +country;
    }


 private  class GeocoderHandler extends Handler {
     @Override
     public void handleMessage(Message message) {
         String locationAddress;
         switch (message.what) {
             case 1:
                 Bundle bundle = message.getData();
                 locationAddress = bundle.getString("Latitude");
                 break;
             default:
                 locationAddress = null;
         }
         latLongTV.setText(locationAddress);
     }

 }




  private   class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude()
            );
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(getApplicationContext(), "Provider status changed", Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(getApplicationContext(), "Provider disabled by the user. GPS turned off", Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(getApplicationContext(), "Provider enabled by the user. GPS turned on", Toast.LENGTH_LONG).show();
        }
    }

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

            ParserTask parserTask = new  ParserTask();
            parserTask.execute(result);
            DataParser parser1= new DataParser();
            HashMap<String,String> durishion = parser1.getDuration(result);
            String distance =durishion.get("distance");
            String duration =durishion.get("duration");
            distanceNm.add(distance);

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
//                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }




}
