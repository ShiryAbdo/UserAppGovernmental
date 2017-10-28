package egypt.service.governmentall;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Test extends AppCompatActivity {
    Button show_map ;
    double LATITUDE_ID;
    double LONGITUDE_ID;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        show_map =(Button)findViewById(R.id.show_map);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            LATITUDE_ID = bundle.getDouble("LATITUDE_ID");
            LONGITUDE_ID = bundle.getDouble("LONGITUDE_ID");
        }
        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double latitude = 40.714728;
                double longitude = -73.998672;
                String label = "ABC Label";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=31.043735, 31.377689");
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(intent);
            }
        });
    }
}
