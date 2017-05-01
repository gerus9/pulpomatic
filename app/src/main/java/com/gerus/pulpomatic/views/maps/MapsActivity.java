package com.gerus.pulpomatic.views.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gerus.pulpomatic.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    @BindView(R.id.myFAB)
    protected TextView btnFake;

    @BindView(R.id.card_view)
    protected CardView mCardView;

    @BindView(R.id.btn_postion)
    protected FloatingActionButton btnPostion;
    @BindView(R.id.btn_erase)
    protected FloatingActionButton btnErase;

    private SupportMapFragment mapFragment;
    private MapsPresenter mPresenter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_layout);
        ButterKnife.bind(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPresenter = new MapsPresenterImpl(this);

        btnPostion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnErase.show();
            }
        });

        btnFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnErase.hide();
            }
        });

        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setCleanMarkers();
            }
        });

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
        Log.d("Ger","Entre al destroy");
        mPresenter.terminate();
        Log.d("Ger","Entre al parte 2 ");
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Properties
        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);

        mPresenter.setOnMapReady();
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
        mPresenter.setMapClick(poLatLng);
    }


    public void showMessage(String psMsg) {
        mCardView.setVisibility(View.VISIBLE);
        btnFake.setText(psMsg);
    }

    public void showTweet(String psMsg) {

    }

    public Marker showMarkers(int idImage, LatLng poCurrentPosition) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(idImage);
        return mMap.addMarker(new MarkerOptions().title("Current Position").icon(icon).position(poCurrentPosition));
    }

    public Circle showGeoference(LatLng poPosition, int radius, int color){
        return mMap.addCircle(new CircleOptions().center(poPosition).radius(radius).fillColor(color));
    }

    public void showCenterMap(LatLng currentPosition) {
        mMap.animateCamera(CameraUpdateFactory.scrollBy(250-(float)Math.random()*500-250, 250-(float)Math.random()*500),5000,null);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));
    }

    public void hideCardView(){
        mCardView.setVisibility(View.GONE);
    }
}
