package egypt.service.governmentall;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class ShowINmap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng location ;
    double LATITUDE_ID ,LATITUDE_ID_crountLocation;
    double LONGITUDE_ID ,LONGITUDE_ID_crountLocation;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_inmap);
        bundle=getIntent().getExtras();
         if (bundle != null) {
             LATITUDE_ID = bundle.getDouble("LATITUDE_ID");
             LONGITUDE_ID = bundle.getDouble("LONGITUDE_ID");
             LATITUDE_ID_crountLocation=bundle.getDouble("LATITUDE_ID_crountLocation");
             LONGITUDE_ID_crountLocation=bundle.getDouble("LATITUDE_ID_crountLocation");
         }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(LATITUDE_ID, LONGITUDE_ID);

        ArrayList<LatLng> points = new ArrayList<LatLng>();
        PolylineOptions polyLineOptions = new PolylineOptions();
        points.add(new LatLng(LATITUDE_ID_crountLocation,LONGITUDE_ID_crountLocation));
        points.add(new LatLng(LATITUDE_ID,LONGITUDE_ID));
        polyLineOptions.width(7 * 1);
        polyLineOptions.geodesic(true);
        polyLineOptions.color(getApplicationContext().getResources().getColor(R.color.bg_register));
        polyLineOptions.addAll(points);
        Polyline polyline = mMap.addPolyline(polyLineOptions);
        polyline.setGeodesic(true);
//         mMap.addMarker(new MarkerOptions().position(sydney).title("القاهره"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f));
    }
}
