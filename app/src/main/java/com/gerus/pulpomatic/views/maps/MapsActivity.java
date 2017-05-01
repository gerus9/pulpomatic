package com.gerus.pulpomatic.views.maps;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

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
import butterknife.OnClick;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    @BindView(R.id.myFAB)
    protected TextView btnFake;

    @BindView(R.id.infoDestiny)
    protected FrameLayout frameLayout;

    @BindView(R.id.btn_postion)
    protected FloatingActionButton btnPostion;
    @BindView(R.id.btn_erase)
    protected FloatingActionButton btnErase;

    private SupportMapFragment mapFragment;
    private MapsPresenter mPresenter;
    private GoogleMap mMap;

    private Animation animShow, animHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_layout);
        ButterKnife.bind(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPresenter = new MapsPresenterImpl(this);

        initAnimation();

    }

    private void initAnimation()
    {
        animShow = AnimationUtils.loadAnimation( this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( this, R.anim.view_hide);
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
        //frameLayout.setVisibility(View.VISIBLE);
        //prcAnimate(frameLayout, true);
        setVisible(frameLayout,true);
        btnFake.setText(psMsg);
    }

    public void showTweet(String psMsg) {

    }

    public void setModifyPaddingMaps(){
        int padding = (int) getResources().getDimension(R.dimen.dp15);
        mMap.setPadding(padding,padding,padding,frameLayout.getHeight());
    }

    public void setOriginalPaddingMaps(){
        int padding = (int) getResources().getDimension(R.dimen.dp15);
        mMap.setPadding(padding,padding,padding,padding);
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
        setVisible(frameLayout, false);
    }

    private void setVisible(View poView, boolean pbShow) {
        if(pbShow){
            poView.setVisibility(View.VISIBLE);
            poView.startAnimation(animShow);
        } else {
            poView.startAnimation(animHide);
            poView.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.btn_postion)
    public void submit(View view) {
        mPresenter.setCenterPosition();
    }

    @OnClick(R.id.btn_erase)
    public void resetMarker(View view) {
        mPresenter.setCleanMarkers();
    }


}
