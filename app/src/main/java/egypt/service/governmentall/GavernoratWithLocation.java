package egypt.service.governmentall;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by falcon on 04/10/2017.
 */

public class GavernoratWithLocation {

    String governoratname;
    LatLng Locatio ;

    public GavernoratWithLocation(String governoratname, LatLng locatio) {
        this.governoratname = governoratname;
        Locatio = locatio;
    }

    public String getGovernoratname() {
        return governoratname;
    }

    public LatLng getLocatio() {
        return Locatio;
    }
}
