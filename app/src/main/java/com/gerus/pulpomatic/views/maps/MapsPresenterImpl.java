package com.gerus.pulpomatic.views.maps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.gerus.pulpomatic.R;
import com.gerus.pulpomatic.RulesVO;
import com.gerus.pulpomatic.navigator.Navigator;
import com.gerus.pulpomatic.services.LocationService;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


/**
 * Created by gerus-mac on 29/04/17.
 */

public class MapsPresenterImpl implements MapsPresenter {
    private MapsActivity mView;
    private MapsInteractor mInteractor;
    private Marker mPersonalMarker, mDestinyMarker;
    private Circle mGeoference;


    public MapsPresenterImpl(MapsActivity poActivity) {
        mView = poActivity;
        mView.startService(Navigator.getService(mView));
        mInteractor = new MapsInteractorImpl(this);
    }

    @Override
    public void start() {
        mInteractor.setActivityLive(true);
        mView.bindService(Navigator.getService(mView), mInteractor.getLocationConnection(), Context.BIND_AUTO_CREATE);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {
        mInteractor.setActivityLive(false);
        mView.unbindService(mInteractor.getLocationConnection());
    }

    @Override
    public void terminate() {
        Log.d("Entre", "terminate");
        if (!isPreviousMarker()) {
            Log.e("MUEReeeeeee", "servicio");
            mView.stopService(Navigator.getService(getContext()));
        }
    }

    @Override
    public Context getContext() {
        return mView;
    }

    @Override
    public void setMapClick(LatLng mPosition) {
        if(!isPreviousMarker()){
            showDestinyMarker(mPosition);
        }

    }

    private void showDestinyMarker(LatLng poPosition) {
        if (mDestinyMarker != null) mDestinyMarker.remove();
        mDestinyMarker = mView.showMarkers(R.mipmap.ic_marker, poPosition);
        mInteractor.setDestinyMarker(poPosition);
        mInteractor.getDistance();
    }

    @Override
    public void setOnMapReady() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInteractor.getCurrentPosition() != null) {
                    onPositionChange(mInteractor.getCurrentPosition());
                    mView.showCenterMap(mInteractor.getCurrentPosition());
                }
                if (isPreviousMarker()) {
                    showDestinyMarker(mInteractor.getDestinyMarker());
                }
            }
        }, 2500);
    }


    @Override
    public void setCleanMarkers() {
        if (isPreviousMarker()) {
            if (mDestinyMarker != null) mDestinyMarker.remove();
            if (mGeoference != null) mGeoference.remove();
            mInteractor.removeDestinyMarker();
            mView.hideCardView();
        }
    }

    @Override
    public boolean isPreviousMarker() {
        return mInteractor.getDestinyMarker() != null;
    }

    @Override
    public void onPositionChange(LatLng mPosition) {
        Log.d("MapsPresenterImpl", "onPositionChange");
        if (mPosition != null) {
            if (mPersonalMarker != null) mPersonalMarker.remove();
            mPersonalMarker = mView.showMarkers(R.mipmap.ic_personal_marker, mPosition);
        }
    }

    @Override
    public void onDistance(RulesVO poRules) {
        mView.showMessage(poRules.getText());
        if (mGeoference != null) {mGeoference.remove();}
        mGeoference = mView.showGeoference(mInteractor.getDestinyMarker(), (int) poRules.getDistance(), poRules.getColor());

    }

}
