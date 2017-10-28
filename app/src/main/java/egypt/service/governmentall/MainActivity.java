package egypt.service.governmentall;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    TextView sms_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_new);

        sms_text = (TextView) findViewById(R.id.sms_text);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyLocationListener());

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
            String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
            sms_text.setText(message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.i("Location Details", message);

        }

    }
     class MyLocationListener implements LocationListener {
         public void onLocationChanged(Location location) {
             String message = String.format(
                     "New Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude()
             );
             Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

}
