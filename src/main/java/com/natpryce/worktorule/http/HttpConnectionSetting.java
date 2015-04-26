package com.natpryce.worktorule.http;

import com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl;

import java.net.HttpURLConnection;

public interface HttpConnectionSetting {
    void applyTo(HttpURLConnection cx);

    HttpConnectionSetting NONE = new HttpConnectionSetting() {
        @Override
        public void applyTo(HttpURLConnection cx) {
        }
    };
}
