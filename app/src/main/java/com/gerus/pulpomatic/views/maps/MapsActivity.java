package com.gerus.pulpomatic.views.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gerus.pulpomatic.BuildConfig;
import com.gerus.pulpomatic.R;
import com.gerus.pulpomatic.dialogs.Dialogs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
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

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 20;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 30;
    public static boolean isLive = false;

    @BindView(R.id.myFAB)
    protected TextView btnFake;

    @BindView(R.id.infoDestiny)
    protected FrameLayout frameLayout;

    @BindView(R.id.btn_tweet)
    protected Button btnTweet;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.btn_erase)
    protected FloatingActionButton btnErase;

    private Activity mActivity;
    private MenuItem mMenuSearch, mMenuZoom;
    private SupportMapFragment mapFragment;
    private MapsPresenter mPresenter;
    private GoogleMap mMap;
    private Dialogs mDialogs;

    private Animation animShow, animHide, animShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Ger", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_layout);
        ButterKnife.bind(this);
        mActivity = this;
        setSupportActionBar(toolbar);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mPresenter = new MapsPresenterImpl(this);
        mDialogs = new Dialogs(this);

        initAnimation();
        checkPlayServices();
    }

    private void initAnimation() {
        Log.d("Ger", "initAnimation");
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
        animShake = AnimationUtils.loadAnimation(this, R.anim.view_shake);
    }

    @Override
    protected void onStart() {
        Log.d("Ger", "onStart");
        mPresenter.start();
        isLive = true;
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("Ger", "onResume");
        mPresenter.resume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d("Ger", "Stop");
        mPresenter.stop();
        isLive = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("Ger", "onDestroy");
        mPresenter.terminate();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_menu, menu);
        mMenuSearch = menu.findItem(R.id.action_search);
        mMenuZoom = menu.findItem(R.id.action_zoom);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    e.printStackTrace();
                }
                return true;
            case R.id.action_zoom:
                prcZoom();
                return true;
            case R.id.action_location:
                mPresenter.setCenterPosition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prcZoom() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mPresenter.getMarkers(), 150));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Ger", "onMapReady");
        mMap = googleMap;
        prcOnMapReady();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(BuildConfig.DEBUG);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void prcOnMapReady()  {
        mMap.setOnMapClickListener(this);
        mPresenter.setOnMapReady();
    }

    @Override
    protected void onRestart() {
        Log.d("Ger", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d("Ger", "onRestart");
        super.onPause();
    }

    @Override
    public void onMapClick(LatLng poLatLng) {
        mPresenter.setMapClick(poLatLng);
    }

    public void showQuestionMarker(final LatLng poLatLng) {
        mDialogs.showQuestionMessage(getString(R.string.question_marker), new Dialogs.onAnswer() {
            @Override
            public void isAccept(boolean pbDecision) {
                if (pbDecision) mPresenter.setSaveDestiny(poLatLng);
            }
        });
    }

    public void showMessage(String psMsg) {
        setVisible(frameLayout, VISIBLE, true);
        btnFake.setText(psMsg);
    }

    public void hideCardView() {
        setVisible(frameLayout, GONE, true);
    }

    public void showTweet() {
        btnTweet.setVisibility(VISIBLE);
    }

    public void hideTweet() {
        btnTweet.setVisibility(INVISIBLE);
    }

    public void setModifyPaddingMaps() {
        int padding = (int) getResources().getDimension(R.dimen.dp15);
        mMap.setPadding(padding, padding, padding, frameLayout.getHeight());
    }

    public void setOriginalPaddingMaps() {
        int padding = (int) getResources().getDimension(R.dimen.dp15);
        mMap.setPadding(padding, padding, padding, padding);
    }

    public Marker showMarkers(int idImage, LatLng poCurrentPosition) {
        Marker voMarker = null;
        if(mMap!=null){
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(idImage);
            voMarker = mMap.addMarker(new MarkerOptions().icon(icon).position(poCurrentPosition));
        }
        return voMarker;
    }

    public Circle showGeoference(LatLng poPosition, int radius, int color) {
        return mMap.addCircle(new CircleOptions().center(poPosition).radius(radius).strokeWidth(0).strokeColor(Color.TRANSPARENT).fillColor(color));
    }

    public void showCenterMap(LatLng currentPosition) {
        if(mMap!=null){
            mMap.animateCamera(CameraUpdateFactory.scrollBy(250 - (float) Math.random() * 500 - 250, 250 - (float) Math.random() * 500), 5000, null);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));
        }
    }

    public void shakeDelete() {
        btnErase.startAnimation(animShake);
    }

    @OnClick(R.id.btn_tweet)
    public void tweet(View view) {
        prcZoom();
        showProgressBar(getString(R.string.progress_snapshot));
        mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {

            public void onSnapshotReady(Bitmap bitmap) {
                mPresenter.sendSnapShot(bitmap);
                showDismissProgressBar();
            }
        });
    }

    @OnClick(R.id.btn_erase)
    public void resetMarker(View view) {
        mPresenter.setCleanMarkers();
    }

    @OnClick(R.id.infoDestiny)
    public void findDestiny(View view) {
        mPresenter.setFindDestiny();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                showQuestionMarker(place.getLatLng());
            }
        }
    }

    public void hideSearchButton() {
        mMenuSearch.setVisible(false);
    }

    public void showSearchButton() {
        mMenuSearch.setVisible(true);
    }

    private void setVisible(View poView, int piVisible, boolean pbanimate) {
        if (piVisible == VISIBLE) {
            if (poView.getVisibility() != VISIBLE) {
                poView.setVisibility(piVisible);
                if (pbanimate) poView.startAnimation(animShow);
            }
        } else {
            if (poView.getVisibility() != piVisible) {
                poView.startAnimation(animHide);
                if (pbanimate) poView.setVisibility(piVisible);
            }
        }
    }

    public void showZoomButton() {
        mMenuZoom.setVisible(true);
    }

    public void hideZoomButton() {
        mMenuZoom.setVisible(false);
    }

    public void showDialogNotPosition() {
        mDialogs.showInfoMessage(getString(R.string.error_no_position), null);
    }

    public void showProgressBar(String psTxt) {
        mDialogs.showProgressBar(psTxt);
    }

    public void showDismissProgressBar() {
        mDialogs.dismissProgressBar();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

}
