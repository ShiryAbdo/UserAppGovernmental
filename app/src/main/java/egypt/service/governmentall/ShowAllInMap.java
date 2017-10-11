package egypt.service.governmentall;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ShowAllInMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> myList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_in_map);
        myList = (ArrayList<LatLng>) getIntent().getSerializableExtra("mylist");
//        ArrayList<GavernoratWithLocation> testing = (ArrayList<GavernoratWithLocation>) ShowAllInMap.this.getIntent().getParcelableArrayListExtra("extraextra");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        for (int i = 0; i < myList.size(); i++) {
//            LatLng sydney = new LatLng(myList.get(i).latitude,myList.get(i).longitude);
//             mMap.addMarker(new MarkerOptions().position(sydney).title("القاهره"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f));
//
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f));
//
//        }
        insertMarkers(myList);

    }


    private void insertMarkers(ArrayList<LatLng> list) {
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < list.size(); i++) {
            final LatLng position = new LatLng(list.get(i).latitude, list.get(i).longitude);
            final MarkerOptions options = new MarkerOptions().position(position);

            mMap.addMarker(options);
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10.0f));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10.0f));

            builder.include(position);
        }

    }
}
