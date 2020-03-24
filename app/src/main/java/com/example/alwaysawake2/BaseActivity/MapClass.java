package com.example.alwaysawake2.BaseActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapClass {


    public GoogleMap latLngCircle(GoogleMap googleMap, CircleOptions circleOptions, LatLng LatLng, double radius) {
        circleOptions = new CircleOptions().center(LatLng)
                .fillColor(Color.parseColor("#B38fc1e0"))
                .strokeWidth(0.5f)
                .radius(radius);
        googleMap.addCircle(circleOptions);

        return googleMap;
    }

    public String getPlaceNameOrAddress(Geocoder geocoder, double latitude, double longitude) {
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        String placeNameOrAddress = list.get(0).getAddressLine(0);
        return placeNameOrAddress;
    }


    public double getDistance(Context context, double targetLatitude, double targetLongitude) {
        double distance = 0;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return distance;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location targetLocation = new Location("target");
        targetLocation.setLatitude(targetLatitude);
        targetLocation.setLongitude(targetLongitude);
        distance = location.distanceTo(targetLocation);

return distance;
    }



}
