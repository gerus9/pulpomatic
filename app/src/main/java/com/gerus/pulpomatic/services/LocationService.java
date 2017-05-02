package com.gerus.pulpomatic.services;

/**
 * Created by gerus-mac on 28/04/17.
 */
import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.gerus.pulpomatic.models.RulesVO;
import com.gerus.pulpomatic.notifications.Notifications;
import com.gerus.pulpomatic.sharedPreferences.MapsSP;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final int LOCATION_INTERVAL_DEFAULT = 600000;  // 10 min
    public static final float LOCATION_DISTANCE_DEFAULT = 600f;  //

    private LocationCallback mCallback;
    private Location mLastLocation = new Location("");
    private Location mDestiny;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static final String TAG = LocationService.class.getSimpleName();
    private static boolean isActiveAlive = false;

    private int LOCATION_INTERVAL = LOCATION_INTERVAL_DEFAULT;
    private float LOCATION_DISTANCE = LOCATION_DISTANCE_DEFAULT;

    private boolean isChangePosition = false;
    private final IBinder mBinder = new LocalBinder();
    private RulesVO mRules;
    private Notifications mNotifications;


    public Location getDestiny() {
        return mDestiny;
    }

    public void setDestiny(LatLng poLatLng) {
        if (poLatLng == null) {
            mDestiny = null;
        } else {
            this.mDestiny = new Location("");
            mDestiny.setLatitude(poLatLng.latitude);
            mDestiny.setLongitude(poLatLng.longitude);
        }
    }

    public void onActivityLive(boolean poActivityLive) {
        isActiveAlive = poActivityLive;
        if (isActiveAlive) {
            mNotifications.prcRemoveNotificationStatus();
        } else {
            if (getDestiny() != null) mNotifications.prcStatus(mRules.getText());
        }
    }

    public void getDistance() {
        if (getDestiny() != null) {
            mRules.setDistance(mLastLocation.distanceTo(getDestiny()));
            Log.e(TAG, "distance: " + mRules.getDistance());
            prcUpdateIntervals(mRules.getDistance());
            if (isActiveAlive && mCallback != null) {
                Log.e(TAG, "mCallback: " + (mCallback == null) + "");
                Log.e(TAG, "mRules: " + (mRules == null) + "");
                mCallback.onDistance(mRules);
            } else {
                prcShowNotification();
            }
        }
    }


    private void prcUpdateIntervals(double poDistance) {
        if (poDistance < RulesVO.MAX_VALUE) {
            Log.e(TAG, " *** poDistance > RulesVO.MAX_VALUE *** ");

            Log.e(TAG, " *** mRules.LOCATION_INTERVAL: "+mRules.LOCATION_INTERVAL);
            Log.e(TAG, " *** LOCATION_INTERVAL: "+ LOCATION_INTERVAL);

            if (mRules.LOCATION_INTERVAL != LOCATION_INTERVAL) {
                LOCATION_INTERVAL = mRules.LOCATION_INTERVAL;
                LOCATION_DISTANCE = mRules.LOCATION_DISTANCE;
                Log.e(TAG, " *** Cambie los listeners *** ");
                mLocationRequest.setInterval(mRules.LOCATION_INTERVAL);
                mLocationRequest.setSmallestDisplacement(mRules.LOCATION_DISTANCE);

                Log.e(TAG, " *** LOCATION_DISTANCE: " + mRules.LOCATION_DISTANCE);
                Log.e(TAG, " *** LOCATION_INTERVAL: " + mRules.LOCATION_INTERVAL);
                startLocationUpdates();
            }
        }
    }


    private void prcShowNotification() {
        mNotifications.prcStatus(mRules.getText());
    }

    public void getLastPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mCallback.onLocationChange(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        isChangePosition = true;
        mLastLocation.set(location);
        if (mCallback != null)
            mCallback.onLocationChange(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        getDistance();
    }

    public class LocalBinder extends Binder {

        public LocationService getService(LocationCallback poCallback) {
            mCallback = poCallback;
            return LocationService.this;
        }
    }

    public LatLng getPosition() {
        return (isChangePosition) ? new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()) : null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        onActivityLive(true);
        return mBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.e(TAG, "unbindService");
        super.unbindService(conn);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate " + isActiveAlive);
        isActiveAlive = false;
        init();
    }

    private void init() {
        Log.e(TAG, "init");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setSmallestDisplacement(LOCATION_DISTANCE);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mRules = new RulesVO(getApplication());
        mNotifications = new Notifications(getApplication());
        LatLng voDestiny = new MapsSP(getApplication()).getLastDestinyPosition();
        if (voDestiny != null) {
            setDestiny(voDestiny);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }


    public interface LocationCallback {
        void onLocationChange(LatLng poLocation);

        void onProviderDisabled(String psProvider);

        void onDistance(RulesVO poRules);
    }

}