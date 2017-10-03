package egypt.service.governmentall;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_near_places);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bundle = getIntent().getExtras();
        AreastArarry = new ArrayList<>();
        latLongTV = (TextView) findViewById(R.id.latLongTV);


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

      LatLng  LOH =  LocationFromAddress(result);

        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(LOH.latitude);//your coords of course
        targetLocation.setLongitude(LOH.longitude);

        float distanceInMeters =  targetLocation.distanceTo(myLocation);


//        locationAddress.getAddressFromLocation(address,
//                getApplicationContext(), new GeocoderHandler());


        mDatabase.child("users").child("Service").child(typeService).child("places").child("OtherplacesOfService").child(gavernorate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String value = dataSnapshot1.getValue(String.class);
                        AreastArarry.add(value);
                        data.add(value);
                        adapter = new ChoseServiceDataAdapter(data, ShowNearPLACES.this);
                        recyclerView.setAdapter(adapter);


                    }
                } else {
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
