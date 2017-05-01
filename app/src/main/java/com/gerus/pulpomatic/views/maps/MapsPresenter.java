package com.gerus.pulpomatic.views.maps;

import com.gerus.pulpomatic.RulesVO;
import com.gerus.pulpomatic.base.BasePresenter;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gerus-mac on 29/04/17.
 */

public interface MapsPresenter extends BasePresenter {

    void setMapClick(LatLng mPosition);
    void setOnMapReady();
    void setCleanMarkers();
    void setCenterPosition();
    boolean isPreviousMarker();

    void onPositionChange(LatLng mPosition);
    void onDistance(RulesVO poRules);


}
