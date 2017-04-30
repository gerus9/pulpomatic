package com.gerus.pulpomatic.views.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.gerus.pulpomatic.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private SupportMapFragment mapFragment;
    private MapsPresenter mPresenter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPresenter = new MapsPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        mPresenter.start();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mPresenter.resume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.terminate();
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Properties
        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }


    @Override
    public void onMapClick(LatLng poLatLng) {
        mPresenter.onMapClick(poLatLng);
    }

    public void resetMarkers() {
        mMap.clear();
    }

    public void showMessage(String psMsg) {

    }

    public void showTweet(String psMsg) {

    }
    public Marker showMarkers(int idImage, LatLng poCurrentPosition){
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(idImage);
        return mMap.addMarker(new MarkerOptions().title("Current Position").icon(icon).position(poCurrentPosition));
    }

}
