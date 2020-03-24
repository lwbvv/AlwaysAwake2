package com.example.alwaysawake2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alwaysawake2.BaseActivity.BaseActivity;
import com.example.alwaysawake2.BaseActivity.MapClass;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.internal.impl.net.pablo.PlaceResult;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ActivityE3Map extends BaseActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    private GoogleMap mGoogleMap;
    PlacesClient placesClient;
    LatLng mLatLng;
    CircleOptions circleOptions;
    Button successButton;
    TextView putRadius;
    double radius = 300;
    MapClass mapClass = new MapClass();
    Geocoder geocoder = new Geocoder(ActivityE3Map.this);
String placeNameOrAddress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_map);
        successButton = findViewById(R.id.map_successButton);
        putRadius = findViewById(R.id.map_radius);
        putRadius.setText("반경설정 (초기값 300M)");
        autoComplete();//지도 플레이스명 자동완성 메소드 밑에 정의 해 놓았음
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_map); //맵 프레그먼트 매칭
        mapFragment.getMapAsync(ActivityE3Map.this); //맵 비동기화 스레드 콜백??????

        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = mLatLng.latitude; //마커로 찍은 좌표의 위도(가로)
                double longitude = mLatLng.longitude; //마커로 찍은 좌표의 경도(세로축)

                //인텐트로 위도 경도와 반지름 길이(M)를 넘겨준다
                Intent intent = new Intent(ActivityE3Map.this, ActivityE2HabitCreating.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("radius", radius);
                intent.putExtra("placeNameOrAddress", placeNameOrAddress);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        putRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();// 마커의 반경 입력시키는 다이얼로그 띄워줌
            }
        });

    }

    /**
     * 마커의 반경 M 설정 다이얼로그
     */
            void dialog(){
        final EditText editText = new EditText(ActivityE3Map.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityE3Map.this)
                .setTitle("마커 범위")
                .setMessage("반경 입력")
                .setView(editText)
                .setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        putRadius.setText(editText.getText().toString() + "M");
                        radius = Double.parseDouble(editText.getText().toString());
                        mGoogleMap.clear();
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(mLatLng);
                        mGoogleMap.addMarker(markerOptions).showInfoWindow();
                        mGoogleMap = mapClass.latLngCircle(mGoogleMap,circleOptions,mLatLng,radius);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }


    /**
     * autoComplete 메소드
     */
    void autoComplete() {
        Places.initialize(getApplicationContext(),"AIzaSyDMA9IoMnzPR_17BRdeATGpMX4Era04Sjk");//플레이스 초기설정
        placesClient = Places.createClient(ActivityE3Map.this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),"AIzaSyDMA9IoMnzPR_17BRdeATGpMX4Era04Sjk");
        }
        // Initialize the AutocompleteSupportFragment.
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)); //검색결과를 통해 가져올 필드값을 설정해 준다
// Set up a PlaceSelectionListener to handle the response.

        autocompleteFragment.setCountry("KR");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //플레이스를 검색하면 마커를 찍고 mLatLng에 위도경도를 반환 해 준다.
                // TODO: Get info about the selected place.
                Log.i("플레이스", "Place: " + place.getName() + ", "+ place.getLatLng());
                mLatLng = place.getLatLng();
                mGoogleMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(place.getLatLng());
                mGoogleMap.addMarker(markerOptions).showInfoWindow();
                CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(15);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mGoogleMap.animateCamera(cameraUpdate);
                //주소값 or 장소명 리턴
                placeNameOrAddress = place.getName();
                Log.e("주소", placeNameOrAddress);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("에러다", "An error occurred: " + status);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatLng);
        mGoogleMap.addMarker(markerOptions).showInfoWindow();
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        //주소값 or 장소명 리턴
        placeNameOrAddress = mapClass.getPlaceNameOrAddress(geocoder, mLatLng.latitude, mLatLng.longitude);
        mGoogleMap = mapClass.latLngCircle(mGoogleMap,circleOptions,mLatLng,radius);
        Log.e("주소", placeNameOrAddress);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LocationManager locationManager = (LocationManager) ActivityE3Map.this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        mGoogleMap.addMarker(new MarkerOptions().position(mLatLng).title("Marker in Seoul"));
        if (ContextCompat.checkSelfPermission(ActivityE3Map.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(ActivityE3Map.this);
            mGoogleMap.setOnMyLocationClickListener(ActivityE3Map.this);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            mGoogleMap = mapClass.latLngCircle(mGoogleMap,circleOptions,mLatLng,radius);
            //주소값 or 장소명 리턴
           placeNameOrAddress = mapClass.getPlaceNameOrAddress(geocoder, mLatLng.latitude, mLatLng.longitude);
            Log.e("주소", placeNameOrAddress);

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mLatLng = latLng;
                    mGoogleMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(mLatLng);
                    mGoogleMap.addMarker(markerOptions).showInfoWindow();
                    Toast.makeText(ActivityE3Map.this, "location:\n" + latLng, Toast.LENGTH_LONG).show();
                    //주소값 or 장소명 리턴
                    placeNameOrAddress = mapClass.getPlaceNameOrAddress(geocoder, mLatLng.latitude, mLatLng.longitude);
                    mGoogleMap = mapClass.latLngCircle(mGoogleMap,circleOptions,mLatLng,radius);
                    Log.e("주소", placeNameOrAddress);
                }
            });
        } else {
            // Show rationale and request permission.
        }
    }

}
