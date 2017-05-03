package com.gerus.pulpomatic.views.maps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.gerus.pulpomatic.R;
import com.gerus.pulpomatic.models.RulesVO;
import com.gerus.pulpomatic.navigator.Navigator;
import com.gerus.pulpomatic.utils.UImages;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.List;


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
        mView.bindService(Navigator.getService(mView), mInteractor.getLocationConnection(), Context.BIND_AUTO_CREATE);
        mInteractor.setActivityLive(true);
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
        if(!isPreviousMarker()){
            mView.unbindService(mInteractor.getLocationConnection());
            Log.e("unbindService", "OK");
        }
    }


    @Override
    public void terminate() {
        if (!isPreviousMarker()) {
            mView.stopService(Navigator.getService(getContext()));
        }
    }

    @Override
    public Context getContext() {
        return mView;
    }

    @Override
    public void setMapClick(LatLng mPosition) {
        if (isPreviousMarker()) {
            mView.shakeDelete();
        } else {
            mView.showQuestionMarker(mPosition);
        }
    }

    private void showDestinyMarker(LatLng poPosition) {
        if (mDestinyMarker != null) mDestinyMarker.remove();
        mDestinyMarker = mView.showMarkers(R.drawable.ic_marker, poPosition);
        mInteractor.setDestinyMarker(poPosition);
        mInteractor.getDistance();
        mView.hideSearchButton();
        mView.showZoomButton();
    }

    @Override
    public void setOnMapReady() {
        Log.d("Ger-Presenter", "setOnMapReady: ");
        mView.setOriginalPaddingMaps();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Ger-Presenter", "Handler: ");
                if (mInteractor.getCurrentPosition() != null) {
                    Log.d("Ger-Presenter", "Handler 1 ");
                    onPositionChange(mInteractor.getCurrentPosition());
                    Log.d("Ger-Presenter", "Handler 2 ");
                    mView.showCenterMap(mInteractor.getCurrentPosition());
                    Log.d("Ger-Presenter", "Handler 3 ");
                }
                if (isPreviousMarker()) {
                    Log.d("Ger-Presenter", "Handler 4 ");
                    showDestinyMarker(mInteractor.getDestinyMarker());
                }
            }
        }, 1000);
    }


    @Override
    public void setCleanMarkers() {
        if (isPreviousMarker()) {
            if (mDestinyMarker != null) mDestinyMarker.remove();
            if (mGeoference != null) mGeoference.remove();
            mInteractor.removeDestinyMarker();
            mView.hideCardView();
            mView.setOriginalPaddingMaps();
            mView.hideTweet();
            mView.showSearchButton();
            mView.hideZoomButton();
        }
    }

    @Override
    public void setCenterPosition() {
        if (mInteractor.getCurrentPosition() != null) {
            mView.showCenterMap(mInteractor.getCurrentPosition());
        } else {
            mView.showDialogNotPosition();
            mInteractor.getLastKnowPosition();
        }
    }

    @Override
    public void setFindDestiny() {
        if (mInteractor.getDestinyMarker() != null) {
            mView.showCenterMap(mInteractor.getDestinyMarker());
        }
    }

    @Override
    public void setSaveDestiny(LatLng poLatLng) {
        showDestinyMarker(poLatLng);
        mView.showCenterMap(poLatLng);
        mView.hideSearchButton();
    }

    @Override
    public boolean isPreviousMarker() {
        return mInteractor.getDestinyMarker() != null;
    }

    @Override
    public LatLngBounds getMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (mPersonalMarker != null) builder.include(mPersonalMarker.getPosition());
        if (mDestinyMarker != null) builder.include(mDestinyMarker.getPosition());
        return builder.build();
    }

    @Override
    public void sendSnapShot(Bitmap bitmap) {

        String psTxt = String.format(mView.getString(R.string.tweet), mDestinyMarker.getPosition().latitude, mDestinyMarker.getPosition().longitude);
        boolean isTwitter = false;
        String twitter_package= "com.twitter.android";
        String path = MediaStore.Images.Media.insertImage(mView.getContentResolver(), UImages.prcCompressBitmap(bitmap), mView.getString(R.string.description_image), null);

        Intent viTweetIntent = Navigator.getTwitter(Uri.parse(path), psTxt);
        final PackageManager packageManager = mView.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(viTweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : list) {
            String p = resolveInfo.activityInfo.packageName;
            if (p != null && p.startsWith(twitter_package)) {
                viTweetIntent.setPackage(p);
                isTwitter = true;
                break;
            }
        }
        if(isTwitter){
            mView.startActivity(viTweetIntent);
        } else {
            mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + twitter_package)));
        }
    }

    @Override
    public void onPositionChange(LatLng mPosition) {
        if (mPosition != null) {
            if (mPersonalMarker != null) mPersonalMarker.remove();
            mPersonalMarker = mView.showMarkers(R.drawable.ic_personal_marker, mPosition);
        }
    }

    @Override
    public void onDistance(RulesVO poRules) {
        mView.showMessage(poRules.getText());
        mView.setModifyPaddingMaps();
        if (mGeoference != null) {
            mGeoference.remove();
        }
        mGeoference = mView.showGeoference(mInteractor.getDestinyMarker(), (int) poRules.getDistance(), poRules.getColor());

        if (poRules.isNearly()) {
            mView.showTweet();
        } else {
            mView.hideTweet();
        }
    }

}
