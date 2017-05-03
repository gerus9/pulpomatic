package com.gerus.pulpomatic.views.maps;

import android.graphics.Bitmap;

import com.gerus.pulpomatic.models.RulesVO;
import com.gerus.pulpomatic.base.BasePresenter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by gerus-mac on 29/04/17.
 */

public interface MapsPresenter extends BasePresenter {

    void setMapClick(LatLng mPosition);
    void setOnMapReady();
    void setCleanMarkers();
    void setCenterPosition();
    void setFindDestiny();
    void setSaveDestiny(LatLng poLatLng);
    boolean isPreviousMarker();
    LatLngBounds getMarkers();
    void sendSnapShot(Bitmap bitmap);

    void onPositionChange(LatLng mPosition);
    void onDistance(RulesVO poRules);

}
