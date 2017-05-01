package com.gerus.pulpomatic.services;

/**
 * Created by gerus-mac on 28/04/17.
 */

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.gerus.pulpomatic.Notifications;
import com.gerus.pulpomatic.RulesVO;
import com.gerus.pulpomatic.sharedPreferences.MapsSP;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class LocationService extends Service {

    private LocationCallback mCallback;
    private Location mLastLocation = new Location("");
    private Location mDestiny;
    public static final String TAG = LocationService.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static boolean isActiveAlive = false;
    private boolean isChangePosition = false;
    private final IBinder mBinder = new LocalBinder();
    private RulesVO mRules;
    private Notifications mNotifications;

    private static final int LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = 50f;


    public Location getDestiny() {
        return mDestiny;
    }

    public void setDestiny(LatLng poLatLng) {
        if(poLatLng==null){
            mDestiny = null;
        } else {
            this.mDestiny = new Location("");
            mDestiny.setLatitude(poLatLng.latitude);
            mDestiny.setLongitude(poLatLng.longitude);
        }
    }

    public void onActivityLive(boolean poActivityLive) {
        isActiveAlive = poActivityLive;
    }

    public void getDistance() {
        if (getDestiny() != null){
            mRules.setDistance(mLastLocation.distanceTo(getDestiny()));
            Log.e(TAG, "distance: " + mRules.getDistance());
            if(isActiveAlive && mCallback!=null){
                Log.e(TAG, "mCallback: "+ (mCallback==null)+"");
                Log.e(TAG, "mRules: " + (mRules==null)+"");
                mCallback.onDistance(mRules);
            } else {
                mNotifications.prcStatus(mRules.getText());
            }
        }
    }


    public class LocalBinder extends Binder {

        public LocationService getService(LocationCallback poCallback) {
            mCallback = poCallback;
            return LocationService.this;
        }
    }

    public LatLng getPosition() {
        return (isChangePosition) ? new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()): null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        isActiveAlive = true;
        mNotifications.prcRemoveNotificationStatus();
        return mBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.e(TAG, "unbindService");
        isActiveAlive = false;
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
        Log.e(TAG, "onCreate "+isActiveAlive);
        isActiveAlive = false;
        init();
    }

    private void init() {
        Log.e(TAG, "init");

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
        mRules = new RulesVO(getApplication());
        mNotifications = new Notifications(getApplication());
        LatLng voDestiny = new MapsSP(getApplication()).getLastDestinyPosition();
        if(voDestiny!=null){
            setDestiny(voDestiny);
        }

    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    /////// Location methodss

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "Lat:" + location.getLatitude() +" Long:" +location.getLongitude());
            Log.e(TAG, "isActiveAlive: " + isActiveAlive);
            isChangePosition = true;
            mLastLocation.set(location);
            if(mCallback!=null) mCallback.onLocationChange(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            getDistance();
        }


        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            if (mCallback != null) mCallback.onProviderDisabled(provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public interface LocationCallback {
        void onLocationChange(LatLng poLocation);

        void onProviderDisabled(String psProvider);

        void onDistance(RulesVO poRules);
    }

}