package com.gerus.pulpomatic;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

/**
 * Created by gerus-mac on 30/04/17.
 */

public class RulesVO {

    private Context mContext;
    private double mDistance = -1;
    private String mText;
    private int mColor = Color.BLACK;
    private static final int MAX_VALUE = 200;

    public RulesVO(Context poContext){
        mContext = poContext;
        mText = getTextByDistance();
        mColor = getColor();
    }

    public String getTextByDistance() {
        String vsTxt = "";
        if (mDistance > MAX_VALUE) {
            vsTxt = mContext.getString(R.string.distance_200);
        } else if (mDistance <= 200 && mDistance > 100) {
            vsTxt = mContext.getString(R.string.distance_100);
        } else if (mDistance <= 100 && mDistance > 50) {
            vsTxt = mContext.getString(R.string.distance_50);
        } else if (mDistance <= 50 && mDistance > 10) {
            vsTxt = mContext.getString(R.string.distance_10);
        } else if (mDistance < 10) {
            vsTxt = mContext.getString(R.string.distance_0);
        } else {
            vsTxt = "";
        }
        return vsTxt;
    }

    public int getColor() {
        int viColor = Color.BLACK;
        if (mDistance > MAX_VALUE) {
            viColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
        } else if (mDistance <= MAX_VALUE && mDistance > 100) {
            viColor = ContextCompat.getColor(mContext, R.color.colorAccent);
        } else if (mDistance <= 100 && mDistance > 50) {
            viColor = ContextCompat.getColor(mContext, android.R.color.holo_green_dark);
        } else if (mDistance <= 50 && mDistance > 10) {
            viColor = ContextCompat.getColor(mContext, android.R.color.white);
        } else if (mDistance < 10) {
            viColor = ContextCompat.getColor(mContext, android.R.color.holo_purple);
        }
        return viColor;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double poDistance) {
        if(poDistance>MAX_VALUE) poDistance = MAX_VALUE+1;
        this.mDistance = poDistance;
        mText = getTextByDistance();
        mColor = getColor();
    }

    public String getText() {
        return mText;
    }
}
