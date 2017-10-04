package egypt.service.governmentall;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by falcon on 04/10/2017.
 */

public class DataLocation {

    String nameOfLocation ;
    float distance ;
    LatLng LatlangLocatio;

    public DataLocation(String nameOfLocation, float distance , LatLng latLng) {
        this.nameOfLocation = nameOfLocation;
        this.distance = distance;
        this.LatlangLocatio=latLng;
    }

    public String getNameOfLocation() {
        return nameOfLocation;
    }

    public float getDistance() {
        return distance;
    }

    public LatLng getLatlangLocatio() {
        return LatlangLocatio;
    }
}
