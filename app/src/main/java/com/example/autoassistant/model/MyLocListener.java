package com.example.autoassistant.model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class MyLocListener implements LocationListener {

    private LocInterfaceListener LocInterfaceListener;


    @Override
    public void onLocationChanged(@NonNull Location location) {
        LocInterfaceListener.OnLocationChanged(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    public void setLocInterfaceListener(com.example.autoassistant.model.LocInterfaceListener locInterfaceListener) {
        LocInterfaceListener = locInterfaceListener;
    }

}
