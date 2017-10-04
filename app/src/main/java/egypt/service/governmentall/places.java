package egypt.service.governmentall;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by falcon on 04/10/2017.
 */

public class places {
    Map<String, Object>  OtherplacesOfService =new HashMap();
    double latitude ;
    double longitude ;
    String name ;


    public places() {
    }

    public places(Map<String, Object> otherplacesOfService, double latitude, double longitude, String name) {
        OtherplacesOfService = otherplacesOfService;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }


    public Map<String, Object> getOtherplacesOfService() {
        return OtherplacesOfService;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}
