package com.gerus.pulpomatic.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

public abstract class SP {

    private final Context mContext;
    private SharedPreferences mSharedFile;

    public SP(Context poContext, String psFile) {
        mContext = poContext;
        mSharedFile = mContext.getSharedPreferences(psFile, Context.MODE_PRIVATE);
    }

    protected String getSharedString(String psFile, String psKey, String psDefault) {
        return mSharedFile.getString(psKey, psDefault);
    }

    protected void setSharedString(String psKey, String psValue) {
        SharedPreferences.Editor voEditor = mSharedFile.edit();
        voEditor.putString(psKey, psValue);
        voEditor.commit();
    }

    protected boolean getSharedBoolean(String psKey, boolean pbDefault) {
        return mSharedFile.getBoolean(psKey, pbDefault);
    }

    protected void setSharedBoolean(String psKey, boolean pbType) {
        SharedPreferences.Editor voEditor = mSharedFile.edit();
        voEditor.putBoolean(psKey, pbType);
        voEditor.commit();
    }

    protected int getSharedInt(String psKey, int pbDefault) {
        return mSharedFile.getInt(psKey, pbDefault);
    }

    protected void setSharedInt(String psKey, int pbType) {
        SharedPreferences.Editor voEditor = mSharedFile.edit();
        voEditor.putInt(psKey, pbType);
        voEditor.commit();
    }

    protected Float getSharedFloat(String psKey, float pbDefault) {
        return mSharedFile.getFloat(psKey, pbDefault);
    }

    protected void setSharedFloat(String psKey, float pbType) {
        SharedPreferences.Editor voEditor = mSharedFile.edit();
        voEditor.putFloat(psKey, pbType);
        voEditor.commit();
    }

    protected void removeShared(String psKey) {
        SharedPreferences.Editor voEditor = mSharedFile.edit();
        voEditor.remove(psKey);
        voEditor.commit();
    }

    protected void removeShared(String ... psKeys) {
        SharedPreferences.Editor voEditor = mSharedFile.edit();
        for (String psKey: psKeys) {
            voEditor.remove(psKey);
        }
        voEditor.commit();
    }

    public void removeSharedFile() {
        SharedPreferences.Editor voEditor = mSharedFile.edit();
        voEditor.clear();
        voEditor.commit();
    }

}
