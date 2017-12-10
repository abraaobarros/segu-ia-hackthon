package segu.segu;

import android.location.Location;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by abraao on 10/12/2017.
 */

class Dado {
    double lat;
    double lng;
    int hora;
    double tarifa;

    public Dado(double lat, double lng, int hora, double tarifa) {
        this.lat = lat;
        this.lng = lng;
        this.hora = hora;
        this.tarifa = tarifa;
    }

    public Dado(String[] tokens) {
        this.lat = Double.parseDouble(tokens[0]);
        this.lng = Double.parseDouble(tokens[1]);;
        this.hora = Integer.parseInt(tokens[2]);;
        this.tarifa = Double.parseDouble(tokens[3]);;
    }

    public boolean isNear(Location actualLocation) {
        if ((actualLocation.getLatitude() - lat)*(actualLocation.getLatitude() - lat)+(actualLocation.getLongitude() - lng)*(actualLocation.getLongitude() - lng)<0.001)
            return true;
        return false;
    }

    public boolean isBetweenTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.HOUR_OF_DAY)==hora)
            return true;
        return false;
    }
}
