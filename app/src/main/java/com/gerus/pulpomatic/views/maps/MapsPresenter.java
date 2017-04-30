package com.gerus.pulpomatic.views.maps;

import com.gerus.pulpomatic.base.BasePresenter;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gerus-mac on 29/04/17.
 */

public interface MapsPresenter extends BasePresenter {

    void onMapClick(LatLng mPosition);
    void setResetMarker();
    boolean isPreviousMarker();

    void onPositionChange(LatLng mPosition);

}
