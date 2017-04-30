package com.gerus.pulpomatic.views.maps;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gerus.pulpomatic.R;
import com.gerus.pulpomatic.navigator.Navigator;
import com.gerus.pulpomatic.services.LocationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


/**
 * Created by gerus-mac on 29/04/17.
 */

public class MapsPresenterImpl implements MapsPresenter {
    private MapsActivity mActivity;
    private MapsInteractor mInteractor;
    private boolean isDestinyMarker = false;
    private Marker mPersonalMarker, mDestinyMarker;


    public MapsPresenterImpl(MapsActivity poActivity){
        mActivity = poActivity;
        mInteractor = new MapsInteractorImpl(this);
    }

    @Override
    public void start() {
        mActivity.bindService(new Intent(mActivity, LocationService.class), mInteractor.getLocationConnection(), Context.BIND_AUTO_CREATE);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void terminate() {
        if(mInteractor.getMarkerPosition()!=null){
            mActivity.startService(Navigator.getService(mActivity));
        }
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public void onMapClick(LatLng mPosition) {
        if(!isPreviousMarker()){
            isDestinyMarker = true;
            mInteractor.setMarkerPosition(mPosition);
            if(mDestinyMarker!=null) mDestinyMarker.remove();
            mDestinyMarker = mActivity.showMarkers(R.mipmap.ic_marker, mPosition);
        }
    }

    @Override
    public void setResetMarker() {

    }

    @Override
    public boolean isPreviousMarker() {
        return isDestinyMarker;
    }

    @Override
    public void onPositionChange(LatLng mPosition) {
        Log.d("MapsPresenterImpl","onPositionChange");
        if(mPosition!=null){
            if(mPersonalMarker!=null) mPersonalMarker.remove();
            mPersonalMarker = mActivity.showMarkers(R.mipmap.ic_personal_marker,mPosition);
        }
    }

}
