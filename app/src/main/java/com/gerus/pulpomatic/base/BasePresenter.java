package com.gerus.pulpomatic.base;

import android.content.Context;

/**
 * Created by gerus-mac on 29/04/17.
 */

public interface BasePresenter {
    void start();
    void resume();
    void pause();
    void stop();
    void terminate();
    Context getContext();
}
