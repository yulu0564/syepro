package com.syepro.app.api.network;

public interface ProgressCancelListener {
    void onCancelProgress();

    void onDismissProgress();
}