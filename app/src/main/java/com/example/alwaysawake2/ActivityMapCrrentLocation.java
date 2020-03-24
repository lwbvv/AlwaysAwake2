package com.example.alwaysawake2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class ActivityMapCrrentLocation extends BaseActivity implements OnMapReadyCallback {
LatLng mlatLng;
    GoogleMap mGoogleMap;
TextView currentLocation_radius;
    double distance = 0;
    Location targetLocation = new Location("target");
    CircleOptions circleOptions;
double latitude, longitude, radius;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_current_location);
currentLocation_radius = findViewById(R.id.currentLocation_radius);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.currentLocation_map); //맵 프레그먼트 매칭
        mapFragment.getMapAsync(ActivityMapCrrentLocation.this); //맵 비동기화 스레드 콜백??????
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        radius = intent.getDoubleExtra("radius", 0);
        currentLocation_radius.setText("반경 "+radius + "M");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        inArea(ActivityMapCrrentLocation.this, latitude,longitude,mGoogleMap,radius);
    }




    public void inArea(Context context, double targetLatitude, double targetLongitude, final GoogleMap googleMap, final double radius) {

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
            return;
        }
       final MarkerOptions markerOptions = new MarkerOptions();
        final LatLng latLng = new LatLng(targetLatitude, targetLongitude);
        markerOptions.position(latLng);

        circleOptions = new CircleOptions().center(latLng)
                .fillColor(Color.parseColor("#B38fc1e0"))
                .strokeWidth(0.5f)
                .radius(radius);


        targetLocation.setLatitude(targetLatitude);
        targetLocation.setLongitude(targetLongitude);

        //로케이션 리스너
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                distance = location.distanceTo(targetLocation);
                if(distance > radius){
                    GoogleMap rGooglemap = googleMap;
                    rGooglemap.clear();
                    googleMap.addMarker(markerOptions);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(15);
                    LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
                    mGoogleMap.animateCamera(cameraUpdate);
                    circleOptions = new CircleOptions().center(latLng)
                            .fillColor(Color.parseColor("#CC8fc1e0"))//하늘색
                            .strokeWidth(0.5f)
                            .radius(radius);
                    rGooglemap.addCircle(circleOptions);
                    Log.e("실시간1", ""+distance);

                }else{
                    GoogleMap rGooglemap = googleMap;
                    rGooglemap.clear();
                    googleMap.addMarker(markerOptions);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(15);
                    LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(lng));
                    mGoogleMap.animateCamera(cameraUpdate);
                    circleOptions = new CircleOptions().center(latLng)
                            .fillColor(Color.parseColor("#CCe4a2a0"))//빨강색
                            .strokeWidth(0.5f)
                            .radius(radius);
                    Log.e("실시간2", ""+distance);
                    rGooglemap.addCircle(circleOptions);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1,
                locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                locationListener);
    }
}
