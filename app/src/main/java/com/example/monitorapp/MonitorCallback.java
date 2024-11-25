package com.example.monitorapp;

import com.android.volley.VolleyError;

import java.util.List;

public interface MonitorCallback {
    void onMonitoresObtidos(List<Monitor> monitores);
    void onError(VolleyError error);
    void onMonitorObtido(Monitor m);
}
