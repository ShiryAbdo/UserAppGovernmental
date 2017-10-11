package egypt.service.governmentall;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
 import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
 import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
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
    double latitude , longtitude;
    LatLng pos;
    TextView latLongTV;
    String address,result;
    ArrayList<LatLng> locationArrayList ,distanceArray;
    ArrayList<GavernoratWithLocation>gavernoratWithLocations ;
    AdatorOfareas adatorOfareas ;
    LatLng  LOH ;
    Location targetLocation ;
    ArrayList<DataLocation> dataLocationsArray;
    Button ShowALLnMap ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_near_places);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bundle = getIntent().getExtras();
        AreastArarry = new ArrayList<>();
         locationArrayList =new ArrayList<>();
        distanceArray=new ArrayList<>();
        dataLocationsArray=new ArrayList<>();
        gavernoratWithLocations=new ArrayList<>();
        ShowALLnMap=(Button)findViewById(R.id.ShowALLinMap);



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
        result =area+","+"محافظة"+gavernorate;

        LOH=  LocationFromAddress(result);

        targetLocation= new Location("");//provider name is unnecessary
        targetLocation.setLatitude(LOH.latitude);//your coords of course
        targetLocation.setLongitude(LOH.longitude);

//        float distanceInMeters =  targetLocation.distanceTo(myLocation);


//        locationAddress.getAddressFromLocation(address,
//                getApplicationContext(), new GeocoderHandler());
        mDatabase.child("users").child("Service").child(typeService).child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String value = dataSnapshot1.getKey();
                        places places = dataSnapshot1.getValue(places.class);
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


                        if (distance(gavernoratWithLocations.get(i).getLocatio().latitude, gavernoratWithLocations.get(i).getLocatio().longitude, targetLocation.getLatitude(), targetLocation.getLongitude()) < 6371) {

                            String adress = gavernoratWithLocations.get(i).getGovernoratname();
                            Location newLocation = new Location("");//provider name is unnecessary
                            newLocation.setLatitude(gavernoratWithLocations.get(i).getLocatio().latitude);//your coords of course
                            newLocation.setLongitude(gavernoratWithLocations.get(i).getLocatio().longitude);
                            LatLng sydney = new LatLng(gavernoratWithLocations.get(i).getLocatio().latitude, gavernoratWithLocations.get(i).getLocatio().longitude);
                            float distanceInMeters = targetLocation.distanceTo(newLocation);
                            DataLocation dataLocation = new DataLocation(adress, distanceInMeters, sydney);
//                        if(!dataLocationsArray.contains(dataLocation)){
                            dataLocationsArray.add(dataLocation);
//                        }


                        }
                        adatorOfareas = new AdatorOfareas(dataLocationsArray, ShowNearPLACES.this, LOH);
                        recyclerView.setAdapter(adatorOfareas);

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
                Intent intent = new Intent(ShowNearPLACES.this,ShowAllInMap.class);
                 intent.putExtra("mylist", distanceArray);
                startActivity(intent);
            }
        });








    }




    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

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
}
